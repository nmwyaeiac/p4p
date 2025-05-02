import java.awt.*;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */

/**
 *
 * Dialogue pour afficher les statistiques et scores des joueurs.
 * Cette classe permet de visualiser les performances des joueurs
 * sous forme de graphiques et statistiques diverses.
 * 
 * @author wassil
 */

public class ScoresDlg extends javax.swing.JDialog {
    private final LesJoueurs lj; // Liste des joueurs
    private final LesParties lp; // Liste des parties jouées
    private final PanneauImage panImage; // Panneau pour afficher les images des joueurs

    /**
     * Constructeur de la JDialogue
     * Initialise l'interface graphique et les données.
     * 
     * @param parent Fenêtre parente
     * @param modal Définit si le dialogue est modal
     * @param lj Liste des joueurs
     * @param lp Liste des parties
     */
    public ScoresDlg(java.awt.Frame parent, boolean modal, LesJoueurs lj, LesParties lp){
     super(parent, modal);
        initComponents();
        this.lj = lj;
        this.lp = lp;
        ImageIcon icon = new ImageIcon(getClass().getResource("/joueurDefaut.png")); //image par défaut
        Image img = icon.getImage();
        initNomJoueurs(); // Remplissage de la liste des joueurs disponible
        panImage = new PanneauImage(); // Création du panneau d'image
        PImage.add(panImage); // Ajout du panneau au conteneur
        panImage.setImage(img); // Affichage de l'image par défaut
        ChoixGraph.addItem("Statistiques Avancées"); // Ajout de l'option statistiques avancées au menu déroulant
    }
    
    /**
     * Initialise la liste des noms de joueurs dans l'interface.
     * Remplit la JList avec les pseudos de tous les joueurs.
     */
    private void initNomJoueurs(){
        DefaultListModel nj = new DefaultListModel(); // Modèle pour la liste
        NomJoueurs.setModel(nj); // Association du modèle à la JList
        for(int i=0; i<this.lj.getNbJoueurs(); i++){
            nj.addElement(this.lj.getJoueur(i).getPseudo()); // Ajout de chaque pseudo à la liste
        }
    }
    /**
     * Surcharge de la méthode paint pour redessiner la fenêtre.
     * Appelée lors d'un rafraîchissement de l'interface.
     * 
     * @param g Contexte graphique
     */
    @Override
    public void paint(Graphics g){
        // redessiner la fenêtre lors d'un rafraichissement
        // elle peut être appellée par la méthode repaint()
        super.paint(g); // appelle la méthode de la classe JDialog
        dessineResPartG(); // pour dessiner l'histogramme à l'ouverture de la JDialog
    }
     /**
     * Dessine un camembert montrant les résultats de toutes les parties.
     * Affiche la répartition entre victoires J1, J2 et matchs nuls.
     */
    private void dessineResPartG(){
        Graphics g = PanGraph.getGraphics(); // Récupère le contexte graphique du panneau
        g.clearRect(0, 0, PanGraph.getWidth(), PanGraph.getHeight()); // Efface la zone de dessin
        
        // Initialisation des compteurs
        int nbPGagnéesJ1 = 0; // Parties gagnées par J1
        int nbPGagnéesJ2 = 0; // Parties gagnées par J2
        int nbPNulles = 0;    // Parties nulles
        
        // Calcul des statistiques à partir des données
        for(int i=0; i<lp.getNbPartie(); i++){
            int res = lp.getPartie(i).getRes(); // Récupère le résultat de la partie
            switch (res) {
                case 1 -> nbPGagnéesJ1++; // Incrémente le compteur J1
                case 2 -> nbPGagnéesJ2++; // Incrémente le compteur J2
                case 0 -> nbPNulles++;   // Incrémente le compteur des parties nulles
                default -> {
                }
            }
        }
        
        int total = nbPGagnéesJ1 + nbPGagnéesJ2 + nbPNulles; // Total des parties
        int[] valeurs = {nbPGagnéesJ1, nbPGagnéesJ2, nbPNulles}; // Tableau des valeurs
        String[] labels = {"Joueur 1 victoires", "Joueur 2 victoires", "Matchs nuls"}; // Légendes
        Color[] couleurs = {Color.RED, new java.awt.Color(101,52,0), Color.GRAY}; // Couleurs des secteurs
        
        // Calcul des dimensions du graphique
        int panelWidth = PanGraph.getWidth();
        int panelHeight = PanGraph.getHeight();
        int diametre = Math.min(panelWidth / 2, panelHeight) - 20; // Diamètre adapté
        int x = 20; // Position x
        int y = (panelHeight - diametre) / 2; // Position y centrée
        
        if (total > 0) {
            // Dessin des secteurs du camembert
            int startAngle = 0;
            for (int i = 0; i < valeurs.length; i++) {
                if (valeurs[i] > 0) { // Ne dessine que les valeurs positives
                    int arcAngle = (int) Math.round((double) valeurs[i] * 360 / total); // Calcul de l'angle
                    g.setColor(couleurs[i]); // Définition de la couleur
                    g.fillArc(x, y, diametre, diametre, startAngle, arcAngle); // Dessin du secteur
                    startAngle += arcAngle; // Mise à jour de l'angle de départ
                }
            }
            
            // Position de la légende
            int legendX = x + diametre + 30;
            int legendY = y;
            
            // Titre de la légende
            g.setColor(Color.BLACK);
            Font defaultFont = g.getFont();
            Font boldFont = new Font(defaultFont.getName(), Font.BOLD, 14);
            g.setFont(boldFont);
            g.drawString("Légende", legendX, legendY);
            g.setFont(defaultFont);
            
            // Contenu de la légende
            for (int i = 0; i < valeurs.length; i++) {
                g.setColor(couleurs[i]); // Couleur du carré de légende
                g.fillRect(legendX, legendY + 25 + i * 30, 15, 15); // Dessin du carré
                g.setColor(Color.BLACK); // Couleur du texte
                g.drawString(labels[i] + " : " + valeurs[i], legendX + 25, legendY + 25 + i * 30 + 12); // Texte
            }
            
            // Affichage du total
            g.drawString("Total des parties : " + total, legendX, legendY + 25 + valeurs.length * 30 + 20);
        } else {
            // Message si aucune partie
            g.setColor(Color.BLACK);
            g.drawString("Aucune partie à afficher", panelWidth / 2 - 80, panelHeight / 2);
        }
    }


