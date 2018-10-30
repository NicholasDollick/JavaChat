import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.Date;
import java.awt.event.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;

public class ClientTest
{
	//constructor
	public static void main(String [] args) throws InterruptedException {
		int port = 2423;
		DH dh = new DH();
		
		//configure ssl
		System.setProperty("javax.net.ssl.trustStore", "myTrustStore.jts");
		System.setProperty("javax.net.ssl.trustStorePassword", "asdf123");
		//System.setProperty("javax.net.debug", "ssl");
		try {
			SSLSocketFactory sockFact = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket sslSocket = (SSLSocket)sockFact.createSocket("localhost",port); //something about inet addy might be neded
			DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());
			DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());
			
			//small test of DH key exchange
			try {
				//generate keys
				System.out.println("{*} Starting key exchange");
				dh.generateKeys();
				System.out.println("{*} Waiting for Key");
				int len = inputStream.readInt();
				if(len > 0) {
					byte[] m = new byte[len];
					inputStream.readFully(m, 0, m.length);
					dh.receivePublicKey(m);
				}
				System.out.println("{+} Key Recieved");
				
				
				
				System.out.println("{*} Sending Key");
				byte[] pubKey = dh.getPublicKeyEnc();
				len = pubKey.length;
				outputStream.writeInt(len);
				outputStream.write(pubKey);
				System.out.println("{+} Key Sent");
				
				dh.getShared();
				System.out.println(dh.getSecret());
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			System.out.println();
			System.out.println("{+} Communication now encrypted");
			System.out.println("{*} Beginning test with server echo...");
			System.out.println();
			
			while(true) {
				System.out.print("Write a message: ");
				String out = System.console().readLine();
				
				byte[] messageOut = AES.encrypt(out, dh.getSecret());
				int len = messageOut.length;
				outputStream.writeInt(len);
				outputStream.write(messageOut);
				
				
				
				int readInLen = inputStream.readInt();
				byte[] readIn;
				String message = "";
				
				if(readInLen > 0)
				{
					readIn = new byte[readInLen];
					inputStream.readFully(readIn, 0, readInLen);
					message = AES.decrypt(readIn, dh.getSecret());
				}
				System.out.println(message);
				
				if(out.equals("CLOSE")) {
					break;
				}				
			}
			/*
			while(true) {
				System.out.println("Waiting for client...");
				System.out.println("Accepted connection from: " + connection);
				ServerWorker worker = new ServerWorker(connection);
				worker.start();
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}