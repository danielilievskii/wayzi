package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.Report;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaSpecificationRepository<Report, Long> {

}
