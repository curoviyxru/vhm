package moe.crx.verticles;

import io.vertx.core.eventbus.Message;
import io.vertx.core.shareddata.AsyncMap;
import moe.crx.api.responses.ClanJoinResponse;

import java.util.ArrayList;
import java.util.Objects;

import static moe.crx.verticles.Administrator.CLAN_MEMBERS;
import static moe.crx.verticles.Administrator.MEMBER_JOINED;
import static moe.crx.verticles.ClanWatcher.INTERNAL_ERROR;
import static moe.crx.verticles.ClanWatcher.REGISTRATION_ERROR;

public abstract class AbstractModerator extends AbstractMember {

    interface MembersOperation<T> {
        void perform(Message<T> event,
                     AsyncMap<String, ArrayList<String>> map,
                     ArrayList<String> list);
    }
    protected <T> void lockAndGetMembersList(Message<T> event, String clanName, MembersOperation<T> operation) {
        vertx.sharedData().getLock(clanName, lockResult -> {
            if (lockResult.failed()) {
                event.fail(INTERNAL_ERROR, "get_lock_error");
                return;
            }

            vertx.sharedData().<String, ArrayList<String>>getAsyncMap(clanName + CLAN_MEMBERS, mapResult -> {
                if (mapResult.failed()) {
                    lockResult.result().release();
                    event.fail(INTERNAL_ERROR, "get_members_map_error");
                    return;
                }

                var map = mapResult.result();
                map.get(clanName + CLAN_MEMBERS, getResult -> {
                    if (getResult.failed()) {
                        lockResult.result().release();
                        event.fail(INTERNAL_ERROR, "get_members_error");
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

    protected <T> void addNewMember(int maxMembers,
                                String clanName,
                                String userName,
                                Message<T> event,
                                AsyncMap<String, ArrayList<String>> map,
                                ArrayList<String> list) {
        if (list.size() >= maxMembers) {
            event.fail(REGISTRATION_ERROR, "maximum_members");
            return;
        }

        if (list.contains(userName)) {
            event.fail(REGISTRATION_ERROR, "member_already_registered");
            return;
        }

        list.add(userName);
        map.put(clanName + CLAN_MEMBERS, list, putResult -> {
            if (putResult.failed()) {
                event.fail(INTERNAL_ERROR, "put_members_error");
                return;
            }

            event.reply(new ClanJoinResponse(MEMBER_JOINED, maxMembers).toJson());
            System.out.printf("[%s] User %s added.%n", clanName, userName);
        });
    }
}
