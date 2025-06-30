package Sulekhaai.WHBRD.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/proxy")
public class ImageProxyController {

    private static final String EXTERNAL_IMAGE_URL = "https://sulekhaapi.samsanlabs.com/stream";

    @GetMapping("/image")
    public ResponseEntity<byte[]> proxyImage() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                EXTERNAL_IMAGE_URL,
                HttpMethod.GET,
                null,
                byte[].class
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Or IMAGE_PNG, based on actual type
            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(null);
        }
    }
}
