package com.schmidmt.demo.controllers;

import com.schmidmt.demo.models.MyGrantAuthority;
import com.schmidmt.demo.models.User;
import com.schmidmt.demo.models.dao.MyGrantAuthorityRepository;
import com.schmidmt.demo.models.dao.UserRepository;
import com.schmidmt.demo.models.forms.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyGrantAuthorityRepository grantRepo;

    @GetMapping("/login")
    public String getLoginPage() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        model.addAttribute(new SignupForm());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String processSignup(
            HttpServletRequest request,
            @ModelAttribute @Valid SignupForm form,
            Errors errors,
            Model model
    ) {

        if (errors.hasErrors()) {
            model.addAttribute("signupForm", form);
            return "user/signup";
        }

        if (!form.passwordsMatch()) {
            model.addAttribute("signupForm", form);
            model.addAttribute("error", "Passwords do not match");
            return "user/signup";
        }

        if (userRepository.findByUsername(form.getUsername()) != null) {
            model.addAttribute("signupForm", form);
            model.addAttribute("error", "User already exists");
            return "user/signup";
        }


        // Create user
        Set<MyGrantAuthority> grants = new HashSet<>();
        grants.add(grantRepo.getByRole("USER"));
        User user = new User(form.getUsername(), grants, form.getPassword());
        userRepository.save(user);
        user = userRepository.findByUsername(user.getUsername());

        // Add user to session
        try {
            request.login(form.getUsername(), form.getPassword());
        } catch (ServletException e) {
            System.out.println("Failed to add user to session: " + e.toString());
        }
        return "redirect:/";
    }
}
