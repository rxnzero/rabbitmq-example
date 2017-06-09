package com.dhlee.rabbitmq.queue.hello;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv2 {

  private final static String QUEUE_NAME = "hello";
  static int execCount = 0;
  
  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");
    factory.setUri("amqp://guest:guest@localhost:5672");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
   
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
    	  System.out.println("CorrelationId = " + properties.getCorrelationId());	  
    	if( "consumer2".equals(properties.getCorrelationId()) ) {  
	        String message = new String(body, "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	        execCount++;
      	}
      }
    };
    boolean autoAck = true;
    String consumerTag = channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    System.out.println(" [x] consumerTag =" + consumerTag);
    
    while(true) {
    	if(execCount > 3) channel.basicCancel(consumerTag);
    }
  }
}