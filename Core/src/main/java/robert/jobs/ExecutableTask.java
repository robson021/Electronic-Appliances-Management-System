package robert.jobs;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;
import robert.enums.TaskType;

public final class ExecutableTask implements Runnable {

	private static final short INITIAL_SLEEP = 1_000;

	private final TaskType taskType;

	private final long threadSleep;

	private final Runnable task;

	private ExecutableTask(Builder builder) {
		Assert.notNull(builder.taskType);
		Assert.notNull(builder.task);

		this.taskType = builder.taskType;
		this.task = builder.task;

		if (taskType.equals(TaskType.PERIODICAL_JOB) && builder.threadSleep < 1_000) {
			throw new RuntimeException("Invalid task sleep!");
		}

		this.threadSleep = builder.threadSleep;
	}

	@Override
	public final void run() {
		try {
			Thread.sleep(INITIAL_SLEEP);
		} catch (InterruptedException ignored) {
		} finally {
			executeTask();
		}
	}

	private void executeTask() {
		try {
			if (this.taskType.equals(TaskType.SINGLE_RUN)) {
				this.task.run();
			} else {
				runPeriodicalJob(this.task);
			}
		} catch (Throwable ignored) {
		}
	}

	private void runPeriodicalJob(Runnable task) {
		while (true) {
			task.run();
			try {
				Thread.sleep(this.threadSleep);
			} catch (InterruptedException ignored) {
			}
		}
	}

	public static class Builder {
		private TaskType taskType = null;
		private long threadSleep = -1;
		private Runnable task = null;

		public Builder withTaskType(TaskType tt) {
			this.taskType = tt;
			return this;
		}

		public Builder withThreadSleep(long sleepTime) {
			this.threadSleep = sleepTime;
			return this;
		}

		public Builder withTask(Runnable task) {
			this.task = task;
			return this;
		}

		public ExecutableTask build() {
			return new ExecutableTask(this);
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
