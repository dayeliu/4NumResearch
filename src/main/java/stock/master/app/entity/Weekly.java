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
@Table(name = "weekly_info")
public class Weekly implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Weekly(Long id, String stockId, Date date, Long over_400_amount, double over_400_percent,
			int over_400_people, int bet_400_600_people, int bet_600_800_people, int bet_800_1000_people,
			double over_1000_percent, int over_1000_people) {
		super();
		this.id = id;
		this.stockId = stockId;
		this.date = date;
		this.over_400_amount = over_400_amount;
		this.over_400_percent = over_400_percent;
		this.over_400_people = over_400_people;
		this.bet_400_600_people = bet_400_600_people;
		this.bet_600_800_people = bet_600_800_people;
		this.bet_800_1000_people = bet_800_1000_people;
		this.over_1000_percent = over_1000_percent;
		this.over_1000_people = over_1000_people;
	}

	@Id
	@Column(name = "Id")
	@GeneratedValue(generator = "weekly_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(
		name = "weekly_generator",
		sequenceName = "weekly_sequence",
		initialValue = 1,
		allocationSize = 1
	)
	private Long id;
	
	@Column(name = "stock_id", nullable = false)
	private String stockId;
	
	@Column(name = "date", nullable = false)
	private Date date;
	
	@Column(name = "over_400_amount", nullable = false)
	private Long over_400_amount;
	
	@Column(name = "over_400_percent", nullable = false)
	private double over_400_percent;
	
	@Column(name = "over_400_people", nullable = false)
	private int over_400_people;
	
	@Column(name = "bet_400_600_people", nullable = false)
	private int bet_400_600_people;
	
	@Column(name = "bet_600_800_people", nullable = false)
	private int bet_600_800_people;
	
	@Column(name = "bet_800_1000_people", nullable = false)
	private int bet_800_1000_people;
	
	@Column(name = "over_1000_percent", nullable = false)
	private double over_1000_percent;
	
	@Column(name = "over_1000_people", nullable = false)
	private int over_1000_people;

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

	public Long getOver_400_amount() {
		return over_400_amount;
	}

	public void setOver_400_amount(Long over_400_amount) {
		this.over_400_amount = over_400_amount;
	}

	public double getOver_400_percent() {
		return over_400_percent;
	}

	public void setOver_400_percent(double over_400_percent) {
		this.over_400_percent = over_400_percent;
	}

	public int getOver_400_people() {
		return over_400_people;
	}

	public void setOver_400_people(int over_400_people) {
		this.over_400_people = over_400_people;
	}

	public int getBet_400_600_people() {
		return bet_400_600_people;
	}

	public void setBet_400_600_people(int bet_400_600_people) {
		this.bet_400_600_people = bet_400_600_people;
	}

	public int getBet_600_800_people() {
		return bet_600_800_people;
	}

	public void setBet_600_800_people(int bet_600_800_people) {
		this.bet_600_800_people = bet_600_800_people;
	}

	public int getBet_800_1000_people() {
		return bet_800_1000_people;
	}

	public void setBet_800_1000_people(int bet_800_1000_people) {
		this.bet_800_1000_people = bet_800_1000_people;
	}

	public double getOver_1000_percent() {
		return over_1000_percent;
	}

	public void setOver_1000_percent(double over_1000_percent) {
		this.over_1000_percent = over_1000_percent;
	}

	public int getOver_1000_people() {
		return over_1000_people;
	}

	public void setOver_1000_people(int over_1000_people) {
		this.over_1000_people = over_1000_people;
	}
	
	
}
