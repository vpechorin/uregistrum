package net.pechorina.uregistrum.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.pechorina.uregistrum.exceptions.EndpointExistsException;
import net.pechorina.uregistrum.exceptions.EndpointNotFoundException;
import net.pechorina.uregistrum.model.Endpoint;
import net.pechorina.uregistrum.service.EndpointService;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/endpoints")
public class EndpointController {

	@Autowired
	Environment env;

	@Autowired
	private EndpointService endpointService;

	@Autowired
	private EndpointResourceAssembler endpointResourceAssembler;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EndpointResource>> list() {
		List<Endpoint> list = endpointService.listAll();
		List<EndpointResource> resources = endpointResourceAssembler
				.toResources(list);
		return new ResponseEntity<List<EndpointResource>>(resources,
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{name}")
	public ResponseEntity<EndpointResource> lookup(
			@PathVariable("name") String name) throws EndpointNotFoundException {
		Endpoint e = endpointService.lookupEndpoint(name);
		EndpointResource res = endpointResourceAssembler.toResource(e);
		return new ResponseEntity<EndpointResource>(res, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<EndpointResource> createEndpoint(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "address", required = true) String address,
			@RequestParam(value = "description", required = false, defaultValue = "") String description,
			@RequestParam(value = "version", required = false, defaultValue = "") String version) throws EndpointExistsException {
		Endpoint savedEntity = endpointService.addEndpoint(name, address,
				description, version);
		EndpointResource resource = endpointResourceAssembler
				.toResource(savedEntity);
		return new ResponseEntity<EndpointResource>(resource,
				HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<EndpointResource> saveOrUpdateEndpoint(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "version", required = false) String version) {
		Endpoint e;
		try {
			e = endpointService.lookupEndpoint(name);
		} catch (EndpointNotFoundException e1) {
			e = new Endpoint();
			e.setRegistered(new DateTime());
			e.setName(name);
		}
		if (address != null)
			e.setAddress(address);
		if (description != null)
			e.setDescription(description);
		if (version != null)
			e.setVersion(version);
		Endpoint savedEntity = endpointService.saveEndpoint(e);
		EndpointResource resource = endpointResourceAssembler
				.toResource(savedEntity);
		return new ResponseEntity<EndpointResource>(resource,
				HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
	public void remove(
			@PathVariable("name") String name, HttpServletResponse response) throws EndpointNotFoundException {
		endpointService.deleteEndpoint(name);
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
