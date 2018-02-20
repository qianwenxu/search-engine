package com.pr.xu;

public class scoredoc{
	String id;
	String content;
	String title;
	String pr;
	String theid;
	public String getid(){
		return id;
	}
	public String getcontent(){
		return content;
	}
	public String gettitle(){
		return title;
	}
	public String getpr(){
		return pr;
	}
	public String getins(){
		if(content.length()>30){
			return content.substring(0, 30)+"...";
		}
		else if(content.length()>10){
			return content.substring(0, 10)+"...";
		}
		else{
			return "";
		}
	}
	public String gettheid(){
		return theid;
	}
}