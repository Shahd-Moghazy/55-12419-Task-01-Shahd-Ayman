package controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Value("${USER_NAME}")
    private String userName;

    @Value("${ID}")
    private String id;

    @GetMapping("/welcome")
    public String welcome() {
        return "Hello " + userName + " " + id + ", from Notes API";
    }
}