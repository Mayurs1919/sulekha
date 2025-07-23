package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {
            "http://localhost:5173",
            "http://192.168.1.63:5173",   
            "http://192.168.1.63:8080", 
            "http://192.168.1.69",  
            "https://aa68580acfc2.ngrok-free.app",
            "https://sulekha-aii.netlify.app"
        },
        allowCredentials = "true")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {


    @Autowired
    private DashboardService dashboardService;

    /* ------------------------------------------------------------------
     * 1.  ROOT  (/dashboard)  → same payload as /dashboard/summary
     * ------------------------------------------------------------------ */
    @GetMapping({ "", "/" })
    public ResponseEntity<?> dashboardRoot(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    /* ------------------------------------------------------------------
     * 2.  ORIGINAL SUMMARY  (/dashboard/summary)
     * ------------------------------------------------------------------ */
    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    /* ------------------------------------------------------------------
     * 3.  OPTIONAL ANALYTICS  (/dashboard/analytics)
     * ------------------------------------------------------------------ */
    @GetMapping("/analytics")
    public ResponseEntity<?> getAnalytics(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(dashboardService.getAnalytics());
    }
}
