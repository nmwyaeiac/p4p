/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author wassil
 */
public class Partie {
    private Joueur j1; // Joueur 1 de la partie
    private Joueur j2; // Joueur 2 de la partie
    private int nbCoups; // Nombre de coups de la partie (On aura un compteur qui va compter le nombre de coups, pendant la partie.)
    private int res; // résultat de la partie (1 si j1 à gagné) (2 si j2 à gagné) (0 si match nul) chaque joueur a 8 pions

    public Partie(Joueur j1, Joueur j2, int nbCoups, int res) { // constructeur standard
        this.j1 = j1;
        this.j2 = j2;
        this.nbCoups = nbCoups;
        this.res = res;
    }
    
    public Partie(){ // constructeur par défaut
        this.j1 = null;
        this.j2 = null;
        this.nbCoups = 0;
        this.res = -1;
    }

    // getter et setter des attributs
    public Joueur getJ1() {
        return j1;
    }

    public void setJ1(Joueur j1) {
        this.j1 = j1;
    }

    public Joueur getJ2() {
        return j2;
    }

    public void setJ2(Joueur j2) {
        this.j2 = j2;
    }

    public int getNbCoups() {
        return nbCoups;
    }

    public void setNbCoups(int nbCoups) {
        this.nbCoups = nbCoups;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
    
    @Override
    public String toString(){
        String s = "Partie{"+"j1="+j1.getPseudo()+", j2="+j2.getPseudo();
        s+=",nbCoups="+nbCoups+",res="+res+'}';
        return s;
    }
}
