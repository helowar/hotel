package com.mangocity.client.hotel.search.serviceImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;

public abstract class HotelListPageImpl implements ClickHandler{
	
	public int pageSize = 10;
	public int curPage = 1;
	public int hotelCount = 0;
	public int pages = 0;
	
	public final static int NUMBUTTONS = 7;// button的个数
	public final static int NUMTOSHOWSYMBOL=NUMBUTTONS/2 ;//大于或小于该数的话显示SYMBOL
	//页面分割符号
	public String SYMBOL = "...";
	//上一页
	Button frontPageButton;
	//下一页
	Button nextPageButton;
	//copy
	Button frontPageButton_copy;
	Button nextPageButton_copy;
	
	Button firstPageButton;
	Button[] pageButtons;
	Button lastPageButton;
	HorizontalPanel mainPageBar = new HorizontalPanel();
	HorizontalPanel samplePageBar = new HorizontalPanel();
	
	//6/100页
	private InlineHTML pageHTML ;
	private InlineHTML pageHTML_copy;
	
	public HorizontalPanel getMainPageBar(){
		return mainPageBar;
	}
	
	public HorizontalPanel getSamplePageBar(){
		return samplePageBar;
	}
	
	/**
	 * 使用该方法得到相应的bar
	 */
	public void setPageParams(int count, int pageSize, int curPage){
		this.hotelCount = count;
		this.pageSize = pageSize;
		this.curPage =curPage;
		pages =  hotelCount/pageSize;
		if(pages == 0 ){
			return ;
		}
		if(hotelCount%pageSize != 0){
			pages ++  ;
		}	
		pageHTML = new InlineHTML("<em>"+curPage+"</em>/"+pages+"页");
		pageHTML.setStyleName("gwtSpanMiddlePage");
		pageHTML_copy = new InlineHTML("<em>"+curPage+"</em>/"+pages+"页");
		pageHTML_copy.setStyleName("gwtSpanMiddlePage");
		
		initPageButtons();
		setMainPageBar();
		setSamplePageBar();
	}
	
	private void initFrontAndBextPageButton(){
		 //上一页,下一页
		frontPageButton = new Button();
		frontPageButton.setText("上一页");
		frontPageButton.addClickHandler(this);
		nextPageButton = new Button();
		nextPageButton.setText("下一页");
		nextPageButton.addClickHandler(this);
		//copy
		frontPageButton_copy = new Button();
		frontPageButton_copy.setText("上一页");
		frontPageButton_copy.addClickHandler(this);
		nextPageButton_copy = new Button();
		nextPageButton_copy.setText("下一页");
		nextPageButton_copy.addClickHandler(this);
	}
	
	private void initPageButtons(){
	    pageButtons = new Button[pages<=NUMBUTTONS?pages:NUMBUTTONS]; 
	  //上一页,下一页
	    initFrontAndBextPageButton();
	    initFirstPageButton();
	    pageButtons[0] = firstPageButton;
	    if(pages==1){
	    	return;
	    }
		for(int i =1 ;i <pageButtons.length - 1;i++){
			pageButtons[i] = new Button();
		}
		initLastPageButton();
		pageButtons[pageButtons.length - 1] =  lastPageButton;
		//添加事件
		for(int i = 0 ; i<pageButtons.length;i++){
			pageButtons[i].addClickHandler(this);
		}
	}
	
	private void initFirstPageButton(){
		firstPageButton = new Button();
	}
	private void initLastPageButton(){
		lastPageButton = new Button();
	}
	
	// set pages format
	private void setMainPageBar(){
		mainPageBar.clear();
		mainPageBar.add(pageHTML);
		//上一页
		mainPageBar.add(frontPageButton);
		//第一个button
		pageButtons[0].setText(String.valueOf(1));
		pageButtons[0].setTabIndex(1);
		if(curPage == 1){
			pageButtons[0].setEnabled(false);
			frontPageButton.setEnabled(false);
			frontPageButton_copy.setEnabled(false);
		}else{
			frontPageButton.setEnabled(true);
			frontPageButton_copy.setEnabled(true);
		}
		if(curPage == pages){
			nextPageButton.setEnabled(false);
			nextPageButton_copy.setEnabled(false);
		}else{
			nextPageButton.setEnabled(true);
			nextPageButton_copy.setEnabled(true);
		}
		mainPageBar.add(pageButtons[0]);
		//中间5个
		setMiddleButtons(curPage);
		//最后一个button
		pageButtons[pageButtons.length - 1].setText(String.valueOf(pages));
		pageButtons[pageButtons.length - 1].setTabIndex(pages);
		mainPageBar.add(pageButtons[pageButtons.length - 1]);
		//按钮是否灰显
		for(int i = 0; i <pageButtons.length; i++){
			pageButtons[i].setEnabled(true);
			if(pageButtons[i].getTabIndex() == curPage){
				pageButtons[i].setEnabled(false);
				pageButtons[i].setStyleName("gwtPageSelected", true);
			}
		}
		//下一页
		mainPageBar.add(nextPageButton);
	}
	
	
	private void setMiddleButtons(int curPage){
		if(curPage - 1 > NUMTOSHOWSYMBOL && pages > NUMBUTTONS){
			mainPageBar.add(new InlineHTML(SYMBOL));
		}
		for(int i = 1 ; i < pageButtons.length - 1;i++){
			pageButtons[i].setText(String.valueOf(i+1));
			pageButtons[i].setTabIndex(i+1);
			mainPageBar.add(pageButtons[i]);
		}
		if(pages - curPage > NUMTOSHOWSYMBOL && pages > NUMBUTTONS){
			mainPageBar.add(new InlineHTML(SYMBOL));
		}
		//改变button的index
		if(curPage - 1 > NUMTOSHOWSYMBOL && pages > NUMBUTTONS ){
			for(int i = 1 ; i < pageButtons.length - 1;i++){
				pageButtons[i].setText(String.valueOf(curPage-NUMTOSHOWSYMBOL+i));
				pageButtons[i].setTabIndex(curPage-NUMTOSHOWSYMBOL+i);
			}
			//最后几个调整下显示
			if(pages - curPage < NUMTOSHOWSYMBOL){
				for(int i = 1 ; i < pageButtons.length - 1;i++){
					pageButtons[i].setText(String.valueOf(pages-NUMBUTTONS+1+i));
					pageButtons[i].setTabIndex(pages-NUMBUTTONS+1+i);
				}
			}
		}
	}
	
	
     private void setSamplePageBar(){	
		samplePageBar.clear();
		//上一页
		samplePageBar.add(pageHTML_copy);
		samplePageBar.add(frontPageButton_copy);
		samplePageBar.add(nextPageButton_copy);
	}
	/**
	 * method extends clickHandler
	 */
	public void onClick(ClickEvent event) {
		Button pageButton = (Button)event.getSource();
		if("上一页".equals(pageButton.getText())){
			setCurPage(curPage - 1);
		}
		else if("下一页".equals(pageButton.getText())){
			setCurPage(curPage + 1);
		}
		else{
		   setCurPage(pageButton.getTabIndex());
		}
		//setMainPageBar();
		onclickService();
	}
	
	
	public abstract void onclickService();
	
	public void setCurPage(int curPage){
		this.curPage = curPage;
	}
	
	public int getCurPage(){
		return curPage;
	}

}
