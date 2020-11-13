package stock.master.app.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import stock.master.app.entity.Quarterly;

public interface QuarterlyRepository extends JpaRepository<Quarterly, Long>, JpaSpecificationExecutor<Quarterly> {

	@Transactional
	void deleteByStockId(String stockId);

	boolean existsByStockId(String stockId);
	
	List<Quarterly> findByStockIdOrderByDateDesc(String stockId);
	Quarterly findTop1ByStockIdOrderByDateDesc(String stockId);
}
