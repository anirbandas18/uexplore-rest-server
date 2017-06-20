package com.ufl.uexplore.service;

import java.util.List;

import org.geojson.LngLatAlt;

import com.ufl.uexplore.dto.RequestedLocationDTO;

public interface ConveyancePolygonService {
	
	public List<LngLatAlt> findConveyancePolygonAroundLocation(RequestedLocationDTO location);

}
