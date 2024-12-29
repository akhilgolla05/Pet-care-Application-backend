package com.learnboot.universalpetcare.event.listner;

import com.learnboot.universalpetcare.email.EmailService;
import com.learnboot.universalpetcare.event.AppointmentApprovedEvent;
import com.learnboot.universalpetcare.event.AppointmentBookedEvent;
import com.learnboot.universalpetcare.event.AppointmentDeclinedEvent;
import com.learnboot.universalpetcare.event.RegistrationCompleteEvent;
import com.learnboot.universalpetcare.models.Appointment;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.services.token.IVerificationTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationEventListner implements ApplicationListener<ApplicationEvent> {

    private final EmailService emailService;
    private final IVerificationTokenService verificationTokenService;

    @Value("${front-end.base.url}")
    private String frondEndBaseUrl;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Object source = event.getSource();

        switch (source.getClass().getSimpleName()) {

            case "RegistrationCompleteEvent":
                if(source instanceof RegistrationCompleteEvent) {
                    RegistrationCompleteEvent registrationCompleteEvent = (RegistrationCompleteEvent) event;
                    handleSendRegistrationVerificationEmail(registrationCompleteEvent);
                }
                break;

            case "AppointmentBookedEvent":
                if(source instanceof Appointment) {
                    AppointmentBookedEvent appointmentBookedEvent = (AppointmentBookedEvent) event;
                    try {
                        handleAppointmentBookedConfirmationEmail(appointmentBookedEvent);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "AppointmentApprovedEvent":
                if(source instanceof Appointment) {
                    AppointmentApprovedEvent appointmentApprovedEvent = (AppointmentApprovedEvent) event;
                    try {
                        handleAppointmentApprovedNotification(appointmentApprovedEvent);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "AppointmentDeclinedEvent":
                if(source instanceof Appointment) {
                    AppointmentDeclinedEvent appointmentDeclinedEvent = (AppointmentDeclinedEvent) event;
                    try {
                        handleAppointmentDeclinedNotification(appointmentDeclinedEvent);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            default:
                break;
        }


    }

    //registration email
    private void handleSendRegistrationVerificationEmail(RegistrationCompleteEvent event){
        User user = event.getUser();
        //generate a token for the User
        String vToken = UUID.randomUUID().toString();
        //save the token for the user
        verificationTokenService.saveVerificationTokenForUser(vToken, user);
        //build the verification URL
        String verificationUrl = frondEndBaseUrl + "/email-verification?token="+vToken;
        try{
            sendRegistrationVerificationEmail(user, verificationUrl);
        }catch (MessagingException | UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }

    private void sendRegistrationVerificationEmail(User user, String verificationUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Registration verification email";
        String senderName = "Universal Pet Care";
        String mailContent = "<p>Hi, "+ user.getFirstName()+", </p>"+
                "<p>Thank you for your registration</p>"+
                "<p>Please, follow the link to complete your verification code.</p>"+
                "<a href=\"" + verificationUrl + "\">verify Your Email</a>"+
                "<p>Thank you <br> Universal Pet care Team</p>";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }

    //appointment confirmation email
    private void handleAppointmentBookedConfirmationEmail(AppointmentBookedEvent event) throws MessagingException, UnsupportedEncodingException {

        Appointment appointment = event.getAppointment();
        User veterinary = appointment.getVeterinarian();
        sendAppointmentBookedNotification(veterinary, frondEndBaseUrl);
    }

    private void sendAppointmentBookedNotification(User veterinary, String frondEndBaseUrl) throws MessagingException, UnsupportedEncodingException {

        String subject = "Appointment booked notification";
        String senderName = "Universal Pet Care";
        String mailContent = "<p>Hi, "+ veterinary.getFirstName()+", </p>"+
                "<p>You have a new Appointment Scheduled</p>"+
                "<a href=\"" + frondEndBaseUrl + "\">Please check the portal to view the information</a>"+
                "<p>Thank you <br> Universal Pet care Team</p>";
        emailService.sendEmail(veterinary.getEmail(),subject,senderName,mailContent);
    }

    //appointment Approved email
    public void handleAppointmentApprovedNotification(AppointmentApprovedEvent event) throws MessagingException, UnsupportedEncodingException {

        Appointment appointment = event.getAppointment();
        User patient = appointment.getPatient();
        sendAppointmentApprovedNotification(patient, frondEndBaseUrl);
    }

    private void sendAppointmentApprovedNotification(User patient, String frondEndBaseUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Appointment approved notification";
        String senderName = "Universal Pet Care";
        String mailContent = "<p>Hi, "+ patient.getFirstName()+", </p>"+
                "<p>Your Appointment has been Approved</p>"+
                "<a href=\"" + frondEndBaseUrl + "\">Please check the portal to view the information</a>"+
                "<p>Thank you <br> Universal Pet care Team</p>";
        emailService.sendEmail(patient.getEmail(),subject,senderName,mailContent);

    }

    //appointment declined

    public void handleAppointmentDeclinedNotification(AppointmentDeclinedEvent event) throws MessagingException, UnsupportedEncodingException {

        Appointment appointment = event.getAppointment();
        User patient = appointment.getPatient();
        sendAppointmentDeclinedNotification(patient, frondEndBaseUrl);
    }

    private void sendAppointmentDeclinedNotification(User patient, String frondEndBaseUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Appointment Declined notification";
        String senderName = "Universal Pet Care";
        String mailContent = "<p>Hi, "+ patient.getFirstName()+", </p>"+
                "<p>We are sorry, your Appointment has been Declined</p>"+
                "<a href=\"" + frondEndBaseUrl + "\">Please check the portal to view the information</a>"+
                "<p>Thank you <br> Universal Pet care Team</p>";
        emailService.sendEmail(patient.getEmail(),subject,senderName,mailContent);

    }
}
