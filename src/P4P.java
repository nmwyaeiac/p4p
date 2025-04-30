/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
public class P4P extends javax.swing.JFrame {

    /**
     * Creates new form P4P
     */
    
    private LesJoueurs listeJ; // liste de tous les joueurs
    private LesParties lp; // liste de toutes les parties
    private Joueur joueur1;
    private Joueur joueur2;
    private Jeu lejeu;
    private int joueurCourant; // numéro du joueur courant vaut 1 pour joueur1 et 2 pour joueur2 
    private Plateau PJeu;
    private int nbgj1; // nombre de galets du joueur 1
    private int nbgj2; // nombre de galets du joueur 2
    private int xsel, ysel; // indices de la case sélectionnée par un joueur lors de son tour de jeu, case où il dépose son pion
    private Case caseCourante; // caseCourante - copie de la case sélectionnée pour le tour du joueur
    private int nbPionsDepot; // nombre de pions de cette caseCourante, identiquement le nombre de pions à égrainer
    private int xd, yd; // position où le joueur a effectué son dernier dépot
    private int pxd, pyd; // position précédente du dernier dépot
    private int compt; // compteur pour le nombre de coups d'une partie
    private boolean selection; // indicateur pour savoir si c'est le début du tour d'un joueur, ou en cours d'égrainage
    private boolean finPartie; // indicateur pour savoir si la partie est terminée
    // On remplace le JPanel par PanneauImage pour le redimensionnement
    private PanneauImage panPhotoJ1;
    private PanneauImage panPhotoJ2;
    
    public P4P(){
        initComponents();
        this.listeJ = new LesJoueurs();
        this.lp = new LesParties();
        this.listeJ.creationJoueursTest(this.lp);
        this.joueur1 = this.listeJ.getJoueur(0);
        this.joueur2 = this.listeJ.getJoueur(1);
        this.lejeu = new Jeu();
        this.PJeu = lejeu.getPlateau();
        initPanneau();
        affichePanneau();
        this.PJeu.afficheValPlateau(); // affichage sur la console pour contrôle des valeurs
        this.joueurCourant = 1;
        this.nbgj1 = 8;
        this.nbgj2 = 8;
        this.compt = 0;
        this.caseCourante = null;
        this.selection = false;
        this.finPartie = false;
        Message.setText("C'est au joueur 1 de jouer"); // Message du composant de type JTextField en bas de l'interface
        // Création des nouveaux panneaux image
        panPhotoJ1 = new PanneauImage();
        panPhotoJ2 = new PanneauImage();
        // Ajout d'une marge intérieure de 45px pour le bon espacement du PanneauImage lors de l'affichage des images
        panPhotoJ1.setBorder(BorderFactory.createEmptyBorder(45, 45, 45, 45));
        panPhotoJ2.setBorder(BorderFactory.createEmptyBorder(45, 45, 45, 45));
        // Supprime les composants des panneaux comportant les images et ajoute les PanneauImage
        PhotoJ1.removeAll();
        PhotoJ1.setLayout(new GridLayout(1,1));
        PhotoJ1.add(panPhotoJ1);
        PhotoJ2.removeAll();
        PhotoJ2.setLayout(new GridLayout(1,1));
        PhotoJ2.add(panPhotoJ2);
        afficheJoueurs();
    } 

    public void initPanneau(){
        PCentre.setLayout(new GridLayout(4, 4, 10, 10)); // Organisation en grille 4x4
        for(int i = 0; i<16 ; i++){ //sert à crée les boutons
            JPanel pan = new JPanel();
            pan.setLayout(new GridLayout(2,1));
            JButton BH = new JButton(); //a chaque itération on crée un bouton
            BH.setName(String.valueOf(i));
            BH.addActionListener(new java.awt.event.ActionListener(){ //on ajoute une action
                public void actionPerformed(ActionEvent evt){
                    traitementActionPerformed(evt); //quand on clique sur le bouton on appel la fonction
                }
            });
            pan.add(BH); //on ajoute le bouton au panneau centrale
            JPanel PB = new JPanel(); // Panneau vide sous le bouton
            pan.add(PB);
            PCentre.add(pan); // Ajout de l'ensemble (bouton + panneau) au panneau central
        }
    }
    
