package com.dhlee.rabbitmq.object;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class SendObject {

  private final static String QUEUE_NAME = "object.queue";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    
    List<String> phoneNumbers = new ArrayList<String>();
	phoneNumbers.add("02-1234-1234");
	phoneNumbers.add("010-1212-3434");
	User userWrite = new User(1, "홍길동", 10, phoneNumbers);
	byte[] extBytes = ExternalizeUtil.toBytes(userWrite);
	
	// content-type
	AMQP.BasicProperties props = new AMQP.BasicProperties
    		.Builder()
    		.contentType(MessageProperties.BASIC.getContentType())
    		.build();
    
	
	channel.basicPublish("", QUEUE_NAME, props, extBytes);
	
    System.out.println(" [x] Sent  USer : " + userWrite);

    channel.close();
    connection.close();
  }
}
