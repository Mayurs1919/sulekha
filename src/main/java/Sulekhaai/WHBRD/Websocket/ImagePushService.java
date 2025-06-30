package Sulekhaai.WHBRD.Websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ImagePushService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void push(String cameraId, String base64Img) {
        messagingTemplate.convertAndSend("/topic/image/" + cameraId, base64Img);
    }
}