    public void afficheJoueurs(){
        LPseudoJ1.setText(this.joueur1.getPseudo());
        LPseudoJ2.setText(this.joueur2.getPseudo());
        panPhotoJ1.setImage(this.joueur1.getPhoto().getImage());
        panPhotoJ2.setImage(this.joueur2.getPhoto().getImage());
        BCoulJ1.setBackground(Color.red);
        BCoulJ2.setBackground(new Color(101,51,0)); // couleur Marron
    }
    
    private void affichePanneau(){
        for(int i=0; i<=3; i++){
            for(int j=0; j<=3; j++){
                int num = i*4+j; // Calcul du numéro de la case
                JPanel pan = (JPanel) PCentre.getComponent(num); // Récupération du panneau de la case
                JButton BH = (JButton) pan.getComponent(0); // Premier élément du panneau
                int val = PJeu.getSommetCase(i, j); // Récupération du sommet de la pile de pions
                if(val == -1){
                    BH.setBackground(Color.yellow);
                }
                if(val == 0){
                    BH.setBackground(Color.orange);
                }
                if(val == 1){
                    BH.setBackground(Color.red);
                }
                if(val == 2){
                    BH.setBackground(new java.awt.Color(101,52,0)); // Marron
                }
                // Récupération du panneau inférieur
                JPanel PB = (JPanel) pan.getComponent(1);
                PB.setBackground(Color.yellow);
                PB.removeAll(); // Supprime tous les composants existants
                
                // Récupération des pions empilés sur la case 
                Case c = PJeu.getCase(i, j);
                int nbPions = c.getNbPions();
                PB.setLayout(new GridLayout(1, Math.max(nbPions - 1, 1))); // Grille de 1 ligne et (nbPions-1) colonnes 
                // Ajout des boutons pour les pions (sauf le sommet)
                for(int k=0; k<nbPions-1; k++){ 
                    JButton btPion  = new JButton();
                    int pionVal = c.getValCase(k);
                    // Couleur en fonction du pion
                    if(pionVal == 0){
                        btPion.setBackground(Color.orange);
                    }
                    if(pionVal == 1){
                        btPion.setBackground(Color.red);
                    }
                    if(pionVal == 2){ 
                        btPion.setBackground(new java.awt.Color(101,52,0)); // marron
                    }
                    PB.add(btPion);
                }
            }
        }
        this.revalidate();
        this.repaint();
    }
    
