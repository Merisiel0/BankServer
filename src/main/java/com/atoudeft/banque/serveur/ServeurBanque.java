package com.atoudeft.banque.serveur;

import com.atoudeft.banque.Banque;
import com.atoudeft.banque.io.EntreesSorties;
import com.atoudeft.commun.net.Connexion;
import com.atoudeft.serveur.Serveur;

import java.util.ListIterator;
/**
 * Cette classe étend (hérite) la classe Serveur et y ajoute le nécessaire pour que le
 * serveur soit un serveur de banque.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2024-08-20
 */
public class ServeurBanque extends Serveur {
    public static final int DELAI_INACTIVITE = 5000;
    //Référence vers la banque gérée par ce serveur :
    private Banque banque;
    //Thread qui supprime les connexions inactives :
    private ThreadDesInactifs threadDesInactifs;
    /**
     * Crée un serveur qui va écouter sur le port spécifié.
     *
     * @param port int Port d'écoute du serveur
     */
    public ServeurBanque(int port) {
        super(port);
    }

    /**
     * Retourne la banque gérée par ce serveur.
     * @return la banque gérée par ce serveur
     */
    public Banque getBanque() {
        return banque;
    }
    /**
     * Démarre le serveur de banque, s'il n'a pas déjà été démarré. Démarre le thread qui écoute l'arrivée de clients,
     * le thread qui écoute l'arrivée de texte et le thread qui supprime les connexions inactives. Mets en place le
     * gestionnaire des événements du serveur.
     *
     * @return boolean true, si le serveur a été démarré correctement, false, si le serveur a déjà été démarré ou s'il
     * y a un problème de connexion
     */
    @Override
    public boolean demarrer() {
        if (super.demarrer()) {
            this.banque = EntreesSorties.charger();
            if (this.banque==null)
                this.banque = new Banque("BankEts");
            threadDesInactifs = new ThreadDesInactifs(this);
            threadDesInactifs.start();
            return true;
        }
        return false;
    }
    /**
     * Arrête le serveur en arrêtant les threads qui écoutent l'arrivée de client et l'arrivée de texte et les threads
     * qui supprime les connexions inactives et qui traite les nouveaux clients.
     */
    @Override
    public void arreter() {
        super.arreter();
        if (threadDesInactifs!=null)
            threadDesInactifs.interrupt();
        EntreesSorties.sauvegarder(banque);
    }

    /**
     * Fournit la liste des numéros des comptes-client connectés, séparés par le symbole :.
     * @return la liste des numéros des comptes-client connectés
     */
    public String list() {
        String s = "";
        for (Connexion cnx:connectes) {
                 //((ConnexionBanque)cnx) = polymorphisme
            s += ((ConnexionBanque)cnx).getNumeroCompteClient() + ":";
        }
        return s;
    }
    /**
     * Supprime toutes les connexions inactives (celles dont le délai d'inactivité dépasse DELAI_INACTIVITE - voir énoncé
     * du TP).
     */
    public void supprimeInactifs() {
        //System.out.println(".");

        //À définir :

        //je veux parcourir tout les Clients sur le serveur
        for (Connexion individu:connectes){  //parcours tout les clients qui sont "connecté" dans connexion
            //((ConnexionBanque)cnx) = polymorphisme

            long instant = (((ConnexionBanque)individu).getTempsDerniereOperation());
            //System.out.println(instant);
              ((ConnexionBanque) individu).estInactifDepuis(instant - System.currentTimeMillis());

            if(((ConnexionBanque) individu).estInactifDepuis(System.currentTimeMillis()-instant)){
                //le délai donné 30 000 millisecondes
                //System.out.println("Un compte est bel et bien inactif");

                //afficher "END
                individu.envoyer("END");
                //Ferme Connexion
                individu.close();
                //enlever de la liste des conectées
                connectes.remove(individu);
                break;
            }
        }
    }
}