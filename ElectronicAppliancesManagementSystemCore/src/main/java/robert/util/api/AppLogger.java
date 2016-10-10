package robert.util.api;

public interface AppLogger {
    void info(String msg);

    void debug(String msg);

    void warn(String msg);

    void error(String msg);
}
