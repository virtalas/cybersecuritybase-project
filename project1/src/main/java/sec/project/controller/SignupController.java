package sec.project.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    private final String ADMIN_USERNAME = "default"; // TODO: Change from default
    private final String ADMIN_PASSWORD = "default"; // TODO: Change from default
    
    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private HttpSession session;

    @RequestMapping("*")
    public String defaultMapping(Model model) {
        signupRepository.save(new Signup("Matti", "Kurvasenkuja 3", 736481436784L));
        model.addAttribute("list", signupRepository.findAll());
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) {
        model.addAttribute("list", signupRepository.findAll());
        model.addAttribute("userId", session.getAttribute("userId"));
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, Model model) {
        final String sql = "INSERT INTO Signup (name, address) VALUES ('" + name + "', '" + address + "');";
        jdbcTemplate.execute(sql);
        
        Long userId = signupRepository.findAll().get(signupRepository.findAll().size() - 1).getId();

        model.addAttribute("newUserId", userId);
        session.setAttribute("userId", userId);
        return "done";
    }

    @RequestMapping(value = "/edit/{userId}", method = RequestMethod.GET)
    public String editForm(@PathVariable("userId") long id, Model model) {
        model.addAttribute("user", signupRepository.findOne(id));
        return "edit";
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@RequestParam String name, @RequestParam String address, @RequestParam Long userId, Model model) {
        Signup s = signupRepository.findOne(userId);
        s.setName(name);
        s.setAddress(address);
        signupRepository.save(s);
        return "redirect:/form";
    }
    
    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public String adminLogin(@RequestParam String username, @RequestParam String password, Model model) {
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return "redirect:/admin/settings";
        }
        return "redirect:/form";
    }
    
    @RequestMapping(value = "/admin/settings", method = RequestMethod.GET)
    public String adminSettings(Model model) {
        return "settings";
    }
    
    @RequestMapping(value = "/admin/clear", method = RequestMethod.POST)
    public String adminLogin(Model model) {
        signupRepository.deleteAll();
        return "redirect:/form";
    }
}
