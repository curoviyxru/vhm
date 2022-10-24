package moe.crx;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import moe.crx.logger.CompositeLogger;
import moe.crx.logger.ConsoleLogger;
import moe.crx.logger.FileLogger;
import moe.crx.logger.Logger;
import org.jetbrains.annotations.NotNull;

public final class LoggerModule extends AbstractModule {

    private String tag;
    private String type;

    public LoggerModule(@NotNull String[] args) {
        parse(args);
    }

    private void parse(@NotNull String[] args) {
        boolean isTag = false, isType = false;
        for (String arg : args) {
            switch (arg) {
                case "--type" -> {
                    isType = true;
                    isTag = false;
                }
                case "--tag" -> {
                    isType = false;
                    isTag = true;
                }
                default -> {
                    if (isType)
                        type = arg;
                    if (isTag)
                        tag = arg;
                    isType = false;
                    isTag = false;
                }
            }
        }

        if (type == null) {
            throw new IllegalStateException("Logger type is not declared. Use launch argument --type.");
        }
        if (tag == null && !type.equals("console")) {
            throw new IllegalStateException("Logging tag is not declared. Use launch argument --tag.");
        }
    }

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("tag")).toInstance(tag);
        bind(Logger.class).annotatedWith(Names.named("console")).to(ConsoleLogger.class);
        bind(Logger.class).annotatedWith(Names.named("file")).to(FileLogger.class);

        switch (type) {
            case "file" -> bind(Logger.class).to(FileLogger.class);
            case "console" -> bind(Logger.class).to(ConsoleLogger.class);
            default -> bind(Logger.class).to(CompositeLogger.class);
        }
    }
}
