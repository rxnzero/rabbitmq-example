package com.dhlee.rabbitmq.object;

import com.rabbitmq.client.*;

import java.io.IOException;

public class RecvObject {

  private final static String QUEUE_NAME = "object.queue";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
    	  
    	if(MessageProperties.BASIC.getContentType().equals(properties.getContentType())) {
	      	User userExt = new User();
	      	try {
	      		userExt = (User)ExternalizeUtil.fromBytes(userExt,  body);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println(" [x] Received User : " + userExt);
    	}
    	else {
    		String message = new String(body, "UTF-8");
    		System.out.println(" [x] Received : " + message);
    	}
      }
    };
    boolean autoAck = true;
    channel.basicConsume(QUEUE_NAME, autoAck, consumer);
  }
}