package com.smart.Smartcontactmanager.Controller;

import com.smart.Smartcontactmanager.dao.UserRepository;
import com.smart.Smartcontactmanager.entities.Contact;
import com.smart.Smartcontactmanager.entities.User;
import com.smart.Smartcontactmanager.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @RequestMapping("/")
    public String home(Model model){
     model.addAttribute("title","Home-Smart Contact Manager");
    return "home";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About-Smart Contact Manager");
        return "about";
    }


    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title","Register-Smart Contact Manager");
        model.addAttribute("user",new User());
        return "signup";
    }

    @RequestMapping(value="/do_register",method= RequestMethod.POST)
    public String register(@ModelAttribute("user") User user, BindingResult result1, @RequestParam(value="agreement",
            defaultValue = "false") boolean agreement, Model model, HttpSession session){
       try{
           if(!agreement){
               throw new Exception("You have not accept agreement");
           }
           if(result1.hasErrors()){
               model.addAttribute("user",user);
               return "signup";
           }
           user.setRole("ADMIN");
           user.setEnabled(true);
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           User result=userRepository.save(user);

           model.addAttribute("user",new User());
           session.setAttribute("message",new Message("Successfully Registered!","alert-success"));
            return "signup";
       }
       catch(Exception e){
        model.addAttribute("user",user);
        session.setAttribute("message",new Message(e.getMessage(),"alert-danger"));
           return "signup";
       }
    }

    @GetMapping("/signin")
    public String customLogin(Model model){
        model.addAttribute("title","Login-Smart Contact Manager");
        return "login";
    }

}
