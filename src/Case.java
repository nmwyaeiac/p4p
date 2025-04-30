import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zeelu
 */
public class Case {
    private ArrayList<Integer>pions;

    public Case(){
        this.pions = new ArrayList<>();
    }

    public ArrayList<Integer> getPions() {
        return pions;
    }

    public void setPions(ArrayList<Integer> pions) {
        this.pions = pions;
    }
    
    public int getValCase(int i){
        if((!estVide())&& i>=0 && i<this.pions.size())
            return this.pions.get(i);
        else
            return -1;
    }
    
    public int getNbPions(){
        return pions.size();
    }
    
    public void videCase(){
        this.pions.clear(); // supprime tout les pions de la case
    }
    
    public boolean estVide(){
        return this.pions.isEmpty();
    }
    
    public int getSommetCase(){
        if(!estVide())
            return this.pions.get(this.pions.size()-1);
        else
            return -1;
    }
    
    public void empilePion(int val){
        this.pions.add(val);
    }
    
    public int defilePion(){
        int val;
        if(!estVide()){
            val = this.pions.get(0);
            this.pions.remove(0); //enleve l'élement à la position 0
            return val;
        }
        else{
            return -1;
        }
    }
}
