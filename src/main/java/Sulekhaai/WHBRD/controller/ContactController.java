package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitContactForm(@RequestBody ContactFormRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate required fields
            if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Full name is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getSubject() == null || request.getSubject().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Subject is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Message is required");
                return ResponseEntity.badRequest().body(response);
            }

            // Create email content
            String subject = "New Contact Form Submission: " + request.getSubject();
            String emailContent = buildEmailContent(request);
            
            // Send email to admin (you can configure the admin email)
            String adminEmail = "admin@yourdomain.com"; // Replace with your admin email
            try {
                emailService.sendEmail(adminEmail, subject, emailContent);
            } catch (Exception e) {
                System.err.println("Failed to send admin email: " + e.getMessage());
                // Continue with user email even if admin email fails
            }
            
            // Send confirmation email to user
            String userSubject = "Thank you for contacting us";
            String userEmailContent = buildUserConfirmationEmail(request);
            try {
                emailService.sendEmail(request.getEmail(), userSubject, userEmailContent);
            } catch (Exception e) {
                System.err.println("Failed to send user confirmation email: " + e.getMessage());
                // Don't fail the entire request if user email fails
            }
            
            response.put("success", true);
            response.put("message", "Contact form submitted successfully. We'll get back to you soon!");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to submit contact form. Please try again later.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    private String buildEmailContent(ContactFormRequest request) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h2>New Contact Form Submission</h2>");
        content.append("<p><strong>Full Name:</strong> ").append(request.getFullName()).append("</p>");
        content.append("<p><strong>Email:</strong> ").append(request.getEmail()).append("</p>");
        if (request.getCompany() != null && !request.getCompany().trim().isEmpty()) {
            content.append("<p><strong>Company:</strong> ").append(request.getCompany()).append("</p>");
        }
        content.append("<p><strong>Subject:</strong> ").append(request.getSubject()).append("</p>");
        content.append("<p><strong>Message:</strong></p>");
        content.append("<p>").append(request.getMessage().replace("\n", "<br>")).append("</p>");
        content.append("<hr>");
        content.append("<p><small>This message was sent from your website contact form.</small></p>");
        content.append("</body></html>");
        
        return content.toString();
    }
    
    private String buildUserConfirmationEmail(ContactFormRequest request) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h2>Thank you for contacting us!</h2>");
        content.append("<p>Dear ").append(request.getFullName()).append(",</p>");
        content.append("<p>We have received your message and will get back to you within 24 hours.</p>");
        content.append("<p><strong>Your message details:</strong></p>");
        content.append("<p><strong>Subject:</strong> ").append(request.getSubject()).append("</p>");
        content.append("<p><strong>Message:</strong></p>");
        content.append("<p>").append(request.getMessage().replace("\n", "<br>")).append("</p>");
        content.append("<hr>");
        content.append("<p>Best regards,<br>Your Dashboard Team</p>");
        content.append("</body></html>");
        
        return content.toString();
    }
    
    // Inner class for request body
    public static class ContactFormRequest {
        private String fullName;
        private String email;
        private String company;
        private String subject;
        private String message;
        
        // Getters and Setters
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
} 