package moe.crx.logger;

public final class ConsoleLogger extends Logger {

    @Override
    public void log(String message) {
        System.out.printf("[%d] %s%n", currentIndex, message);
        currentIndex += 1;
    }
}
