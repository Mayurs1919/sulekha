package Sulekhaai.WHBRD.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker.url:tcp://localhost:1883}")
    private String brokerUrl;

    @Value("${mqtt.client.id:sulekha-backend}")
    private String clientId;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;

    @Value("${mqtt.topic.device.status:device/+/status}")
    private String deviceStatusTopic;

    @Value("${mqtt.topic.device.command:device/+/command}")
    private String deviceCommandTopic;

    @Value("${mqtt.topic.camera.stream:camera/+/stream}")
    private String cameraStreamTopic;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        
        options.setServerURIs(new String[]{brokerUrl});
        options.setCleanSession(true);
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        options.setAutomaticReconnect(true);
        
        if (!username.isEmpty()) {
            options.setUserName(username);
        }
        if (!password.isEmpty()) {
            options.setPassword(password.toCharArray());
        }
        
        factory.setConnectionOptions(options);
        return factory;
    }

    // Inbound channel for receiving messages
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // Outbound channel for sending messages
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    // Message producer for incoming MQTT messages
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                clientId + "_inbound", mqttClientFactory(),
                deviceStatusTopic, cameraStreamTopic, "device/+/heartbeat", "camera/+/control"
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    // Message handler for outgoing MQTT messages
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                clientId + "_outbound", mqttClientFactory()
        );
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("device/broadcast");
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }

    // Getters for topic names (used by service classes)
    public String getDeviceStatusTopic() {
        return deviceStatusTopic;
    }

    public String getDeviceCommandTopic() {
        return deviceCommandTopic;
    }

    public String getCameraStreamTopic() {
        return cameraStreamTopic;
    }
}