//ScaleiQ Ingestor use the following dependencies. For the demo; some versions are not the latest, but the compatible ones
//with the artifacts.
//Setup and import libraries under build.gradle
// // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
//        implementation 'org.apache.kafka:kafka-clients:3.4.0'
//
//        // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
//        implementation 'org.slf4j:slf4j-api:2.0.6'
//
//        // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
//        implementation 'org.slf4j:slf4j-simple:2.0.6'
//
//        // https://mvnrepository.com/artifact/com.launchdarkly/okhttp-eventsource
//        implementation 'com.launchdarkly:okhttp-eventsource:2.5.0'
//
//        // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
//        implementation 'com.squareup.okhttp3:okhttp:4.10.0'




import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class IngestorDataStreamDemo {

    public static void main(String[] args) throws InterruptedException {
        //Set Kafka settings variables accordingly
        String bootstrapserver = "127.0.0.1:9092";

        //Create Producer Properties
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapserver);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //Tuning Parameters, Adjust Accordingly, based on initial Metrics Testing
        //This settings will adjust the batching mechanism to increase performance
        //Note: These metrics are a combination, based on Metrics, do not change the values, individually
        props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20"); //milliseconds delay
        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); //KB
        props.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); //Compression Type


        //Safe Producer Settings for Kafka versions earlier than 3.0. Remove comments below to activate them.
        //props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        //props.setProperty(ProducerConfig.ACKS_CONFIG, "all"); //Same as acks= -1 (Exactly once delivery)
        //props.setProperty((ProducerConfig.RETRIES_CONFIG, Integer.toString((Integer.MAX_VALUE));

        // Create the Ingestor Data Stream Demo producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        String topic = "IngestorDataStreamDemo";


        //EventHandler Class Call, the function will call the eventHandler class
        EventHandler eventHandler = new IngestorEventHandler(producer, topic); //Modify with class definition


        //User-Interface Add required v.2.0 for customer url.Ingestor development internal
        // Add Data Stream url , example url below for testing

        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();


        //Start the producer
        eventSource.start();

        //Add external; user input
        // Stream producer for x minutes for the demo testing. Set the timeout per customers time requirement
        TimeUnit.MINUTES.sleep(1);

    }
}
