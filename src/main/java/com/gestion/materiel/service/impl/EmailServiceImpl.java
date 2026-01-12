package com.gestion.materiel.service.impl;

import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.BesoinExprime;
import com.gestion.materiel.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.from:no-reply@gestion-materiel.com}")
    private String fromEmail;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
    
    @Override
    public void sendValidationEmail(Agent superior, BesoinExprime besoin) {
        if (superior == null || superior.getEmail() == null || superior.getEmail().trim().isEmpty()) {
            logger.warn("Impossible d'envoyer l'email de validation : supérieur hiérarchique sans email (ID: {})", 
                    superior != null ? superior.getId() : "null");
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(superior.getEmail());
            helper.setSubject("Nouveau besoin exprimé à valider - " + besoin.getTypeMateriel().getNom());
            
            String htmlContent = buildValidationEmailContent(superior, besoin);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            logger.info("Email de validation envoyé avec succès à {} ({})", 
                    superior.getEmail(), superior.getNom() + " " + superior.getPrenom());
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de validation à {} : {}", 
                    superior.getEmail(), e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'envoi de l'email de validation : {}", 
                    e.getMessage(), e);
        }
    }
    
    @Override
    public void sendConfirmationEmail(Agent creator, BesoinExprime besoin) {
        if (creator == null || creator.getEmail() == null || creator.getEmail().trim().isEmpty()) {
            logger.warn("Impossible d'envoyer l'email de confirmation : agent créateur sans email (ID: {})", 
                    creator != null ? creator.getId() : "null");
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(creator.getEmail());
            helper.setSubject("Confirmation de création de besoin exprimé");
            
            String htmlContent = buildConfirmationEmailContent(creator, besoin);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            logger.info("Email de confirmation envoyé avec succès à {} ({})", 
                    creator.getEmail(), creator.getNom() + " " + creator.getPrenom());
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de confirmation à {} : {}", 
                    creator.getEmail(), e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'envoi de l'email de confirmation : {}", 
                    e.getMessage(), e);
        }
    }
    
    private String buildValidationEmailContent(Agent superior, BesoinExprime besoin) {
        Agent creator = besoin.getAgent();
        String creatorName = creator.getNom() + " " + creator.getPrenom();
        String superiorName = superior.getNom() + " " + superior.getPrenom();
        String typeMateriel = besoin.getTypeMateriel().getNom();
        String dateBesoin = besoin.getDateBesoin().format(DATE_FORMATTER);
        String dateCreation = besoin.getDateCreation().format(DATETIME_FORMATTER);
        String motif = besoin.getMotif();
        String observation = besoin.getObservation() != null ? besoin.getObservation() : "Aucune";
        
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #1976d2; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }" +
                ".info-box { background-color: white; padding: 15px; margin: 10px 0; border-left: 4px solid #1976d2; }" +
                ".label { font-weight: bold; color: #555; }" +
                ".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>Nouveau besoin exprimé à valider</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Bonjour " + superiorName + ",</p>" +
                "<p>Un nouveau besoin exprimé nécessite votre validation.</p>" +
                "<div class='info-box'>" +
                "<p><span class='label'>Agent demandeur :</span> " + creatorName + "</p>" +
                "<p><span class='label'>Type de matériel :</span> " + typeMateriel + "</p>" +
                "<p><span class='label'>Date du besoin :</span> " + dateBesoin + "</p>" +
                "<p><span class='label'>Date de création :</span> " + dateCreation + "</p>" +
                "<p><span class='label'>Motif :</span> " + motif + "</p>" +
                "<p><span class='label'>Observation :</span> " + observation + "</p>" +
                "</div>" +
                "<p>Veuillez vous connecter à l'application pour valider ce besoin.</p>" +
                "<p>Cordialement,<br>Système de Gestion de Matériel</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Cet email a été envoyé automatiquement. Merci de ne pas y répondre.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    
    private String buildConfirmationEmailContent(Agent creator, BesoinExprime besoin) {
        String creatorName = creator.getNom() + " " + creator.getPrenom();
        String typeMateriel = besoin.getTypeMateriel().getNom();
        String dateBesoin = besoin.getDateBesoin().format(DATE_FORMATTER);
        String dateCreation = besoin.getDateCreation().format(DATETIME_FORMATTER);
        String motif = besoin.getMotif();
        String observation = besoin.getObservation() != null ? besoin.getObservation() : "Aucune";
        
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #4caf50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }" +
                ".info-box { background-color: white; padding: 15px; margin: 10px 0; border-left: 4px solid #4caf50; }" +
                ".label { font-weight: bold; color: #555; }" +
                ".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>Confirmation de création de besoin exprimé</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Bonjour " + creatorName + ",</p>" +
                "<p>Votre besoin exprimé a été créé avec succès et a été transmis à votre supérieur hiérarchique pour validation.</p>" +
                "<div class='info-box'>" +
                "<p><span class='label'>Type de matériel :</span> " + typeMateriel + "</p>" +
                "<p><span class='label'>Date du besoin :</span> " + dateBesoin + "</p>" +
                "<p><span class='label'>Date de création :</span> " + dateCreation + "</p>" +
                "<p><span class='label'>Motif :</span> " + motif + "</p>" +
                "<p><span class='label'>Observation :</span> " + observation + "</p>" +
                "<p><span class='label'>Statut :</span> CRÉÉ</p>" +
                "</div>" +
                "<p><strong>Prochaines étapes :</strong></p>" +
                "<ul>" +
                "<li>Votre supérieur hiérarchique recevra une notification pour valider votre demande</li>" +
                "<li>Vous serez informé de l'avancement de votre demande</li>" +
                "<li>Vous pouvez suivre le statut de votre demande dans l'application</li>" +
                "</ul>" +
                "<p>Cordialement,<br>Système de Gestion de Matériel</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Cet email a été envoyé automatiquement. Merci de ne pas y répondre.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
