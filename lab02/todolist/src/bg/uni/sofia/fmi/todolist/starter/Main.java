package bg.uni.sofia.fmi.todolist.starter;

import java.time.LocalDate;

import bg.uni.sofia.fmi.todolist.enums.TaskStatus;
import bg.uni.sofia.fmi.todolist.implementation.Task;
import bg.uni.sofia.fmi.todolist.implementation.ToDoListStarter;

public class Main {
	public static void main(String[] args) {
		final Task[] tasks = {
				new Task.Builder(5, TaskStatus.DONE, LocalDate.of(2017, 4, 20)).title("MathTask")
						.description("Sum two digits.").build(),
				new Task.Builder(2, TaskStatus.IN_PROCESS, LocalDate.of(2016, 12, 5)).title("GeographyTask")
						.description("Locate New York.").build(),
				new Task.Builder(1, TaskStatus.INITIAL, LocalDate.of(2016, 11, 4)).title("BiologyTask")
						.description("Guess the organism.").build(),
				new Task.Builder(3, TaskStatus.DONE, LocalDate.of(2017, 4, 21)).title("HistoryTask")
						.description("Speak about Adolf Hitler").build(),
				new Task.Builder(4, TaskStatus.IN_PROCESS, LocalDate.of(2016, 10, 31)).title("PhysicsTask")
						.description("Write down the law of Newton.").build(),
				new Task.Builder(4, TaskStatus.DONE, LocalDate.of(2016, 11, 11)).title("ChemistryTask")
						.description("What is C2H5OH?").build(),
				new Task.Builder(5, TaskStatus.DONE, LocalDate.of(2016, 11, 5)).title("ForeignLanguageTask")
						.description("Translate the text.").build() };
		new ToDoListStarter(tasks).start();
	}
}
