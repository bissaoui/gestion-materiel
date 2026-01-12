package com.gestion.materiel.service;

import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.BesoinExprime;

public interface EmailService {
    
    /**
     * Envoie un email au supérieur hiérarchique pour demander la validation d'un besoin
     * @param superior Le supérieur hiérarchique qui doit valider
     * @param besoin Le besoin exprimé à valider
     */
    void sendValidationEmail(Agent superior, BesoinExprime besoin);
    
    /**
     * Envoie un email de confirmation à l'agent qui a créé le besoin
     * @param creator L'agent qui a créé le besoin
     * @param besoin Le besoin exprimé créé
     */
    void sendConfirmationEmail(Agent creator, BesoinExprime besoin);
}
