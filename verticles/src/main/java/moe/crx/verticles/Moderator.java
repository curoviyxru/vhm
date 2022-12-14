package moe.crx.verticles;

import io.vertx.core.Promise;
import moe.crx.api.requests.ClanJoin;
import moe.crx.api.responses.ClanJoinResponse;
import moe.crx.verticles.factory.EnumerableFactory;

import static moe.crx.api.requests.ClanJoin.MODERATOR;
import static moe.crx.verticles.Administrator.*;
import static moe.crx.verticles.ClanWatcher.INTERNAL_ERROR;
import static moe.crx.verticles.ClanWatcher.REGISTRATION_ERROR;

public final class Moderator extends AbstractModerator {

    public static final String MODERATOR_NAME_FORMAT = "Moderator#%d";
    private final String userName;
    private final String clanName;
    private int maxMembers;

    public Moderator(String userName, String clanName) {
        this.userName = userName;
        this.clanName = clanName;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var request = new ClanJoin(MODERATOR, userName);
        vertx.eventBus().<ClanJoinResponse>request(clanName + CLAN_JOIN_MODERATOR, request, handler -> {
            if (handler.failed()) {
                System.out.printf("[%s] I failed to join my clan %s. ;( It's time to rest! :P%n", userName, clanName);
                startPromise.fail(handler.cause());
                return;
            }

            var response = handler.result().body();
            if (response == null) {
                startPromise.fail("body_is_null");
                return;
            }

            System.out.printf("[%s] Moderator joined clan %s (%d max members) with message: %s%n", userName, clanName, response.getMaxMembers(), response.getMessage());

            this.maxMembers = response.getMaxMembers();
            listenRequests();
            startListeningToChat(clanName, userName);

            startPromise.complete();
        });
    }

    private void listenRequests() {
        vertx.eventBus().<ClanJoin>consumer(clanName + CLAN_JOIN, event -> {
            var request = event.body();
            if (request == null) {
                event.fail(INTERNAL_ERROR, "body_is_null");
                return;
            }

            if (request.getUserType().equals(MODERATOR)) return;

            lockAndGetMembersList(event, clanName, (e, map, list) -> {
                if (list.size() >= maxMembers) {
                    event.fail(REGISTRATION_ERROR, "maximum_members");
                    return;
                }

                if (list.contains(request.getUserName())) {
                    event.fail(REGISTRATION_ERROR, "member_already_registered");
                    return;
                }

                list.add(request.getUserName());
                map.put(clanName + CLAN_MEMBERS, list, putResult -> {
                    if (putResult.failed()) {
                        event.fail(INTERNAL_ERROR, "put_members_error");
                        return;
                    }

                    event.reply(new ClanJoinResponse(MEMBER_JOINED, maxMembers));
                    System.out.printf("[%s] User %s added to clan %s.%n", userName, request.getUserName(), clanName);
                });
            });
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
