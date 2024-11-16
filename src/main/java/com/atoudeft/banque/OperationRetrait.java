package com.atoudeft.banque;

import java.io.Serializable;

public class OperationRetrait extends Operation implements Serializable {
    /**
     * Constructeur d'une opération de retrait
     * @param montant le montant de l'opération
     */
    public OperationRetrait (double montant){
        super(TypeOperation.DEPOT, System.currentTimeMillis(), montant);
    }

    /**
     * Retourne une chaine décrivant l'opération de retrait
     * @return l'opération de retrait
     */
    public String toString(){
        StringBuilder phrase = new StringBuilder();
        phrase.append(this.getDate());
        phrase.append(" \t");
        phrase.append(this.getOperation());
        phrase.append(" \t");
        phrase.append(this.getMontant());
        return phrase.toString();
    }
}