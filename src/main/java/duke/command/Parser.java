package duke.command;

import duke.exception.InvalidInputException;
import duke.exception.InvalidOutputException;
import duke.exception.MissingDeadlineDescriptionException;
import duke.exception.MissingEventDescriptionException;
import duke.exception.MissingTaskNumberException;
import duke.exception.MissingTodoDescriptionException;
import duke.exception.NonIntegerTaskNumberException;
import duke.exception.OutOfBoundsTaskNumberException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;

public abstract class Parser {
    /**
     * Parses the task according to user input and the type of task provided
     * 
     * @param type  the type of task to parse
     * @param input the user input
     * @return the new task created
     * @throws MissingTodoDescriptionException     if no description provided for todo task
     * @throws MissingDeadlineDescriptionException if no description provided for deadline task
     * @throws MissingEventDescriptionException    if no description provided for event task
     */
    public static Task parseTask(String type, String input) throws MissingTodoDescriptionException,
            MissingDeadlineDescriptionException, MissingEventDescriptionException {
        int descriptionIndex;
        String description;
        Task newTask;

        switch (type) {
        case Ui.TODO_PHRASE:
            descriptionIndex = input.indexOf(Ui.TODO_PHRASE);

            try {
                description = input.substring(descriptionIndex + Ui.TODO_PHRASE.length() + 1,
                        input.length());
            } catch (StringIndexOutOfBoundsException e) {
                throw new MissingTodoDescriptionException();
            }

            newTask = new Todo(description);

            break;
        case Ui.DEADLINE_PHRASE:
            descriptionIndex = input.indexOf(Ui.DEADLINE_PHRASE);
            int byIndex = input.indexOf(Ui.BY_PHRASE);

            String by;
            try {
                // add one to remove space
                // minus one to remove space
                description = input.substring(descriptionIndex + Ui.DEADLINE_PHRASE.length() + 1,
                        byIndex - 1);
                by = input.substring(byIndex + Ui.BY_PHRASE.length() + 1, input.length());
            } catch (StringIndexOutOfBoundsException e) {
                throw new MissingDeadlineDescriptionException();
            }

            newTask = new Deadline(description, by);

            break;
        case Ui.EVENT_PHRASE:
            descriptionIndex = input.indexOf(Ui.EVENT_PHRASE);
            int atIndex = input.indexOf(Ui.AT_PHRASE);

            String at;
            try {
                // add one to remove space
                // minus one to remove space
                description = input.substring(descriptionIndex + Ui.EVENT_PHRASE.length() + 1,
                        atIndex - 1);
                at = input.substring(atIndex + Ui.AT_PHRASE.length() + 1, input.length());
            } catch (StringIndexOutOfBoundsException e) {
                throw new MissingEventDescriptionException();
            }

            newTask = new Event(description, at);

            break;
        default:
            newTask = null;

            break;
        }

        return newTask;
    }

    /**
     * Parses the task number according to user input and the type of task provided
     * 
     * @param type  the type of task to parse
     * @param input the user input
     * @return the task number from the input
     * @throws MissingTaskNumberException     if the task number is missing
     * @throws NonIntegerTaskNumberException  if the task number is not an integer
     * @throws OutOfBoundsTaskNumberException if the task number is <= 0 or > current number of
     *                                        tasks
     */
    public static int parseTaskNumber(String type, String input) throws MissingTaskNumberException,
            NonIntegerTaskNumberException, OutOfBoundsTaskNumberException {
        String taskNumString;
        int taskNumInt;

        switch (type) {
        case Ui.MARK_PHRASE:
            try {
                taskNumString = input.substring(Ui.MARK_PHRASE.length() + 1);
            } catch (StringIndexOutOfBoundsException e) {
                throw new MissingTaskNumberException();
            }

            try {
                taskNumInt = Integer.parseInt(taskNumString);
            } catch (NumberFormatException e) {
                throw new NonIntegerTaskNumberException();
            }

            if (!isValidTaskNumber(taskNumInt)) {
                throw new OutOfBoundsTaskNumberException();
            }

            break;
        case Ui.UNMARK_PHRASE:
            try {
                taskNumString = input.substring(Ui.UNMARK_PHRASE.length() + 1);
            } catch (StringIndexOutOfBoundsException e) {
                throw new MissingTaskNumberException();
            }

            try {
                taskNumInt = Integer.parseInt(taskNumString);
            } catch (NumberFormatException e) {
                throw new NonIntegerTaskNumberException();
            }

            if (!isValidTaskNumber(taskNumInt)) {
                throw new OutOfBoundsTaskNumberException();
            }

            break;
        case Ui.DELETE_PHRASE:
            try {
                taskNumString = input.substring(Ui.DELETE_PHRASE.length() + 1);
            } catch (StringIndexOutOfBoundsException e) {
                throw new MissingTaskNumberException();
            }

            try {
                taskNumInt = Integer.parseInt(taskNumString);
            } catch (NumberFormatException e) {
                throw new NonIntegerTaskNumberException();
            }

            if (!isValidTaskNumber(taskNumInt)) {
                throw new OutOfBoundsTaskNumberException();
            }

            break;
        default:
            taskNumInt = 1;
            break;
        }

        // zero-index
        taskNumInt -= 1;

        return taskNumInt;
    }

