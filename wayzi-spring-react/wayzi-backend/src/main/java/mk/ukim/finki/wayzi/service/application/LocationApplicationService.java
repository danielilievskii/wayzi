package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.dto.DisplayLocationDto;

import java.util.List;

public interface LocationApplicationService {
    List<DisplayLocationDto> findAll();
}
