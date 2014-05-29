package net.pechorina.uregistrum.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = { @Index(name = "regIdx", columnList = "registered"),
		@Index(name = "expireIdx", columnList = "expires") })
public class Endpoint implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(Endpoint.class);
	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String description;
	private String scheme;
	private String host;
	private int port;
	private String path;
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
	
	public Endpoint(String name, URI uri, String username, String password) {
		super();
		this.name = name;
		this.scheme = uri.getScheme();
		this.host = uri.getHost();
		this.port = uri.getPort();
		this.path = uri.getPath();
		this.username = username;
		this.password = password;
	}
	
	public Endpoint(String name, String uriStr, String username, String password) {
		super();
		URI uri = null;
		try {
			uri = new URI(uriStr);
			this.name = name;
			this.scheme = uri.getScheme();
			this.host = uri.getHost();
			this.port = uri.getPort();
			this.path = uri.getPath();
			this.username = username;
			this.password = password;
		} catch (URISyntaxException e) {
			logger.error("Bad URI syntax: " + uriStr + " Exception: " + e);
		}
	}
	
	public Endpoint(String name, String uriStr) {
		super();
		URI uri = null;
		try {
			uri = new URI(uriStr);
			this.name = name;
			this.scheme = uri.getScheme();
			this.host = uri.getHost();
			this.port = uri.getPort();
			this.path = uri.getPath();
		} catch (URISyntaxException e) {
			logger.error("Bad URI syntax: " + uriStr + " Exception: " + e);
		}
	}

	public Endpoint(String name, String scheme, String host,
			int port, String path, String username,
			String password) {
		super();
		this.name = name;
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Endpoint [name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", uri=|");
		builder.append(scheme);
		builder.append("://");
		builder.append(host);
		builder.append(":");
		builder.append(port);
		builder.append(path);
		builder.append("|, version=");
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
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + port;
		result = prime * result + ((scheme == null) ? 0 : scheme.hashCode());
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
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (port != other.port)
			return false;
		if (scheme == null) {
			if (other.scheme != null)
				return false;
		} else if (!scheme.equals(other.scheme))
			return false;
		return true;
	}

}
