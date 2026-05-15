package com.darkross.wssecuritycore.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String username, String rawPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("andresarcangel00@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Bienvenido al Sistema de Seguridad - Credenciales de Acceso");

            String htmlBody = buildWelcomeEmailHtml(username, rawPassword);
            helper.setText(htmlBody, true); // true indica que es HTML

            mailSender.send(message);
            log.info("Correo de bienvenida enviado exitosamente a: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Error al enviar correo de bienvenida a {}: {}", toEmail, e.getMessage());
        }
    }

    private String buildWelcomeEmailHtml(String username, String rawPassword) {
        return """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Bienvenido al Sistema de Seguridad</title>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>🚀 Bienvenido al Sistema de Seguridad</h1>
                </div>

                <div class="content">
                    <div class="welcome-message">
                        ¡Tu cuenta ha sido creada exitosamente!
                    </div>

                    <p>Te damos la bienvenida a nuestro sistema de seguridad. 
                    A continuación encontrarás tus credenciales de acceso:</p>

                    <div class="credentials-box">
                        <div class="credential-item">
                            <span class="credential-label">👤 Usuario:</span>
                            <span class="credential-value">"""
                + username +
                """
                                    </span>
                                </div>
        
                                <div class="credential-item">
                                    <span class="credential-label">🔐 Contraseña:</span>
                                    <span class="credential-value">"""
                + rawPassword +
                """
                                    </span>
                                </div>
                            </div>
        
                        </div>
                    </div>
                </body>
                </html>
                """;
    }
}
