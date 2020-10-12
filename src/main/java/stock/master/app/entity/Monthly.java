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
	
	@Column(name = "week", nullable = false)
	private Date week;
	
	@Column(name = "average", nullable = false)
	private double average;
	
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
	
	@Column(name = "over_1000_people", nullable = false)
	private int over_1000_people;
	
	@Column(name = "over_1000_percent", nullable = false)
	private double over_1000_percent;
}
