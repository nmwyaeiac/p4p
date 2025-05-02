import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author wassil
 */
public class LesParties {
    private final ArrayList<Partie>lstp; // Pour gérer l'ensemble des parties
    
    public LesParties(){
        this.lstp = new ArrayList<>();
    }
    
    public Partie getPartie(int i){ // Donne la partie d'indice i sans vérifier que i est correct
        return lstp.get(i);
    }
    
    public int getIndicePartie(Partie p){ // Redonne l'indice d'une partie passée en paramètre (IndexOf() méthode de ArrayList<>
        return this.lstp.indexOf(p); 
    }
    
    public int getNbPartie(){ // Renvoie la taille de la liste
        return this.lstp.size();
    }
    
    public void ajoutePartie(Partie p){ // Ajoute une partie (avec add)
       this.lstp.add(p);
    }
    
    public LesParties rechPartie(String p){ // Retourne toutes les parties d'un joueur passé en paramètre avec son pseudo qu'il soit j1 ou j2 
        LesParties lp = new LesParties();
        for(int i = 0; i<lstp.size(); i++){
            if(lstp.get(i).getJ1().getPseudo().equals(p)||lstp.get(i).getJ2().getPseudo().equals(p)){
                lp.ajoutePartie(lstp.get(i));
            }
        }
        return lp;
    }
    
    public String toString(){
        String s = "";
        for(int i=0; i<lstp.size(); i++){
            s+= "Partie "+i+" {"+lstp.get(i).toString()+"}\n";
        }
        return s;
    }  
}
