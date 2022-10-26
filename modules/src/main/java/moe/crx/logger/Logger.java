package moe.crx.logger;

public abstract class Logger {

    protected int currentIndex = 1;

    public abstract void log(String message);
}
