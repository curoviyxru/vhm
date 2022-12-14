package moe.crx.verticles;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import moe.crx.api.requests.ClanJoin;
import moe.crx.api.requests.MemberSend;
import moe.crx.api.responses.ActiveClansResponse;
import moe.crx.api.responses.ClanMembersResponse;
import moe.crx.verticles.factory.EnumerableFactory;

import java.util.Random;

import static moe.crx.api.requests.ClanJoin.MEMBER;
import static moe.crx.verticles.Administrator.CLAN_JOIN;
import static moe.crx.verticles.Administrator.CLAN_MEMBERS_LIST;
import static moe.crx.verticles.ClanWatcher.CLAN_LIST;
import static moe.crx.verticles.ClanWatcher.INTERNAL_ERROR;

public final class Member extends AbstractMember {

    public static final String MEMBER_NAME_FORMAT = "Member#%d";
    private static final Random random = new Random();
    private static final int FULL_PROBABILITY = 100;
    private static final String MESSAGE_FORMAT = "Hello, %s!";

    private final String userName;
    private String currentClanName;
    private final int joinProbability;
    private final int joinDelay;
    private final int chatDelay;

    public Member(String userName, int joinProbability, int joinDelay, int chatDelay) {
        this.userName = userName;
        this.joinProbability = joinProbability;
        this.joinDelay = joinDelay;
        this.chatDelay = chatDelay;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        tryJoin();
        startPromise.complete();
    }

    private void tryJoin() {
        vertx.setTimer(joinDelay, timerHandler -> vertx.eventBus().<JsonObject>request(CLAN_LIST, null, listHandler -> {
            if (listHandler.failed()) {
                if (random.nextInt(FULL_PROBABILITY) >= joinProbability) {
                    System.out.printf("[%s] I can't get clans list! ;( Will try again later...%n", userName);
                    tryJoin();
                } else {
                    System.out.printf("[%s] I can't get clans list! ;( I'm tired...%n", userName);
                }
                return;
            }

            var body = listHandler.result().body();
            if (body == null)
                return;

            var response = new ActiveClansResponse().fromJson(body).getClans();
            var randomId = random.nextInt(response.size());
            var clanName = response.get(randomId);
            var request = new ClanJoin(MEMBER, userName).toJson();

            System.out.printf("[%s] Trying to join clan %s! :/%n", userName, clanName);
            vertx.eventBus().<String>request(clanName + CLAN_JOIN, request, joinHandler -> {
                if (joinHandler.failed()) {
                    if (random.nextInt(FULL_PROBABILITY) >= joinProbability) {
                        System.out.printf("[%s] I failed to join clan %s! ;( Will try again later...%n", userName, clanName);
                        tryJoin();
                    } else {
                        System.out.printf("[%s] I failed to join clan %s! ;( I'm tired...%n", userName, clanName);
                    }
                    return;
                }

                this.currentClanName = clanName;
                System.out.printf("[%s] I joined clan %s! Yay! :)%n", userName, clanName);
                startChatting();
            });
        }));
    }

    private void startChatting() {
        startListeningToChat(currentClanName, userName);
        vertx.setPeriodic(chatDelay, chatTimerHandler -> vertx.eventBus().<JsonObject>request(currentClanName + CLAN_MEMBERS_LIST, null, clanHandler -> {
            if (clanHandler.failed()) {
                System.out.printf("[%s] I failed to get members of clan %s! ;( Will try again later...%n", userName, currentClanName);
                return;
            }

            var body = clanHandler.result().body();
            if (body == null)
                return;

            var list = new ClanMembersResponse().fromJson(body).getMembers();
            var randomId = random.nextInt(list.size());
            while (list.size() > 1 && list.get(randomId).equals(userName)) {
                randomId = random.nextInt(list.size());
            }
            var recipient = list.get(randomId);
            var request = new MemberSend(userName, String.format(MESSAGE_FORMAT, recipient)).toJson();

            System.out.printf("[%s] Trying to send message to %s! :/%n", userName, recipient);
            vertx.eventBus().<String>request(currentClanName + '.' + recipient + MEMBER_SEND, request, sendHandler -> {
                if (sendHandler.failed()) {
                    System.out.printf("[%s] I failed to send message to %s! ;( Will try again later...%n", userName, recipient);
                    return;
                }

                System.out.printf("[%s] I sent message to %s! Yay! :)%n", userName, recipient);
            });
        }));
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        stopPromise.complete();
    }

    public static final class Factory extends EnumerableFactory<Member> {

        private final int joinProbability;
        private final int joinDelay;
        private final int chatDelay;

        public Factory(int startId, int joinProbability, int joinDelay, int chatDelay) {
            super(startId);
            this.joinProbability = joinProbability;
            this.joinDelay = joinDelay;
            this.chatDelay = chatDelay;
        }

        @Override
        public Class<Member> verticleType() {
            return Member.class;
        }

        @Override
        public Member createVerticle() {
            var nextId = getNextId();
            return new Member(String.format(MEMBER_NAME_FORMAT, nextId), joinProbability, joinDelay, chatDelay);
        }
    }
}
