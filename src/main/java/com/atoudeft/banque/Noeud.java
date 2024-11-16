package com.atoudeft.banque;

/**DÉBUT DE CODE EMPRUNTÉ/ADAPTÉ
 * Code emprunté de la classe 5 du cours INF111 donné par M.Pichette
 * tel qu'autorisé à la page 31 de l'énoncé du Travail Pratique
 * Classe Noeud de l'exercice des listes chainées
 * (Consulté le 16-11-2024)
 * https://ena.etsmtl.ca/course/view.php?id=24295#section-5
 *
 * */

import java.io.Serializable;

/**
 * Cette classe implémente le noeud d'une liste chainée
 *
 * Le type de l'élément est abstrait en utilisant la classe Object
 */

public class Noeud implements Serializable {
    private final Object element;
    private Noeud suivant;


    // constructeur par paramètre
    public Noeud(Object element) {
        this.element = element;
    }

    // accesseurs
    public Noeud getSuivant() {
        return suivant;
    }

    public Object getElement() {
        return element;
    }

    // mutateurs
    public void setSuivant(Noeud suivant) {
        this.suivant = suivant;
    }

    public String toString() {
        return "Noeud : " + element.toString();
    }
}
/*****************FIN DU CODE EMPRUNTÉ/ADAPTÉ*****************/