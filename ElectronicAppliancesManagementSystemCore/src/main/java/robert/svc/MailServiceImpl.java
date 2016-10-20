package robert.svc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import robert.svc.api.MailService;
import robert.svc.api.TaskSchedulerService;
import robert.utils.api.AppLogger;

import javax.mail.internet.MimeMessage;
import java.io.File;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class MailServiceImpl implements MailService {

    private final AppLogger log;

    private final JavaMailSender javaMailSender;

    private final TaskSchedulerService taskScheduler;

    @Autowired
    public MailServiceImpl(AppLogger log, JavaMailSender javaMailSender, TaskSchedulerService taskScheduler) {
        this.log = log;
        this.javaMailSender = javaMailSender;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void sendEmail(String receiverEmail, String topic, String message, File attachment) {
        taskScheduler.submitNewTask(new MailRunnable(receiverEmail, topic, message, attachment));
    }

    private class MailRunnable implements Runnable {
        final String receiverEmail, topic, body;

        final File attachment;

        MailRunnable(String receiverEmail, String topic, String message, File attachment) {
            this.receiverEmail = receiverEmail;
            this.topic = topic;
            this.body = message;
            this.attachment = attachment;
        }

        @Override
        public void run() {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper;

                helper = new MimeMessageHelper(mimeMessage, true);
                helper.setSubject(topic);
                helper.setTo(receiverEmail);
                helper.setText(body);
                if (attachment != null) {
                    FileSystemResource f = new FileSystemResource(attachment);
                    helper.addAttachment(f.getFilename(), f);
                }
                javaMailSender.send(mimeMessage);
            } catch (Exception e) {
                log.error(e);
            } finally {
                log.debug("Mailer thread finished:", receiverEmail);
            }
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }
}
