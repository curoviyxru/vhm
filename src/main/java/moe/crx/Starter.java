package moe.crx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import moe.crx.verticles.Administrator;
import moe.crx.verticles.ClanWatcher;
import moe.crx.verticles.Member;
import moe.crx.verticles.Moderator;

public final class Starter {

    public static void main(String[] rawArgs) {
        var args = new InputArgs(rawArgs);
        Vertx.clusteredVertx(new VertxOptions(), result -> {
            if (result.failed()) {
                System.out.println("[Starter] Vertx failed to load!");
                return;
            }

            final var vertx = result.result();

            switch (args.getType()) {
                case "watcher" -> {
                    var factory = new ClanWatcher.Factory(args.getStartId());
                    vertx.registerVerticleFactory(factory);
                    vertx.deployVerticle(factory.deployName(), new DeploymentOptions().setInstances(args.getCount()));
                }
                case "administrator" -> {
                    var factory = new Administrator.Factory(args.getStartId(), args.getMaxMembers(), args.getMaxModerators());
                    vertx.registerVerticleFactory(factory);
                    vertx.deployVerticle(factory.deployName(), new DeploymentOptions().setInstances(args.getCount()));
                }
                case "moderator" -> {
                    var offset = 0;
                    for (Integer clanId : args.getModeratorClanId()) {
                        var factory = new Moderator.Factory(args.getStartId() + offset, clanId);
                        vertx.registerVerticleFactory(factory);
                        vertx.deployVerticle(factory.deployName(), new DeploymentOptions().setInstances(args.getCount()));
                        vertx.unregisterVerticleFactory(factory);
                        offset += args.getCount();
                    }
                }
                case "member" -> {
                    var factory = new Member.Factory(args.getStartId(), args.getJoinProbability(), args.getJoinDelay(), args.getChatDelay());
                    vertx.registerVerticleFactory(factory);
                    vertx.deployVerticle(factory.deployName(), new DeploymentOptions().setInstances(args.getCount()));
                }
            }
        });
    }
}
