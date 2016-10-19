package robert.svc.api;

import java.io.File;

public interface MailService {
    void sendEmail(String receiverEmail, String topic, String message, File attachment);
}