    public void traitementActionPerformed(ActionEvent evt){
        if(!finPartie){
            JButton jb = (JButton) evt.getSource(); // Récupére le bouton cliqué
            int num = Integer.parseInt(jb.getName());
            int x = num/4;
            int y = num-x*4;
            MessageErreur.setText(""); // JLabel nommé MessageErreur à côté de la zone de Message pour afficher des messages d'erreurs pendant le jeu
            if(this.selection == false){
                if(PJeu.getCase(x, y).estVide()){
                    this.MessageErreur.setText("Case Vide !"); // Il est interdit de déposer un pion sur une case vide
                }
                else{
                    this.compt++;
                    selection = true; //indicateur pour indiquer qu'on est est au cours d'un tour de jeu (phase d'engrainage)
                    this.xsel = x; this.ysel = y; // position initiale cliquée pour le dépôt du pion du joueur
                    this.xd = x; this.yd = y; // position de dépôt du galets (ici position initiale)
                    this.pxd = -1; this.pyd = -1; // position précédente du dernier dépôt (ici pas de précédent)
                    this.PJeu.deposePionCase(joueurCourant, xsel, ysel); // ajoute la valeur du joueur courant dans la liste des pions de la case
                    if(this.joueurCourant == 1){
                        this.nbgj1--;
                    }
                    else{
                        this.nbgj2--;
                    }
                    // on duplique la case sélectionnée dans la case courante
                    this.caseCourante = new Case();
                    for(int i=0; i<this.PJeu.getCase(xsel, ysel).getNbPions(); i++){
                        this.caseCourante.empilePion(this.PJeu.getCase(xsel, ysel).getValCase(i));
                    }
                    this.nbPionsDepot = this.caseCourante.getNbPions(); // nombre de pions sur cette case sélectionnée (y compris le pion que l'on vient de déposer)
                    affichePanneau(); // affichage du panneau avec ce pion
                    afficheCaseCourante(); // affichage en partie droite de la pile des galets qui seront à déposer (pour plus de facilité pour jouer)
                    this.PJeu.getCase(xsel, ysel).videCase(); // vide la case où on a déposé le nouveau pion, pour ensuite régrainer les pions              
                }
            }
            else{
                if(this.nbPionsDepot != 0){ // s'il reste des pions à poser
                    int xa = x; int ya = y; // (xa , ya) est la position choisie pour déposer (clic sur cette case par le joueur)
                    int val = this.caseCourante.getValCase(0); // récupération de la valeur du pion à déposer
                    int code;
                    code = this.lejeu.jouePion(pxd, pyd, xd, yd, xa, ya, val); // (pxd, pyd) position précédente du dépot d'un pion (xd, yd) position du pion que l'on à déposé (xa, ya) position futur pour dépot
                    if(code == 0){ // le pion à été déposé sur une case correcte (adjacente sans retour arrière)
                        this.nbPionsDepot--; // on décrémente le nombre de pions à déposer
                        this.caseCourante.defilePion(); // on supprime ce pion de la case initiale
                        pxd = xd; pyd = yd; xd = xa; yd = ya; // on remet à jour les positions
                        affichePanneau(); afficheCaseCourante(); // on réaffiche le jeu plateau et les galets à déposer
                        if(this.nbPionsDepot == 0){ // s'il n y a plus de pions
                            traiteFinTour(); // on traite la fin du tour (changement de joueur ou fin de partie)
                        }
                    }
                    else{
                        this.MessageErreur.setText("Mouvement impossible"); // il n'est pas sur une case correcte donc on affiche un message d'erreur
                    }
                }
            }
        }
        this.revalidate();
        this.repaint();
    }
    
    private void traiteFinTour(){
        this.selection = false;
        this.PJeu.afficheValPlateau(); // affichage console pour vérifier les valeurs du plateau (de type Plateau)
        if(this.lejeu.gagne(joueurCourant)){
            Message.setText("Joueur "+joueurCourant+" a gagné !"); // si le joueur 1 ou 2 gagne on affiche qu'il a gagné
            Partie part = new Partie(this.joueur1, this.joueur2, this.compt, this.joueurCourant); // on créer une nouvelle partie 
            this.lp.ajoutePartie(part); // on ajoute la partie créer dans la liste des parties
            this.listeJ.enregistrerResultat(this.joueur1, this.joueur2, part.getRes()); // enregistre les résultats de la partie dans la liste
            this.compt = 0; // on remet le compteur de coups à 0
            this.finPartie = true; // on indique que la partie est terminée      
        }
        else{
            if(this.joueurCourant==1){ // si le joueur courant est le joueur 1
                this.joueurCourant=2; // on fixe le joueur 2 comme joueur courant
                if(this.nbgj2 == 0){ // si le nombre de galets du joueur 2 est à 0
                    Partie part = new Partie(this.joueur1, this.joueur2, this.compt, this.joueurCourant); // on créer une nouvelle partie 
                    this.lp.ajoutePartie(part); // on ajoute la partie créer dans la liste des parties
                    this.listeJ.enregistrerResultat(this.joueur1, this.joueur2, 0); // enregistre les résultats de la partie dans la liste
                    this.finPartie = true; // on indique que la partie est terminée
                    Message.setText("Match nul"); // on affiche que c'est un match nul
                     
                }
                else{
                    Message.setText("C'est au joueur 2 de jouer"); // sinon on affiche que c'est au tour du joueur 2
                }
            }
            else{
                this.joueurCourant = 1; // sinon on fixe le joueur 1 comme joueur courant
                if(this.nbgj1 == 0){ // si le nombre de galets du joueur 1 est à 0
                    Partie part = new Partie(this.joueur1, this.joueur2, this.compt, this.joueurCourant); // on créer une nouvelle partie 
                    this.lp.ajoutePartie(part); // on ajoute la partie créer dans la liste des parties
                    this.listeJ.enregistrerResultat(this.joueur1, this.joueur2, 0); // enregistre les résultats de la partie dans la liste
                    this.finPartie = true; // on indique que la partie est terminée
                    Message.setText("Match nul"); // on affiche que c'est un match nul 
                }
                else{
                    Message.setText("C'est au joueur 1 de jouer"); // sinon on affiche que c'est au tour du joueur 1
                }
            }
        }
        affichePanneau(); 
    }
    
