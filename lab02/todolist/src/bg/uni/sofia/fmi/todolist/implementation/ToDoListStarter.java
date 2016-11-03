package bg.uni.sofia.fmi.todolist.implementation;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ToDoListStarter {

	private String menu = new StringBuilder().append("Изберете опция:\n")
			.append("1) Всички задачи подредени по приоритет\n").append("2) Задачи със статус IN PROCESS\n")
			.append("3) Задачи, които да се завършат в следващите три дни\n").append("4) Изход\n").toString();

	private Task[] searchArray;

	public ToDoListStarter(Task[] searchArray) {
		this.searchArray = searchArray;
	}

	public void start() {
		boolean exit = false;
		final Scanner scanner = new Scanner(System.in);

		while (!exit) {
			showMenu();
			promptUser();
			try {
				int chosenOption = Integer.parseInt(scanner.nextLine());
				if (chosenOption < 1 || chosenOption > 4) {
					throw new IllegalArgumentException();
				}
				final ToDoList toDoListSearcher = new ToDoList(searchArray);
				Task[] resultArray = getResultArray(toDoListSearcher, chosenOption);
				if (Objects.nonNull(resultArray)) {
					System.out.println(Arrays.toString(resultArray));
				} else {
					exit = true;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Невалидна опция. Избери пак!");
			}

		}

		scanner.close();
	}

	private Task[] getResultArray(ToDoList toDoListSearcher, int chosenOption) {
		Task[] resultTasksArray = null;
		switch (chosenOption) {
		case 1: {
			resultTasksArray = toDoListSearcher.getTasksOrderedByPriority();
			break;
		}

		case 2: {
			resultTasksArray = toDoListSearcher.getTasksWithInProcessStatus();
			break;
		}

		case 3: {
			resultTasksArray = toDoListSearcher.getTaskToBeFinishedByNextThreeDays();
			break;
		}

		case 4: {
			resultTasksArray = null;
		}
		}

		return resultTasksArray;
	}

	private void promptUser() {
		System.out.println("Вашият избор (1-4):");
	}

	private void showMenu() {
		System.out.println(menu);
	}

}