    /**
     * Dessine un camembert des résultats des parties d'un joueur spécifique.
     * Affiche la répartition entre victoires, défaites et matchs nuls.
     * 
     * @param indice Indice du joueur dans la liste
     */
    private void dessineResPartJoueur(int indice){
        Graphics g = PanGraph.getGraphics(); // Contexte graphique
        g.clearRect(0, 0, PanGraph.getWidth(), PanGraph.getHeight()); // Efface la zone
        
        Joueur j = lj.getJoueur(indice); // Joueur sélectionné
        int gagnées = 0; // Compteur de parties gagnées
        int perdues = 0;  // Compteur de parties perdues
        int nulles = 0;   // Compteur de parties nulles
        
        // Calcul des statistiques du joueur
        for(int i=0; i<lp.getNbPartie(); i++){
            Partie p = lp.getPartie(i); // Partie courante
            int res = p.getRes(); // Résultat de la partie
            
            // Si le joueur est J1
            if(j.equals(p.getJ1())){
                switch (res) {
                    case 1 -> gagnées++; // Victoire
                    case 2 -> perdues++; // Défaite
                    case 0 -> nulles++;  // Nul
                    default -> {
                    }
                }
            } 
            // Si le joueur est J2
            else if(j.equals(p.getJ2())){
                switch (res) {
                    case 2 -> gagnées++; // Victoire
                    case 1 -> perdues++; // Défaite
                    case 0 -> nulles++;  // Nul
                }
            }
        }
        
        int total = gagnées + perdues + nulles; // Total des parties du joueur
        
        if(total == 0){
            // Message si aucune partie
            g.setColor(Color.BLACK);
            g.drawString("Aucune partie à afficher", PanGraph.getWidth() / 2 - 80, PanGraph.getHeight() / 2);
        } else {
            // Préparation des données pour le graphique
            int[] valeurs = {gagnées, nulles, perdues}; // Valeurs à afficher
            String[] labels = {"Parties gagnées", "Parties nulles", "Parties perdues"}; // Légendes
            Color[] couleurs = {Color.GREEN, Color.ORANGE, Color.RED}; // Couleurs
            
            // Dimensions du graphique
            int panelWidth = PanGraph.getWidth();
            int panelHeight = PanGraph.getHeight();
            int diametre = Math.min(panelWidth / 2, panelHeight) - 20; // Diamètre réduit
            int x = 20; // Position x
            int y = (panelHeight - diametre) / 2; // Position y centrée
            
            // Dessin des secteurs
            int startAngle = 0;
            for (int i = 0; i < valeurs.length; i++) {
                if (valeurs[i] > 0) { // Ne dessine que si valeur positive
                    int arcAngle = (int) Math.round((double) valeurs[i] * 360 / total); // Angle du secteur
                    g.setColor(couleurs[i]); // Couleur
                    g.fillArc(x, y, diametre, diametre, startAngle, arcAngle); // Dessin
                    startAngle += arcAngle; // Mise à jour de l'angle
                }
            }
            
            // Légende
            int legendX = x + diametre + 30;
            int legendY = y;
            
            // Titre de la légende
            g.setColor(Color.BLACK);
            Font defaultFont = g.getFont();
            Font boldFont = new Font(defaultFont.getName(), Font.BOLD, 14);
            g.setFont(boldFont);
            g.drawString("Légende", legendX, legendY);
            g.setFont(defaultFont);
            
            // Contenu de la légende
            for (int i = 0; i < valeurs.length; i++) {
                g.setColor(couleurs[i]); // Couleur du carré
                g.fillRect(legendX, legendY + 25 + i * 30, 15, 15); // Carré
                g.setColor(Color.BLACK); // Couleur du texte
                g.drawString(labels[i] + " : " + valeurs[i], legendX + 25, legendY + 25 + i * 30 + 12); // Texte
            }
            
            // Total
            g.drawString("Total des parties : " + total, legendX, legendY + 25 + valeurs.length * 30 + 20);
        }
    }

