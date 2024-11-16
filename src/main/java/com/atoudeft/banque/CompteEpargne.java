package com.atoudeft.banque;

public class CompteEpargne extends CompteBancaire {
    private final int limiteDeFrais = 1000;
    private final int frais = 2;
    private float tauxInterets;

    /**
     * Crée un compte cheque.
     * @param numero numéro du compte
     * @param tauxInterets taux d'interets du compte (0.01 = 1%)
     */
    public CompteEpargne(String numero, float tauxInterets) {
        super(numero, TypeCompte.EPARGNE);
        this.tauxInterets = tauxInterets;
    }

    public void ajouterInterets() {
        solde += solde * tauxInterets;
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
     * Si la solde contient moins que limiteDeFrais avant l'operation,
     * des frais seront ajoutés.
     */
    public boolean debiter(double montant) {
        if(montant < 0 || solde < montant) return false;
        solde -= solde < limiteDeFrais ? montant + frais : montant;
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
