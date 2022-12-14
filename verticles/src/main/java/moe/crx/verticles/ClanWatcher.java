package moe.crx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.Lock;
import moe.crx.api.requests.ClanRegister;
import moe.crx.api.requests.ClanUnregister;
import moe.crx.api.responses.ActiveClansResponse;
import moe.crx.verticles.factory.EnumerableFactory;

import java.util.ArrayList;
import java.util.Objects;

import static moe.crx.verticles.ClanConstants.*;

public final class ClanWatcher extends AbstractVerticle {

    private final String watcherName;

    public ClanWatcher(String watcherName) {
        this.watcherName = watcherName;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().<JsonObject>consumer(CLAN_REGISTER, event -> lockAndGetClanList(event, this::registerClan));
        vertx.eventBus().<JsonObject>consumer(CLAN_UNREGISTER, event -> lockAndGetClanList(event, this::unregisterClan));
        vertx.eventBus().consumer(CLAN_LIST, event -> lockAndGetClanList(event, this::getActiveClans));

        System.out.printf("[%s] Clan watcher has started.%n", watcherName);
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        System.out.printf("[%s] Clan watcher has stopped.%n", watcherName);
        stopPromise.complete();
    }

    interface ClansOperation<T> {
        void perform(Message<T> event, AsyncResult<Lock> lockResult, AsyncMap<String, ArrayList<String>> map, ArrayList<String> list);
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
                        operation.perform(event, lockResult, map, list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        });
    }

    private void registerClan(Message<JsonObject> event, AsyncResult<Lock> lockResult, AsyncMap<String, ArrayList<String>> map, ArrayList<String> clanList) {
        final var json = event.body();
        if (json == null) {
            lockResult.result().release();
            event.fail(BAD_REQUEST_ERROR, "body_is_null");
            return;
        }

        var clan = new ClanRegister().fromJson(json);
        final var name = clan.getClanName();

        if (clanList.contains(name)) {
            lockResult.result().release();
            event.fail(REGISTRATION_ERROR, "clan_already_registered");
            System.out.printf("[%s] Clan %s already registered. Failing.%n", watcherName, name);
            return;
        }

        clanList.add(name);
        map.put(OPENED_CLANS, clanList, putResult -> {
            if (putResult.failed()) {
                lockResult.result().release();
                event.fail(INTERNAL_ERROR, "put_error");
                return;
            }

            lockResult.result().release();
            event.reply(CLAN_REGISTERED);
            System.out.printf("[%s] Clan %s went online.%n", watcherName, name);
        });
    }

    private void unregisterClan(Message<JsonObject> event, AsyncResult<Lock> lockResult, AsyncMap<String, ArrayList<String>> map, ArrayList<String> clanList) {
        final var json = event.body();
        if (json == null) {
            lockResult.result().release();
            event.fail(BAD_REQUEST_ERROR, "body_is_null");
            return;
        }

        var clan = new ClanUnregister().fromJson(json);
        final var name = clan.getClanName();

        if (!clanList.contains(name)) {
            lockResult.result().release();
            event.fail(REGISTRATION_ERROR, "clan_not_registered");
            System.out.printf("[%s] Clan %s is not registered. Failing.%n", watcherName, name);
            return;
        }

        clanList.remove(name);
        map.put(OPENED_CLANS, clanList, putResult -> {
            if (putResult.failed()) {
                lockResult.result().release();
                event.fail(INTERNAL_ERROR, "put_error");
                return;
            }

            lockResult.result().release();
            event.reply(CLAN_UNREGISTERED);
            System.out.printf("[%s] Clan %s went offline.%n", watcherName, name);
        });
    }

    private void getActiveClans(Message<Object> event, AsyncResult<Lock> lockResult, AsyncMap<String, ArrayList<String>> map, ArrayList<String> clanList) {
        lockResult.result().release();
        event.reply(new ActiveClansResponse(clanList).toJson());
        System.out.printf("[%s] Serving active clans list. (%d clans)%n", watcherName, clanList.size());
    }

    public static final class Factory extends EnumerableFactory<ClanWatcher> {

        public Factory(int startId) {
            super(startId);
        }

        @Override
        public Class<ClanWatcher> verticleType() {
            return ClanWatcher.class;
        }

        @Override
        public ClanWatcher createVerticle() {
            return new ClanWatcher(String.format(WATCHER_NAME_FORMAT, getNextId()));
        }
    }
}
