package moe.crx.logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.FileWriter;
import java.io.IOException;

public final class FileLogger extends Logger {

    private final String tag;

    @Inject
    public FileLogger(@Named("tag") String tag) {
        this.tag = tag;
    }

    @Override
    public void log(String message, int index) {
        try (FileWriter writer = new FileWriter("current.log", true)) {
            writer.write(String.format("<%s>", tag));
            writer.write(String.format("[%d] %s", index, message));
            writer.write(String.format("</%s>%n", tag));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
