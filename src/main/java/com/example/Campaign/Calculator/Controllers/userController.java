package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.User;
import com.example.Campaign.Calculator.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;


@Controller
@SessionAttributes("loggedInUser")
public class userController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("title", "login");
        return "login"; // Display login page
    }

    @PostMapping("/login")
    public String login(@RequestParam String login,
                        @RequestParam String password,
                        Model model){

        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            return "/login";
        }

        User logged_user = userRepository.findByLoginAndPassword(login, password);

        if(logged_user != null){
            model.addAttribute("loggedInUser", logged_user);
            return "redirect:/mainPage"; // Redirect to main page after successful login
        } else {
            // add error atributte to the model
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("title", "register");
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String nickname,
                           @RequestParam String login,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        // if any field is null or empty, return to the registration page
        if (nickname == null || nickname.isEmpty()
                || password == null || password.isEmpty()
                || login == null || login.isEmpty()
                || email == null || email.isEmpty()) {
            return "redirect:/register";
        }

        // Check if user with such username already exists
        User existingUser = userRepository.findByLogin(login);

        if (existingUser != null) {
            // add error atributte to the model
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        // Create a new user object
        User user = new User(nickname, password, login, email);
        userRepository.save(user);
        return "redirect:/login"; // Redirect to login page after registration
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.removeAttribute("loggedInUser");
            session.invalidate();
        }
        // Set loggedInUser attribute to null in the model
        model.addAttribute("loggedInUser", null);
        return "redirect:/"; // Redirect to the login page after logging out
    }

    @GetMapping("/users")
    public String users(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login page if user not authenticated
        }

        // Get the logged-in user's information
        model.addAttribute("user", loggedInUser);

        return "users"; // Display user info page
    }
}
