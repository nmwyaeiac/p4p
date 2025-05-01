/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author wassil
 */
public class Jeu {
    private final Plateau platJeu;
    
    public Jeu(){
        this.platJeu = new Plateau(); // créer un nouveau plateau 
        this.platJeu.initPlateauJeu(); // place les galets neutres dans les 4 coins du plateau
        this.platJeu.afficheValPlateau(); // méthode qui affiche les valeurs dans la fenêtre output
    }
    
    public Plateau getPlateau(){
        return this.platJeu;
    }
    
    public boolean gagne(int valj){ // valj vaut 1 si joueur 1 qui à gagné ou 2 sinon 
        int compt;
        // Vérification des lignes
        for(int i=0; i<4; i++) {
            compt = 0;
            for(int j=0; j<4; j++) {
                if(this.platJeu.getCase(i, j).getSommetCase() == valj) {
                    compt++;
                }
            }
            if(compt == 4){
                return true;
            }     
        }
        // Vérification des colonnes
        for(int j=0; j<4; j++) {
            compt = 0;
            for(int i=0; i<4; i++) {
                if(this.platJeu.getCase(i, j).getSommetCase() == valj) {
                    compt++;
                }
            }
            if(compt == 4){ 
                return true;
            }
        }
        // Vérification de la diagonale haut-gauche vers bas-droit
        compt = 0;
        for(int i=0; i<4; i++) {
            if(this.platJeu.getCase(i, i).getSommetCase() == valj) {
                compt++;
            }
        }
        if(compt == 4){
            return true;
        }
        // Vérification de la diagonale haut-droite vers bas-gauche
        compt = 0;
        for(int i=0; i<4; i++) {
            if(this.platJeu.getCase(i, 3 - i).getSommetCase() == valj) {
                compt++;
            }
        }
        if(compt == 4){
            return true;
        }
        // Si aucune condition de victoire
        return false;
    }
    
    public boolean adjacent(int xd, int yd, int xa, int ya){ // verification que 2 cases sont adjacentes (en ligne et colonne)
        return (xa == xd-1 && ya==yd)||(xa == xd+1 && ya==yd)||(xa == xd && ya==yd-1)||(xa == xd && ya==yd+1);
    }
    
    public int jouePion(int pxd, int pyd, int xd, int yd, int xa, int ya, int valj){ // (pxd,pyd) position précédente (xd,yd) position courante (xa,ya) position ou on veut déposer unn galet (valj) valeur du pion à déposer
        int code=0;
        if(pxd==xa && pyd==ya){
            System.out.print("Mouvement interdi retour en arrière ");
            code = 1;
        }
        else{
            if(!adjacent(xd,yd,xa,ya)){
                System.out.print("Interdi case non adjacentes ");
                code = 2;
            }
            else{ // on dépose le pion
                this.platJeu.deposePionCase(valj, xa, ya);
            }
        }
        return code;
    }
}
