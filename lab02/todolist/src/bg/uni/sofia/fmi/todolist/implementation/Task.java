package bg.uni.sofia.fmi.todolist.implementation;

import java.time.LocalDate;

import bg.uni.sofia.fmi.todolist.enums.TaskStatus;

public class Task implements Comparable<Task> {

	private TaskStatus status;

	private int priority;

	private LocalDate deadline;

	private String title;

	private String description;

	public static class Builder {

		private final int priority;

		private final TaskStatus status;

		private final LocalDate deadline;

		private String title;

		private String description;

		public Builder(int priority, TaskStatus status, LocalDate deadline) {
			this.priority = priority;
			this.status = status;
			this.deadline = deadline;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Task build() {
			return new Task(this);
		}
	}

	private Task(Builder builder) {
		this.title = builder.title;
		this.description = builder.description;
		this.status = builder.status;
		this.priority = builder.priority;
		this.deadline = builder.deadline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	@Override
	public int compareTo(Task task) {
		return this.priority - task.priority;
	}

	@Override
	public String toString() {
		return "Task [title=" + title + ", description=" + description + ", status=" + status + ", priority=" + priority
				+ ", deadline=" + deadline + "]";
	}

}
