package bg.uni.sofia.fmi.todolist.implementation;

import java.time.LocalDate;
import java.util.Arrays;

import bg.uni.sofia.fmi.todolist.enums.TaskStatus;

final class ToDoList {

	private Task[] searchArray;

	public ToDoList(Task[] searchArray) {
		this.searchArray = searchArray;
	}

	Task[] getTasksOrderedByPriority() {
		Arrays.sort(searchArray);
		return searchArray;
	}

	Task[] getTasksWithInProcessStatus() {
		final Task[] tempTasksArray = new Task[searchArray.length];
		int newIndex = 0;
		for (int i = 0; i < searchArray.length; i++) {
			final Task task = searchArray[i];
			if (task.getStatus().equals(TaskStatus.IN_PROCESS)) {
				tempTasksArray[newIndex++] = task;
			}
		}
		final Task[] resultTasksArray = new Task[newIndex];
		System.arraycopy(tempTasksArray, 0, resultTasksArray, 0, newIndex);

		Arrays.sort(resultTasksArray);
		return resultTasksArray;
	}

	Task[] getTaskToBeFinishedByNextThreeDays() {
		final Task[] tempTasksArray = new Task[searchArray.length];
		final LocalDate todayDate = LocalDate.now();
		final LocalDate dateAfterThreeDays = todayDate.plusDays(4);

		int newIndex = 0;

		for (int i = 0; i < searchArray.length; i++) {
			final Task task = searchArray[i];
			if (task.getDeadline().isBefore(dateAfterThreeDays) && task.getDeadline().isAfter(todayDate)
					&& (task.getStatus().equals(TaskStatus.IN_PROCESS)
							|| task.getStatus().equals(TaskStatus.INITIAL))) {
				tempTasksArray[newIndex++] = task;
			}
		}
		final Task[] resultTasksArray = new Task[newIndex];
		System.arraycopy(tempTasksArray, 0, resultTasksArray, 0, newIndex);

		Arrays.sort(resultTasksArray);
		return resultTasksArray;
	}
}
