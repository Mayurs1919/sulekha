package Sulekhaai.WHBRD.controller;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://192.168.1.200:5173",
        "http://192.168.1.200:8081",
       
        "https://sulekha-aii.netlify.app"
})
public class ChatController {

    @Value("#{systemEnvironment['OPENAI_API_KEY']}")
private String openaiApiKey;

    private OpenAiService openAiService;

    @PostConstruct
    public void init() {
        if (openaiApiKey == null || openaiApiKey.isBlank()) {
            System.err.println("[ERROR] OpenAI API key is missing. Please set 'OPENAI_API_KEY' in the environment.");
        } else {
            this.openAiService = new OpenAiService(openaiApiKey);
        }
    }

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody Map<String, String> body) {
        if (openAiService == null) {
            return ResponseEntity.status(500).body(Map.of("reply", "OpenAI API key is not configured on the server."));
        }

        String prompt = body.get("prompt");
        if (prompt == null || prompt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("reply", "Prompt is required."));
        }

        try {
            CompletionRequest request = CompletionRequest.builder()
                    .prompt(prompt)
                    .model("gpt-3.5-turbo-instruct")
                    .maxTokens(150)
                    .temperature(0.7)
                    .build();

            StringBuilder reply = new StringBuilder();
            for (CompletionChoice choice : openAiService.createCompletion(request).getChoices()) {
                reply.append(choice.getText());
            }

            return ResponseEntity.ok(Map.of("reply", reply.toString().trim()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("reply", "Error: " + e.getMessage()));
        }
    }
}
