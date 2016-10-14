package robert.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.entity.Appliance;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
public class TestController {

	private final ApplianceBuildingRoomManagementDao applRoomDao;

	@Autowired
	public TestController(ApplianceBuildingRoomManagementDao applRoomDao) {
		this.applRoomDao = applRoomDao;
	}

	@RequestMapping("/test")
	public String hello() {
		return "Hello World " + new Date().toString();
	}

	@RequestMapping("/all-appliances")
	public List<String> getAllavilableAppliances() {
		Iterable<Appliance> allAppliances = applRoomDao.findAllAppliances();
		List<String> applianes = new LinkedList<>();
		int i = 1;
		for (Appliance appl : allAppliances) {
			applianes.add("(" + i++ + ") " + appl.getName());
		}
		return applianes;
	}
}
