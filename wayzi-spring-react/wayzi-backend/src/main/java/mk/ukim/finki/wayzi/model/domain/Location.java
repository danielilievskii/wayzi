package mk.ukim.finki.wayzi.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private Double longitude;
    private Double latitude;

    private String displayName;

    public Location(String name, String city, Double latitude, Double longitude) {
        this.name = name;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.displayName = this.city == null ? this.name : this.name + ", " + this.city;
    }

    public String getDisplayName() {
        return this.city == null ? this.name : this.name + ", " + this.city;
    }
}
