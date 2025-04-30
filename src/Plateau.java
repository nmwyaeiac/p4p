/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zeelu
 */
public class Plateau {
    private Case tab[][]; //matrice de Case 4x4 -> pour représenter l'état du jeu
    
    public Plateau(){
        this.tab = new Case [4][4];
        initPlateau();
    }
    
    private void initPlateau(){ // double boucle sur la matrice, pour initialiser chaque case -> new Case()
        for(int i=0; i<4; i++){
            for(int j=0 ; j<4; j++){
                this.tab[i][j] = new Case(); // ajoute une liste sur chaque determinant
            }
        }
    }
    
    public boolean caseValide(int x, int y){ // vérifie si la case est valide si index 0<=i<=3
        return x>=0 && x<=3 && y>=0 && y<=3;
    }
    
    public Case getCase(int x, int y){
        if(caseValide(x,y)){ // si case existe
            return tab[x][y]; // rend la liste
        }
        else{
            return null; // sinon retourne rien
        }
    }
    
    public int getSommetCase(int x, int y){
        if(caseValide(x,y)){
            return getCase(x,y).getSommetCase(); //retourne la dernière valeur de la liste
        }
        else{
            return -1;
        }
    }
    
    public void deposePionCase(int val, int x, int y){
        if(caseValide(x,y))
        {
            this.tab[x][y].empilePion(val);// getCase(x,y).empilePion(val) : autre facon de faire
        }
    }
    
    public void initPlateauJeu(){
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                this.tab[i][j].videCase();
            }
        }
        deposePionCase(0,0,0);
        deposePionCase(0,0,3);
        deposePionCase(0,3,0);
        deposePionCase(0,3,3);
        deposePionCase(0,0,0);
        deposePionCase(0,0,3);
        deposePionCase(0,3,0);
        deposePionCase(0,3,3);
    }
    
    public void afficheValPlateau(){
        for(int i=0; i<4; i++){
            for(int j=0 ; j<4; j++){
                if(!tab[i][j].estVide()){
                    System.out.print(tab[i][j].getPions()+" ");
                }
                else{
                    System.out.print("Vide ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
