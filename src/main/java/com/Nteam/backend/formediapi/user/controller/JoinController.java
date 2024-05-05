package com.Nteam.backend.formediapi.user.controller;

import com.Nteam.backend.formediapi.user.dto.JoinDTO;
import com.Nteam.backend.formediapi.user.entity.UserEntity;
import com.Nteam.backend.formediapi.user.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class JoinController {
    private JoinService joinService;

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }



    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());
        joinService.joinProcess(joinDTO);

        return "ok";
    }
}
