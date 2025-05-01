import java.awt.*;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */

/**
 *
 * @author wassil
 */
public class ScoresDlg extends javax.swing.JDialog {
    private final LesJoueurs lj; // attribut de type LesJoueurs pour stocker la liste des joueurs 
    private final LesParties lp; // attribut de type LesParties pour stocker la liste des parties jouées
    private final PanneauImage panImage; // on créer un PanneauImage pour un bonne affichage des Images

    /**
     * Creates new form ScoresDlg
     * 
     */
    public ScoresDlg(java.awt.Frame parent, boolean modal, LesJoueurs lj, LesParties lp){
        super(parent, modal);
        initComponents();
        this.lj = lj;
        this.lp = lp;
        ImageIcon icon = new ImageIcon(getClass().getResource("/joueurDefaut.png")); // on récupère l'image par défaut(joueurDefaut.png)
        Image img = icon.getImage();
        initNomJoueurs(); // pour remplir JList des pseudos
        panImage = new PanneauImage(); // création du nouveau PanneauImage(panImage)
        PImage.add(panImage); // ajoute le Panneau Image au Panel PPhoto
        panImage.setImage(img); // on ajoute l'image par défaut pour montrer que personne n'est sélectionné
    }
    
    private void initNomJoueurs(){ // rempli la JList(NomJoueurs) avec les pseudos de tout les joueurs
        DefaultListModel nj = new DefaultListModel(); // usage d'un modèle pour remplir la JList
        NomJoueurs.setModel(nj);
        for(int i=0; i<this.lj.getNbJoueurs(); i++){
            nj.addElement(this.lj.getJoueur(i).getPseudo());
        }
    }
    
