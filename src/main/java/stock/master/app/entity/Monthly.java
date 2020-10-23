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
@Table(name = "monthly_info")
public class Monthly implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(generator = "monthly_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(
		name = "monthly_generator",
		sequenceName = "monthly_sequence",
		initialValue = 1,
		allocationSize = 1
	)
	private Long id;

	@Column(name = "stock_id", nullable = false)
	private String stockId;
	
	@Column(name = "date", nullable = false)
	private Date date;
	
	@Column(name = "income", nullable = false)
	private String income;
	
	@Column(name = "mom", nullable = false)
	private String mom;
	
	@Column(name = "income_last_year", nullable = false)
	private String incomeLastYear;
	
	@Column(name = "yoy", nullable = false)
	private String yoy;
	
	@Column(name = "accurate", nullable = false)
	private String accurate;
	
	@Column(name = "accurate_last_year", nullable = false)
	private String accurateLastYear;
	
	@Column(name = "accurate_yoy", nullable = false)
	private String acccurate_yoy;

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

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getMom() {
		return mom;
	}

	public void setMom(String mom) {
		this.mom = mom;
	}

	public String getIncomeLastYear() {
		return incomeLastYear;
	}

	public void setIncomeLastYear(String incomeLastYear) {
		this.incomeLastYear = incomeLastYear;
	}

	public String getYoy() {
		return yoy;
	}

	public void setYoy(String yoy) {
		this.yoy = yoy;
	}

	public String getAccurate() {
		return accurate;
	}

	public void setAccurate(String accurate) {
		this.accurate = accurate;
	}

	public String getAccurateLastYear() {
		return accurateLastYear;
	}

	public void setAccurateLastYear(String accurateLastYear) {
		this.accurateLastYear = accurateLastYear;
	}

	public String getAcccurate_yoy() {
		return acccurate_yoy;
	}

	public void setAcccurate_yoy(String acccurate_yoy) {
		this.acccurate_yoy = acccurate_yoy;
	}

	@Override
	public String toString() {
		return "Monthly [id=" + id + ", stockId=" + stockId + ", date=" + date + ", income=" + income + ", mom=" + mom
				+ ", incomeLastYear=" + incomeLastYear + ", yoy=" + yoy + ", accurate=" + accurate
				+ ", accurateLastYear=" + accurateLastYear + ", acccurate_yoy=" + acccurate_yoy + "]";
	}
}
