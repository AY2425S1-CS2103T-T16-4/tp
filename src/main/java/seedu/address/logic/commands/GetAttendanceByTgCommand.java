package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_GROUP;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialGroup;
import seedu.address.ui.AttendanceWindow;

/**
 * Retrieves the attendance of all students in a specific tutorial group.
 */
public class GetAttendanceByTgCommand extends Command {
    public static final String COMMAND_WORD = "getattg";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Retrieves the attendance of all students in a specific tutorial group.\n"
            + "Parameters: "
            + PREFIX_TUTORIAL_GROUP + "TUTORIAL_GROUP\n"
            + "Example: "
            + COMMAND_WORD + " "
            + PREFIX_TUTORIAL_GROUP + " A01";

    public static final String MESSAGE_SUCCESS = "Attendance for students in tutorial group %1$s:\n%2$s";
    public static final String MESSAGE_NO_STUDENTS = "No students found in tutorial group %1$s.";
    private static AttendanceWindow currentWindow;

    private final TutorialGroup tutorialGroup;


    /**
     * Creates a GetAttendanceByTGCommand to retrieve attendance records of students in the specified tutorial group.
     *
     * @param tutorialGroup The tutorial group for which to retrieve attendance records.
     */
    public GetAttendanceByTgCommand(TutorialGroup tutorialGroup) {
        this.tutorialGroup = tutorialGroup;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        List<Student> students = model.getStudentsByTutorialGroup(tutorialGroup);

        if (students.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_STUDENTS, tutorialGroup));
        }

        if (currentWindow != null) {
            currentWindow.close();
        }
        currentWindow = new AttendanceWindow(this.tutorialGroup);
        currentWindow.show(model);
        return new CommandResult("Attendance window opened for Tutorial Group: " + tutorialGroup.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GetAttendanceByTgCommand)) {
            return false;
        }
        GetAttendanceByTgCommand otherCommand = (GetAttendanceByTgCommand) other;
        return tutorialGroup.equals(otherCommand.tutorialGroup);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tutorialGroup", tutorialGroup).toString();
    }

    public void setAttendanceWindow(AttendanceWindow window) {
        this.currentWindow = window;
    }

    /**
     * Closes the currently opened attendance window.
     * @return true if the window was closed, false if no window was open.
     */
    public static boolean closeCurrentWindow() {
        if (currentWindow != null) {
            currentWindow.close();
            currentWindow = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean undo(Model model) {
        return closeCurrentWindow();
    }
}
