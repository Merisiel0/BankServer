package com.atoudeft.banque;

import java.io.Serializable;

public class OperationFacture extends Operation implements Serializable {
    /**
     * Constructeur d'une opération de paiement de facture
     * @param montant le montant de l'opération
     */
    private String numero, description;
    public OperationFacture (double montant, String numero, String description){
        super(TypeOperation.FACTURE, System.currentTimeMillis(), montant);
        this.numero = numero;
        this.description=description;
    }
    /**
     * Retourne une chaine décrivant l'opération de paiement de facture
     * @return l'opération de paiement de facture
     */
    public String toString(){
        StringBuilder phrase = new StringBuilder();
        phrase.append(this.getDate());
        phrase.append(" \t");
        phrase.append(this.getOperation());
        phrase.append(" \t");
        phrase.append(this.getMontant());
        phrase.append(" \t");
        phrase.append(numero);
        phrase.append(" \t");
        phrase.append(description);
        return phrase.toString();
    }
}