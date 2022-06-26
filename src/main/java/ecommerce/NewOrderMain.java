package ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties())) {
      for (int i = 0; i <= 100; i++) {
        String value = "123,1212,545000";
        String key = UUID.randomUUID().toString() + "123,1212,545000";
        ProducerRecord<String, String> record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", key, value);
        producer.send(record, callback()).get();
        String email = "Thank you for your order! We are processing your order!";
        ProducerRecord<String, String> emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", key, email);
        producer.send(emailRecord, callback()).get();
      }
    }
  }

  private static Callback callback() {
    return (data, ex) -> {
      if (ex != null) {
        ex.printStackTrace();
        return;
      }
      System.out.println("Sucesso enviando " + data.topic() + ":::partition" + data.partition() + "/ offset" + data.offset() + "/ timestamp" + data.timestamp());
    };
  }

  private static Properties properties() {
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    return properties;
  }
}
