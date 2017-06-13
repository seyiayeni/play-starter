package models;

import javax.persistence.*;
import java.util.Date;

/**
 * @author seyi
 */

@MappedSuperclass
public class Model {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
