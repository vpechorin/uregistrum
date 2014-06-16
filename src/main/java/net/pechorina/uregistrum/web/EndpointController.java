package net.pechorina.uregistrum.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pechorina.uregistrum.exceptions.EndpointExistsException;
import net.pechorina.uregistrum.exceptions.EndpointNotFoundException;
import net.pechorina.uregistrum.model.Endpoint;
import net.pechorina.uregistrum.service.EndpointService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/endpoints")
public class EndpointController {
	private static final Logger logger = LoggerFactory.getLogger(EndpointController.class);

	@Autowired
	Environment env;

	@Autowired
	private EndpointService endpointService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Endpoint>> list() {
		List<Endpoint> list = endpointService.listAll();
		return new ResponseEntity<List<Endpoint>>(list,	HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{name}")
	public ResponseEntity<Endpoint> lookup(
			@PathVariable("name") String name) throws EndpointNotFoundException {
		Endpoint e = endpointService.lookupEndpoint(name);
		return new ResponseEntity<Endpoint>(e, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Endpoint> createEndpoint(@RequestBody Endpoint endpoint, HttpServletRequest request) throws EndpointExistsException {
		DateTime now = new DateTime();
		endpoint.setRegistered(now.toDate());
		endpoint.setExpires( now.plusSeconds(env.getProperty("endpoint.expires.sec", Integer.class)).toDate() );
		Endpoint savedEntity = endpointService.addEndpoint(endpoint);
		logger.info("CREATE: " + savedEntity + " Src:" + request.getRemoteAddr());
		return new ResponseEntity<Endpoint>(savedEntity, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{name}")
	public void saveOrUpdateEndpoint(@PathVariable("name") String name, @RequestBody Endpoint endpoint,
			HttpServletRequest request,
			HttpServletResponse response)	{		
		
		Endpoint e;
		try {
			e = endpointService.lookupEndpoint(name);
			endpoint.setRegistered(e.getRegistered());
		} catch (EndpointNotFoundException e1) {
			e = endpoint;
			DateTime now = new DateTime();
			endpoint.setRegistered(now.toDate());
			endpoint.setExpires( now.plusSeconds(env.getProperty("endpoint.expires.sec", Integer.class)).toDate() );
			endpoint.setName(name);
		}

		endpointService.saveEndpoint(endpoint);
		logger.info("UPDATE: " + endpoint + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
	public void remove(
			@PathVariable("name") String name, HttpServletRequest request, HttpServletResponse response) throws EndpointNotFoundException {
		endpointService.deleteEndpoint(name);
		logger.info("DELETE: " + name + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler
	ResponseEntity handleExceptions(Exception ex) {
		ResponseEntity responseEntity = null;
		if (ex instanceof EndpointNotFoundException) {
			responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
		} else if(ex instanceof EndpointExistsException) {
			responseEntity = new ResponseEntity(HttpStatus.CONFLICT);
		} else {
			responseEntity = new ResponseEntity(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
}
