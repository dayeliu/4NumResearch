package stock.master.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "quarterly_info")
public class Quarterly implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Quarterly() {
		super();
	}

	@Id
	@Column(name = "Id")
	@GeneratedValue(generator = "quarterly_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(
		name = "quarterly_generator",
		sequenceName = "quarterly_sequence",
		initialValue = 1,
		allocationSize = 1
	)
	private Long id;

	@Column(name = "stock_id", nullable = false)
	private String stockId;

	@Column(name = "date", nullable = false)
	private Date date;
	
	@Column(name = "gross_margin", nullable = false)
	private String gm; // 毛利率
	
	@Column(name = "operating_profit_ratio", nullable = false)
	private String opr; // 營業利益率
	
	@Column(name = "net_profit_after_tax", nullable = false)
	private String npat; // 稅後純益率
	
	@Column(name = "eps", nullable = false)
	private String eps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getGm() {
		return gm;
	}

	public void setGm(String gm) {
		this.gm = gm;
	}

	public String getOpr() {
		return opr;
	}

	public void setOpr(String opr) {
		this.opr = opr;
	}

	public String getNpat() {
		return npat;
	}

	public void setNpat(String npat) {
		this.npat = npat;
	}

	public String getEps() {
		return eps;
	}

	public void setEps(String eps) {
		this.eps = eps;
	}
}
