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
}
