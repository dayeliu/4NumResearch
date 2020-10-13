package stock.master.app.resource.vo;

import java.util.List;

import stock.master.app.entity.BasicInfo;

public class updateStockListResult {
	private int totalCount;

	private int addedCount;
	private List<BasicInfo> addStockIds;
	
	private int deletedCount;
	private List<BasicInfo> deleteStockIds;

	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getAddedCount() {
		return addedCount;
	}
	public void setAddedCount(int addedCount) {
		this.addedCount = addedCount;
	}
	public List<BasicInfo> getAddStockIds() {
		return addStockIds;
	}
	public void setAddStockIds(List<BasicInfo> addStockIds) {
		this.addStockIds = addStockIds;
	}
	public int getDeletedCount() {
		return deletedCount;
	}
	public void setDeletedCount(int deletedCount) {
		this.deletedCount = deletedCount;
	}
	public List<BasicInfo> getDeleteStockIds() {
		return deleteStockIds;
	}
	public void setDeleteStockIds(List<BasicInfo> deleteStockIds) {
		this.deleteStockIds = deleteStockIds;
	}
	@Override
	public String toString() {
		String str = "updateStockListResult [\n" +
				"totalCount = " + totalCount + "\n" +
				", addedCount = " + addedCount + "\n" + 
				", addStockIds = "+ addStockIds + "\n" + 
				", deletedCount=" + deletedCount + "\n" +
				", deleteStockIds=" + deleteStockIds + "\n" +
				"]";

		return str;
	}
	
	
}
