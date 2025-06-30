package Sulekhaai.WHBRD.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import Sulekhaai.WHBRD.model.ImageStream;

@Controller
public class ImageStreamController {

    @MessageMapping("/stream")
    public ImageStream broadcastImage(ImageStream image) {
        return image;
    }
}

