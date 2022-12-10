package moe.crx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class InputArgs {

    private enum ParseState {
        NONE, TYPE, COUNT, START_ID, MAX_MEMBERS, MAX_MODERATORS, MODERATOR_CLAN_ID, JOIN_PROBABILITY, JOIN_DELAY,
        CHAT_DELAY
    }

    private String type = "watcher";
    private int count = 1;
    private int startId = 0;
    private int maxMembers = 10;
    private int maxModerators = 1;
    private int moderatorClanId = 0;
    private int joinProbability = 50;
    private int joinDelay = 5000;
    private int chatDelay = 10000;

    public InputArgs(@NotNull String[] args) {
        parse(args);
    }

    private void parse(@NotNull String[] args) {
        ParseState state = ParseState.NONE;
        if (args != null) for (String arg : args) {
            switch (arg) {
                case "--type" -> state = ParseState.TYPE;
                case "--count" -> state = ParseState.COUNT;
                case "--startId" -> state = ParseState.START_ID;
                case "--maxMembers" -> state = ParseState.MAX_MEMBERS;
                case "--maxModerators" -> state = ParseState.MAX_MODERATORS;
                case "--moderatorClanId" -> state = ParseState.MODERATOR_CLAN_ID;
                case "--joinProbability" -> state = ParseState.JOIN_PROBABILITY;
                case "--joinDelay" -> state = ParseState.JOIN_DELAY;
                case "--chatDelay" -> state = ParseState.CHAT_DELAY;
                default -> {
                    switch (state) {
                        case TYPE -> type = arg;
                        case COUNT -> count = Integer.parseInt(arg);
                        case START_ID -> startId = Integer.parseInt(arg);
                        case MAX_MEMBERS -> maxMembers = Integer.parseInt(arg);
                        case MAX_MODERATORS -> maxModerators = Integer.parseInt(arg);
                        case MODERATOR_CLAN_ID -> moderatorClanId = Integer.parseInt(arg);
                        case JOIN_PROBABILITY -> joinProbability = Integer.parseInt(arg);
                        case JOIN_DELAY -> joinDelay = Integer.parseInt(arg);
                        case CHAT_DELAY -> chatDelay = Integer.parseInt(arg);
                    }
                    state = ParseState.NONE;
                }
            }
        }
        switch (type) {
            case "watcher", "moderator", "administrator", "member":
                break;
            default:
                throw new RuntimeException("You declared wrong type. watcher, moderator, administrator, member are only allowed.");
        }
    }
}
