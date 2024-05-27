package com.smart.Smartcontactmanager.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    public Boolean sendEmail(String to, String subject, String message) throws MessagingException {
        boolean f=false;
        String from="a@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("anjan.aghera@drcsystems.com", "iije eeay izuh ljlg");
            }
        });
        MimeMessage m = new MimeMessage(session);
        m.setFrom(from);
        m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        m.setSubject(subject);
        m.setText(message);
        Transport.send(m);
        f=true;
        return f;
    }

}
