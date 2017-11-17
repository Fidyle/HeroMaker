package HeroMaker;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fidyle
 */
public class MainHeroMaker extends javax.swing.JFrame {

    /**
     * Creates new form MainHeroMaker
     */
    EntityManagerFactory emf = null;
    EntityManager em = null;
    JPanel actualPanel;
    public MainHeroMaker() {
        initComponents();
        actualPanel = jPanelHeroList;
        
        //Search of db location
        String dbPath = null;
        try {
            dbPath = new File(".").getCanonicalPath();
            dbPath += "\\database\\";
            System.out.println(dbPath);
            System.setProperty("derby.system.home", dbPath);
            connectDatabase();
            updateHeroTable();
        } catch (IOException ex) {
            Logger.getLogger(MainHeroMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMain = new javax.swing.JPanel();
        jPanelHeroList = new javax.swing.JPanel();
        jScrollPaneTableHeroList = new javax.swing.JScrollPane();
        jTableHeroList = new javax.swing.JTable();
        jButtonSearch = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();
        jLabelSearchTitle = new javax.swing.JLabel();
        jLabelTableTitle = new javax.swing.JLabel();
        jPanelNew = new javax.swing.JPanel();
        jButtonPanelNewReturn = new javax.swing.JButton();
        jMenuTop = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuFileNew = new javax.swing.JMenuItem();
        jMenuFileSeparator = new javax.swing.JPopupMenu.Separator();
        jMenuFileExit = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuHelp = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HeroMaker");
        setMinimumSize(new java.awt.Dimension(500, 350));
        setResizable(false);
        setSize(new java.awt.Dimension(500, 350));

        jPanelMain.setLayout(new java.awt.CardLayout());

        jTableHeroList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Creator", "Race", "Class", "Level"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableHeroList.setToolTipText("Double click on a line to load a hero");
        jTableHeroList.getTableHeader().setReorderingAllowed(false);
        jTableHeroList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableHeroListMouseClicked(evt);
            }
        });
        jScrollPaneTableHeroList.setViewportView(jTableHeroList);

        jButtonSearch.setText("Search");

        jTextFieldSearch.setToolTipText("hero name");

        jLabelSearchTitle.setText("Search by name");

        jLabelTableTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelTableTitle.setText("List of local Heroes");

