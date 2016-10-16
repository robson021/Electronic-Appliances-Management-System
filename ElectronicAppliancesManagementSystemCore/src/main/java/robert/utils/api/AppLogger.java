package robert.utils.api;

public interface AppLogger {

    int getLoggingLevel();

	void info(Object... msg);

	void debug(Object... msg);

	void warn(Object... msg);

	void error(Object... msg);
}
