package moe.crx;

import com.google.inject.Inject;
import moe.crx.logger.Logger;

import java.util.NoSuchElementException;
import java.util.Scanner;

public final class Application {

    public final Logger logger;

    @Inject
    public Application(Logger logger) {
        this.logger = logger;
    }

    public void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for new lines. Key in Ctrl+D to exit.");
            while (true) {
                logger.log(scanner.nextLine());
            }
        } catch (IllegalStateException | NoSuchElementException e) {
        }
    }
}
