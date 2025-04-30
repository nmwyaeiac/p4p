import javax.swing.JButton;
import java.awt.*;
import javax.swing.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author wassil
 */
public class BoutonImage extends JButton{
    private Image img;

    public BoutonImage(){
        super();
        this.img = null;
    }
    
    public BoutonImage(Image im){
        super();
        this.img = im;
    }
    
    public Image getImage(){
        return this.img;
    }
    
    public void setImage(Image im){
        this.img = im;
        this.repaint();
    }
    
    @Override
    public void paint(Graphics g){ // On surcharge pour mettre à jour l'icône du bouton en fonction de sa taille
        super.paint(g);
        if(this.img != null){
            System.out.println("draw dans le bouton "+this.getWidth()+" "+this.getHeight());
            Image imgB = this.img.getScaledInstance(this.getWidth(),this.getHeight(), Image.SCALE_DEFAULT); // L’image est automatiquement redimensionnée à la taille du bouton grâce à getScaledInstance(...)
            this.setIcon(new ImageIcon(imgB));
        }
    }
}
