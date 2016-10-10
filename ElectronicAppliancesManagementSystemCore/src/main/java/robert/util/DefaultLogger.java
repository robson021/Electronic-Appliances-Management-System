package robert.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import robert.util.api.AppLogger;

@Component
public class DefaultLogger implements AppLogger {

    private final Logger log = Logger.getLogger(DefaultLogger.class);

    private final int loggingLvl;

    @Autowired
    public DefaultLogger(@Value("${robert.app.logging.level}") final String logLvl) {
        loggingLvl = Integer.parseInt(logLvl);
        log.info("Logging level: " + loggingLvl);
    }

    @Override
    public void info(String msg) {
        if (loggingLvl >= 1)
            log.info(msg);
    }

    @Override
    public void warn(String msg) {
        if (loggingLvl >= 1)
            log.warn(msg);
    }

    @Override
    public void debug(String msg) {
        if (loggingLvl == 2)
            log.debug(msg);
    }


    @Override
    public void error(String msg) {
        log.error(msg);
    }
}
