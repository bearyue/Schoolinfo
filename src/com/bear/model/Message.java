package com.bear.model;



public class Message {
	int message_id;
	int user_id;
	String message_content;
	String message_time;
    int message_gtimes;
    int buttonimage;
    int buttonstate;
	
	public int getMessage_id() {
		return message_id;
	}
	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}
	public String getMessage_time() {
		return message_time;
	}
	public void setMessage_time(String message_time) {
		this.message_time = message_time;
	}
	public int getMessage_gtimes() {
		return message_gtimes;
	}
	public void setMessage_gtimes(int message_gtimes) {
		this.message_gtimes = message_gtimes;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
    public int getButtonimage() {
        return buttonimage;
    }

    public void setButtonimage(int buttonimage) {
        this.buttonimage = buttonimage;
    }
    public int getButtonstate() {
        return buttonstate;
    }

    public void setButtonstate(int buttonstate) {
        this.buttonstate = buttonstate;
    }
}
