package stock.master.app.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "basic_info")
public class BasicInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "stock_id", nullable = false)
	private String stockId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "class")
	private String stockClass;

	@Column(name = "category")
	private String category;

	@Column(name = "amount", nullable = false)
	private Long amount;

	public BasicInfo() {
		this.stockId = "";
		this.name = "";
		this.stockClass = "";
		this.category = "";
		this.amount = 0L;
	}

	public BasicInfo(String stockId, String name, String stockClass, String category, Long amount) {
		this.stockId = stockId;
		this.name = name;
		this.stockClass = stockClass;
		this.category = category;
		this.amount = amount;
	}
	
	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStockClass() {
		return stockClass;
	}

	public void setStockClass(String stockClass) {
		this.stockClass = stockClass;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
}
