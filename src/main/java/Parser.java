public abstract class Parser {
    public static Task parseTask(String type, String input) throws InvalidTodoDescriptionException,
            InvalidDeadlineDescriptionException, InvalidEventDescriptionException {
        int descriptionIndex;
        String description;
        Task newTask;

        switch (type) {
        case InputManager.TODO_PHRASE:
            descriptionIndex = input.indexOf(InputManager.TODO_PHRASE);

            try {
                description = input.substring(
                        descriptionIndex + InputManager.TODO_PHRASE.length() + 1, input.length());
            } catch (StringIndexOutOfBoundsException e) {
                throw new InvalidTodoDescriptionException();
            }

            newTask = new Todo(description);
            break;
        case InputManager.DEADLINE_PHRASE:
            descriptionIndex = input.indexOf(InputManager.DEADLINE_PHRASE);
            int byIndex = input.indexOf(InputManager.BY_PHRASE);

            String by;
            try {
                // add one to remove space
                // minus one to remove space
                description = input.substring(
                        descriptionIndex + InputManager.DEADLINE_PHRASE.length() + 1, byIndex - 1);
                by = input.substring(byIndex + InputManager.BY_PHRASE.length() + 1, input.length());
            } catch (StringIndexOutOfBoundsException e) {
                throw new InvalidDeadlineDescriptionException();
            }

            newTask = new Deadline(description, by);
            break;
        case InputManager.EVENT_PHRASE:
            descriptionIndex = input.indexOf(InputManager.EVENT_PHRASE);
            int atIndex = input.indexOf(InputManager.AT_PHRASE);

            String at;
            try {
                // add one to remove space
                // minus one to remove space
                description = input.substring(
                        descriptionIndex + InputManager.EVENT_PHRASE.length() + 1, atIndex - 1);
                at = input.substring(atIndex + InputManager.AT_PHRASE.length() + 1, input.length());
            } catch (StringIndexOutOfBoundsException e) {
                throw new InvalidEventDescriptionException();
            }

            newTask = new Event(description, at);
            break;
        default:
            newTask = null;
            break;
        }

        return newTask;
    }

    public static int parseTaskNumber(String type, String input) {
        String taskNumString;
        int taskNumInt;

        switch (type) {
        case InputManager.MARK_PHRASE:
            taskNumString = input.substring(InputManager.MARK_PHRASE.length() + 1);
            taskNumInt = Integer.parseInt(taskNumString);
            break;
        case InputManager.UNMARK_PHRASE:
            taskNumString = input.substring(InputManager.UNMARK_PHRASE.length() + 1);
            taskNumInt = Integer.parseInt(taskNumString);
            break;

        default:
            taskNumInt = 1;
            break;
        }

        // zero-index
        taskNumInt -= 1;

        return taskNumInt;
    }
}
