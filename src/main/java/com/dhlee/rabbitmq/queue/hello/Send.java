package com.dhlee.rabbitmq.queue.hello;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    String message = "Hello World!";
    
    AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
    
    Map<String, Object> headers = new HashMap<String, Object>();
    
    headers.put("latitude",  51.5252949);
    headers.put("longitude", -0.0905493);

    builder.headers(headers);

//    builder.expiration("1000");
    builder.correlationId("consumer");
    AMQP.BasicProperties properties = builder.build();
    
    channel.basicPublish("", QUEUE_NAME, properties, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");
    
    builder.correlationId("consumer2");
    properties = builder.build();
    channel.basicPublish("", QUEUE_NAME, properties, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");
    
    channel.close();
    connection.close();
  }
}
