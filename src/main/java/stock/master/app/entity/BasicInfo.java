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
	private String amount;

	public BasicInfo(String stockId, String name, String stockClass, String category, String amount) {
		super();
		this.stockId = stockId;
		this.name = name;
		this.stockClass = stockClass;
		this.category = category;
		this.amount = amount;
	}
}
