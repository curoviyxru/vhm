package moe.crx.verticles;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import moe.crx.api.requests.ClanJoin;
import moe.crx.api.responses.ClanJoinResponse;
import moe.crx.verticles.factory.EnumerableFactory;

import static moe.crx.api.requests.ClanJoin.MODERATOR;
import static moe.crx.verticles.ClanConstants.*;

public final class Moderator extends AbstractModerator {

    private final String userName;
    private final String clanName;
    private int maxMembers;

    public Moderator(String userName, String clanName) {
        this.userName = userName;
        this.clanName = clanName;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var request = new ClanJoin(MODERATOR, userName).toJson();
        vertx.eventBus().<JsonObject>request(clanName + CLAN_JOIN_MODERATOR, request, handler -> {
            if (handler.failed()) {
                System.out.printf("[%s] I failed to join my clan %s. ;( It's time to rest! :P%n", userName, clanName);
                startPromise.fail(handler.cause());
                return;
            }

            var json = handler.result().body();
            if (json == null) {
                startPromise.fail("body_is_null");
                return;
            }

            var response = new ClanJoinResponse().fromJson(json);

            System.out.printf("[%s] Moderator joined clan %s (%d max members) with message: %s%n", userName, clanName, response.getMaxMembers(), response.getMessage());

            this.maxMembers = response.getMaxMembers();
            listenRequests();
            startListeningToChat(clanName, userName);

            startPromise.complete();
        });
    }

    private void listenRequests() {
        vertx.eventBus().<JsonObject>consumer(clanName + CLAN_JOIN, event -> {
            var json = event.body();
            if (json == null) {
                event.fail(BAD_REQUEST_ERROR, "body_is_null");
                return;
            }

            var request = new ClanJoin().fromJson(json);

            if (request.getUserType().equals(MODERATOR)) return;

            lockAndGetMembersList(event, clanName, (e, lockResult, map, list) ->
                    addNewMember(maxMembers, clanName, request.getUserName(), event, lockResult, map, list));
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        stopPromise.complete();
    }

    public static final class Factory extends EnumerableFactory<Moderator> {

        private final int moderatorClanId;

        public Factory(int startId, int moderatorClanId) {
            super(startId);
            this.moderatorClanId = moderatorClanId;
        }

        @Override
        public Class<Moderator> verticleType() {
            return Moderator.class;
        }

        @Override
        public Moderator createVerticle() {
            var nextId = getNextId();
            return new Moderator(String.format(MODERATOR_NAME_FORMAT, nextId), String.format(CLAN_NAME_FORMAT, moderatorClanId));
        }
    }
}
