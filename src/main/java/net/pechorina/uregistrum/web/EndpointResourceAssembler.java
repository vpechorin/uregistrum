package net.pechorina.uregistrum.web;

import net.pechorina.uregistrum.model.Endpoint;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class EndpointResourceAssembler extends
		ResourceAssemblerSupport<Endpoint, EndpointResource> {

	public EndpointResourceAssembler() {
		super(EndpointController.class, EndpointResource.class);
	}

	@Override
	public EndpointResource toResource(Endpoint endpoint) {
		EndpointResource res = instantiateResource(endpoint);
		res.endpoint = endpoint;
		res.add(linkTo(EndpointController.class).slash(endpoint.getName())
				.withSelfRel());
		return res;
	}

}
