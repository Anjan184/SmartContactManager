package com.smart.Smartcontactmanager.Controller;

import com.smart.Smartcontactmanager.dao.UserRepository;
import com.smart.Smartcontactmanager.entities.User;
import com.smart.Smartcontactmanager.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
public class ForgotController {

    Random random=new Random(1000);

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/forgot")
     public String openEmailForm(){
        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email, HttpSession session) throws MessagingException {
       int otp= random.nextInt(99999);
       String subject="OTP from SCM";
       String message="OTP is "+otp;
       String to=email;
        boolean flag=emailService.sendEmail(to,subject,message);
        if(flag){
            session.setAttribute("myotp",otp);
            session.setAttribute("email",email);
            return "verify";
        }
        else {
            session.setAttribute("message","Recheck your email id!!");
            return "forgot_email_form";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam("otp") int otp,HttpSession session){
        int myOtp=(int)session.getAttribute("myotp");
        String email=(String) session.getAttribute("email");
        if(myOtp==otp){
            User user = userRepository.getUserByUserName(email);
            if(user==null){
                session.setAttribute("message","User not exist with this email");
                return "forgot_email_form";
            }
            else {
                return "password_change_form";
            }

        }
        else{
            session.setAttribute("message","Wrong OTP Please try again");
        return "verify";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session){
        String email=(String) session.getAttribute("email");
        User user=userRepository.getUserByUserName(email);
        user.setPassword(bCryptPasswordEncoder.encode(newpassword));
        userRepository.save(user);
        return "redirect:/signin?change=Password Changed Successfully..";
    }

}
