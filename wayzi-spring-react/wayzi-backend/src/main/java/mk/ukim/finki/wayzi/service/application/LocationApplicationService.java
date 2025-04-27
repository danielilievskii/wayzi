package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.DisplayLocationDto;

import java.util.List;

public interface LocationApplicationService {
    List<DisplayLocationDto> findAll();
    DisplayLocationDto findById(Long id);
}
