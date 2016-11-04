package robert.svc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import robert.svc.api.MailService;
import utils.SpringTest;

public class MailServiceImplTest extends SpringTest {

	private final String receiver = "invoice.writer.app@gmail.com";

	@Autowired
	private MailService mailService;

	@Override
	public void setup() throws Exception {
	}

	@Test
	public void sendEmail() throws Exception {
		mailService.sendEmail(receiver, "Test message", "test test test", null);
		System.out.println("sent");
	}

}