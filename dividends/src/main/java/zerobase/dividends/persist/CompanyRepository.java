package zerobase.dividends.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.dividends.persist.entity.CompanyEntity;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByTicker(String ticker);

    // Optional을 쓰는 이유 = NullPointException 방지, 값이 없는 경우에도 깔끔한 코드 구현 가능
    Optional<CompanyEntity> findByName(String name);

}