    @Override
    public void paint(Graphics g){
        // redessiner la fenêtre lors d'un rafraichissement
        // elle peut être appellée par la méthode repaint()
        super.paint(g); // appelle la méthode de la classe JDialog
        dessineResPartG(); // pour dessiner l'histogramme à l'ouverture de la JDialog
    }
    
private void dessineResPartG(){ // qui trace le camembert des résultats de toutes les parties
    Graphics g = PanGraph.getGraphics(); // Graphics contient les méthodes de dessin et PanGraph JPanel de dessin du camembert
    g.clearRect(0, 0, PanGraph.getWidth(), PanGraph.getHeight()); // efface la zone de dessin
    // Initialisation des compteurs
    int nbPGagnéesJ1 = 0; // compte le nombre de parties gagnées par le joueur 1
    int nbPGagnéesJ2 = 0; // compte le nombre de parties gagnées par le joueur 2
    int nbPNulles = 0; // compte le nombre de parties nulles
    for(int i=0; i<lp.getNbPartie(); i++){ // boucle pour remplir les compteurs à partir de toutes les parties enregistrées
        int res = lp.getPartie(i).getRes(); // récupère le résultat de la partie : 1 = J1 gagné, 2 = J2 gagné, 0 = match nul
        if(res == 1){
            nbPGagnéesJ1++; // J1 a gagné
        }
        else if(res == 2){
            nbPGagnéesJ2++; // J2 a gagné
        }
        else if(res == 0){
            nbPNulles++; // Nul
        }
    }
    
    int total = nbPGagnéesJ1 + nbPGagnéesJ2 + nbPNulles;
    int[] valeurs = {nbPGagnéesJ1, nbPGagnéesJ2, nbPNulles}; // tableau des valeurs à afficher
    String[] labels = {"Joueur 1 victoires", "Joueur 2 victoires", "Matchs nuls"}; // message(legende) pour chaque secteur
    Color[] couleurs = {Color.RED, new java.awt.Color(101,52,0), Color.GRAY}; // couleur des secteurs (on utilise du marron pour le joueur 2)
    
    // CORRECTION: Calculs pour le bon affichage du camembert avec des dimensions adaptées
    int panelWidth = PanGraph.getWidth();
    int panelHeight = PanGraph.getHeight();
    
    // Réduire la taille du camembert pour laisser de la place à la légende
    int diametre = Math.min(panelWidth / 2, panelHeight) - 20; 
    int x = 20; // position x fixée à gauche avec une marge
    int y = (panelHeight - diametre) / 2; // position y centrée
    
    if (total > 0) {
        int startAngle = 0;
        for (int i = 0; i < valeurs.length; i++) {
            if (valeurs[i] > 0) { // On ne dessine que si la valeur est > 0
                int arcAngle = (int) Math.round((double) valeurs[i] * 360 / total);
                g.setColor(couleurs[i]);
                g.fillArc(x, y, diametre, diametre, startAngle, arcAngle);
                startAngle += arcAngle;
            }
        }
        
        // CORRECTION: Repositionner la légende sur le côté avec assez d'espace
        int legendX = x + diametre + 30; // augmenter l'espacement entre camembert et légende
        int legendY = y;
        
        // Titre de la légende
        g.setColor(Color.BLACK);
        Font defaultFont = g.getFont();
        Font boldFont = new Font(defaultFont.getName(), Font.BOLD, 14);
        g.setFont(boldFont);
        g.drawString("Légende", legendX, legendY);
        g.setFont(defaultFont);
        
        // Contenu de la légende avec espacement vertical amélioré
        for (int i = 0; i < valeurs.length; i++) {
            g.setColor(couleurs[i]);
            g.fillRect(legendX, legendY + 25 + i * 30, 15, 15); // Augmenter l'espacement vertical
            g.setColor(Color.BLACK);
            g.drawString(labels[i] + " : " + valeurs[i], legendX + 25, legendY + 25 + i * 30 + 12);
        }
        
        // Ajouter le nombre total de parties avec un meilleur espacement
        g.drawString("Total des parties : " + total, legendX, legendY + 25 + valeurs.length * 30 + 20);
    } else {
        // Si aucune partie n'a été jouée
        g.setColor(Color.BLACK);
        g.drawString("Aucune partie à afficher", panelWidth / 2 - 80, panelHeight / 2);
    }
}

// Méthode corrigée pour dessineResPartJoueur
private void dessineResPartJoueur(int indice){ // qui trace le camembert des résultats de toutes les parties d'un joueur
    Graphics g = PanGraph.getGraphics(); // Graphics contient les méthodes de dessin et PanGraph JPanel de dessin du camembert
    g.clearRect(0, 0, PanGraph.getWidth(), PanGraph.getHeight()); // efface la zone de dessin
    Joueur j = lj.getJoueur(indice); // récupère le joueur correspondant à l'index sélectionné
    int gagnées = 0; 
    int perdues = 0; 
    int nulles = 0;
    for(int i=0; i<lp.getNbPartie(); i++){ // parcours toutes les parties pour calculer les statistiques du joueur
        Partie p = lp.getPartie(i); // récupère la partie à l'indice i
        int res = p.getRes(); // résultat de la partie
        if(j.equals(p.getJ1())){ // si le joueur 1 est dans cette partie
            if(res == 1){ // Il a gagné
                gagnées++;
            }
            else if(res == 2){ // Il a perdu
                perdues++; 
            }
            else if(res == 0){ // Nul
                nulles++;
            }
        } 
        else if(j.equals(p.getJ2())){ // si le joueur est le joueur 2
            switch (res) {
                case 2 -> // Il a gagné
                    gagnées++;
                case 1 -> // Il a perdu
                    perdues++;
                case 0 -> // Nul
                    nulles++;
                default -> {
                }
            }
        }
    }
    
    int total = gagnées + perdues + nulles; // nombre total de parties jouées
    
    if(total == 0){ // si aucune partie jouée
        g.setColor(Color.BLACK); // texte en noir
        g.drawString("Aucune partie à afficher", PanGraph.getWidth() / 2 - 80, PanGraph.getHeight() / 2); // on affiche ce message
    } else {
        int[] valeurs = {gagnées, nulles, perdues}; // tableau des valeurs à afficher
        String[] labels = {"Parties gagnées", "Parties nulles", "Parties perdues"}; // message(legende) pour chaque secteur
        Color[] couleurs = {Color.GREEN, Color.ORANGE, Color.RED}; // couleur des secteurs
        
        // CORRECTION: Calculs pour le bon affichage du camembert
        int panelWidth = PanGraph.getWidth();
        int panelHeight = PanGraph.getHeight();
        
        int diametre = Math.min(panelWidth / 2, panelHeight) - 20; // Diamètre réduit du camembert
        int x = 20; // position x fixée à gauche
        int y = (panelHeight - diametre) / 2; // position y centrée
        
        int startAngle = 0;
        for (int i = 0; i < valeurs.length; i++) {
            if (valeurs[i] > 0) { // On ne dessine que si la valeur est > 0
                int arcAngle = (int) Math.round((double) valeurs[i] * 360 / total);
                g.setColor(couleurs[i]);
                g.fillArc(x, y, diametre, diametre, startAngle, arcAngle);
                startAngle += arcAngle;
            }
        }
        
        // CORRECTION: Repositionner la légende sur le côté droit
        int legendX = x + diametre + 30;
        int legendY = y;
        
        // Titre de la légende
        g.setColor(Color.BLACK);
        Font defaultFont = g.getFont();
        Font boldFont = new Font(defaultFont.getName(), Font.BOLD, 14);
        g.setFont(boldFont);
        g.drawString("Légende", legendX, legendY);
        g.setFont(defaultFont);
        
        // Contenu de la légende avec espacement vertical amélioré
        for (int i = 0; i < valeurs.length; i++) {
            g.setColor(couleurs[i]);
            g.fillRect(legendX, legendY + 25 + i * 30, 15, 15); // Augmenter l'espacement vertical
            g.setColor(Color.BLACK);
            g.drawString(labels[i] + " : " + valeurs[i], legendX + 25, legendY + 25 + i * 30 + 12);
        }
        
        // Ajouter le nombre total de parties avec un meilleur espacement
        g.drawString("Total des parties : " + total, legendX, legendY + 25 + valeurs.length * 30 + 20);
    }
}

// Méthode corrigée pour dessinePourcentageVictoire
private void dessinePourcentageVictoire(int index){ // qui trace le pourcentage de victoire d'un joueur par rapport à ses parties jouées 
    Graphics g = PanGraph.getGraphics(); // Graphics contient les méthodes de dessin et PanGraph JPanel de dessin
    g.clearRect(0, 0, PanGraph.getWidth(), PanGraph.getHeight()); // efface la zone de dessin
    Joueur j = lj.getJoueur(index); // récupère le joueur sélectionné
    int gagnées = 0; // nombre de victoires
    int total = 0; // nombre total de parties jouées par ce joueur
    for(int i=0; i<lp.getNbPartie(); i++) { // on parcourt toutes les parties enregistrées du joueur
        Partie p = lp.getPartie(i); // récupère la partie d'indice i
        int res = p.getRes(); // récupère le résultat de cette partie
        if(j.equals(p.getJ1())){ // si le joueur est le joueur 1
            total++; // il a joué cette partie donc on augmente son nombres de parties jouées
            if(res == 1){ // et s'il a gagné on augmente son nombre de victoire
                gagnées++; 
            }
        }
        else if(j.equals(p.getJ2())){ // Sinon si le joueur est le joueur 2
            total++; // il a joué cette partie donc on augmente son nombres de parties jouées
            if(res == 2){ // et s'il a gagné on augmente son nombre de victoire
                gagnées++;
            }
        }
    }
    
    if(total == 0){ // si aucune partie n'est jouée par le joueur
        g.setColor(Color.BLACK); // texte en noir
        g.drawString("Aucune partie à afficher", PanGraph.getWidth() / 2 - 80, PanGraph.getHeight() / 2); // on affiche ce message au centre du panneau
    } else {
        int pourcentage = (int) ((gagnées * 100.0) / total); // calcul du pourcentage
        int perdues = total - gagnées; // nombre de parties non gagnées
        
        // CORRECTION: Calculs pour le bon affichage du camembert
        int panelWidth = PanGraph.getWidth();
        int panelHeight = PanGraph.getHeight();
        
        int diametre = Math.min(panelWidth / 2, panelHeight) - 20; // Diamètre réduit
        int x = 20; // position x fixée à gauche
        int y = (panelHeight - diametre) / 2; // position y centrée
        
        // Dessiner le camembert avec deux secteurs : victoires et défaites/nulles
        Color[] couleurs = {Color.CYAN, Color.LIGHT_GRAY};
        String[] labels = {"Victoires", "Défaites/Nulles"};
        int[] valeurs = {gagnées, perdues};
        
        // Dessiner le camembert
        int startAngle = 0;
        for (int i = 0; i < valeurs.length; i++) {
            if (valeurs[i] > 0) {
                int arcAngle = (int) Math.round((double) valeurs[i] * 360 / total);
                g.setColor(couleurs[i]);
                g.fillArc(x, y, diametre, diametre, startAngle, arcAngle);
                startAngle += arcAngle;
            }
        }
        
        // CORRECTION: Dessiner la légende sur le côté droit avec un meilleur espacement
        int legendX = x + diametre + 30;
        int legendY = y;
        
        // Titre de la légende
        g.setColor(Color.BLACK);
        Font defaultFont = g.getFont();
        Font boldFont = new Font(defaultFont.getName(), Font.BOLD, 14);
        g.setFont(boldFont);
        g.drawString("Légende", legendX, legendY);
        g.setFont(defaultFont);
        
        // Contenu de la légende avec espacement vertical amélioré
        for (int i = 0; i < valeurs.length; i++) {
            g.setColor(couleurs[i]);
            g.fillRect(legendX, legendY + 25 + i * 30, 15, 15); // Augmenter l'espacement vertical
            g.setColor(Color.BLACK);
            if (i == 0) {
                g.drawString(labels[i] + " : " + pourcentage + "%", legendX + 25, legendY + 25 + i * 30 + 12);
            } else {
                g.drawString(labels[i] + " : " + (100 - pourcentage) + "%", legendX + 25, legendY + 25 + i * 30 + 12);
            }
        }
        
        // Ajouter des informations supplémentaires avec un meilleur espacement
        g.drawString("Total : " + total, legendX, legendY + 25 + valeurs.length * 30 + 20);
        g.drawString("Victoires : " + gagnées, legendX, legendY + 25 + valeurs.length * 30 + 40);
    }
}
   
       

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PHaut = new javax.swing.JPanel();
        LTitre = new javax.swing.JLabel();
        PCentre = new javax.swing.JPanel();
        PGauche = new javax.swing.JPanel();
        PGCentre = new javax.swing.JPanel();
        PImage = new javax.swing.JPanel();
        PGCBas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        NomJoueurs = new javax.swing.JList<>();
        PGBas = new javax.swing.JPanel();
        BVisualiser = new javax.swing.JButton();
        PDroit = new javax.swing.JPanel();
        BDHaut = new javax.swing.JPanel();
        LSelectionner = new javax.swing.JLabel();
        ChoixGraph = new javax.swing.JComboBox<>();
        PanGraph = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        LTitre.setText("Performances des Joueurs");
        PHaut.add(LTitre);

