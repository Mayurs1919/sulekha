package Sulekhaai.WHBRD.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalErrorHandler implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(WebRequest webRequest) {
        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(
                (ServletWebRequest) webRequest, ErrorAttributeOptions.defaults());

        Map<String, Object> customError = new HashMap<>();
        customError.put("success", false);
        customError.put("message", errorDetails.getOrDefault("error", "Unknown error"));
        customError.put("status", errorDetails.get("status"));
        customError.put("path", errorDetails.get("path"));
        customError.put("timestamp", errorDetails.get("timestamp"));

        HttpStatus status = HttpStatus.valueOf((int) errorDetails.getOrDefault("status", 500));
        return new ResponseEntity<>(customError, status);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 45f4df5 (Updated Spring Boot backend: added, modified, and deleted files)
