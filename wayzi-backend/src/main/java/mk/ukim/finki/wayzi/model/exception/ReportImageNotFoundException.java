package mk.ukim.finki.wayzi.model.exception;

import mk.ukim.finki.wayzi.model.exception.base.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportImageNotFoundException extends EntityNotFoundException {
    public ReportImageNotFoundException(Long imageId) {
        super(String.format("Report image with id: %d was not found", imageId));
    }
}
