package com.atoudeft.banque;

import java.io.Serializable;

public class OperationDepot extends Operation implements Serializable {
    /**
     * Constructeur d'une opération de dépot
     * @param montant le montant de l'opération
     */
    public OperationDepot (double montant){
        super(TypeOperation.DEPOT, System.currentTimeMillis(), montant);
    }
    /**
     * Retourne une chaine décrivant l'opération de dépot
     * @return l'opération de dépot
     */
    public String toString(){
        StringBuilder phrase = new StringBuilder();
        phrase.append(this.getDate());
        phrase.append(" \t");
        phrase.append(this.getOperation());
        phrase.append(" \t\t");
        phrase.append(this.getMontant());
        return phrase.toString();
    }
}
