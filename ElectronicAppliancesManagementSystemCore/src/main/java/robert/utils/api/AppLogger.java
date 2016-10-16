package robert.utils.api;

public interface AppLogger {

    int getLoggingLevel();

	void info(Object... msg);

	void debug(Object... msg);

	void debug(Exception e);

	void warn(Object... msg);

	void error(Object... msg);

	void error(Exception e);
}
