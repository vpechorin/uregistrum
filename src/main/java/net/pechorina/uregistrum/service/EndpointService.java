package net.pechorina.uregistrum.service;

import java.util.List;

import javax.transaction.Transactional;

import net.pechorina.uregistrum.exceptions.EndpointExistsException;
import net.pechorina.uregistrum.exceptions.EndpointNotFoundException;
import net.pechorina.uregistrum.model.Endpoint;
import net.pechorina.uregistrum.repos.EndpointRepositary;

import org.jasypt.util.text.BasicTextEncryptor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EndpointService {
	
	@Autowired
	private EndpointRepositary endpointRepo;
	
	@Autowired
	Environment env;
	
	@Transactional
	public Endpoint addEndpoint(Endpoint p) throws EndpointExistsException {
		Endpoint existingEntity = endpointRepo.findOne(p.getName());
		if (existingEntity != null) throw new EndpointExistsException("Endpoint already exists: " + p.getName());
		updateEncPassword(p);
		Endpoint entity = endpointRepo.saveAndFlush(p);
		return entity;
	}
	
	@Transactional
	public Endpoint saveEndpoint(Endpoint p) {
		updateDateTime(p);
		updateEncPassword(p);
		Endpoint entity = endpointRepo.saveAndFlush(p);
		return entity;
	}
	
	@Transactional
	public Endpoint addEndpoint(String name, String localAddress, String remoteAddress, int port, String path, String description, String version) throws EndpointExistsException {
		Endpoint existingEntity = endpointRepo.findOne(name);
		if (existingEntity != null) throw new EndpointExistsException("Endpoint already exists: " + name);

		Endpoint e = new Endpoint();
		e.setName(name);
		e.setLocalAddress(localAddress);
		e.setRemoteAddress(remoteAddress);
		e.setPort(port);
		e.setPath(path);
		e.setDescription(description);
		e.setVersion(version);
		DateTime now = new DateTime();
		DateTime expireDt = now.plusSeconds(env.getProperty("endpoint.expires.sec", Integer.class));
		e.setRegistered(now);
		e.setExpires(expireDt);
		
		updateEncPassword(e);
		Endpoint entity = endpointRepo.saveAndFlush(e);
		return entity;
	}
	
	@Transactional
	public List<Endpoint> listAll() {
		List<Endpoint> list = endpointRepo.findAll();
		return list;
	}
	
	@Transactional
	public void removeEndpoint(String name) throws EndpointNotFoundException {
		Endpoint p = endpointRepo.findOne(name);
		if (p != null) {
			endpointRepo.delete(p);
		}
		else {
			throw new EndpointNotFoundException("Cannot find endpoint: " + name);
		}
	}
	
	private void updateDateTime(Endpoint p) {
		DateTime now = new DateTime();
		DateTime expireDt = now.plusSeconds(env.getProperty("endpoint.expires.sec", Integer.class));
		p.setExpires(expireDt);
	}
	
	@Transactional
	public Endpoint updateEndpoint(String name) {
		Endpoint p = endpointRepo.findOne(name);

		if (p != null) {
			updateDateTime(p);
			updateEncPassword(p);
			endpointRepo.saveAndFlush(p);
		}
		return p;
	}
	
	@Transactional
	public Endpoint lookupEndpoint(String name) throws EndpointNotFoundException {
		Endpoint p = endpointRepo.findOne(name);
		if (p == null) throw new EndpointNotFoundException("Cannot find endpoint: " + name);
		updatePassword(p);
		return p;
	}
	
	@Transactional
	public void deleteEndpoint(Endpoint e) {
		endpointRepo.delete(e);
	}
	
	@Transactional
	public void deleteEndpoint(String name) {
		endpointRepo.delete(name);
	}

	private void updateEncPassword(Endpoint e) {
		if (e.getPassword() == null) {
			e.setEncryptedPassword(null);
			return;
		}
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(env.getProperty("app.secret.key"));
		String encryptedPass = textEncryptor.encrypt(e.getPassword());
		e.setEncryptedPassword(encryptedPass);
	}
	
	private void updatePassword(Endpoint e) {
		if (e.getEncryptedPassword() == null) {
			e.setPassword(null);
			return;
		}
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(env.getProperty("app.secret.key"));
		String plainText = textEncryptor.decrypt(e.getEncryptedPassword());
		e.setPassword(plainText);
	}

}
