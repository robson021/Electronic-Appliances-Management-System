package robert.web.svc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.entity.Appliance;
import robert.utils.api.AppLogger;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
public class TestController {

	private final AppLogger log;

	private final ApplianceBuildingRoomManagementDao applRoomDao;

	@Autowired
	public TestController(AppLogger log, ApplianceBuildingRoomManagementDao applRoomDao) {
		this.log = log;
		this.applRoomDao = applRoomDao;
	}

	@RequestMapping("/test")
	public String hello() {
		return "Hello World " + new Date().toString();
	}

	@RequestMapping("/all-appliances")
	public List<String> getAllavilableAppliances() {
		Iterable<Appliance> allAppliances = applRoomDao.findAllAppliances();
		List<String> appliances = new LinkedList<>();
		int i = 1;
		for (Appliance appl : allAppliances) {
			appliances.add("(" + i++ + ") " + appl.getName());
		}

		log.debug("Return " + appliances.toString());
		return appliances;
	}
}