   /**
     * Dessine un camembert montrant le pourcentage de victoires d'un joueur.
     * Affiche la proportion entre victoires et défaites/nulles.
     * 
     * @param index Indice du joueur dans la liste
     */
    private void dessinePourcentageVictoire(int index){
        Graphics g = PanGraph.getGraphics(); // Contexte graphique
        g.clearRect(0, 0, PanGraph.getWidth(), PanGraph.getHeight()); // Effacement
        
        Joueur j = lj.getJoueur(index); // Joueur sélectionné
        int gagnées = 0; // Compteur de victoires
        int total = 0;   // Compteur total de parties
        
        // Parcours des parties pour calculer les statistiques
        for(int i=0; i<lp.getNbPartie(); i++) {
            Partie p = lp.getPartie(i); // Partie courante
            int res = p.getRes(); // Résultat
            
            // Si le joueur est J1
            if(j.equals(p.getJ1())){
                total++; // Incrémente le total
                if(res == 1){
                    gagnées++; // Incrémente les victoires si gagné
                }
            }
            // Si le joueur est J2
            else if(j.equals(p.getJ2())){
                total++; // Incrémente le total
                if(res == 2){
                    gagnées++; // Incrémente les victoires si gagné
                }
            }
        }
        
        if(total == 0){
            // Message si aucune partie
            g.setColor(Color.BLACK);
            g.drawString("Aucune partie à afficher", PanGraph.getWidth() / 2 - 80, PanGraph.getHeight() / 2);
        } else {
            int pourcentage = (int) ((gagnées * 100.0) / total); // Calcul du pourcentage
            int perdues = total - gagnées; // Calcul des non-victoires
            
            // Dimensions du graphique
            int panelWidth = PanGraph.getWidth();
            int panelHeight = PanGraph.getHeight();
            int diametre = Math.min(panelWidth / 2, panelHeight) - 20; // Diamètre
            int x = 20; // Position x
            int y = (panelHeight - diametre) / 2; // Position y
            
            // Données pour le camembert
            Color[] couleurs = {Color.CYAN, Color.LIGHT_GRAY};
            String[] labels = {"Victoires", "Défaites/Nulles"};
            int[] valeurs = {gagnées, perdues};
            
            // Dessin du camembert
            int startAngle = 0;
            for (int i = 0; i < valeurs.length; i++) {
                if (valeurs[i] > 0) { // Ne dessine que si valeur positive
                    int arcAngle = (int) Math.round((double) valeurs[i] * 360 / total); // Angle
                    g.setColor(couleurs[i]); // Couleur
                    g.fillArc(x, y, diametre, diametre, startAngle, arcAngle); // Dessin
                    startAngle += arcAngle; // Mise à jour de l'angle
                }
            }
            
            // Légende
            int legendX = x + diametre + 30;
            int legendY = y;
            
            // Titre de la légende
            g.setColor(Color.BLACK);
            Font defaultFont = g.getFont();
            Font boldFont = new Font(defaultFont.getName(), Font.BOLD, 14);
            g.setFont(boldFont);
            g.drawString("Légende", legendX, legendY);
            g.setFont(defaultFont);
            
            // Contenu de la légende
            for (int i = 0; i < valeurs.length; i++) {
                g.setColor(couleurs[i]); // Couleur du carré
                g.fillRect(legendX, legendY + 25 + i * 30, 15, 15); // Carré
                g.setColor(Color.BLACK); // Couleur du texte
                // Affiche le pourcentage correspondant
                if (i == 0) {
                    g.drawString(labels[i] + " : " + pourcentage + "%", legendX + 25, legendY + 25 + i * 30 + 12);
                } else {
                    g.drawString(labels[i] + " : " + (100 - pourcentage) + "%", legendX + 25, legendY + 25 + i * 30 + 12);
                }
            }
            
            // Informations supplémentaires
            g.drawString("Total : " + total, legendX, legendY + 25 + valeurs.length * 30 + 20);
            g.drawString("Victoires : " + gagnées, legendX, legendY + 25 + valeurs.length * 30 + 40);
        }
    }
   


