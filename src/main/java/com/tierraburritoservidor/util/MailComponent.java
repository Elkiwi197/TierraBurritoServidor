package com.tierraburritoservidor.util;

import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.config.ConfigurationProperties;
import com.tierraburritoservidor.errors.exceptions.CorreoException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MailComponent {

    private final ConfigurationProperties configurationProperties;
    @Value(value = "${app.subject}")
    private String asunto;
    @Value(value = "${spring.mail.username}")
    private String remitente;

    private final JavaMailSender javaMailSender;

    public MailComponent(JavaMailSender javaMailSender, ConfigurationProperties configurationProperties) {
        this.javaMailSender = javaMailSender;
        this.configurationProperties = configurationProperties;
    }

    public void mandarCorreoActivacion(String destinatario, int idUsuario, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(remitente);
        message.setTo(destinatario);
        message.setSubject(asunto);
        String ip = configurationProperties.getIp();
        message.setText("Haga clic sobre \"http://" + ip + ":8080/signup/activar/" + idUsuario + "?codigo=" + codigo + "\" para activar su cuenta");
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error(e.getMessage());
            throw new CorreoException();
        }

    }
}
