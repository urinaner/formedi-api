package com.Nteam.backend.formediapi.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin")
    public String adminP() {

        return "admin Controller";
    }
}