    private void afficheCaseCourante(){
        if(this.caseCourante != null){
            PPions.removeAll();
            int nbPions = this.nbPionsDepot;
            System.out.println(nbPions);
            PPions.setLayout(new GridLayout(nbPions, 1));
            for(int k=0; k<nbPions; k++){
                JButton panPion = new JButton();
                int v = this.caseCourante.getValCase(k);
                switch(v){
                    case 0:
                        panPion.setBackground(Color.orange);
                        break;
                    case 1:
                        panPion.setBackground(Color.red);
                        break;
                    case 2:
                        panPion.setBackground(new Color(101,52,0)); // marron
                        break;
                }
                PPions.add(panPion);
            }
        }
        else{
            PPions.removeAll();
            this.PPions.validate();
            this.PPions.repaint();
            this.setSize(800,600);
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

        PGauche = new javax.swing.JPanel();
        PJoueur1 = new javax.swing.JPanel();
        PInfo1 = new javax.swing.JPanel();
        LNomJ1 = new javax.swing.JLabel();
        LPseudoJ1 = new javax.swing.JLabel();
        BCoulJ1 = new javax.swing.JButton();
        PhotoJ1 = new javax.swing.JPanel();
        BPhotoJ1 = new javax.swing.JButton();
        PJoueur2 = new javax.swing.JPanel();
        PInfo2 = new javax.swing.JPanel();
        LNomJ2 = new javax.swing.JLabel();
        LPseudoJ2 = new javax.swing.JLabel();
        BCoulJ2 = new javax.swing.JButton();
        PhotoJ2 = new javax.swing.JPanel();
        BPhotoJ2 = new javax.swing.JButton();
        PBas = new javax.swing.JPanel();
        LMessage = new javax.swing.JLabel();
        Message = new javax.swing.JTextField();
        MessageErreur = new javax.swing.JLabel();
        PCentre = new javax.swing.JPanel();
        PDroit = new javax.swing.JPanel();
        LPPions = new javax.swing.JLabel();
        PPions = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        MJeu = new javax.swing.JMenu();
        Quitter = new javax.swing.JMenuItem();
        Rejouer = new javax.swing.JMenuItem();
        MJoueurs = new javax.swing.JMenu();
        AjouterJoueur = new javax.swing.JMenuItem();
        VisualiserJoueur = new javax.swing.JMenuItem();
        MSelectionner = new javax.swing.JMenuItem();
        MStatistiques = new javax.swing.JMenu();
        Classement = new javax.swing.JMenuItem();
        Scores = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PGauche.setLayout(new java.awt.GridLayout(2, 1));

        PJoueur1.setLayout(new java.awt.GridLayout(2, 1));

        PInfo1.setLayout(new java.awt.GridLayout(3, 1));

        LNomJ1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        LNomJ1.setText("Joueur 1 :");
        PInfo1.add(LNomJ1);

        LPseudoJ1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        LPseudoJ1.setText("Pseudo joueur 1");
        PInfo1.add(LPseudoJ1);
        PInfo1.add(BCoulJ1);

        PJoueur1.add(PInfo1);

        PhotoJ1.setLayout(new java.awt.GridLayout(1, 1));

        BPhotoJ1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mario.png"))); // NOI18N
        PhotoJ1.add(BPhotoJ1);

        PJoueur1.add(PhotoJ1);

        PGauche.add(PJoueur1);

        PJoueur2.setLayout(new java.awt.GridLayout(2, 1));

        PInfo2.setLayout(new java.awt.GridLayout(3, 1));

        LNomJ2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        LNomJ2.setText("Joueur 2 :");
        PInfo2.add(LNomJ2);

        LPseudoJ2.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        LPseudoJ2.setText("Pseudo joueur 2");
        PInfo2.add(LPseudoJ2);
        PInfo2.add(BCoulJ2);

        PJoueur2.add(PInfo2);

        PhotoJ2.setLayout(new java.awt.GridLayout(1, 1));

        BPhotoJ2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sonic.png"))); // NOI18N
        PhotoJ2.add(BPhotoJ2);

        PJoueur2.add(PhotoJ2);

        PGauche.add(PJoueur2);

        getContentPane().add(PGauche, java.awt.BorderLayout.WEST);

        LMessage.setText("Message :");
        PBas.add(LMessage);
        PBas.add(Message);
        PBas.add(MessageErreur);

        getContentPane().add(PBas, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout PCentreLayout = new javax.swing.GroupLayout(PCentre);
        PCentre.setLayout(PCentreLayout);
        PCentreLayout.setHorizontalGroup(
            PCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PCentreLayout.setVerticalGroup(
            PCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(PCentre, java.awt.BorderLayout.CENTER);

        PDroit.setLayout(new java.awt.GridLayout(4, 1));

        LPPions.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        LPPions.setText("Galets à déposer");
        PDroit.add(LPPions);

        PPions.setBackground(new java.awt.Color(255, 204, 153));

        javax.swing.GroupLayout PPionsLayout = new javax.swing.GroupLayout(PPions);
        PPions.setLayout(PPionsLayout);
        PPionsLayout.setHorizontalGroup(
            PPionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PPionsLayout.setVerticalGroup(
            PPionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        PDroit.add(PPions);

        getContentPane().add(PDroit, java.awt.BorderLayout.EAST);

        MJeu.setText(" Jeu");

        Quitter.setText("Quitter");
        Quitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitterActionPerformed(evt);
            }
        });
        MJeu.add(Quitter);

        Rejouer.setText("Rejouer");
        Rejouer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RejouerActionPerformed(evt);
            }
        });
        MJeu.add(Rejouer);

        jMenuBar1.add(MJeu);

        MJoueurs.setText("Joueurs");
        MJoueurs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MJoueursActionPerformed(evt);
            }
        });

        AjouterJoueur.setText("Ajouter Joueur");
        AjouterJoueur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterJoueurActionPerformed(evt);
            }
        });
        MJoueurs.add(AjouterJoueur);

        VisualiserJoueur.setText("Viusaliser Joueur");
        VisualiserJoueur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VisualiserJoueurActionPerformed(evt);
            }
        });
        MJoueurs.add(VisualiserJoueur);

        MSelectionner.setText("Selectionner Joueur");
        MSelectionner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MSelectionnerActionPerformed(evt);
            }
        });
        MJoueurs.add(MSelectionner);

        jMenuBar1.add(MJoueurs);

        MStatistiques.setText("Statistiques");

        Classement.setText("Classement");
        Classement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClassementActionPerformed(evt);
            }
        });
        MStatistiques.add(Classement);

        Scores.setText("Scores");
        Scores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ScoresActionPerformed(evt);
            }
        });
        MStatistiques.add(Scores);

        jMenuBar1.add(MStatistiques);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AjouterJoueurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterJoueurActionPerformed
        // TODO add your handling code here:
        SaisieJoueurDlg a = new SaisieJoueurDlg(this,true);
        a.setSize(400,400);
        a.setTitle("Ajouter Joueur");
        a.setVisible(true);
        if(a.getOK()){
            Joueur nouveau = a.getJoueur();
            if(nouveau != null){
                listeJ.ajouteJoueur(nouveau);
                // Définit ce joueur comme Joueur 1
                this.joueur1 = nouveau;
                this.joueurCourant = 1; // Le joueur 1 commence
                this.nbgj1 = 8; // (ex: nombre de pions si besoin)
                this.nbgj2 = 8;
                this.compt = 0;
                this.caseCourante = null;
                this.selection = false;
                this.finPartie = false;

                // Mise à jour de l'affichage
                LPseudoJ1.setText(nouveau.getPseudo());
                panPhotoJ1.setImage(nouveau.getPhoto().getImage());
                Message.setText("C'est au joueur 1 de jouer");

                // Lance une nouvelle partie
                this.PJeu.initPlateauJeu(); 
                affichePanneau(); 
                this.setSize(800, 800);
            }
        }
    }//GEN-LAST:event_AjouterJoueurActionPerformed

    private void VisualiserJoueurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VisualiserJoueurActionPerformed
        // TODO add your handling code here:
        VisuJoueurDlg a = new VisuJoueurDlg(this,true,this.listeJ);
        a.setSize(400,400);
        a.setTitle("Visualiser Joueur");
        a.setVisible(true);
    }//GEN-LAST:event_VisualiserJoueurActionPerformed

    private void MSelectionnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MSelectionnerActionPerformed
        // TODO add your handling code here:
        SelectionJoueursDlg diag = new SelectionJoueursDlg(this,true,this.listeJ);
        diag.setVisible(true);
        if(diag.getOK()){
            //on a validé, on récupere les joueurs
            this.joueur1 = diag.getJoueur1();
            this.joueur2 = diag.getJoueur2();
            afficheJoueurs();
        }
    }//GEN-LAST:event_MSelectionnerActionPerformed

    private void QuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitterActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_QuitterActionPerformed

    private void ClassementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClassementActionPerformed
        // TODO add your handling code here:
        ClassementDlg diag = new ClassementDlg(this,true,this.listeJ,this.lp);
        diag.setSize(650,650);
        diag.setTitle("Classement Joueurs");
        diag.setVisible(true);
    }//GEN-LAST:event_ClassementActionPerformed

    private void ScoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ScoresActionPerformed
        // TODO add your handling code here:
        ScoresDlg diag = new ScoresDlg(this,true,this.listeJ, this.lp);
        diag.setSize(600,600);
        diag.setTitle("Classement Joueurs");
        diag.setVisible(true);
    }//GEN-LAST:event_ScoresActionPerformed

    private void RejouerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RejouerActionPerformed
        // TODO add your handling code here:
        this.PJeu.initPlateauJeu();
        affichePanneau();
        this.joueurCourant = 1;
        this.nbgj1 = 8;
        this.nbgj2 = 8;
        this.compt = 0;
        this.caseCourante = null;
        this.selection = false;
        this.finPartie = false;
        Message.setText("C'est au joueur 1 de jouer"); // Message du composant de type JTextField en bas de l'interface
        afficheCaseCourante();
        this.setSize(800, 800);
    }//GEN-LAST:event_RejouerActionPerformed

    private void MJoueursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MJoueursActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MJoueursActionPerformed

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
            java.util.logging.Logger.getLogger(P4P.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(P4P.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(P4P.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(P4P.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                P4P lc = new P4P();
                lc.setSize(800,800);
                lc.setTitle("Puissance 4 Plus");
                lc.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AjouterJoueur;
    private javax.swing.JButton BCoulJ1;
    private javax.swing.JButton BCoulJ2;
    private javax.swing.JButton BPhotoJ1;
    private javax.swing.JButton BPhotoJ2;
    private javax.swing.JMenuItem Classement;
    private javax.swing.JLabel LMessage;
    private javax.swing.JLabel LNomJ1;
    private javax.swing.JLabel LNomJ2;
    private javax.swing.JLabel LPPions;
    private javax.swing.JLabel LPseudoJ1;
    private javax.swing.JLabel LPseudoJ2;
    private javax.swing.JMenu MJeu;
    private javax.swing.JMenu MJoueurs;
    private javax.swing.JMenuItem MSelectionner;
    private javax.swing.JMenu MStatistiques;
    private javax.swing.JTextField Message;
    private javax.swing.JLabel MessageErreur;
    private javax.swing.JPanel PBas;
    private javax.swing.JPanel PCentre;
    private javax.swing.JPanel PDroit;
    private javax.swing.JPanel PGauche;
    private javax.swing.JPanel PInfo1;
    private javax.swing.JPanel PInfo2;
    private javax.swing.JPanel PJoueur1;
    private javax.swing.JPanel PJoueur2;
    private javax.swing.JPanel PPions;
    private javax.swing.JPanel PhotoJ1;
    private javax.swing.JPanel PhotoJ2;
    private javax.swing.JMenuItem Quitter;
    private javax.swing.JMenuItem Rejouer;
    private javax.swing.JMenuItem Scores;
    private javax.swing.JMenuItem VisualiserJoueur;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables

}
