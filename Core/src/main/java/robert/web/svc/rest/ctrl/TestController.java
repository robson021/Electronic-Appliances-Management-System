package robert.web.svc.rest.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import robert.db.dao.AdminDao;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.db.entity.Appliance;
import robert.svc.api.MailService;
import robert.svc.appliance.ApplianceConnectorImpl;
import robert.utils.api.AppLogger;
import robert.web.svc.rest.responses.asm.UserAssembler;
import robert.web.svc.rest.responses.json.UserDTO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static robert.web.svc.rest.ctrl.api.AdminPanelCtrl.GET_ALL_INACTIVE_ACCOUNTS_URL;

@RestController
@RequestMapping("/test")
public class TestController {

	private final AppLogger log;

	private final ApplianceBuildingRoomManagementDao applRoomDao;

	private final AdminDao adminDao;

	private final MailService mailService;

	private final UserDao userDao;

	private final ApplianceConnectorImpl applianceConnector;

	@Autowired
	public TestController(AppLogger log, ApplianceBuildingRoomManagementDao applRoomDao, AdminDao adminDao, MailService mailService, UserDao userDao, ApplianceConnectorImpl applianceConnector) {
		this.log = log;
		this.applRoomDao = applRoomDao;
		this.adminDao = adminDao;
		this.mailService = mailService;
		this.userDao = userDao;
		this.applianceConnector = applianceConnector;
	}

	@RequestMapping("/hello")
	public String hello() {
		return "Hello World " + new Date().toString();
	}

	@RequestMapping("/all-appliances")
	public List<String> getAvailableAppliances() {
		Iterable<Appliance> allAppliances = applRoomDao.findAllAppliances();
		List<String> appliances = new LinkedList<>();
		int i = 1;
		for (Appliance appl : allAppliances) {
			appliances.add("(" + i++ + ") " + appl.getName() + "; ");
		}

		log.debug("Return:", appliances.toString());
		return appliances;
	}

	@RequestMapping(GET_ALL_INACTIVE_ACCOUNTS_URL)
	public List<UserDTO> getAllInactiveAccounts() {
		log.debug("test inactive accounts");

		List<UserDTO> userDTOS = UserAssembler.convertToUserDR(adminDao.getAllInactiveUsers());

		log.debug(userDTOS);
		return userDTOS;
	}

	@RequestMapping("/email")
	public HttpStatus sendEmail() {
		mailService.sendEmail("invoice.writer.app@gmail.com", "test", "Test email.", null);
		return HttpStatus.OK;
	}

	@RequestMapping("/mock-appliance")
	public String getAccessToMockAppliance() throws Exception {
		return applianceConnector.connectToTheAppliance("http://51.254.115.19:8081", 2, null, 0, null);
	}
}
