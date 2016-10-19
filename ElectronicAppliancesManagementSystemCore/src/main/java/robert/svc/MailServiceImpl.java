package robert.svc;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sun.istack.internal.Nullable;

import robert.svc.api.MailService;
import robert.svc.api.TaskSchedulerService;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    private final TaskSchedulerService taskScheduler;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, TaskSchedulerService taskScheduler) {
        this.javaMailSender = javaMailSender;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void sendEmail(String receiverEmail, String topic, String message, @Nullable File attachment) {
        // todo
    }
}
