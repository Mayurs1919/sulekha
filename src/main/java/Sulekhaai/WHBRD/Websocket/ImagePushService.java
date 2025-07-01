package Sulekhaai.WHBRD.Websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ImagePushService {

    private final SimpMessagingTemplate messagingTemplate;

    public ImagePushService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void push(String cameraId, String base64Image) {
        // WebSocket destination: /topic/preview/{cameraId}
        String destination = "/topic/preview/" + cameraId;
        messagingTemplate.convertAndSend(destination, base64Image);
    }
}