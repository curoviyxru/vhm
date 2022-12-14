package moe.crx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.shareddata.AsyncMap;
import moe.crx.api.requests.ClanRegister;
import moe.crx.api.requests.ClanUnregister;
import moe.crx.api.responses.ActiveClansResponse;

import java.util.ArrayList;
import java.util.Objects;

public final class ClanWatcher extends AbstractVerticle {

    public static final String CLAN_REGISTER = "clan.register";
    public static final String CLAN_UNREGISTER = "clan.unregister";
    private static final String OPENED_CLANS = "openedClans";
    public static final String CLAN_REGISTERED = "clan_registered";
    public static final String CLAN_UNREGISTERED = "clan_unregistered";
    public static final int INTERNAL_ERROR = -1;
    public static final int REGISTRATION_ERROR = -2;
    public static final String CLAN_LIST = "clan.list";

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().<ClanRegister>consumer(CLAN_REGISTER, event -> lockAndGetClanList(event, this::registerClan));
        vertx.eventBus().<ClanUnregister>consumer(CLAN_UNREGISTER, event -> lockAndGetClanList(event, this::unregisterClan));
        vertx.eventBus().consumer(CLAN_LIST, event -> lockAndGetClanList(event, this::getActiveClans));

        System.out.println("[ClanWatcher] Clan watcher started.");
        startPromise.complete();
    }

    interface ClansOperation<T> {
        void perform(Message<T> event, AsyncMap<String, ArrayList<String>> map, ArrayList<String> list);
    }

    private <T> void lockAndGetClanList(Message<T> event, ClansOperation<T> operation) {
        vertx.sharedData().getLock(OPENED_CLANS, lockResult -> {
            if (lockResult.failed()) {
                event.fail(INTERNAL_ERROR, "get_lock_error");
                return;
            }

            vertx.sharedData().<String, ArrayList<String>>getAsyncMap(OPENED_CLANS, mapResult -> {
                if (mapResult.failed()) {
                    lockResult.result().release();
                    event.fail(INTERNAL_ERROR, "get_map_error");
                    return;
                }

                var map = mapResult.result();
                map.get(OPENED_CLANS, getResult -> {
                    if (getResult.failed()) {
                        lockResult.result().release();
                        event.fail(INTERNAL_ERROR, "get_error");
                        return;
                    }

                    var list = Objects.requireNonNullElse(getResult.result(), new ArrayList<String>());
                    try {
                        operation.perform(event, map, list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lockResult.result().release();
                });
            });
        });
    }

    private void registerClan(Message<ClanRegister> event, AsyncMap<String, ArrayList<String>> map, ArrayList<String> clanList) {
        final var clan = event.body();
        if (clan == null) {
            event.fail(INTERNAL_ERROR, "body_is_null");
            return;
        }

        final var name = clan.getClanName();

        if (clanList.contains(name)) {
            event.fail(REGISTRATION_ERROR, "clan_already_registered");
            System.out.printf("[ClanWatcher] Clan %s already registered. Failing.%n", name);
            return;
        }

        clanList.add(name);
        map.put(OPENED_CLANS, clanList, putResult -> {
            if (putResult.failed()) {
                event.fail(INTERNAL_ERROR, "put_error");
                return;
            }

            event.reply(CLAN_REGISTERED);
            System.out.printf("[ClanWatcher] Clan %s went online.%n", name);
        });
    }

    private void unregisterClan(Message<ClanUnregister> event, AsyncMap<String, ArrayList<String>> map, ArrayList<String> clanList) {
        final var clan = event.body();
        if (clan == null) {
            event.fail(INTERNAL_ERROR, "body_is_null");
            return;
        }

        final var name = clan.getClanName();

        if (!clanList.contains(name)) {
            event.fail(REGISTRATION_ERROR, "clan_not_registered");
            System.out.printf("[ClanWatcher] Clan %s is not registered. Failing.%n", name);
            return;
        }

        clanList.remove(name);
        map.put(OPENED_CLANS, clanList, putResult -> {
            if (putResult.failed()) {
                event.fail(INTERNAL_ERROR, "put_error");
                return;
            }

            event.reply(CLAN_UNREGISTERED);
            System.out.printf("[ClanWatcher] Clan %s went offline.%n", name);
        });
    }

    private void getActiveClans(Message<Object> event, AsyncMap<String, ArrayList<String>> map, ArrayList<String> clanList) {
        event.reply(new ActiveClansResponse(clanList));
        System.out.printf("[ClanWatcher] Serving active clans list. (%d clans)%n", clanList.size());
    }
}
