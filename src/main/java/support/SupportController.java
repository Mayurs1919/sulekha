package support;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @GetMapping("/overview")
    public Map<String, Object> overview() {
        return Map.of(
            "faq", List.of("How to reset password?", "How to change theme?"),
            "contactOptions", Map.of("email", "support@example.com", "phone", "+91-9876543210")
        );
    }
}
