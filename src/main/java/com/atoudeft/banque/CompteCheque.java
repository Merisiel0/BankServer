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
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        historique.empiler(new OperationTransfer(montant, numeroCompteDestinataire));
        return false;
    }

    @Override
    public boolean afficherHistorique() {
        if (historique.estVide()){
            System.out.println("HISTORIQUE VIDE");
        } else {
            PileChainee pileTemporaire = new PileChainee();
            for (int i = 0; i < historique.getTaille(); i++) {
                Operation courant = (Operation) historique.depiler();
                System.out.println(courant);
                pileTemporaire.empiler(courant);
            }
            for (int i = 0; i < pileTemporaire.getTaille(); i++) {
                Operation courant = (Operation) pileTemporaire.depiler();
                historique.empiler(courant);
            }
        }
        return true;
    }
}
