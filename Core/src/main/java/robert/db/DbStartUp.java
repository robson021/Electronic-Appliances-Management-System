package robert.db;

import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Room;
import robert.db.entity.User;
import robert.enums.Validation;
import robert.exceptions.ApplianceException;
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

		final String adminEmail = "admin@a.pl";

		try {
			// admin acc
			User user = new User();
			user.setEmail(adminEmail);
			user.setName("Admin");
			user.setSurname("Admin");
			user.setPassword("Passwd.123");
			user.setActivated(true);
			user.setAdminPrivileges(true);

			userDao.saveUser(user);
		} catch (Throwable ignored) {
			return;
		} finally {
			log.info("Admin account:", userDao.findUserByEmail(adminEmail));
		}

		if (log.getLoggingLevel() < 2)
			return;

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
		appliance.setUniqueCode(Validation.MOCK_APPLIANCE_UNIQUE_CODE);
		room.addNewAppliance(appliance);

		Appliance appliance2 = new Appliance();
		appliance2.setName("Epson EX3212 SVGA 3LCD Projector");
		appliance2.setRoom(room2);
		appliance2.setUniqueCode(Validation.MOCK_APPLIANCE_UNIQUE_CODE);
		room2.addNewAppliance(appliance2);

		b4.addRoom(room);
		b4.addRoom(room2);
		applianceBuildingRoomManagementDao.saveBuilding(b4);

		Appliance appliance3 = new Appliance();
		appliance3.setName("Lenovo Pocket Projector");
		appliance3.setUniqueCode(Validation.MOCK_APPLIANCE_UNIQUE_CODE);

		applianceBuildingRoomManagementDao.addApplianceToTheRoom(appliance3, "B4", "122");

		addReservationsForUser(adminEmail);
	}

    private void addReservationsForUser(String email) throws ApplianceException {
        Iterable<Appliance> allAppliances = applianceBuildingRoomManagementDao.findAllAppliances();
        Date currentTime = new Date();
        for (Appliance appliance : allAppliances) {
            userDao.makeReservationForAppliance(email, appliance.getId(), currentTime, RandomUtils.nextInt(20, 180));
        }
    }
}
