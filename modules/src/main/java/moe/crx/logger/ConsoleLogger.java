package moe.crx.logger;

public final class ConsoleLogger implements Logger {

    private int currentIndex = 1;

    @Override
    public void increaseIndex() {
        currentIndex += 1;
    }

    @Override
    public void log(String message) {
        System.out.printf("[%d] %s%n", currentIndex, message);
        increaseIndex();
    }
}
