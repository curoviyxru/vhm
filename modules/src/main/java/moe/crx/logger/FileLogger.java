package moe.crx.logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileLogger extends Logger {

    private final String tag;
    private final File file = new File("current.log");

    @Inject
    public FileLogger(@Named("loggerTag") String tag) {
        this.tag = tag;
        file.delete();
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
        currentIndex += 1;
    }
}
