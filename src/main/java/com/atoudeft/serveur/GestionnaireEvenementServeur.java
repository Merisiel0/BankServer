package com.atoudeft.serveur;

import com.atoudeft.banque.*;
import com.atoudeft.banque.serveur.ConnexionBanque;
import com.atoudeft.banque.serveur.ServeurBanque;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;
import java.util.List;
import java.util.Objects;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        ServeurBanque serveurBanque = (ServeurBanque)serveur;
        Banque banque;
        ConnexionBanque cnx;
        String msg, typeEvenement, argument, numCompteClient, nip;
        String[] t;

        int nbComptes;
        double montant;
        boolean present;
        List<CompteBancaire> listeDesComptes;
        StringBuilder reconstruction;

        if (source instanceof Connexion) {
            cnx = (ConnexionBanque) source;
            System.out.println("SERVEUR: Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            cnx.setTempsDerniereOperation(System.currentTimeMillis());
            switch (typeEvenement) {
                /******************* COMMANDES GÉNÉRALES *******************/
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    serveurBanque.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des numéros de comptes-clients connectés :
                    cnx.envoyer("LIST " + serveurBanque.list());
                    break;
                case "CONNECT":
                    if (cnx.getNumeroCompteClient()!=null) {
                        cnx.envoyer("CONNECT NO");
                        break;
                    }
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.envoyer("CONNECT NO");
                    }
                    else {
                        numCompteClient = t[0];
                        nip = t[1];
                        banque = serveurBanque.getBanque();
                        CompteClient compte = banque.getCompteClient((numCompteClient));
                        if (compte==null){
                            cnx.envoyer("CONNECT NO");
                            break;
                        }
                        present =false;
                        for (int i =0; i<serveur.connectes.size(); i++){
                            ConnexionBanque autre = (ConnexionBanque) serveur.connectes.get(i);
                            if (Objects.equals(autre.getNumeroCompteClient(),numCompteClient)){
                                present=true;
                            }
                        }
                        if((!compte.getNip().equals(nip))||present) {
                            cnx.envoyer("CONNECT NO");
                        } else {
                            cnx.setNumeroCompteClient(numCompteClient);
                            cnx.setNumeroCompteActuel(compte.getCompteCheque().getNumero());
                            cnx.envoyer("CONNECT OK");
                        }
                    }
                    break;
                case "HIST": //Demande l'historique des actions :
                    if (cnx.getNumeroCompteClient()==null) {
                        cnx.envoyer("HIST NO");
                        break;
                    }
                    banque = serveurBanque.getBanque();
                    nbComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes().size();
                    listeDesComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes();
                    for (int i = 0; i<nbComptes; i++){
                        if (listeDesComptes.get(i).getNumero().equals(cnx.getNumeroCompteActuel())){
                            cnx.envoyer(listeDesComptes.get(i).afficherHistorique());
                            break;
                        }
                    }
                    break;
                /******************* COMMANDES DE GESTION DE COMPTES *******************/
                case "SELECT":
                    if (cnx.getNumeroCompteClient()==null) {
                        cnx.envoyer("SELECT NO");
                        break;
                    }
                    argument = evenement.getArgument();
                    banque = serveurBanque.getBanque();
                    CompteClient compteActuel = banque.getCompteClient(cnx.getNumeroCompteClient());
                    if(argument.equals("cheque")){
                        cnx.setNumeroCompteActuel(compteActuel.getCompteCheque().getNumero());
                        cnx.envoyer("SELECT OK");
                        break;
                    }
                    else if(argument.equals("epargne")){
                        cnx.setNumeroCompteActuel(compteActuel.getCompteEpargne().getNumero());
                        cnx.envoyer("SELECT OK");
                        break;
                    }
                    cnx.envoyer("SELECT NO");
                    break;
                case "DEPOT":
                    if (cnx.getNumeroCompteClient()==null) {
                        cnx.envoyer("DEPOT NO");
                        break;
                    }
                    try {
                        montant = Double.parseDouble(evenement.getArgument());
                    } catch (NumberFormatException e){
                        cnx.envoyer("DEPOT NO");
                        break;
                    }
                    banque = serveurBanque.getBanque();
                    nbComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes().size();
                    listeDesComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes();
                    for (int i = 0; i<nbComptes; i++){
                        if (listeDesComptes.get(i).getNumero().equals(cnx.getNumeroCompteActuel())){
                            if(!listeDesComptes.get(i).crediter(montant)){
                                cnx.envoyer("DEPOT NO");
                                break;
                            }
                            cnx.envoyer("DEPOT OK");
                            break;
                        }
                    }
                    break;
                case "RETRAIT":
                    if (cnx.getNumeroCompteClient()==null) {
                        cnx.envoyer("RETRAIT NO");
                        break;
                    }
                    try {
                        montant = Double.parseDouble(evenement.getArgument());
                    } catch (NumberFormatException e){
                        cnx.envoyer("RETRAIT NO");
                        break;
                    }
                    banque = serveurBanque.getBanque();
                    nbComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes().size();
                    listeDesComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes();
                    for (int i = 0; i<nbComptes; i++){
                        if (listeDesComptes.get(i).getNumero().equals(cnx.getNumeroCompteActuel())){
                            if(!listeDesComptes.get(i).debiter(montant)){
                                cnx.envoyer("RETRAIT NO");
                                break;
                            }
                            cnx.envoyer("RETRAIT OK");
                            break;
                        }
                    }
                    break;
                case "FACTURE":
                    if (cnx.getNumeroCompteClient()==null) {
                        cnx.envoyer("FACTURE NO");
                        break;
                    }
                    argument = evenement.getArgument();
                    t = argument.split(" ");
                    if(t.length>3){
                        reconstruction=new StringBuilder();
                        for (int i=2; i<t.length-1;i++){
                            reconstruction.append(t[i]);
                        }
                        t[2]=reconstruction.toString();
                    }else if (t.length<3) {
                        cnx.envoyer("FACTURE NO");
                        break;
                    }
                    try {
                        montant = Double.parseDouble(t[0]);
                    } catch (NumberFormatException e){
                        cnx.envoyer("FACTURE NO");
                        break;
                    }
                    banque = serveurBanque.getBanque();
                    nbComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes().size();
                    listeDesComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes();
                    for (int i = 0; i<nbComptes; i++){
                        if (listeDesComptes.get(i).getNumero().equals(cnx.getNumeroCompteActuel())){
                            if(!listeDesComptes.get(i).payerFacture(t[1],montant,t[2])){
                                cnx.envoyer("FACTURE NO");
                                break;
                            }
                            cnx.envoyer("FACTURE OK");
                            break;
                        }
                    }
                    break;
                case "TRANSFER":
                    if (cnx.getNumeroCompteClient()==null) {
                        cnx.envoyer("TRANSFER NO");
                        break;
                    }
                    argument = evenement.getArgument();
                    t = argument.split(" ");
                    if(t.length>2){
                        cnx.envoyer("TRANSFER NO");
                        break;
                    } else if (t.length<2) {
                        cnx.envoyer("TRANSFER NO");
                        break;
                }
                    try {
                        montant = Double.parseDouble(t[0]);
                    } catch (NumberFormatException e){
                        cnx.envoyer("TRANSFER NO");
                        break;
                    }
                    banque = serveurBanque.getBanque();
                    nbComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes().size();
                    listeDesComptes = banque.getCompteClient(cnx.getNumeroCompteClient()).getComptes();
                    for (int i = 0; i<nbComptes; i++){
                        if (listeDesComptes.get(i).getNumero().equals(cnx.getNumeroCompteActuel())){
                            if(!listeDesComptes.get(i).transferer(montant, t[1])){
                                cnx.envoyer("TRANSFER NO");
                                break;
                            }
                            cnx.envoyer("TRANSFER OK");
                            break;
                        }
                    }
                    break;
                case "EPARGNE":
                    if (cnx.getNumeroCompteClient()==null) {
                        cnx.envoyer("EPARGNE NO");
                        break;
                    }
                    banque = serveurBanque.getBanque();
                    if(banque.hasCompteEpargne(cnx.getNumeroCompteClient())) {
                        cnx.envoyer("EPARGNE NO");
                        break;
                    }
                    String numero = "";
                    boolean numeroLibre = false;
                    while(!numeroLibre) {
                        numero = CompteBancaire.genereNouveauNumero();
                        numeroLibre = banque.estNumeroLibre(numero);
                    }
                    banque.getCompteClient(cnx.getNumeroCompteClient()).ajouter(new CompteEpargne(numero, 0.05f));
                    cnx.envoyer("EPAGNE OK");
                    break;
                case "NOUVEAU": //Crée un nouveau compte-client :
                    if (cnx.getNumeroCompteClient()!=null) {
                        cnx.envoyer("NOUVEAU NO deja connecte");
                        break;
                    }
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.envoyer("NOUVEAU NO");
                    }
                    else {
                        numCompteClient = t[0];
                        nip = t[1];
                        banque = serveurBanque.getBanque();
                        if (banque.ajouter(numCompteClient,nip)) {          //si on peut ajouter le Compte
                            cnx.setNumeroCompteClient(numCompteClient);     //modifie le Numero du Compte Client
                            cnx.setNumeroCompteActuel(banque.getNumeroCompteParDefaut(numCompteClient));  //*******
                            cnx.envoyer("NOUVEAU OK " + t[0] + " cree");
                        }
                        else
                            cnx.envoyer("NOUVEAU NO ");
                    }
                    break;
                /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}