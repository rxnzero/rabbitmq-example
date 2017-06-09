package com.dhlee.rabbitmq.queue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
	public App() throws Exception{
		int messageCount = 1 ;
		QueueConsumer consumer = new QueueConsumer("queue.test");
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();
		
		Producer producer = new Producer("queue.test");
		
		for (int i = 0; i < messageCount; i++) {
			HashMap message = new HashMap();
			message.put("message number", i);
			producer.sendMessage(message);
			System.out.println("Message Number "+ i +" sent.");
		}
	}
	
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception{
	  new App();
	}

}
