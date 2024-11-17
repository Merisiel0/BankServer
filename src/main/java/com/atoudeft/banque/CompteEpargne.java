package com.atoudeft.banque;

public class CompteEpargne extends CompteBancaire {
    private final int limiteDeFrais = 1000;
    private final int frais = 2;
    private float tauxInterets;

    /**
     * Crée un compte épargne.
     * @param numero numéro du compte
     * @param tauxInterets taux d'intérêts du compte (0.01 = 1%)
     */
    public CompteEpargne(String numero, float tauxInterets) {
        super(numero, TypeCompte.EPARGNE);
        this.tauxInterets = tauxInterets;
    }

    /**
     * Ajoute les intérêts à la solde.
     */
    public void ajouterInterets() {
        solde += solde * tauxInterets;
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
     * Retire le montant du solde s'il est strictement positif et
     * qu'il y a assez de fonds.
     * Si la solde contient moins que la limite de frais avant l'operation,
     * des frais seront ajoutés.
     * @param montant le montant a debiter
     * @return false si le montant n'est pas positif ou s'il n'y a pas asser de fonds, sinon true.
     */
    @Override
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
