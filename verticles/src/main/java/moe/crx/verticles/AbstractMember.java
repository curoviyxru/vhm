package moe.crx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import moe.crx.api.requests.MemberSend;

import static moe.crx.verticles.ClanConstants.*;

public abstract class AbstractMember extends AbstractVerticle {

    protected void startListeningToChat(String currentClanName, String userName) {
        vertx.eventBus().<JsonObject>consumer(currentClanName + '.' + userName + MEMBER_SEND, event -> {
            var json = event.body();
            if (json == null) {
                event.fail(BAD_REQUEST_ERROR, "body_is_null");
                return;
            }

            var request = new MemberSend().fromJson(json);

            System.out.printf("[%s] %s sent me a message: %s. :) It's nice of him.%n",
                    userName,
                    request.getUserName(),
                    request.getMessage());
            event.reply(GOT_MESSAGE);
        });
    }
}