    /**
     * Affiche des statistiques avancées pour un joueur sélectionné.
     * Présente des métriques détaillées comme le ratio victoires/défaites,
     * la performance contre différentes difficultés, et une visualisation
     * de la progression du niveau.
     * 
     * @param index Indice du joueur dans la liste
     */
    private void dessineStatistiquesAvancees(int index) {
        Graphics g = PanGraph.getGraphics(); // Contexte graphique
        g.clearRect(0, 0, PanGraph.getWidth(), PanGraph.getHeight()); // Effacement
        
        Joueur j = lj.getJoueur(index); // Joueur sélectionné
        
        // Initialisation des compteurs statistiques
        int victoires = 0;
        int defaites = 0;
        int nulles = 0;
        int totalCoups = 0;
        int nbParties = 0;
        int plusLongueSerieVictoires = 0;
        int serieVictoireActuelle = 0;
        int partiesContreDifficulte1 = 0;
        int partiesContreDifficulte5plus = 0;
        int victoiresContreDifficulte1 = 0;
        int victoiresContreDifficulte5plus = 0;
        
        // Parcours des parties pour calculer les statistiques
        for(int i=0; i<lp.getNbPartie(); i++) {
            Partie p = lp.getPartie(i); // Partie courante
            boolean estPartieDuJoueur = false;
            boolean victoire = false;
            
            // Si le joueur est J1
            if(j.equals(p.getJ1())){
                estPartieDuJoueur = true;
                if(p.getRes() == 1){
                    victoires++; // Incrémente victoires
                    victoire = true;
                } else if(p.getRes() == 2){
                    defaites++; // Incrémente défaites
                } else {
                    nulles++; // Incrémente nulles
                }
                
                // Analyse du niveau de l'adversaire
                int niveauAdversaire = p.getJ2().getNiveau();
                if(niveauAdversaire == 1){
                    partiesContreDifficulte1++;
                    if(p.getRes() == 1) victoiresContreDifficulte1++;
                } else if(niveauAdversaire >= 5){
                    partiesContreDifficulte5plus++;
                    if(p.getRes() == 1) victoiresContreDifficulte5plus++;
                }
            }
            // Si le joueur est J2
            else if(j.equals(p.getJ2())){
                estPartieDuJoueur = true;
                if(p.getRes() == 2){
                    victoires++; // Incrémente victoires
                    victoire = true;
                } else if(p.getRes() == 1){
                    defaites++; // Incrémente défaites
                } else {
                    nulles++; // Incrémente nulles
                }
                
                // Analyse du niveau de l'adversaire
                int niveauAdversaire = p.getJ1().getNiveau();
                if(niveauAdversaire == 1){
                    partiesContreDifficulte1++;
                    if(p.getRes() == 2) victoiresContreDifficulte1++;
                } else if(niveauAdversaire >= 5){
                    partiesContreDifficulte5plus++;
                    if(p.getRes() == 2) victoiresContreDifficulte5plus++;
                }
            }
            
            // Si la partie concerne le joueur
            if(estPartieDuJoueur) {
                nbParties++; // Incrémente le nombre de parties
                totalCoups += p.getNbCoups(); // Ajoute le nombre de coups
                
                // Gestion des séries de victoires
                if(victoire) {
                    serieVictoireActuelle++;
                    if(serieVictoireActuelle > plusLongueSerieVictoires) {
                        plusLongueSerieVictoires = serieVictoireActuelle;
                    }
                } else {
                    serieVictoireActuelle = 0;
                }
            }
        }
        
        // Calcul des statistiques dérivées
        double ratioVictoiresDefaites = defaites == 0 ? victoires : (double) victoires / defaites;
        double moyenneCoups = nbParties == 0 ? 0 : (double) totalCoups / nbParties;
        double tauxVictoireDebutants = partiesContreDifficulte1 == 0 ? 0 : (double) victoiresContreDifficulte1 / partiesContreDifficulte1 * 100;
        double tauxVictoireExperts = partiesContreDifficulte5plus == 0 ? 0 : (double) victoiresContreDifficulte5plus / partiesContreDifficulte5plus * 100;
        
        // Configuration de l'affichage
        g.setColor(Color.BLACK);
        Font defaultFont = g.getFont();
        Font titleFont = new Font(defaultFont.getName(), Font.BOLD, 16);
        Font sectionFont = new Font(defaultFont.getName(), Font.BOLD, 14);
        
        int yPos = 30; // Position verticale initiale
        int xPos = 20; // Position horizontale initiale
        int lineHeight = 20; // Hauteur de ligne
        int sectionSpacing = 10; // Espacement entre sections
        
        // Titre principal
        g.setFont(titleFont);
        g.drawString("Statistiques avancées pour " + j.getPseudo(), xPos, yPos);
        yPos += lineHeight + sectionSpacing;
        
        // Section Performance générale
        g.setFont(sectionFont);
        g.drawString("Performance générale", xPos, yPos);
        yPos += lineHeight;
        g.setFont(defaultFont);
        // Affichage des statistiques générales
        g.drawString("Parties: " + nbParties + " (V: " + victoires + ", D: " + defaites + ", N: " + nulles + ")", xPos, yPos);
        yPos += lineHeight;
        g.drawString("Ratio V/D: " + String.format("%.2f", ratioVictoiresDefaites), xPos, yPos);
        yPos += lineHeight;
        g.drawString("Plus longue série de victoires: " + plusLongueSerieVictoires, xPos, yPos);
        yPos += lineHeight;
        g.drawString("Moyenne de coups par partie: " + String.format("%.1f", moyenneCoups), xPos, yPos);
        yPos += lineHeight + sectionSpacing;
        
        // Section Performance par niveau
        g.setFont(sectionFont);
        g.drawString("Performance par niveau d'adversaire", xPos, yPos);
        yPos += lineHeight;
        g.setFont(defaultFont);
        // Affichage des statistiques par niveau
        g.drawString("Contre débutants (niveau 1): " + 
                    String.format("%.1f", tauxVictoireDebutants) + "% de victoires", xPos, yPos);
        yPos += lineHeight;
        g.drawString("Contre experts (niveau 5+): " + 
                    String.format("%.1f", tauxVictoireExperts) + "% de victoires", xPos, yPos);
        yPos += lineHeight + sectionSpacing;
        
        // Section Niveau et progression
        g.setFont(sectionFont);
        g.drawString("Niveau et progression", xPos, yPos);
        yPos += lineHeight;
        g.setFont(defaultFont);
        g.drawString("Niveau actuel: " + j.getNiveau(), xPos, yPos);
        yPos += lineHeight;
        
        // Graphique de progression du niveau
        g.drawString("Progression du niveau (simulation):", xPos, yPos);
        yPos += lineHeight;
        
        // Dimensions du graphique
        int graphWidth = PanGraph.getWidth() - 40;
        int graphHeight = 60;
        
        // Fond du graphique
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(xPos, yPos, graphWidth, graphHeight);
        g.setColor(Color.BLACK);
        g.drawRect(xPos, yPos, graphWidth, graphHeight);
        
        // Configuration des barres
        int barWidth = 25; // Largeur des barres
        int gap = 5; // Espace entre les barres
        int totalWidth = 10 * barWidth + 9 * gap; // Largeur totale (10 barres + 9 espaces)
        int startBarX = xPos + (graphWidth - totalWidth) / 2; // Position x de départ
        int niveau = j.getNiveau(); // Niveau actuel du joueur
        
        // Dessin des barres de niveau
        for(int i = 0; i < 10; i++) {
            int barX = startBarX + i * (barWidth + gap); // Position x de la barre
            int barHeight = 0; // Hauteur initiale à 0
            
            // Détermine la hauteur en fonction du niveau
            if(i < niveau) {
                barHeight = graphHeight - 10; // Barre complète pour niveaux acquis
            } else if(i == niveau) {
                barHeight = (int)((graphHeight - 10) * 0.7); // Barre partielle pour niveau actuel
            }
            
            // Dessine la barre si elle a une hauteur
            if(barHeight > 0) {
                g.setColor(new Color(65, 105, 225)); // Couleur bleue
                g.fillRect(barX, yPos + graphHeight - barHeight - 5, barWidth, barHeight); // Remplissage
                g.setColor(Color.BLACK); // Couleur noire pour le contour
                g.drawRect(barX, yPos + graphHeight - barHeight - 5, barWidth, barHeight); // Contour
            }
        }
        
        // Légende des niveaux
        yPos += graphHeight + lineHeight;
        
        // Numéros de niveau sous les barres
        for(int i = 0; i < 10; i++) {
            int numX = startBarX + i * (barWidth + gap) + barWidth/2 - 3; // Position x centrée
            g.drawString(String.valueOf(i+1), numX, yPos); // Affichage du numéro
        }
        
        // Étiquette de l'axe
        yPos += lineHeight;
        g.drawString("Niveau", xPos + graphWidth/2 - 20, yPos);
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
      int ind = this.NomJoueurs.getSelectedIndex();
    if(ind>=0){
        panImage.setImage(this.lj.getJoueur(ind).getPhoto().getImage());
        String choix = (String) ChoixGraph.getSelectedItem();
          switch (choix) {
              case "Resultat des Parties" -> dessineResPartJoueur(ind);
              case "Pourcentage de Victoire" -> dessinePourcentageVictoire(ind);
              case "Statistiques Avancées" -> dessineStatistiquesAvancees(ind);
              default -> {
              }
          }
    }
    }//GEN-LAST:event_BVisualiserActionPerformed

    private void ChoixGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChoixGraphActionPerformed
     int ind = this.NomJoueurs.getSelectedIndex();
    if(ind>=0){
        String choix = (String) ChoixGraph.getSelectedItem();
        if(choix.equals("Resultat des Parties")){
            dessineResPartJoueur(ind);
        }
        else if(choix.equals("Pourcentage de Victoire")){
            dessinePourcentageVictoire(ind);
        }
        else if(choix.equals("Statistiques Avancées")){
            dessineStatistiquesAvancees(ind);
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
