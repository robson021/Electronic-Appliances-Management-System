package robert.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import robert.db.dao.ApplianceRoomManagementDao;
import robert.db.dao.UserDao;
import robert.db.entity.Appliance;
import robert.db.entity.Room;
import robert.db.entity.User;
import robert.util.api.AppLogger;

@Component
public class DbStartUp implements CommandLineRunner {

	private final AppLogger log;

    private final UserDao userDao;

	private final ApplianceRoomManagementDao applianceRoomManagementDao;

    @Autowired
	public DbStartUp(UserDao userDao, AppLogger log, ApplianceRoomManagementDao applianceRoomManagementDao) {
		this.userDao = userDao;
        this.log = log;
		this.applianceRoomManagementDao = applianceRoomManagementDao;
	}

    @Override
    public void run(String... strings) throws Exception {
        if (log.getLoggingLevel() < 2)
            return;

		// admin acc
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


		// room
		Room room = new Room();
		room.setBuilding("B4");
		room.setNumber("122");

		// appliance
		Appliance appliance = new Appliance();
		appliance.setName("Samsung A600 Home Theater Projector");
		appliance.setRoom(room);
		room.addNewAppliance(appliance);

		applianceRoomManagementDao.addNewRoom(room);
	}
}
