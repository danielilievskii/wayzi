package mk.ukim.finki.wayzi.web.dto.location;

import mk.ukim.finki.wayzi.model.domain.Location;

import java.util.List;

public record DisplayLocationDto(
        Long id,
        String name,
        String city,
        Double longitude,
        Double latitude,
        String displayName
) {
    public static DisplayLocationDto from(Location location) {
        return new DisplayLocationDto(
                location.getId(),
                location.getName(),
                location.getCity(),
                location.getLongitude(),
                location.getLatitude(),
                location.getDisplayName()
        );
    }

    public static List<DisplayLocationDto> from(List<Location> locations) {
        return locations.stream().map(DisplayLocationDto::from).toList();
    }
}

