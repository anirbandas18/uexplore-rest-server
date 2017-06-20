package com.ufl.uexplore.dto;

import java.io.Serializable;

import org.geojson.LngLatAlt;

import com.ufl.uexplore.core.TransportMode;

public class RequestedLocationDTO implements Serializable{

	private static final long serialVersionUID = 1191173024545984778L;
	
	private TransportMode mode;
	
	private String emailId;
	
	private Long timeStamp;
	
	private LngLatAlt locationCoordinates;

	public TransportMode getMode() {
		return mode;
	}

	public void setMode(TransportMode mode) {
		this.mode = mode;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public LngLatAlt getLocationCoordinates() {
		return locationCoordinates;
	}

	public void setLocationCoordinates(LngLatAlt locationCoordinates) {
		this.locationCoordinates = locationCoordinates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((locationCoordinates == null) ? 0 : locationCoordinates.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
		RequestedLocationDTO other = (RequestedLocationDTO) obj;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (locationCoordinates == null) {
			if (other.locationCoordinates != null)
				return false;
		} else if (!locationCoordinates.equals(other.locationCoordinates))
			return false;
		if (mode != other.mode)
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RequestedLocationDTO [mode=" + mode + ", emailId=" + emailId + ", timeStamp=" + timeStamp
				+ ", locationCoordinates=" + locationCoordinates + "]";
	}

		
}
