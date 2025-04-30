/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.*;
import javax.swing.ImageIcon;
import java.io.*;
/**
 *
 * @author zeelu
 */
public class LesJoueurs {
    private ArrayList<Joueur> lstj;
    
    public LesJoueurs(){
        this.lstj = new ArrayList<Joueur>();
    }
    public Joueur getJoueur(int i){
        if(this.lstj.get(i)!=null){
            return this.lstj.get(i);
        }
        return null;
    }
    
    public int getIndiceJoueur(Joueur j){
        return this.lstj.indexOf(j);
    }
    
    public int getNbJoueurs(){
        return this.lstj.size();
    }
    
    public void ajouteJoueur(Joueur j){
        this.lstj.add(j);
    }
    
    public Joueur rechJoueur(String p){
        for(int i = 0; i<this.lstj.size();i++){
            if(p.equals(this.lstj.get(i).getPseudo()))
                return this.lstj.get(i);
        }
        return null;
    }
    
    public void supprimeJoueur(Joueur j){
        this.lstj.remove(j);
    }
    
    public void creationJoueursTest(LesParties lp){
        // Création des joueurs test
        
        Joueur jm= new Joueur("Mario"); //expert
        jm.setPhoto(new ImageIcon(getClass().getResource("/mario.png")));
        this.ajouteJoueur(jm);
        Joueur js = new Joueur("Sonic");
        js.setPhoto(new ImageIcon(getClass().getResource("/sonic.png")));
        this.ajouteJoueur(js);
        Joueur jma = new Joueur("Marcus");
        jma.setPhoto(new ImageIcon(getClass().getResource("/marcus.jpg")));
        this.ajouteJoueur(jma);
        Joueur jp = new Joueur("Pikachu");
        jp.setPhoto(new ImageIcon(getClass().getResource("/pikatchu.png")));
        this.ajouteJoueur(jp);
        Joueur jh = new Joueur("Halo");
        jh.setPhoto(new ImageIcon(getClass().getResource("/halo.jpg")));
        this.ajouteJoueur(jh);
        
        // Création des parties test
        Partie p1 = new Partie(jm, js, 12, 1); // Mario gagne Sonic perd
        Partie p2 = new Partie(jm, jp, 15, 0); // Partie nul
        Partie p3 = new Partie(jm, jp, 18, 0); // Partie nul
        Partie p4 = new Partie(jma, jp, 19, 0); // Partie nul
        Partie p5 = new Partie(jma, jp, 20, 0); // Partie nul
        Partie p6 = new Partie(jh, jp, 17, 2); // Pikachu gagne Halo perd
        Partie p7 = new Partie(jh, jp, 13, 2); // Pikachu gagne Halo perd
        Partie p8 = new Partie(jh, jp, 12, 2); // Pikachu gagne Halo perd
        Partie p9 = new Partie(jm, js, 12, 1); // Mario gagne Sonic perd
        Partie p10 = new Partie(jm, jma, 14, 1); // Mario gagne Pikachu perd
        Partie p11 = new Partie(jm, jma, 11, 1); // Mario gagne Marcus perd
        Partie p12 = new Partie(jm, jh, 10, 1); // Mario gagne Halo perd
        Partie p13 = new Partie(jm, jma, 21, 1);  // Mario gagne Marcus perd
        Partie p14 = new Partie(jma, js, 18, 1);  // Marcus gagne Sonic perd
        Partie p15 = new Partie(jma, jh, 13, 1); // Marcus gagne Halo perd
        Partie p16 = new Partie(jm, js, 15, 1);  // Mario gagne Sonic perd
        Partie p17 = new Partie(jm, jh, 17, 1);  // Mario gagne Halo perd
        
        // Résultats des parties
        enregistrerResultat(jm, js, 1);   // p1
        enregistrerResultat(jm, jp, 0);   // p2
        enregistrerResultat(jm, jp, 0);   // p3
        enregistrerResultat(jma, jp, 0);  // p4
        enregistrerResultat(jma, jp, 0);  // p5
        enregistrerResultat(jh, jp, 2);   // p6
        enregistrerResultat(jh, jp, 2);   // p7
        enregistrerResultat(jh, jp, 2);   // p8
        enregistrerResultat(jm, js, 1);   // p9
        enregistrerResultat(jm, jma, 1);  // p10
        enregistrerResultat(jm, jma, 1);  // p11
        enregistrerResultat(jm, jh, 1);   // p12
        enregistrerResultat(jm, jma, 1);  // p13
        enregistrerResultat(jma, js, 1);  // p14
        enregistrerResultat(jma, jh, 1);  // p15
        enregistrerResultat(jm, js, 1);   // p16
        enregistrerResultat(jm, jh, 1);   // p17
        
        //ajout des parties dans la liste des parties
        lp.ajoutePartie(p1);
        lp.ajoutePartie(p2);
        lp.ajoutePartie(p3);
        lp.ajoutePartie(p4);
        lp.ajoutePartie(p5);
        lp.ajoutePartie(p6);
        lp.ajoutePartie(p7);
        lp.ajoutePartie(p8);
        lp.ajoutePartie(p9);
        lp.ajoutePartie(p10);
        lp.ajoutePartie(p11);
        lp.ajoutePartie(p12);
        lp.ajoutePartie(p13);
        lp.ajoutePartie(p14);
        lp.ajoutePartie(p15);
        lp.ajoutePartie(p16);
        lp.ajoutePartie(p17);
    }

    public void enregistrerResultat(Joueur j1, Joueur j2, int res) {
        int resJ1, resJ2;
        int nivJ1 = j1.getNiveau();
        int nivJ2 = j2.getNiveau();
        if (res == 1) { // si c'est le joueur 1 qui gagne
            resJ1 = 1;
            resJ2 = -1;
            j1.ajouterResultat(j2, resJ1, nivJ1); // gagnant donc 1 niveau en plus
            j2.ajouterResultat(j1, resJ2); // perdant donc pas de niveau
        } 
        else if (res == 2) { // sinon si c'est le joueur 2 qui gagne
            resJ1 = -1;
            resJ2 = 1;
            j1.ajouterResultat(j2, resJ1); // perdant donc pas de niveau
            j2.ajouterResultat(j1, resJ2, nivJ2); // gagnant donc 1 niveau en plus
        } 
        else { // sinon il y a égalité
            resJ1 = 0;
            resJ2 = 0;
            j1.ajouterResultat(j2, resJ1); // égalité donc pas de niveau 
            j2.ajouterResultat(j1, resJ2);
        }  
    }
    
    public String toString(){
        String s = " ";
        for(int i = 0; i<this.lstj.size();i++){
            s+= "Joueur "+(i+1)+" : "+this.lstj.get(i)+"\n";
        }
        return s;
    }
}
