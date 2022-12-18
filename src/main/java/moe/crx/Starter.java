package moe.crx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import moe.crx.verticles.Administrator;
import moe.crx.verticles.ClanWatcher;
import moe.crx.verticles.Member;
import moe.crx.verticles.Moderator;
import moe.crx.verticles.factory.EnumerableFactory;

public final class Starter {

    public static <T extends Verticle> void deploy(Vertx vertx,
                              DeploymentOptions options,
                              EnumerableFactory<T> factory) {
        vertx.registerVerticleFactory(factory);
        vertx.deployVerticle(factory.deployName(), options, handler -> {
            if (handler.failed()) {
                System.out.printf("[Starter] Vertx failed to deploy verticle(s). (%s)%n", handler.cause());
                return;
            }

            System.out.printf("[Starter] Verticle(s) deployed with id %s.%n", handler.result());
        });
    }

    public static void main(String[] rawArgs) {
        var args = new InputArgs(rawArgs);
        Vertx.clusteredVertx(new VertxOptions(), result -> {
            if (result.failed()) {
                System.out.println("[Starter] Vertx failed to load!");
                return;
            }

            var vertx = result.result();
            var options = new DeploymentOptions().setInstances(args.getCount());

            switch (args.getType()) {
                case "watcher" -> deploy(vertx, options, new ClanWatcher.Factory(args.getStartId()));
                case "administrator" -> deploy(vertx, options, new Administrator.Factory(args.getStartId(),
                        args.getMaxMembers(), args.getMaxModerators()));
                case "member" -> deploy(vertx, options, new Member.Factory(args.getStartId(),
                        args.getJoinProbability(), args.getJoinDelay(), args.getChatDelay()));
                case "moderator" -> {
                    var offset = 0;
                    for (var clanId : args.getModeratorClanId()) {
                        deploy(vertx, options, new Moderator.Factory(args.getStartId() + offset, clanId));
                        offset += args.getCount();
                    }
                }
            }
        });
    }
}
