package com.cs4390ServerBasedChat.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Semaphore;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


// socket class 
// first step towards implementing the TCP/IP protocol
public class Socket {
	// declaring data fields
	
	private Client client;
	private java.net.Socket socket;
	private ServerSocket bind_socket; // this is to bind the socket
	
	// Once the connection has been established, we will use the sockets to write and 
	// read the data
	// so need read and write buffers
	private InputStream inputStream;
	private OutputStream outputStream;
	
	// constructor
	public Socket() throws IOException{
		client = Client.instance(); // create an instance
		bind_socket = new ServerSocket(); 
		
		// enable so_timeout
		bind_socket.setSoTimeout(client.config.timeoutInterval());
		// create a socket and pass ip and port
		// bind() will assign the local computer's port number(not needed in this case)
		// since we can use any port except the reserved ports
		bind_socket.bind(new InetSocketAddress(client.config.IPAddress, client.config.port));
	}
	// multiple clients will involve in chat
    // using thread and semaphores
	public void waitingForConnection(Semaphore initLock) throws SocketException, SocketTimeoutException,
	InterruptedException, IOException {
		try{
			System.out.println("Listing for Connection on" + bind_socket.getInetAddress().getHostAddress()
					+ ":" + bind_socket.getLocalPort());
			
			initLock.release();
			socket = bind_socket.accept();
			System.out.println("Accepting connection from:" + socket.getPort() );
			bind_socket.close();
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		}catch(SocketException e){
			bind_socket.close();
			System.out.print(e);
		} catch(SocketTimeoutException e){
			bind_socket.close();
			System.out.println(e);
		} catch (IOException e){
			bind_socket.close();
			System.out.println(e);
		}
	}
	// client socket is still open
	
	// Reading message
	// TCP operates on data streams, and messages need to be framed.
	// handle incoming and outgoing data streams
	// when sending data stream, convert the message to a byte of array
	// extract the length of the byte array and send it along with byte array
	
	public String readMessage(){
		try{
			byte[] arrayM = new byte[4]; // 4, 8...
			// what if the message length is not in size
			// display the message
			if(inputStream.read(arrayM) != 4){
				System.out.println("Length Error");
				return null;
			}
			 ByteBuffer msgLenBuff = ByteBuffer.wrap(arrayM);
	         int msgLen = msgLenBuff.getInt();
	         
	         byte[] message = new byte[msgLen];
	         if (inputStream.read(message) != msgLen) {
	                System.out.println("Length Error.");
	                return null;
	            }
	         return new String(message, StandardCharsets.UTF_8);
		} catch(IOException e){
			System.out.println(e);
			return null;
		}
		
	}
	public boolean writeMsg(byte[] msgBytes){
		byte[] msg = new byte[msgBytes.length +4];
		ByteBuffer msgBuff = ByteBuffer.wrap(msg);
		msgBuff.putInt(msgBytes.length);
        msgBuff.put(msgBytes);
        try {
            outputStream.write(msg);
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
       
		
	}
	 public boolean writeMsg(String message) {
         return writeMsg(message.getBytes(StandardCharsets.UTF_8));
     }
	
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return socket.isClosed();
	}
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
