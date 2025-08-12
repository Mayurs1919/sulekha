package Sulekhaai.WHBRD.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@RestController
@Component
public class GlobalErrorHandler implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(WebRequest webRequest) {
        try {
            Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(
                    (ServletWebRequest) webRequest, ErrorAttributeOptions.defaults());

            Map<String, Object> customError = new HashMap<>();
            customError.put("success", false);
            customError.put("message", errorDetails.getOrDefault("error", "Unknown error"));
            customError.put("status", errorDetails.get("status"));
            customError.put("path", errorDetails.get("path"));
            customError.put("timestamp", errorDetails.get("timestamp"));

            Object statusObj = errorDetails.getOrDefault("status", 500);
            HttpStatus status = HttpStatus.valueOf(statusObj instanceof Integer ? (Integer) statusObj : 500);
            return new ResponseEntity<>(customError, status);
        } catch (Exception e) {
            // Fallback error response
            Map<String, Object> fallbackError = new HashMap<>();
            fallbackError.put("success", false);
            fallbackError.put("message", "Internal server error");
            fallbackError.put("status", 500);
            return new ResponseEntity<>(fallbackError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
