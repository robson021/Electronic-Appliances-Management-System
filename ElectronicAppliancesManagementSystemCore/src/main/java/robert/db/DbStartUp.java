package robert.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import robert.dao.UserDao;

@Component
public class DbStartUp implements CommandLineRunner {

    private final UserDao userDao;

    @Autowired
    public DbStartUp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void run(String... strings) throws Exception {

    }
}
