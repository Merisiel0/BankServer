package com.atoudeft.banque;

public class CompteCheque extends CompteBancaire {
    /**
     * Crée un compte cheque.
     * @param numero numéro du compte
     */
    public CompteCheque(String numero) {
        super(numero, TypeCompte.CHEQUE);
    }

    /**
     * Ajoute le montant au solde s'il est strictement positif.
     * @param montant le montant à créditer
     * @return true si l'opération fonctionne, false si le montant n'est pas positif
     */
    @Override
    public boolean crediter(double montant) {
        if(montant < 0) return false;
        solde += montant;
        historique.empiler(new OperationDepot(montant));
        return true;
    }

    /**
     * Retire le montant du solde s'il est strictement positif et qu'il y a asser de fonds.
     * @param montant le montant a debiter
     * @return false si le montant n'est pas positif ou s'il n'y a pas asser de fonds, sinon true.
     */
    @Override
    public boolean debiter(double montant) {
        if(montant < 0 || solde < montant) return false;
        solde -= montant;
        historique.empiler(new OperationDepot(montant));
        return true;
    }

    /**
     *
     * @param numeroFacture
     * @param montant
     * @param description
     * @return false.
     */
    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        historique.empiler(new OperationFacture(montant, numeroFacture, description));
        return false;
    }

    /**
     *
     * @param montant
     * @param numeroCompteDestinataire
     * @return false.
     */
    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        historique.empiler(new OperationTransfer(montant, numeroCompteDestinataire));
        return false;
    }
}
