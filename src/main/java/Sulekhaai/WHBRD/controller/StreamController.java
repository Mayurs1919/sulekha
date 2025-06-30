package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.Websocket.ImagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/proxy")
public class StreamController {

    @Autowired
    private ImagePushService imagePushService;

    // In-memory image store
    private final Map<String, byte[]> latestImageMap = new ConcurrentHashMap<>();

    /**
     * Endpoint to receive image from Raspberry Pi script
     * Python sends { code: cameraId, image: imageFile }
     */
    @PostMapping(value = "/stream", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> receiveImage(
            @RequestPart("code") String cameraId,
            @RequestPart("image") MultipartFile imageFile) throws IOException {

        // Convert image to byte array
        byte[] imageBytes = StreamUtils.copyToByteArray(imageFile.getInputStream());

        // Store for GET access
        latestImageMap.put(cameraId, imageBytes);

        // Also push via WebSocket (if applicable)
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        imagePushService.push(cameraId, base64);

        return Map.of(
                "success", true,
                "cameraId", cameraId,
                "message", "Image received and stored"
        );
    }

    /**
     * Endpoint to serve latest image for a given cameraId
     * Frontend hits this: /api/proxy/stream?cameraId=123
     */
    @GetMapping(value = "/stream", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> serveLatestImage(@RequestParam String cameraId) {
        if (!latestImageMap.containsKey(cameraId)) {
            return ResponseEntity.status(404)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                    .body(("No image found for cameraId: " + cameraId).getBytes());
        }

        byte[] imageBytes = latestImageMap.get(cameraId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}
