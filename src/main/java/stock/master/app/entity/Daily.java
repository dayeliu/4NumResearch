package stock.master.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "daily_info")
public class Daily {
	
	@Id
	@Column(name = "Id")
	@GeneratedValue(generator = "daily_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(
		name = "daily_generator",
		sequenceName = "daily_sequence",
		initialValue = 1,
		allocationSize = 1
	)
	private Long id;

	@Column(name = "stock_id", nullable = false)
	private String stockId;
	
	@Column(name = "date", nullable = false)
	private Date date;
	
	@Column(name = "open", nullable = false)
	private float open;
	
	@Column(name = "close", nullable = false)
	private float close;
	
	@Column(name = "high", nullable = false)
	private float high;
	
	@Column(name = "low", nullable = false)
	private float low;
	
	@Column(name = "volumn", nullable = false)
	private int volumn;
	
	@Column(name = "ma_5", nullable = false)
	private float ma_5;
	
	@Column(name = "ma_10", nullable = false)
	private float ma_10;
	
	@Column(name = "ma_20", nullable = false)
	private float ma_20;
	
	@Column(name = "ma_60", nullable = false)
	private float ma_60;
	
	@Column(name = "wize", nullable = false) // 外資
	private int wize;
	
	@Column(name = "tosin", nullable = false) // 投信
	private int tosin;
	
	@Column(name = "zyin", nullable = false) // 自營
	private int zyin;
	
	@Column(name = "buy", nullable = false) // 融資
	private int buy;
	
	@Column(name = "sell", nullable = false) // 融券
	private int sell;

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

	public float getOpen() {
		return open;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public float getClose() {
		return close;
	}

	public void setClose(float close) {
		this.close = close;
	}

	public float getHigh() {
		return high;
	}

	public void setHigh(float high) {
		this.high = high;
	}

	public float getLow() {
		return low;
	}

	public void setLow(float low) {
		this.low = low;
	}

	public int getVolumn() {
		return volumn;
	}

	public void setVolumn(int volumn) {
		this.volumn = volumn;
	}

	public float getMa_5() {
		return ma_5;
	}

	public void setMa_5(float ma_5) {
		this.ma_5 = ma_5;
	}

	public float getMa_10() {
		return ma_10;
	}

	public void setMa_10(float ma_10) {
		this.ma_10 = ma_10;
	}

	public float getMa_20() {
		return ma_20;
	}

	public void setMa_20(float ma_20) {
		this.ma_20 = ma_20;
	}

	public float getMa_60() {
		return ma_60;
	}

	public void setMa_60(float ma_60) {
		this.ma_60 = ma_60;
	}

	public int getWize() {
		return wize;
	}

	public void setWize(int wize) {
		this.wize = wize;
	}

	public int getTosin() {
		return tosin;
	}

	public void setTosin(int tosin) {
		this.tosin = tosin;
	}

	public int getZyin() {
		return zyin;
	}

	public void setZyin(int zyin) {
		this.zyin = zyin;
	}

	public int getBuy() {
		return buy;
	}

	public void setBuy(int buy) {
		this.buy = buy;
	}

	public int getSell() {
		return sell;
	}

	public void setSell(int sell) {
		this.sell = sell;
	}

	@Override
	public String toString() {
		return "Daily [id=" + id + ", stockId=" + stockId + ", date=" + date + ", open=" + open + ", close=" + close
				+ ", high=" + high + ", low=" + low + ", volumn=" + volumn + ", ma_5=" + ma_5 + ", ma_10=" + ma_10
				+ ", ma_20=" + ma_20 + ", ma_60=" + ma_60 + ", wize=" + wize + ", tosin=" + tosin + ", zyin=" + zyin
				+ ", buy=" + buy + ", sell=" + sell + "]";
	}

}
