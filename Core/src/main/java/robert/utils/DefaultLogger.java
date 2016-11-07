package robert.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import robert.utils.api.AppLogger;

@Component
@Lazy
public class DefaultLogger implements AppLogger {

    private static final Logger log = Logger.getLogger(DefaultLogger.class);

    private final int loggingLvl;

    @Autowired
    public DefaultLogger(@Value("${robert.app.logging.level}") final String logLvl) {
        loggingLvl = Integer.parseInt(logLvl);
        log.info("Logging level: " + loggingLvl);
    }

    @Override
    public int getLoggingLevel() {
        return this.loggingLvl;
    }

    @Override
    public void info(final Object... msg) {
        if (loggingLvl >= 1)
            log.info(displayMessage(msg));
    }

    @Override
    public void warn(final Object... msg) {
        if (loggingLvl >= 1)
            log.warn(displayMessage(msg));
    }

    @Override
    public void debug(final Object... msg) {
        if (loggingLvl >= 2)
            log.info(displayMessage(msg));
    }

    @Override
    public void debug(final Exception e) {
        if (loggingLvl >= 2)
            e.printStackTrace();
    }


    @Override
    public void error(final Object... msg) {
        log.error(displayMessage(msg));
	}

    @Override
    public void error(final Exception e) {
        e.printStackTrace();
    }

    private String displayMessage(final Object... msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\t\t" );
        for (Object m : msg) {
            sb.append(m.toString());
            sb.append(" ");
        }
        return sb.toString();
    }
}
