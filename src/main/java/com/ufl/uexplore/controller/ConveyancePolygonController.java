package com.ufl.uexplore.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ufl.uexplore.core.TransportMode;
import com.ufl.uexplore.dto.RequestedLocationDTO;

@Path(value = "/conveyancePolygon")
public class ConveyancePolygonController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConveyancePolygonController.class);
	private String emailId = "anirbandas18@live.com";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public FeatureCollection getConveyancePolygon(@QueryParam("mode") String transportMode,
			@QueryParam("longitude") Double longitude, @QueryParam("latitude") Double latitude) {
		LngLatAlt locationCoordinates = new LngLatAlt(longitude, latitude);
		TransportMode mode = TransportMode.valueOf(transportMode);
		RequestedLocationDTO rldto = new RequestedLocationDTO();
		rldto.setEmailId(emailId );
		rldto.setLocationCoordinates(locationCoordinates);
		rldto.setMode(mode);
		rldto.setTimeStamp(System.currentTimeMillis());
		LOGGER.info(rldto.toString());
		return null;
	}
	
}
