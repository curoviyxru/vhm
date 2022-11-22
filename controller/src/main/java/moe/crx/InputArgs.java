package moe.crx;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public final class InputArgs {

    private enum ParseState {
        NONE, CAPACITY, FILEPATH
    }

    private String filePath;
    private int capacity;

    public InputArgs(@NotNull String[] args) {
        parse(args);
    }

    private void parse(@NotNull String[] args) {
        ParseState state = ParseState.NONE;
        for (String arg : args) {
            switch (arg) {
                case "--capacity" -> state = ParseState.CAPACITY;
                case "--filepath" -> state = ParseState.FILEPATH;
                default -> {
                    switch (state) {
                        case CAPACITY -> {
                            try {
                                capacity = Integer.parseInt(arg);
                            } catch (NumberFormatException ignored) {
                            }
                            state = ParseState.NONE;
                        }
                        case FILEPATH -> {
                            filePath = arg;
                            state = ParseState.NONE;
                        }
                    }
                }
            }
        }

        if (filePath == null) {
            throw new IllegalStateException("File path is not declared. Use launch argument --filepath.");
        }
    }
}