    /**
     * Parses the saved file data
     * 
     * @param line the current input line from the file
     * @return the saved task and whether the task is marked or not
     * @throws InvalidInputException if the saved data has an invalid format
     */
    public static String[] parseFileInputs(String line) throws InvalidInputException {
        String[] parts = line.split(", ");

        String taskType = parts[0];
        String markStatus = parts[1];
        String taskDescription = parts[2];

        String inputTask;
        switch (taskType) {
        case Ui.TODO_PHRASE:
            inputTask = Ui.TODO_PHRASE + " " + taskDescription;

            break;

        case Ui.DEADLINE_PHRASE:
            inputTask = Ui.DEADLINE_PHRASE + " " + taskDescription + " /by " + parts[3];

            break;

        case Ui.EVENT_PHRASE:
            inputTask = Ui.EVENT_PHRASE + " " + taskDescription + " /at " + parts[3];

            break;

        default:
            throw new InvalidInputException();
        }

        String inputMark;
        if (markStatus.equals("0")) {
            inputMark = Ui.UNMARK_PHRASE + " " + (Task.getTaskCount() + 1);
        } else if (markStatus.equals("1")) {
            inputMark = Ui.MARK_PHRASE + " " + (Task.getTaskCount() + 1);
        } else {
            throw new InvalidInputException();
        }

        String[] fileData = {inputTask, inputMark};

        return fileData;
    }

    /**
     * Parses the file data to be saved
     * 
     * @param line the current line from the task list to save
     * @return the string to be writtent to the file
     * @throws InvalidOutputException if the data to save has an invalid format
     */
    public static String parseFileOutputs(String line) throws InvalidOutputException {
        String[] parts = line.split("]");

        String taskType = parts[0].substring(parts[0].length() - 1);
        String markStatus = parts[1].substring(parts[1].length() - 1);

        String outputTask;
        String description;
        String extra = "";
        switch (taskType) {
        case "T":
            outputTask = "todo";
            description = parts[2].substring(1);

            break;

        case "D":
            outputTask = "deadline";
            int byIndex = parts[2].indexOf(" (by: ");
            description = parts[2].substring(1, byIndex);
            extra = parts[2].substring(byIndex + 6, parts[2].length() - 1);

            break;

        case "E":
            int atIndex = parts[2].indexOf(" (at: ");
            description = parts[2].substring(1, atIndex);
            extra = parts[2].substring(atIndex + 6, parts[2].length() - 1);

            outputTask = "event";

            break;

        default:
            throw new InvalidOutputException();
        }

        String outputMark;
        if (markStatus.equals(" ")) {
            outputMark = "0";
        } else if (markStatus.equals("X")) {
            outputMark = "1";
        } else {
            throw new InvalidOutputException();
        }

        String fileData;
        if (extra.isBlank()) {
            fileData = outputTask + ", " + outputMark + ", " + description;
        } else {
            fileData = outputTask + ", " + outputMark + ", " + description + ", " + extra;
        }

        return fileData;
    }

    private static boolean isValidTaskNumber(int taskNum) {
        if (taskNum > Task.getTaskCount() || taskNum <= 0) {
            return false;
        }

        return true;
    }
}
