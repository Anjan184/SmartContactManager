package com.smart.Smartcontactmanager.Controller;

import com.smart.Smartcontactmanager.dao.ContactRepository;
import com.smart.Smartcontactmanager.dao.UserRepository;
import com.smart.Smartcontactmanager.entities.Contact;
import com.smart.Smartcontactmanager.entities.User;
import lombok.var;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.razorpay.*;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/user")

public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String userName=principal.getName();
        User user=userRepository.getUserByUserName(userName);
        model.addAttribute("user",user);
    }

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("title","User Dashboard");
        String userName=principal.getName();
        User user=userRepository.getUserByUserName(userName);
        model.addAttribute("user",user);
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new Contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String process_contact(@ModelAttribute Contact contact,Principal principal){
        String name=principal.getName();
        User user=userRepository.getUserByUserName(name);
        contact.setUser(user);
        user.getContacts().add(contact);
        userRepository.save(user);
        return "normal/add_contact_form";
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page , Model model, Principal principal){
    String name=principal.getName();
    User user=userRepository.getUserByUserName(name);
    model.addAttribute("title","Show Contact");
    Pageable pageable=PageRequest.of(page,3);
   Page<Contact> contacts= contactRepository.findContactsByUser(user.getId(),pageable);
    model.addAttribute("contacts",contacts);
    model.addAttribute("currentPage",page);
    model.addAttribute("totalPages",contacts.getTotalPages());
   return "normal/show_contacts";
    }

    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cId,Model model,Principal principal){
        Optional<Contact> contactOptional=contactRepository.findById(cId);
        Contact contact=contactRepository.findById(cId).get();
     User user=userRepository.getUserByUserName(principal.getName());
     user.getContacts().remove(contact);
     userRepository.save(user);
    return "redirect:/user/show-contacts/0";
}

    @PostMapping("/update-contact/{cid}")
    public String updateForm(@PathVariable("cid") Integer cid,Model model){
        model.addAttribute("title","Update Contact");
       Contact contact= contactRepository.findById(cid).get();
       model.addAttribute("contact",contact);
        return "normal/update_form";
}

//update contact handler
    @PostMapping("/process-update")
public String updateHandler(@ModelAttribute Contact contact, Model model, HttpSession session,Principal principal){
        try{
            User user=userRepository.getUserByUserName(principal.getName());
            contact.setUser(user);
            contactRepository.save(contact);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/user/show-contacts/0";
}

    @GetMapping("/your-profile")
    public String userProfile(Principal principal,Model model){
        String userName=principal.getName();
        User user=userRepository.getUserByUserName(userName);
        model.addAttribute("user",user);
    return "normal/user_profile";
    }

    @PostMapping("/edit_user")
    public String change_user_details(@RequestParam String name,String email,Principal principal) throws Exception{
        String userName=principal.getName();
        User user=userRepository.getUserByUserName(userName);
            user.setName(name);
            user.setEmail(email);
        userRepository.save(user);
        return "normal/user_profile";
    }

    @GetMapping("/settings")
    public String openSettings(){
        return "normal/settings";
    }

@PostMapping("/change-password")
public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal){
    String name=principal.getName();
    User user=userRepository.getUserByUserName(name);
    if(bCryptPasswordEncoder.matches(oldPassword,user.getPassword())){
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }
        return "normal/user_dashboard";
}

@PostMapping("/create_order")
@ResponseBody
public String createOrder(@RequestBody Map<String,Object> data) throws Exception {
    int amt=Integer.parseInt(data.get("amount").toString());
  RazorpayClient client = new RazorpayClient("rzp_test_oHMMsYxMKfUulO","nCtT6SyNAV6IgjJC9qYYYalF");
    JSONObject ob = new JSONObject();
    ob.put("amount", amt*100);
    ob.put("currency", "INR");
    ob.put("receipt", "txn_123456");
    Order order = client.orders.create(ob);

    return "done";
}

}
