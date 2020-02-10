package com.minhduc.tuto.springboot.bootstrapping.email;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * to send eamil you have to allow "less secure app" and turn off two step
 * verification
 * 
 * @author UE1PHOT
 *
 */
@RestController
@RequestMapping("/rest/email")
public class EmailController {

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendEmail() throws AddressException, MessagingException, IOException {
	sendmail();
	return "Email sent successfully";
    }

    private void sendmail() throws AddressException, MessagingException, IOException {
	Properties props = new Properties();
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.port", "587");

	Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	    protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication("wunsch85@gmail.com", "your_password");
	    }
	});
	Message msg = new MimeMessage(session);
	msg.setFrom(new InternetAddress("wunsch85@gmail.com", false));

	msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("wunsch85@gmail.com"));
	msg.setSubject("Email wiht SpringBoot App");
	msg.setContent("Email wiht SpringBoot App", "text/html");
	msg.setSentDate(new Date());

	MimeBodyPart messageBodyPart = new MimeBodyPart();
	messageBodyPart.setContent("Tutorials point email", "text/html");
	Multipart multipart = new MimeMultipart();
	multipart.addBodyPart(messageBodyPart);
	msg.setContent(multipart);
	Transport.send(msg);
    }
}
