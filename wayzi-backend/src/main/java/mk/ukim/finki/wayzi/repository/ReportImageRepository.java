package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.ReportImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportImageRepository extends JpaRepository<ReportImage, Long> {

}
