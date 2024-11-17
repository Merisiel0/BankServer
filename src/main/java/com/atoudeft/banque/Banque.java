package com.atoudeft.banque;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Banque implements Serializable {
    private String nom;
    private List<CompteClient> comptes;

    public Banque(String nom) {
        this.nom = nom;
        this.comptes = new ArrayList<>();
    }

    /**
     * Recherche un compte-client à partir de son numéro.
     *
     * @param numeroCompteClient le numéro du compte-client
     * @return le compte-client s'il a été trouvé. Sinon, retourne null
     */
    public CompteClient getCompteClient(String numeroCompteClient) {
        CompteClient cpt = new CompteClient(numeroCompteClient,"");
        int index = this.comptes.indexOf(cpt);
        if (index != -1)
            return this.comptes.get(index);
        else
            return null;
    }

    /**
     * Vérifier qu'un compte-bancaire appartient bien au compte-client.
     *
     * @param numeroCompteBancaire numéro du compte-bancaire
     * @param numeroCompteClient    numéro du compte-client
     * @return  true si le compte-bancaire appartient au compte-client
     */
    public boolean appartientA(String numeroCompteBancaire, String numeroCompteClient) {
        throw new NotImplementedException();
    }

    /**
     * Effectue un dépot d'argent dans un compte-bancaire
     *
     * @param montant montant à déposer
     * @param numeroCompte numéro du compte
     * @return true si le dépot s'est effectué correctement
     */
    public boolean deposer(double montant, String numeroCompte) {
        throw new NotImplementedException();
    }

    /**
     * Effectue un retrait d'argent d'un compte-bancaire
     *
     * @param montant montant retiré
     * @param numeroCompte numéro du compte
     * @return true si le retrait s'est effectué correctement
     */
    public boolean retirer(double montant, String numeroCompte) {
        throw new NotImplementedException();
    }

    /**
     * Effectue un transfert d'argent d'un compte à un autre de la même banque
     * @param montant montant à transférer
     * @param numeroCompteInitial   numéro du compte d'où sera prélevé l'argent
     * @param numeroCompteFinal numéro du compte où sera déposé l'argent
     * @return true si l'opération s'est déroulée correctement
     */
    public boolean transferer(double montant, String numeroCompteInitial, String numeroCompteFinal) {
        throw new NotImplementedException();
    }

    /**
     * Effectue un paiement de facture.
     * @param montant montant de la facture
     * @param numeroCompte numéro du compte bancaire d'où va se faire le paiement
     * @param numeroFacture numéro de la facture
     * @param description texte descriptif de la facture
     * @return true si le paiement s'est bien effectuée
     */
    public boolean payerFacture(double montant, String numeroCompte, String numeroFacture, String description) {
        throw new NotImplementedException();
    }

    /**
     * @param numero le numéro a vérifier
     * @return true si le numéro est libre, false s'il est déjà utilisé.
     */
    public boolean estNumeroLibre(String numero) {
        for(int i = 0; i < comptes.size(); i++) {
            if(comptes.get(i).getNumero().equals(numero)) {
                return false;
            }

            for(int j = 0; j < comptes.get(i).getComptes().size(); i++) {
                if(comptes.get(i).getComptes().get(j).getNumero().equals(numero)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Crée un nouveau compte-client avec un numéro et un nip et l'ajoute à la liste des comptes.
     *
     * @param numCompteClient numéro du compte-client à créer
     * @param nip nip du compte-client à créer
     * @return true si le compte a été créé correctement, sinon false
     */
    public boolean ajouter(String numCompteClient, String nip) {
        if(6 > numCompteClient.length() || 8 < numCompteClient.length() // Vérifier que le numéro a entre 6 et 8 caractères.
            || !numCompteClient.matches("[A-Z0-9]+") // Vérifier que le numéro ne contient que des lettres majuscules et des chiffres.
            || 4 > nip.length() || 5 < nip.length() // Vérifier que le nip a entre 4 et 5 caractères.
            || !nip.matches("[0-9]") // Vérifier que le nip ne contient que des chiffres.
            || comptes.stream().anyMatch(n -> n.getNip().equals(nip))) { // Vérifier s'il y a déjà un compte-client avec le numéro.
            return false;
        }

        CompteClient compte = new CompteClient(numCompteClient, nip);

        String numero;
        boolean numeroLibre = false;
        while(!numeroLibre) {
            numero = CompteBancaire.genereNouveauNumero();
            numeroLibre = estNumeroLibre(numero);
        }

        compte.ajouter(new CompteCheque(CompteBancaire.genereNouveauNumero()));

        return comptes.add(compte);
    }

    /**
     * @param numCompteClient le compte client a veriier
     * @return true si le compte client a un compte epargne, false sinon.
     */
    public boolean hasCompteEpargne(String numCompteClient) {
        for(int i = 0; i < comptes.size(); i++) {
            if(comptes.get(i).getNumero().equals(numCompteClient)) {
                return comptes.get(i).getCompteEpargne() != null;
            }
        }
        return false;
    }

    /**
     * Retourne le numéro du compte-chèque d'un client à partir de son numéro de compte-client.
     * Retourne null si le numéro de compte-client n'existe pas.
     *
     * @param numCompteClient numéro de compte-client
     * @return numéro du compte-chèque du client ayant le numéro de compte-client
     */
    public String getNumeroCompteParDefaut(String numCompteClient) {
        for(int i = 0; i < comptes.size(); i++) {
            if(comptes.get(i).getNumero().equals(numCompteClient)) {
                return comptes.get(i).getCompteCheque().getNumero();
            }
        }
        return null;
    }
}