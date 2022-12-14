package moe.crx.verticles;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import moe.crx.api.requests.ClanJoin;
import moe.crx.api.requests.ClanRegister;
import moe.crx.api.requests.ClanUnregister;
import moe.crx.api.responses.ClanJoinResponse;
import moe.crx.api.responses.ClanMembersResponse;
import moe.crx.verticles.factory.EnumerableFactory;

import java.util.ArrayList;
import java.util.Objects;

import static moe.crx.api.requests.ClanJoin.MODERATOR;
import static moe.crx.verticles.ClanWatcher.*;

public final class Administrator extends AbstractModerator {

    public static final String CLAN_NAME_FORMAT = "Clan#%d";
    public static final String ADMIN_NAME_FORMAT = "Admin#%d";
    public static final String CLAN_JOIN_MODERATOR = ".clan.join.moderator";
    public static final String CLAN_JOIN = ".clan.join";
    public static final String CLAN_MEMBERS = ".clan.members";
    public static final String CLAN_MODERATORS = ".clan.moderators";
    public static final String MEMBER_JOINED = "member_joined";
    public static final String CLAN_MEMBERS_LIST = ".clan.members.list";

    private final String clanName;
    private final String userName;
    private final int maxMembers;
    private final int maxModerators;

    public Administrator(String clanName, String userName, int maxMembers, int maxModerators) {
        this.clanName = clanName;
        this.userName = userName;
        this.maxMembers = maxMembers;
        this.maxModerators = maxModerators;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var request = new ClanRegister(clanName, maxMembers, maxModerators).toJson();
        vertx.sharedData().getLock(clanName, lockResult -> {
            if (lockResult.failed()) {
                startPromise.fail(lockResult.cause());
                return;
            }

            vertx.sharedData().<String, ArrayList<String>>getAsyncMap(clanName + CLAN_MEMBERS, mapResult -> {
                if (mapResult.failed()) {
                    lockResult.result().release();
                    startPromise.fail(mapResult.cause());
                    return;
                }

                var map = mapResult.result();
                map.get(clanName + CLAN_MEMBERS, getResult -> {
                    if (getResult.failed()) {
                        lockResult.result().release();
                        startPromise.fail(getResult.cause());
                        return;
                    }

                    var list = Objects.requireNonNullElse(getResult.result(), new ArrayList<String>());

                    list.add(userName);
                    map.put(clanName + CLAN_MEMBERS, list, putResult -> {
                        if (putResult.failed()) {
                            lockResult.result().release();
                            startPromise.fail(putResult.cause());
                            return;
                        }

                        vertx.eventBus().<String>request(CLAN_REGISTER, request, handler -> {
                            lockResult.result().release();

                            if (handler.failed()) {
                                System.out.printf("[%s] Clan failed to register with message: %s%n", clanName, handler.cause());
                                startPromise.fail(handler.cause());
                                return;
                            }

                            var response = handler.result().body();
                            if (response == null) {
                                startPromise.fail("body_is_null");
                                return;
                            }

                            System.out.printf("[%s] Clan registered with message: %s%n", clanName, response);

                            listenRequests();
                            startListeningToChat(clanName, userName);

                            startPromise.complete();
                        });
                    });
                });
            });
        });
    }

    private void listenRequests() {
        vertx.eventBus().<JsonObject>consumer(clanName + CLAN_JOIN_MODERATOR, event -> {
            var json = event.body();
            if (json == null) {
                event.fail(BAD_REQUEST_ERROR, "body_is_null");
                return;
            }

            var request = new ClanJoin().fromJson(json);

            if (!request.getUserType().equals(MODERATOR)) return;

            lockAndGetMembersList(event, clanName, (e, map, list) -> {
                vertx.sharedData().getCounter(clanName + CLAN_MODERATORS, modCounterResult -> {
                    if (modCounterResult.failed()) {
                        event.fail(INTERNAL_ERROR, "get_moderators_counter_error");
                        return;
                    }

                    var modCounter = modCounterResult.result();
                    modCounter.get(modGetResult -> {
                        if (modGetResult.failed()) {
                            event.fail(INTERNAL_ERROR, "get_moderators_error");
                            return;
                        }

                        var moderatorsCount = modGetResult.result();
                        if (moderatorsCount >= maxModerators) {
                            event.fail(REGISTRATION_ERROR, "maximum_moderators");
                            return;
                        }

                        modCounter.incrementAndGet(modAddResult -> {
                            if (modGetResult.failed()) {
                                event.fail(INTERNAL_ERROR, "add_moderators_error");
                                return;
                            }

                            addNewMember(maxMembers, clanName, request.getUserName(), event, map, list);
                        });
                    });
                });
            });
        });
        vertx.eventBus().consumer(clanName + CLAN_MEMBERS_LIST, event -> lockAndGetMembersList(event, clanName, (e, map, list) -> {
            event.reply(new ClanMembersResponse(list).toJson());
            System.out.printf("[%s] Serving clan members list. (%d members)%n", clanName, list.size());
        }));
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        var request = new ClanUnregister(clanName).toJson();
        vertx.eventBus().<String>request(CLAN_UNREGISTER, request, handler -> {
            if (handler.failed()) {
                System.out.printf("[%s] Clan failed to unregister with message: %s%n", clanName, handler.cause());
                stopPromise.fail(handler.cause());
                return;
            }

            var response = handler.result().body();
            if (response == null) {
                stopPromise.fail("body_is_null");
                return;
            }

            System.out.printf("[%s] Clan unregistered with message: %s%n", clanName, response);

            stopPromise.complete();
        });
    }

    public static final class Factory extends EnumerableFactory<Administrator> {

        private final int maxMembers;
        private final int maxModerators;

        public Factory(int startId, int maxMembers, int maxModerators) {
            super(startId);
            if (maxMembers <= 0) throw new RuntimeException("Invalid max members count.");
            if (maxModerators < 0) throw new RuntimeException("Invalid max moderators count.");
            if (maxMembers < maxModerators)
                throw new RuntimeException("Invalid arguments: max members < max moderators.");
            this.maxMembers = maxMembers;
            this.maxModerators = maxModerators;
        }

        @Override
        public Class<Administrator> verticleType() {
            return Administrator.class;
        }

        @Override
        public Administrator createVerticle() {
            var nextId = getNextId();
            return new Administrator(String.format(CLAN_NAME_FORMAT, nextId), String.format(ADMIN_NAME_FORMAT, nextId), maxMembers, maxModerators);
        }
    }
}
