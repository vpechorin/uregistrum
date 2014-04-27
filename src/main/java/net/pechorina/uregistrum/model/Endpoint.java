package net.pechorina.uregistrum.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(indexes={
		@Index(name="regIdx", columnList="registered"),
		@Index(name="expireIdx", columnList="expires")
})
public class Endpoint implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String description;
	private String address;
	private String version;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime registered;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime expires;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public DateTime getRegistered() {
		return registered;
	}

	public void setRegistered(DateTime registered) {
		this.registered = registered;
	}

	public DateTime getExpires() {
		return expires;
	}

	public void setExpires(DateTime expires) {
		this.expires = expires;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Endpoint [name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", address=");
		builder.append(address);
		builder.append(", version=");
		builder.append(version);
		builder.append(", registered=");
		builder.append(registered);
		builder.append(", expires=");
		builder.append(expires);
		builder.append("]");
		return builder.toString();
	}

}
