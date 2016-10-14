package robert.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Room;
import robert.db.entity.User;
import robert.utils.api.AppLogger;

@Component
public class DbStartUp implements CommandLineRunner {

	private final AppLogger log;

    private final UserDao userDao;

	private final ApplianceBuildingRoomManagementDao applianceBuildingRoomManagementDao;

    @Autowired
	public DbStartUp(UserDao userDao, AppLogger log, ApplianceBuildingRoomManagementDao applianceBuildingRoomManagementDao) {
		this.userDao = userDao;
        this.log = log;
		this.applianceBuildingRoomManagementDao = applianceBuildingRoomManagementDao;
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


		log.debug("Adding example rooms and appliances");

		Building b4 = new Building();
		b4.setName("B4");

		// room
		Room room = new Room();
		room.setBuilding(b4);
		room.setNumber("122");

		Room room2 = new Room();
		room2.setBuilding(b4);
		room2.setNumber("110");

		// appliance
		Appliance appliance = new Appliance();
		appliance.setName("Samsung A600 Home Theater Projector");
		appliance.setRoom(room);
		room.addNewAppliance(appliance);

		Appliance appliance2 = new Appliance();
		appliance2.setName("Epson EX3212 SVGA 3LCD Projector");
		appliance2.setRoom(room2);
		room2.addNewAppliance(appliance2);

		b4.addRoom(room);
		b4.addRoom(room2);
		applianceBuildingRoomManagementDao.saveBuilding(b4);

		Appliance appliance3 = new Appliance();
		appliance3.setName("Lenovo Pocket Projector");

		applianceBuildingRoomManagementDao.addApplianceToTheRoom(appliance3, "B4", "122");

	}
}
