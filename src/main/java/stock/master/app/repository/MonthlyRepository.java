package stock.master.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import stock.master.app.entity.Monthly;

public interface MonthlyRepository extends JpaRepository<Monthly, Long>, JpaSpecificationExecutor<Monthly> {

}
