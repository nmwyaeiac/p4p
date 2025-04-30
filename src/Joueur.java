import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.io.*;


public class Joueur {
    private String pseudo;
    private ImageIcon photo; 
    private int niveau; 
    private ArrayList<Joueur> adversaires; 
    private ArrayList<Integer> resultats; 
     

   public Joueur() {
        this.pseudo = "pseudo";
        this.niveau=1;
        this.adversaires= new ArrayList<>();
        this.resultats= new ArrayList<>();
        this.photo = new ImageIcon(getClass().getResource("/joueurDefaut.png"));
    }
   
    public Joueur(String pseudo) {
        this.pseudo = pseudo;
        this.niveau=1;
        this.adversaires= new ArrayList<>();
        this.resultats= new ArrayList<>();
        this.photo = new ImageIcon(getClass().getResource("/joueurDefaut.png"));
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public ImageIcon getPhoto() {
        return this.photo;
    }

    public void setPhoto(ImageIcon photo) {
        this.photo = photo;
    }

    public int getNiveau() {
        return this.niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }
    
    public int getNba()  
    {
        return this.adversaires.size();
    }
    public Joueur getAdversaire(int i) 
    {   if (i>=0 && i< this.adversaires.size())
          return this.adversaires.get(i);
        else return null;
    }
    
    public ArrayList<Integer> getResultatsAdv(String adv) 
    {
        ArrayList<Integer> lst = new ArrayList <>();
        for (int i= 0; i<this.adversaires.size(); i++)
            if (this.adversaires.get(i).pseudo.equals(adv))
                lst.add(this.resultats.get(i));
        return lst;
    }
    
    public LesJoueurs getAdversaires()  
    {
       LesJoueurs lj = new LesJoueurs();
        for (int i=0; i<this.adversaires.size(); i++)
        { Joueur jo = this.adversaires.get(i);
            boolean trouve = false;
            for (int j=0; j< lj.getNbJoueurs() ;j++)
                if (lj.getJoueur(j).pseudo.equals(jo.pseudo))
                    trouve = true;
            if (!trouve) lj.ajouteJoueur(jo);     
        }
        return lj;
    }
    
       public int getNbpartiesGagnees()
    {   int compt=0;
        for(int i=0; i<this.resultats.size(); i++)
          if (this.resultats.get(i)== 1)
             compt++; 
        return compt;
    }
    
	 public int getNbpartiesPerdues()
    {     int compt=0;
          for(int i=0; i<this.resultats.size(); i++)
          if (this.resultats.get(i)== -1)
             compt++; 
          return compt;
    }
       public int getNbpartiesNul()
    {  
        int compt=0;
        for(int i=0; i<this.resultats.size(); i++)
        if (this.resultats.get(i)== 0)
            compt++; 
        return compt;
    }
       
    public void ajouterResultat(Joueur adv, int res) {
        this.adversaires.add(adv);
        this.resultats.add(res);
    }
    
    public void ajouterResultat(Joueur adv, int res, int niv) {
        this.adversaires.add(adv);
        this.resultats.add(res);
        this.niveau = niv+1;
    }

    @Override
    public String toString() {
        String s = "";
        s += "Joueur\n";
        s += "Pseudo " + this.pseudo + "\n";
        s += "Niveau " + this.niveau + "\n";
        for (int i=0; i<this.adversaires.size(); i++) {
            Joueur j = this.getAdversaire(i);
            int res = this.resultats.get(i);
             s += "partie jouée contre "+j.pseudo+" ";
            switch (res) {
                case 1:
                    s += "gagné";
                    break;
                case 0:
                    s += "nul";
                    break;
                case -1:
                    s += "perdu";
                    break;
            }
        s += "\n";
        }
        return s;
    }
}
