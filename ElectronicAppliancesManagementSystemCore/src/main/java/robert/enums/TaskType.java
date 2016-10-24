package robert.enums;

public enum TaskType {

	SINGLE_RUN("single run"),

	PERIODICAL_JOB("periodical job");

	private final String text;

	TaskType(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
