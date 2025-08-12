package Sulekhaai.WHBRD.controller;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mqtt")
public class MqttController {

    @Autowired
    private MqttClient mqttClient;

    @PostMapping("/publish")
    public String publishMessage(
            @RequestParam String topic,
            @RequestParam String message
    ) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1); // QoS level: 0, 1, 2
            mqttClient.publish(topic, mqttMessage);
            return "✅ Message published to topic: " + topic;
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to publish message: " + e.getMessage();
        }
    }

    @GetMapping("/status")
    public String getStatus() {
        if (mqttClient.isConnected()) {
            return "✅ MQTT Client is connected to broker.";
        } else {
            return "⚠️ MQTT Client is NOT connected.";
        }
    }
}
