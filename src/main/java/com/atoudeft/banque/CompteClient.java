package com.atoudeft.banque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompteClient implements Serializable {
    private String numero;
    private String nip;
    private List<CompteBancaire> comptes;


    /**
     * Crée un compte-client avec un numéro et un nip.
     *
     * @param numero le numéro du compte-client
     * @param nip le nip
     */
    public CompteClient(String numero, String nip) {
        this.numero = numero;
        this.nip = nip;
        comptes = new ArrayList<>();
    }

    /**
     * Ajoute un compte bancaire au compte-client.
     *
     * @param compte le compte bancaire
     * @return true si l'ajout est réussi
     */
    public boolean ajouter(CompteBancaire compte) {
        return this.comptes.add(compte);
    }

    /**
     * Retourne le compte cheque du compte-client.
     * Retourne null si aucun n'est trouvé.
     */
    public CompteCheque getCompteCheque() {
        for(int i = 0; i < comptes.size(); i++) {
            if(comptes.get(i).getType() == TypeCompte.CHEQUE) {
                return (CompteCheque)comptes.get(i);
            }
        }
        return null;
    }

    /**
     * Retourne le compte epargne du compte-client.
     * Retourne null si aucun n'est trouvé.
     */
    public CompteEpargne getCompteEpagne() {
        for(int i = 0; i < comptes.size(); i++) {
            if(comptes.get(i).getType() == TypeCompte.EPARGNE) {
                return (CompteEpargne) comptes.get(i);
            }
        }
        return null;
    }

    /**
     * Retourne les comptes bancaires du compte-client.
     */
    public List<CompteBancaire> getComptes() {
        return comptes;
    }
    public String getNumero() { return numero; }
    public String getNip() { return nip; }
}