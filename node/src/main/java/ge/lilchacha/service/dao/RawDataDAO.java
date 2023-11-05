package ge.lilchacha.service.dao;

import ge.lilchacha.service.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
