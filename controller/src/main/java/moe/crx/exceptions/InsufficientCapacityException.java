package moe.crx.exceptions;

public final class InsufficientCapacityException extends IllegalArgumentException {
    public InsufficientCapacityException(String message) {
        super(message);
    }
}
