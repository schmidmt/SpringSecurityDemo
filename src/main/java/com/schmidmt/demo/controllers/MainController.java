package com.schmidmt.demo.controllers;

import com.schmidmt.demo.models.MyUserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String mainPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MyUserPrincipal) {
            username = ((MyUserPrincipal)principal).getUsername();
        } else {
            username = principal.toString();
        }

        model.addAttribute("username", username);
        return "main";
    }
}
