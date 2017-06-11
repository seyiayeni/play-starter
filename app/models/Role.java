package models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author seyi
 */

@Entity
@Table(name="roles")
public class Role extends Model implements Serializable {

	@Column(nullable=false)
	private String role;

	@ElementCollection(fetch=FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name="role_permissions", joinColumns=@JoinColumn(name="role_id"))
    @Column(name="permission")
	private List<Permission> permissions = new ArrayList<>();

	public Role() {}

	public Role(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return this.role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
}