        getContentPane().add(PHaut, java.awt.BorderLayout.NORTH);

        PCentre.setLayout(new java.awt.GridLayout(1, 2));

        PGauche.setLayout(new java.awt.BorderLayout());

        PGCentre.setLayout(new java.awt.GridLayout(2, 1));

        PImage.setLayout(new java.awt.GridLayout(1, 1));
        PGCentre.add(PImage);

        PGCBas.setLayout(new java.awt.GridLayout(1, 1));

        jScrollPane1.setViewportView(NomJoueurs);

        PGCBas.add(jScrollPane1);

        PGCentre.add(PGCBas);

        PGauche.add(PGCentre, java.awt.BorderLayout.CENTER);

        BVisualiser.setText("Visualiser");
        BVisualiser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BVisualiserActionPerformed(evt);
            }
        });
        PGBas.add(BVisualiser);

        PGauche.add(PGBas, java.awt.BorderLayout.SOUTH);

        PCentre.add(PGauche);

        PDroit.setLayout(new java.awt.BorderLayout());

        LSelectionner.setText("Selectionner : ");
        BDHaut.add(LSelectionner);

        ChoixGraph.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Resultat des Parties", "Pourcentage de Victoire" }));
        ChoixGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChoixGraphActionPerformed(evt);
            }
        });
        BDHaut.add(ChoixGraph);

        PDroit.add(BDHaut, java.awt.BorderLayout.NORTH);

        javax.swing.GroupLayout PanGraphLayout = new javax.swing.GroupLayout(PanGraph);
        PanGraph.setLayout(PanGraphLayout);
        PanGraphLayout.setHorizontalGroup(
            PanGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 271, Short.MAX_VALUE)
        );
        PanGraphLayout.setVerticalGroup(
            PanGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 409, Short.MAX_VALUE)
        );

        PDroit.add(PanGraph, java.awt.BorderLayout.CENTER);

        PCentre.add(PDroit);

        getContentPane().add(PCentre, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BVisualiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BVisualiserActionPerformed
        // TODO add your handling code here:
        int ind = this.NomJoueurs.getSelectedIndex(); // récupère l'index du joueur sélectionné
        if(ind>=0){ // si un joueur est sélectionné on continue
            panImage.setImage(this.lj.getJoueur(ind).getPhoto().getImage()); // affiche sa photo dans le PanneauImage(panImage)
            String choix = (String) ChoixGraph.getSelectedItem(); // récupère le choix du graphique à partir de la JComboBox(ChoixGraph)
            if(choix.equals("Resultat des Parties")){ // selon le choix, on trace le graphique correspondant
                dessineResPartJoueur(ind);
            } 
            else if(choix.equals("Pourcentage de Victoire")){
                dessinePourcentageVictoire(ind);
            }
        }
    }//GEN-LAST:event_BVisualiserActionPerformed

    private void ChoixGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChoixGraphActionPerformed
        // TODO add your handling code here:
        int ind = this.NomJoueurs.getSelectedIndex(); // récupère l'index du joueur sélectionné
        if(ind>=0){ // si un joueur est sélectionné on continue
            String choix = (String) ChoixGraph.getSelectedItem(); // récupère le choix du graphique à partir de la JComboBox(ChoixGraph)
            if(choix.equals("Resultat des Parties")){ // selon le choix, on trace le graphique correspondant
                dessineResPartJoueur(ind); // affiche l'histogramme des résultats
            }
            else if(choix.equals("Pourcentage de Victoire")){
                dessinePourcentageVictoire(ind); // affiche l'histogramme du pourcentage
            }
        }
    }//GEN-LAST:event_ChoixGraphActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScoresDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScoresDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScoresDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScoresDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        LesJoueurs lj = new LesJoueurs();
        LesParties lp = new LesParties();
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ScoresDlg dialog = new ScoresDlg(new javax.swing.JFrame(), true, lj, lp);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BDHaut;
    private javax.swing.JButton BVisualiser;
    private javax.swing.JComboBox<String> ChoixGraph;
    private javax.swing.JLabel LSelectionner;
    private javax.swing.JLabel LTitre;
    private javax.swing.JList<String> NomJoueurs;
    private javax.swing.JPanel PCentre;
    private javax.swing.JPanel PDroit;
    private javax.swing.JPanel PGBas;
    private javax.swing.JPanel PGCBas;
    private javax.swing.JPanel PGCentre;
    private javax.swing.JPanel PGauche;
    private javax.swing.JPanel PHaut;
    private javax.swing.JPanel PImage;
    private javax.swing.JPanel PanGraph;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
