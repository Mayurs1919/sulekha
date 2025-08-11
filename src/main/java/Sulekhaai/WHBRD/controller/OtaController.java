package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.services.OtaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/ota")
public class OtaController {

    @Autowired
    private OtaService otaService;

    /**
     * Upload firmware file
     * Endpoint: POST /api/ota/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFirmware(@RequestParam("file") MultipartFile file) {
        try {
            String filename = otaService.uploadFirmware(file);
            return ResponseEntity.ok("Firmware uploaded successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload firmware: " + e.getMessage());
        }
    }

    /**
     * Download the latest firmware
     * Endpoint: GET /api/ota/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<Resource> downloadLatestFirmware() {
        try {
            Resource firmware = otaService.getLatestFirmware();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + firmware.getFilename() + "\"")
                    .body(firmware);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
