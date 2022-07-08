package com.example.demo.service.dto;

import java.util.List;

public class SuccessResponseListDto<I> extends SuccessResponseDto<I> {

	private I data;
	private long totalItems;
	private int totalPages;
	private int currentPage;
	
	public I getData() {
		return data;
	}
	public void setData(I data) {
		this.data = data;
	}
	public long getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
