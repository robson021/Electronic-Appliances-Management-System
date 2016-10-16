package robert.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import robert.utils.api.AppLogger;

import java.util.Calendar;

@Component
@Lazy
public class DefaultLogger implements AppLogger {

    private final Logger log = Logger.getLogger(DefaultLogger.class);

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
    public void info(Object... msg) {
        if (loggingLvl >= 1)
            log.info(displayMessage(msg));
    }

    @Override
    public void warn(Object... msg) {
        if (loggingLvl >= 1)
            log.warn(displayMessage(msg));
    }

    @Override
    public void debug(Object... msg) {
        if (loggingLvl >= 2)
            log.info(displayMessage(msg));
    }


    @Override
    public void error(Object... msg) {
        log.error(msg.toString());
    }

    private String displayMessage(Object... msg) {
        StringBuilder sb = new StringBuilder();
        for (Object m : msg) {
            sb.append(m.toString());
            sb.append(" ");
        }
        return "\n\t\t" + Calendar.getInstance().getTime().toString() + " " + sb.toString();
    }
}
