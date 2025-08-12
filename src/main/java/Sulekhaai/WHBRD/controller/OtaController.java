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
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/ota")
public class OtaController {

    private static final Logger logger = Logger.getLogger(OtaController.class.getName());

    @Autowired
    private OtaService otaService;

    /**
     * Upload firmware file
     * Endpoint: POST /api/ota/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFirmware(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file type
            if (!file.getOriginalFilename().endsWith(".bin") &&
                !file.getOriginalFilename().endsWith(".hex") &&
                !file.getOriginalFilename().endsWith(".zip")) {
                return ResponseEntity.badRequest().body("Invalid file type. Only .bin, .hex, or .zip allowed.");
            }

            String filename = otaService.uploadFirmware(file);
            logger.info("✅ Firmware uploaded: " + filename);
            return ResponseEntity.ok("Firmware uploaded successfully: " + filename);
        } catch (IOException e) {
            logger.severe("❌ Firmware upload failed: " + e.getMessage());
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
            if (firmware == null || !firmware.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            logger.info("⬇️ Firmware downloaded: " + firmware.getFilename());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + firmware.getFilename() + "\"")
                    .body(firmware);
        } catch (IOException e) {
            logger.severe("❌ Failed to fetch latest firmware: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