        javax.swing.GroupLayout jPanelHeroListLayout = new javax.swing.GroupLayout(jPanelHeroList);
        jPanelHeroList.setLayout(jPanelHeroListLayout);
        jPanelHeroListLayout.setHorizontalGroup(
            jPanelHeroListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelHeroListLayout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(jPanelHeroListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelHeroListLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabelSearchTitle)
                        .addGap(32, 32, 32)
                        .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                    .addComponent(jLabelTableTitle)
                    .addComponent(jScrollPaneTableHeroList, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(50, 50, 50))
        );
        jPanelHeroListLayout.setVerticalGroup(
            jPanelHeroListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelHeroListLayout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(jLabelTableTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelHeroListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSearchTitle)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearch))
                .addGap(18, 18, 18)
                .addComponent(jScrollPaneTableHeroList, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101))
        );

        jPanelMain.add(jPanelHeroList, "card2");

        jPanelNew.setMinimumSize(jPanelMain.getMinimumSize());

        jButtonPanelNewReturn.setText("Return");
        jButtonPanelNewReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPanelNewReturnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelNewLayout = new javax.swing.GroupLayout(jPanelNew);
        jPanelNew.setLayout(jPanelNewLayout);
        jPanelNewLayout.setHorizontalGroup(
            jPanelNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNewLayout.createSequentialGroup()
                .addContainerGap(380, Short.MAX_VALUE)
                .addComponent(jButtonPanelNewReturn)
                .addGap(35, 35, 35))
        );
        jPanelNewLayout.setVerticalGroup(
            jPanelNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNewLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jButtonPanelNewReturn)
                .addContainerGap(315, Short.MAX_VALUE))
        );

        jPanelMain.add(jPanelNew, "card3");

        jMenuFile.setText("File");

        jMenuFileNew.setText("New Hero");
        jMenuFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileNewActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuFileNew);
        jMenuFile.add(jMenuFileSeparator);

        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuFileExit);

        jMenuTop.add(jMenuFile);

        jMenuEdit.setText("Edit");
        jMenuTop.add(jMenuEdit);

        jMenuHelp.setText("Help");
        jMenuTop.add(jMenuHelp);

        setJMenuBar(jMenuTop);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuFileExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileExitActionPerformed
        // TODO add your handling code here:
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit now ?", "Warning", JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuFileExitActionPerformed

    private void jTableHeroListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableHeroListMouseClicked
        // TODO add your handling code here:
        int row = jTableHeroList.rowAtPoint(evt.getPoint());
        int col = jTableHeroList.columnAtPoint(evt.getPoint());
        if(col == 5){
            System.out.println("okay");
        }
    }//GEN-LAST:event_jTableHeroListMouseClicked

    private void jMenuFileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileNewActionPerformed
        // TODO add your handling code here:
        actualPanel.setVisible(false);
        actualPanel = jPanelNew;
        actualPanel.setVisible(true);
        
    }//GEN-LAST:event_jMenuFileNewActionPerformed

    private void jButtonPanelNewReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPanelNewReturnActionPerformed
        // TODO add your handling code here:
        actualPanel.setVisible(false);
        actualPanel = jPanelHeroList;
        actualPanel.setVisible(true);
    }//GEN-LAST:event_jButtonPanelNewReturnActionPerformed

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
            java.util.logging.Logger.getLogger(MainHeroMaker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainHeroMaker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainHeroMaker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainHeroMaker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainHeroMaker().setVisible(true);
            }
        });
    }
    public void connectDatabase(){
        if(emf == null){
            try {
                emf = javax.persistence.Persistence.createEntityManagerFactory("HeroMakerPU");
            } catch(Exception e){
                JOptionPane.showMessageDialog(this, "Can't reach local database, the application will close", "Error",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        if(em == null){
            em = emf.createEntityManager();
        }
    }
    public void updateHeroTable(){
        ClassJpaController classController = new ClassJpaController(emf);
        HeroJpaController heroController = new HeroJpaController(emf);
        RaceJpaController raceController = new RaceJpaController(emf);
        
        List<Hero> heroList = heroController.findHeroEntities();
        
        Object[][] finalList = new Object[heroList.size()][5];
        Object[] finalListPart = new Object[5];
        String aClass, aRace;
        int k = 0, aLevel;
       
        for(Hero aHero: heroList){
           
           aClass = classController.findClass(aHero.getLevel().getLevelPK().getIdClass()).getName();
           aRace = raceController.findRace(aHero.getIdRace().getId()).getName();
           aLevel = aHero.getLevel().getLevelPK().getId();
           
           finalListPart = new Object[]{aHero.getName(), aHero.getCreator(), aRace, aClass, aLevel};
           finalList[k] = finalListPart;
           
           k++;
        }
        
        DefaultTableModel tableModel = (DefaultTableModel) jTableHeroList.getModel();
        //Deletes all rows
        tableModel.setRowCount(0);
        tableModel.setDataVector(finalList, new Object[]{"Name","Creator","Race","Class","Level"});
        //Object[][] list = new Object[][]{{"name","creator","race","class",1,"test"},{"name","creator","race","class",1,"test"}};
        //tableModel.addRow(new Object[]{"name","creator","race","class",1,test});
        
        jTableHeroList.setModel(tableModel);
        
        classController = null;
        heroController = null;
        raceController = null;
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPanelNewReturn;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JLabel jLabelSearchTitle;
    private javax.swing.JLabel jLabelTableTitle;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuFileExit;
    private javax.swing.JMenuItem jMenuFileNew;
    private javax.swing.JPopupMenu.Separator jMenuFileSeparator;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuBar jMenuTop;
    private javax.swing.JPanel jPanelHeroList;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelNew;
    private javax.swing.JScrollPane jScrollPaneTableHeroList;
    private javax.swing.JTable jTableHeroList;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
