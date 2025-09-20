package com.automotive.email.service;

import com.automotive.email.entity.ServiceBookingRequestDTO;
import com.automotive.email.entity.SignupEmailRequestDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${login.url}")
    private String loginUrl;

    public EmailServiceImpl (JavaMailSender mailSender,TemplateEngine templateEngine)
    {
        this.mailSender=mailSender;
        this.templateEngine=templateEngine;
    }


    @Override
    public void sendSignUpEmail(SignupEmailRequestDTO request) {
        try
        {
            String to = request.getTo().trim();
            String from = fromAddress.trim();
            String loginUrlToSend=loginUrl.trim();
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Welcome to PitStopPro — your signup is complete");

            // Prepare Thymeleaf context
            Context ctx = new Context();
            ctx.setVariable("name", request.getName());
            ctx.setVariable("role", request.getRole());
            ctx.setVariable("loginUrl", loginUrlToSend);
            ctx.setVariable("supportEmail", "pitstopprowssoa@gmail.com");

            String htmlContent = templateEngine.process("signup-email", ctx); // template name without .html
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


	@Override
	public void sendServiceBookingEmail(@Valid ServiceBookingRequestDTO serviceBookingDTO) {
		try
		{
			String to=serviceBookingDTO.getTo().trim();
			String from=fromAddress.trim();
			String loginUrlToSend=loginUrl.trim();
			MimeMessage message=mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Service Booking is confirmed: "+serviceBookingDTO.getServiceOrderId());
            
            Context ctx = new Context();
            ctx.setVariable("to", serviceBookingDTO.getUserName());
            ctx.setVariable("serviceOrderId", serviceBookingDTO.getServiceOrderId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
            String formattedDate = serviceBookingDTO.getCreatedAt().format(formatter);
            ctx.setVariable("createdAt", formattedDate);
            ctx.setVariable("vehicleVin", serviceBookingDTO.getVehicleVin());
            ctx.setVariable("make",serviceBookingDTO.getMake());
            ctx.setVariable("model", serviceBookingDTO.getModel());
            ctx.setVariable("from", from);
            ctx.setVariable("loginUrl", loginUrlToSend);
            ctx.setVariable("supportEmail", "pitstopprowssoa@gmail.com");
            String htmlContent = templateEngine.process("service-booking-confirmation", ctx); // template name without .html
            helper.setText(htmlContent, true);

            mailSender.send(message);
		}
		catch(MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}
}
