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
     * Ajoute le montant au solde s'il est strictement position.
     * Sinon, retoure false.
     */
    @Override
    public boolean crediter(double montant) {
        if(montant < 0) return false;
        solde += montant;
        historique.empiler(new OperationDepot(montant));
        return true;
    }

    /**
     * Retire le montant du solde s'il est strictement position et
     * qu'il y a assez de fonds. Sinon, retourne false.
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
