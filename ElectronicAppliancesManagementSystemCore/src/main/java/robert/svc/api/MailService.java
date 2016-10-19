package robert.svc.api;

import java.io.File;

import com.sun.istack.internal.Nullable;

public interface MailService {
    void sendEmail(String receiverEmail, String topic, String message, @Nullable File attachment);
}
