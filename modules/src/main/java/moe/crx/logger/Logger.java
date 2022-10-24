package moe.crx.logger;

public abstract class Logger {

    protected int currentIndex = 1;

    public abstract void log(String message, int index);

    public void log(String message) {
        log(message, currentIndex++);
    }
}
