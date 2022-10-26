package moe.crx.logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public final class CompositeLogger extends Logger {

    private final Logger loggerA;
    private final Logger loggerB;

    @Inject
    public CompositeLogger(@Named("compositeLoggerA") Logger loggerA,
                           @Named("compositeLoggerB") Logger loggerB) {
        this.loggerA = loggerA;
        this.loggerB = loggerB;
    }

    @Override
    public void log(String message) {
        loggerA.log(message);
        loggerB.currentIndex += 1;
        loggerB.log(message);
        loggerA.currentIndex += 1;
    }
}
