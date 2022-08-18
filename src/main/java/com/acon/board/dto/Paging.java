package com.acon.board.dto;

import lombok.Getter;
import lombok.ToString;

@Getter@ToString
public class Paging {

	private int page; //요청한 페이지
	private int rowCount; //총 row 수
	private String url;
	private int row=10; //1페이지당 출력될 row 수
	
	private int start; //화면에 출력될 페이지의 시작
	private int end; //화면에 출력될 페이지의 마지막
	private int firstPage=1;
	private int lastPage;
	private int previousPage;
	private int nextPage;
	
	private boolean isFirst;
	private boolean isLast;
	private boolean isPrevious;
	private boolean isNext;
	public Paging(int page, int rowCount, String url) {
		this.page=page;
		this.rowCount=rowCount;
		this.url=url;
		this.setAll();
	}
	public Paging(int page, int rowCount, String url, int row) {
		this.page=page;
		this.rowCount=rowCount;
		this.url=url;	
		this.row=row;
		this.setAll();
	}
	public void setAll() {
		this.lastPage=rowCount/row+((rowCount%row>0)?1:0);
		this.previousPage=page-1;
		this.nextPage=page+1;
		
		this.isFirst=(page>firstPage)?true:false;
		this.isPrevious=(page>firstPage)?true:false;

		this.isLast=(page<lastPage)?true:false;
		this.isNext=(page<lastPage)?true:false;

		
		this.start=this.page-2; //(-1,0)
		this.end=this.page+2;    //(lastPage+1,lastPage+2)
		
		if(this.start<firstPage) {
			end=end-start+1;
			if(end<lastPage) {start=lastPage;}
			start=firstPage;
		}
		if(end>lastPage) {
			start=start-end+lastPage;
			if(start<firstPage) {start=firstPage;}
			end=lastPage;
		}	
	}
	
}








