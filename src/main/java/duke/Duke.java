package duke;

import duke.command.Storage;
import duke.command.Ui;

public abstract class Duke {
    public static final String HORIZONTAL_LINE = "______________________________";

    private static void printIntroduction() {
        final String INTRODUCTION =
                "Hihi, my name is Jay!" + System.lineSeparator() + "What can I do for you today?";

        System.out.println(HORIZONTAL_LINE);
        System.out.println(INTRODUCTION);
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    private static void printExit() {
        final String EXIT = "Goodbye! Hope to see you again!";

        System.out.println(HORIZONTAL_LINE);
        System.out.println(EXIT);
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    /**
     * Starts Duke
     * 
     * @param args the arguments provided
     */
    public static void main(String[] args) {
        Storage.loadFile();

        printIntroduction();
        Ui.readInputs();
        printExit();

        Storage.saveFile();
    }
}
