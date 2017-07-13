package com.cs4390ServerBasedChat.Client;

import java.net.SocketTimeoutException;
import java.util.concurrent.Semaphore;
import java.net.*;



// Session thread for client
// multiple clients are just threads
public final class ClientThread extends Thread {
	Socket socket;
	Client client;
	public Semaphore initLock;
	
	public ClientThread(){
		super(Client.instance().threadGroup(), "NEW SESSION");
		client = Client.instance(); // this is an instance of client class
		this.initLock = new Semaphore(0);
	}
	@Override
	public void interrupt() {
        socket.close();
        super.interrupt();
    }
	@Override
	public void run(){
		try{
			socket = new Socket();
			socket.waitingForConnection(initLock);
			client.setSessionSocket(socket);
			String logon = socket.readMessage();
			if(logon == null){
				return;
			}
			if(logon.equals("Logon")) {
                System.out.println("You are now online. Type \"chat <client id>\""
                             + " to start a chat session.");
                client.setState(Client.State.ONLINE);
            }
		} catch(SocketTimeoutException e){
			System.out.println("Timeout Error ");
			try{
				client.setState(Client.State.OFFLINE);
			} catch(InterruptedException er){
				return;
			}
			this.exitCleanup();
			return;
		} catch( Exception e){
			System.out.println("Error");
			e.printStackTrace();
			this.exitCleanup();
			return;
			
		}
		while(!socket.isClosed()){	// while socket is not closed
			try{
				String msg = socket.readMessage();
				if(msg == null){
					break;
				}
				if(msg.equals("Logon")){
					if(!this.handleStart(msg))
						break;
				}
					else if (msg.matches("^START [0-9]+ [1-9][0-9]*$")) {
	                    if (!this.handleStart(msg)) break;
	                }
			}catch(InterruptedException eb){
				break;
			}
			
	}
}
	private boolean handleStart(String msg) {
		// TODO Auto-generated method stub
		return false;
	}
	private void exitCleanup() {
		// TODO Auto-generated method stub
		
	}
}
