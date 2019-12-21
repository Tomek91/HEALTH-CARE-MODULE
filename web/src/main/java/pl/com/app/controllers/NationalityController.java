package pl.com.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NationalityController {
    @GetMapping("/nationality")
    public String welcome() {
        return "index";
    }

}
