package com.FirstProject.TinyURL.Controller;

import com.FirstProject.TinyURL.dto.admin.AdminDashboardResponse;
import com.FirstProject.TinyURL.dto.admin.AdminTopUserResponse;
import com.FirstProject.TinyURL.dto.admin.AdminUrlResponse;
import com.FirstProject.TinyURL.dto.admin.AdminUserResponse;
import com.FirstProject.TinyURL.exception.UrlNotFoundException;
import com.FirstProject.TinyURL.exception.UserNotFoundException;
import com.FirstProject.TinyURL.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse>  getDashboardStats() {
        return ResponseEntity.ok(adminService.getAdminDashboard());
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }


    @GetMapping("/urls")
    public ResponseEntity<List<AdminUrlResponse>> getAllUrls() {
        return ResponseEntity.ok(adminService.getAllUrls());
    }

    @GetMapping("/top-urls")
    public ResponseEntity<List<AdminUrlResponse>> getTopUrls() {
        return ResponseEntity.ok(adminService.getTopUrls());
    }

    @GetMapping("/top-users")
    public ResponseEntity<List<AdminTopUserResponse>> getTopUsers() {
        return ResponseEntity.ok(adminService.getTopUsers());
    }

    @PatchMapping("/users/{id}/promote")
    public ResponseEntity<String> promoteUser(@PathVariable Long id) {
        adminService.promoteUser(id);

        return ResponseEntity.ok("User promoted successfully");
    }

    @PatchMapping("/users/{id}/demote")
    public ResponseEntity<String> demoteUser(@PathVariable Long id) {
        adminService.demoteUser(id);

        return ResponseEntity.ok("User demoted successfully");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @DeleteMapping("/urls/{id}")
    public ResponseEntity<String> deleteUrl(@PathVariable Long id) throws UrlNotFoundException {
        adminService.deleteUrl(id);
        return ResponseEntity.ok("Url deleted successfully");
    }

}
