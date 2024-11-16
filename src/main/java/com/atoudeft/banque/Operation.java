package com.atoudeft.banque;

import java.io.Serializable;
import java.util.Date;


public abstract class Operation implements Serializable {

    TypeOperation operation;
    Date date;
    double montant;

    /**
     * Constructeur par paramètres d'une opération quelquonque.
     * @param operation le type d'opération
     * @param tempsOperation les millisecondes depuis le 1 Janvier 1970, 00:00:00 GMT
     * @param montant le montant  de l'opération
     */
    protected Operation (TypeOperation operation, long tempsOperation, double montant){
        this.operation=operation;
        date= new Date(tempsOperation);
        this.montant = montant;
    }

    protected String getOperation() {
        return operation.toString();
    }

    protected String getDate() {
        return date.toString();
    }

    protected double getMontant() {
        return montant;
    }

    @Override
    public abstract String toString();
}
