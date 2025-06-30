package Sulekhaai.WHBRD.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestConnectionController {
    @GetMapping("/test-connection")
    public ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("OK");
    }
} 