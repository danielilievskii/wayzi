package mk.ukim.finki.wayzi.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class RouteCoordinatesConverter implements AttributeConverter<List<List<Double>>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<List<Double>> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert route coordinates to JSON", e);
        }
    }

    @Override
    public List<List<Double>> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<List<List<Double>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to route coordinates", e);
        }
    }
}
