package com.alura.platform.business.email;

public class EmailSender {

    public static void send(String recipientEmail, String subject, String body) {
        System.out.println(
                "Simulating sending email to [%s]:\n".formatted(recipientEmail)
        );
        System.out.println("""
            Subject: %s
            Body: %s
            """.formatted(subject, body));
    }
}
