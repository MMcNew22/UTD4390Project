package com.cs4390ServerBasedChat.Client;

import java.io.IOException;
import java.lang.Thread.State;
import java.lang.ThreadGroup;
import java.net.*;

// Client class
public class Client {
	// client id
	private int id;
	// chat session and partner id
	private int sessionid;
	private int partnerId;
	
	// run time management
	private static Client instance;
	private ThreadGroup threadGroup;
	
	// client status
	private State state; // client is just state of a machine
	
	 public Configuration config;	// instance of configuration
	
	// defining protocol messages
	public static enum State{
		HELLO,CONNECTED,CHAT_REQUEST, CHAT_STARTED, UNREACHABLE,END_REQUEST,END_NOTIF,
		CHAT, HISTORY_REQ, HISTORY_RESP, ONLINE, OFFLINE, 
	}

	public static Client instance() {
		// TODO Auto-generated method stub
		return null;
	}

	public ThreadGroup threadGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setState(com.cs4390ServerBasedChat.Client.Client.State online) {
		// TODO Auto-generated method stub
		
	}

	public void setSessionSocket(Socket socket) throws InterruptedException,  IOException {
		
		// TODO Auto-generated method stub
		
	}
	
}
