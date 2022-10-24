package moe.crx.logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public final class CompositeLogger extends Logger {

    private final Logger loggerA;
    private final Logger loggerB;

    @Inject
    public CompositeLogger(@Named("console") Logger loggerA,
                           @Named("file") Logger loggerB) {
        this.loggerA = loggerA;
        this.loggerB = loggerB;
    }

    @Override
    public void log(String message) {
        log(message, currentIndex);
        currentIndex += 2;
    }

    @Override
    public void log(String message, int index) {
        loggerA.log(message, index);
        loggerB.log(message, index + 1);
    }
}
