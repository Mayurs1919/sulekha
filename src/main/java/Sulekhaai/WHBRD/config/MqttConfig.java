package Sulekhaai.WHBRD.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker-url}")
    private String brokerUrl; // e.g., tcp://localhost:1883

    @Value("${mqtt.client-id}")
    private String clientId;  // e.g., spring-mqtt-client

    @Bean
    public MqttClient mqttClient() throws Exception {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        // No username/password for local testing

        MqttClient client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
        client.connect(options);

        System.out.println("✅ Connected to MQTT broker: " + brokerUrl);
        return client;
    }
}
