package moe.crx.verticles;

import io.vertx.core.AbstractVerticle;
import moe.crx.api.requests.MemberSend;

import static moe.crx.verticles.ClanWatcher.INTERNAL_ERROR;

public abstract class AbstractMember extends AbstractVerticle {

    public static final String MEMBER_SEND = ".member.send";
    public static final String GOT_MESSAGE = "got_message";

    protected void startListeningToChat(String currentClanName, String userName) {
        vertx.eventBus().<MemberSend>consumer(currentClanName + '.' + userName + MEMBER_SEND, event -> {
            var request = event.body();
            if (request == null) {
                event.fail(INTERNAL_ERROR, "body_is_null");
                return;
            }

            System.out.printf("[%s] %s sent me a message: %s. :) It's nice of him.%n",
                    userName,
                    request.getUserName(),
                    request.getMessage());
            event.reply(GOT_MESSAGE);
        });
    }
}
