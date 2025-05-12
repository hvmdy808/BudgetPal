package org.example;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;

public class Connector {

        private static final String SENDER_EMAIL = "hamdymody901@gmail.com";
        private static final String SENDER_PASSWORD = "rfgc yyal qwet qakq"; // Use App Password if 2FA is on

        public static boolean sendVerificationEmail(String code, User user) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(SENDER_EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject("Email Verification Code");
                message.setText("Your verification code is: " + code + "\n\nEnter this code to verify your email address.");

                Transport.send(message);
                return true;

            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
        }

        public static String generateVerificationCode() {
            Random random = new Random();
            int code = 100000 + random.nextInt(900000); // 6-digit code
            return String.valueOf(code);
        }

     public static boolean sendResetCode(User user, String code) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("Reset Your Password");
            message.setText("Your password reset code is: " + code + "\n\nUse this code to reset your password.");

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

