package com.atoudeft.banque;

public class CompteCheque extends CompteBancaire {
    /**
     * Crée un compte cheque.
     * @param numero numéro du compte
     * @param type type du compte
     */
    public CompteCheque(String numero) {
        super(numero, TypeCompte.CHEQUE);
    }

    /**
     * Ajoute le montant au solde s'il est strictement position.
     * Sinon, retoure false.
     */
    public boolean crediter(double montant) {
        if(montant < 0) return false;
        solde += montant;
        return true;
    }

    /**
     * Retire le montant du solde s'il est strictement position et
     * qu'il y a assez de fonds. Sinon, retourne false.
     */
    public boolean debiter(double montant) {
        if(montant < 0 || solde < montant) return false;
        solde -= montant;
        return true;
    }

    public boolean payerFacture(String numeroFacture, double montant, String description) {
        return false;
    }

    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false;
    }
}
