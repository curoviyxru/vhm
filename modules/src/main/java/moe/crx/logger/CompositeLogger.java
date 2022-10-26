package moe.crx.logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public final class CompositeLogger implements Logger {

    private final Logger loggerA;
    private final Logger loggerB;

    @Inject
    public CompositeLogger(@Named("compositeLoggerA") Logger loggerA,
                           @Named("compositeLoggerB") Logger loggerB) {
        this.loggerA = loggerA;
        this.loggerB = loggerB;
    }

    @Override
    public void increaseIndex() {
        loggerA.increaseIndex();
        loggerB.increaseIndex();
    }

    @Override
    public void log(String message) {
        loggerA.log(message);
        loggerB.increaseIndex();
        loggerB.log(message);
        loggerA.increaseIndex();
    }
}
