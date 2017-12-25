package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping(value={"/page/{name}"})
    String pageMapping(Model model, @PathVariable("name") String name) {
        List<Signup> all = signupRepository.findAll();
        boolean isSet = false;
        for (Signup signup : all) {
            if (signup.getName().equals(name)) {
                model.addAttribute("name", signup.getName());
                model.addAttribute("address", signup.getAddress());
                isSet = true;
            }
        }
        if (!isSet) {
            model.addAttribute("name", "Not found");
            model.addAttribute("address", "Not found");
        }
        return "page";
    }

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Model model, @RequestParam String name, @RequestParam String address) {
        signupRepository.save(new Signup(name, address));
        model.addAttribute("url", "/page/" + name);
        return "done";
    }
}
