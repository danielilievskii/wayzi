package mk.ukim.finki.wayzi.service.domain.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wayzi.model.dto.MailSendingStatus;
import mk.ukim.finki.wayzi.service.domain.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine emailTemplateEngine;

    @Async
    @Override
    public CompletableFuture<MailSendingStatus> sendMail(String[] to, String subject, String template, Map<String, Object> model) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            message.setFrom("noreply@wayzi.com", "Wayzi");
            message.setTo(to);
            message.setSubject(subject);

            Context context = new Context();
            context.setVariables(model);
            String html = emailTemplateEngine.process(template, context);

            message.setText(html, true);

            mailSender.send(mimeMessage);
            return CompletableFuture.completedFuture(new MailSendingStatus(true, to, subject, null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
