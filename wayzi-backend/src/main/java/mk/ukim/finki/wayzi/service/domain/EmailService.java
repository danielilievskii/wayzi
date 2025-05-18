package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.dto.MailSendingStatus;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<MailSendingStatus> sendMail(String[] to, String subject, String template, Map<String, Object> model);
}
