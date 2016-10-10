package robert.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import robert.db.dao.UserDao;
import robert.db.entity.User;
import robert.util.api.AppLogger;

@Component
public class DbStartUp implements CommandLineRunner {

    private final UserDao userDao;

    private final AppLogger log;

    @Autowired
    public DbStartUp(UserDao userDao, AppLogger log) {
        this.userDao = userDao;
        this.log = log;
    }

    @Override
    public void run(String... strings) throws Exception {
        if (log.getLoggingLevel() < 2)
            return;

        String adminEmail = "admin@a.pl";
        User user = new User();
        user.setEmail(adminEmail);
        user.setAdminPrivileges(true);
        user.setName("Admin");
        user.setSurname("Admin");
        user.setPassword("passwd");

        log.debug("save test admin account");
        userDao.saveUser(user);

        User byEmail = userDao.findUserByEmail(adminEmail);
        log.debug(byEmail);
    }
}
