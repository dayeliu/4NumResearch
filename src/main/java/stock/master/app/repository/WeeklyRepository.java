package stock.master.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import stock.master.app.entity.Weekly;

public interface WeeklyRepository extends JpaRepository<Weekly, Long>, JpaSpecificationExecutor<Weekly> {

}
