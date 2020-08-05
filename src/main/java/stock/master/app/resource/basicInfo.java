package stock.master.app.resource;

public class basicInfo {
	
	private String sid = "";
	private String name = "";
	private Long amount = 0L;
	private String classification = "";
	private String property = "";
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}

	// [TSE] (新美齊 2442) 顯示器*, 顯示器指標 -> 239740 [Tosin : xxx] 
	@Override
	public String toString() {
		String str = "";
		
		str = "[" + property + "] (" + name + " " + sid + ") " + classification + " -> " + (int)(amount*0.0015);
		
		return str;
	}
}
