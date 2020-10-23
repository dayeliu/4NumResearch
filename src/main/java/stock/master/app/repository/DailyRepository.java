package stock.master.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import stock.master.app.entity.Daily;
import stock.master.app.entity.Weekly;

public interface DailyRepository extends JpaRepository<Daily, Long>, JpaSpecificationExecutor<Daily> {

	@Transactional
	void deleteByStockId(String stockId);

	boolean existsByStockId(String stockId);
	
	List<Weekly> findByStockIdOrderByDateDesc(String stockId);
	Weekly findTop1ByStockIdOrderByDateDesc(String stockId);
}
