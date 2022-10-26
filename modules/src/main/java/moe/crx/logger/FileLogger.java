package moe.crx.logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileLogger implements Logger {

    private final String tag;
    private final File file = new File("current.log");
    private int currentIndex = 1;

    @Inject
    public FileLogger(@Named("loggerTag") String tag) {
        this.tag = tag;
        file.delete();
    }

    @Override
    public void increaseIndex() {
        currentIndex += 1;
    }

    @Override
    public void log(String message) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(String.format("<%s>", tag));
            writer.write(String.format("[%d] %s", currentIndex, message));
            writer.write(String.format("</%s>%n", tag));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        increaseIndex();
    }
}
