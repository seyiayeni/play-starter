package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author seyi
 */

@Entity
@Table(name="accounts")
public class Account extends Model implements Serializable {

    @Constraints.Required(message = "This field is required")
    private String name;

    @Constraints.Required(message = "This field is required")
	@Column(unique=true, nullable=false)
    private String email;

	@Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private boolean active;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="account_roles",
            joinColumns=@JoinColumn(name="account_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
	public String toString() {
		return this.name;
	}

    public Set<Permission> permissions() {
        Set<Permission> perms = new HashSet<>();
        roles.forEach(role ->
            role.getPermissions().forEach(perm -> {
                perms.add(perm);
            })
        );
        return perms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
