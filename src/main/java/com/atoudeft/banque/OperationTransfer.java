package com.atoudeft.banque;

import java.io.Serializable;

public class OperationTransfer extends Operation implements Serializable {
    /**
     * Constructeur d'une opération de transfert
     * @param montant le montant de l'opération
     */
    private String numero;
    public OperationTransfer (double montant, String numero){
        super(TypeOperation.TRANSFER, System.currentTimeMillis(), montant);
        this.numero = numero;
    }
    /**
     * Retourne une chaine décrivant l'opération de transfert
     * @return l'opération de transfert
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
        return phrase.toString();
    }
}