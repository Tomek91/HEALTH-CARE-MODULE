package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.com.app.service.DataInitializerService;


@Controller
@RequestMapping("/setup")
@RequiredArgsConstructor
public class SetupController {
    private final DataInitializerService dataInitializerService;

    @GetMapping("/default")
    public String manage(Model model) {
        return "setup/default";
    }

    @GetMapping("/default-data")
    public String getDefaultData() {
        dataInitializerService.initData();
        return "redirect:/";
    }
}
