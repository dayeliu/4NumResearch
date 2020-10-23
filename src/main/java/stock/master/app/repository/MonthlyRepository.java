package stock.master.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import stock.master.app.entity.Monthly;
import stock.master.app.entity.Weekly;

public interface MonthlyRepository extends JpaRepository<Monthly, Long>, JpaSpecificationExecutor<Monthly> {

	@Transactional
	void deleteByStockId(String stockId);

	boolean existsByStockId(String stockId);
	
	Monthly findTop1ByStockIdOrderByDateDesc(String stockId);
	
	List<Monthly> findByStockIdOrderByDateDesc(String stockId);
}
