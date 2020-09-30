package stock.master.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import stock.master.app.entity.BasicInfo;

public interface BasicInfoRepository extends JpaRepository<BasicInfo, String>, JpaSpecificationExecutor<BasicInfo> {
	BasicInfo findByStockId(String stockId);

	List<BasicInfo> findAll();

	List<BasicInfo> findAllByOrderByStockIdAsc();
}
