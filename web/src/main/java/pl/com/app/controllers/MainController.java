package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.com.app.dto.VisitCriteriaHomePageDTO;
import pl.com.app.service.AdviceService;
import pl.com.app.service.OpinionService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class MainController {
    private final OpinionService opinionService;
    private final AdviceService adviceService;

    @GetMapping({"/", "", "index"})
    public String welcome(Model model) {
        model.addAttribute("visitCriteriaHomePage", new VisitCriteriaHomePageDTO());
        model.addAttribute("opinions", opinionService.getNewestOpinions());
        model.addAttribute("advices", adviceService.getNewestAdvicesWithAnswer());
        model.addAttribute("errors", new HashMap<>());

        return "index";
    }

    @PostMapping({"/", ""})
    public String visitHomePageCriteria(@Valid @ModelAttribute VisitCriteriaHomePageDTO visitCriteriaHomePageDTO,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("visitCriteriaHomePage", visitCriteriaHomePageDTO);
            model.addAttribute("opinions", opinionService.getNewestOpinions());
            model.addAttribute("advices", adviceService.getNewestAdvicesWithAnswer());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "index";
        }
        redirectAttributes.addFlashAttribute("visitCriteriaHomePageDTO", visitCriteriaHomePageDTO);
        return "redirect:/add-visit/home-page/doctors-list";
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/notFound")
    public String notFound() {
        return "error-page-404";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "security/accessDenied";
    }

}
