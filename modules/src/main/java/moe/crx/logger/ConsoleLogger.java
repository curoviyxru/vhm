package moe.crx.logger;

public final class ConsoleLogger extends Logger {

    @Override
    public void log(String message, int index) {
        System.out.printf("[%d] %s%n", index, message);
    }
}
