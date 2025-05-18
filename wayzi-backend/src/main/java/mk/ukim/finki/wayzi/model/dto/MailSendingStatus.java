package mk.ukim.finki.wayzi.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MailSendingStatus {
    public boolean sent;
    public String[] to;
    public String subject;
    public String reason;
}
