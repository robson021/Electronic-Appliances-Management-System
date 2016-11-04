package robert.enums;

public enum TaskType {

	SINGLE_RUN("single_run"),

	PERIODICAL_JOB("periodical_job");

	private final String text;

	TaskType(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
