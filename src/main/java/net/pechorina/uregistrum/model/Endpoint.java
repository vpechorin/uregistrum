package net.pechorina.uregistrum.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = { @Index(name = "regIdx", columnList = "registered"),
		@Index(name = "expireIdx", columnList = "expires") })
public class Endpoint implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String description;
	private String localAddress;
	private String remoteAddress;
	private String version;
	private String username;

	@Transient
	private String password;

	@JsonIgnore
	private String encryptedPassword;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime registered;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime expires;

	public Endpoint() {
		super();
	}

	public Endpoint(String name, String localAddress, String remoteAddress) {
		super();
		this.name = name;
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
	}

	public Endpoint(String name, String localAddress, String remoteAddress,
			String username, String password) {
		super();
		this.name = name;
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
		this.username = username;
		this.password = password;
	}

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

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String address) {
		this.localAddress = address;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Endpoint [name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", localAddress=");
		builder.append(localAddress);
		builder.append(", remoteAddress=");
		builder.append(remoteAddress);
		builder.append(", version=");
		builder.append(version);
		builder.append(", username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", encryptedPassword=");
		builder.append(encryptedPassword);
		builder.append(", registered=");
		builder.append(registered);
		builder.append(", expires=");
		builder.append(expires);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localAddress == null) ? 0 : localAddress.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endpoint other = (Endpoint) obj;
		if (localAddress == null) {
			if (other.localAddress != null)
				return false;
		} else if (!localAddress.equals(other.localAddress))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
