package com.dhlee.rabbitmq.queue.hello;

import com.rabbitmq.client.*;

import java.io.IOException;

public class RecvGet {

  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
    int execCount = 0;
    while(true) {
	    GetResponse response = channel.basicGet(QUEUE_NAME, false); 
	    
	    if(response != null) {
	    	AMQP.BasicProperties properties = response.getProps();
	    	System.out.println("CorrelationId = " + properties.getCorrelationId());
		    if( "get".equals(properties.getCorrelationId()) ) {
			    byte[] body = response.getBody();
			    
			    String message = new String(body, "UTF-8");
			    System.out.println(" [x] Get '" + message + "'");
			    channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
			    execCount ++;
			    if(execCount > 3) break;
		    }
	    }
	    else {
	    	Thread.sleep(1000);
	    }
    }
    
  }
}