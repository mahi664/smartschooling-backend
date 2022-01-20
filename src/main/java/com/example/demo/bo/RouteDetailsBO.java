package com.example.demo.bo;

public class RouteDetailsBO {
	private String routeId;
	private String source;
	private String destination;
	private double distance;
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	@Override
	public String toString() {
		return "RouteDetailsBO [routeId=" + routeId + ", source=" + source + ", destination=" + destination
				+ ", distance=" + distance + "]";
	}
}
