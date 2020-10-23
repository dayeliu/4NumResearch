package stock.master.app.resource.vo;

import java.util.List;

import stock.master.app.entity.BasicInfo;
import stock.master.app.entity.Monthly;
import stock.master.app.entity.Weekly;

public class getStockInfoResult {
	private BasicInfo basicInfo;

	private List<Weekly> weekly;
	
	private List<Monthly> monthly;

	public BasicInfo getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(BasicInfo basicInfo) {
		this.basicInfo = basicInfo;
	}

	public List<Weekly> getWeekly() {
		return weekly;
	}

	public void setWeekly(List<Weekly> weekly) {
		this.weekly = weekly;
	}

	public List<Monthly> getMonthly() {
		return monthly;
	}

	public void setMonthly(List<Monthly> monthly) {
		this.monthly = monthly;
	}

	
}
