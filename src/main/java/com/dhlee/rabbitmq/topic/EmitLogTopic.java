package com.dhlee.rabbitmq.topic;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogTopic {

  private static final String EXCHANGE_NAME = "topic_logs";

  public static void main(String[] argv) {
    Connection connection = null;
    Channel channel = null;
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");

      connection = factory.newConnection();
      channel = connection.createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

      String routingKey = getRouting(argv);
      String message = getMessage(argv);
      
      AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
      
      Map<String, Object> headers = new HashMap<String, Object>();
      
      headers.put("latitude",  51.5252949);
      headers.put("longitude", -0.0905493);

      builder.headers(headers);

      builder.expiration("1000");
      
      AMQP.BasicProperties properties = builder.build();
      
      channel.basicPublish(EXCHANGE_NAME, routingKey, properties, message.getBytes("UTF-8"));
      System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");

    }
    catch  (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (connection != null) {
        try {
          connection.close();
        }
        catch (Exception ignore) {}
      }
    }
  }

  private static String getRouting(String[] strings){
    if (strings.length < 1)
    	    return "anonymous.info";
    return strings[0];
  }

  private static String getMessage(String[] strings){
    if (strings.length < 2)
    	    return "Hello World!";
    return joinStrings(strings, " ", 1);
  }

  private static String joinStrings(String[] strings, String delimiter, int startIndex) {
    int length = strings.length;
    if (length == 0 ) return "";
    if (length < startIndex ) return "";
    StringBuilder words = new StringBuilder(strings[startIndex]);
    for (int i = startIndex + 1; i < length; i++) {
        words.append(delimiter).append(strings[i]);
    }
    return words.toString();
  }
}


