package com.dhlee.rabbitmq.topic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/*
-> java -cp $CP ReceiveLogsTopic "#"
-> java -cp $CP ReceiveLogsTopic "kern.*"
-> java -cp $CP ReceiveLogsTopic "*.critical"
-> java -cp $CP ReceiveLogsTopic "kern.*" "*.critical"

java -cp $CP EmitLogTopic "kern.critical" "A critical kernel error"

 */
public class ReceiveLogsTopic {

  private static final String EXCHANGE_NAME = "topic_logs";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
    String queueName = channel.queueDeclare().getQueue();

    if (argv.length < 1) {
      System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
      System.exit(1);
    }

    for (String bindingKey : argv) {
      channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
    }

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        
        System.out.println("Expiration : " + properties.getExpiration());
        
        HashMap<String, Object> headers = (HashMap<String, Object>)properties.getHeaders();
        
        if(headers != null) {
	        Set<String> keys = headers.keySet();
	        for(String key : keys) {
	        	System.out.println("header : " + key + " = " + headers.get(key));
	        }
        }
        System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
      }
    };
    channel.basicConsume(queueName, true, consumer);
  }
}



