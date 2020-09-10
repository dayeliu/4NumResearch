package stock.master.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import stock.master.app.entity.BasicInfo;

public interface BasicInfoRepository extends JpaRepository<BasicInfo, String>, JpaSpecificationExecutor<BasicInfo> {

}
