/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ProjetoPP_Fase1.LogicClasses.ClientRegister;

import ProjetoPP_Fase1.LogicClasses.ClientRegister.ClientPanels.AdicionarOfertaEmpregoPanel;
import ProjetoPP_Fase1.LogicClasses.ClientRegister.ClientPanels.AdicionarOfertaRecursosPanel;
import java.awt.CardLayout;
import java.awt.Component;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author -nikeiZprooo-
 */
public class ClientAPP extends javax.swing.JFrame {

    private ClientRegisterImplementation client;
    
    /**
     * Creates new form ClientAPP
     */
    public ClientAPP() {
        initComponents();
        customInit();
    }
    
    private void customInit(){
        try {
            client = new ClientRegisterImplementation();
        } catch (RemoteException | NotBoundException ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível conectar ao servidor... a sair...");
            System.exit(0);
        }
    }
    
    public void comeHere(Component c){
        mainPanel.remove(c);
        pack();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        programaMenu = new javax.swing.JMenu();
        sairBotaoMenu = new javax.swing.JMenuItem();
        adicionarOfertaMenu = new javax.swing.JMenu();
        ofertaRecursosBotaoMenu = new javax.swing.JMenuItem();
        ofertaEmpregoBotaoMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Projeto PP - Client Register");
        getContentPane().setLayout(new java.awt.CardLayout());

        mainPanel.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel1.setText("Cliente de Adição de Ofertas da e-Trabalho");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(441, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(543, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel1, "card2");

        getContentPane().add(mainPanel, "card2");

        programaMenu.setText("Programa");

        sairBotaoMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        sairBotaoMenu.setText("Sair");
        sairBotaoMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sairBotaoMenuActionPerformed(evt);
            }
        });
        programaMenu.add(sairBotaoMenu);

        jMenuBar1.add(programaMenu);

        adicionarOfertaMenu.setText("Adicionar Oferta");

        ofertaRecursosBotaoMenu.setText("Oferta de Recursos");
        ofertaRecursosBotaoMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ofertaRecursosBotaoMenuActionPerformed(evt);
            }
        });
        adicionarOfertaMenu.add(ofertaRecursosBotaoMenu);

        ofertaEmpregoBotaoMenu.setText("Oferta de Emprego");
        ofertaEmpregoBotaoMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ofertaEmpregoBotaoMenuActionPerformed(evt);
            }
        });
        adicionarOfertaMenu.add(ofertaEmpregoBotaoMenu);

        jMenuBar1.add(adicionarOfertaMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sairBotaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sairBotaoMenuActionPerformed
        System.exit(0);
    }//GEN-LAST:event_sairBotaoMenuActionPerformed

    private void ofertaRecursosBotaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ofertaRecursosBotaoMenuActionPerformed
        mainPanel.add("ofertaRecursos",new AdicionarOfertaRecursosPanel(client,this));
        ((CardLayout)mainPanel.getLayout()).show(mainPanel, "ofertaRecursos");
    }//GEN-LAST:event_ofertaRecursosBotaoMenuActionPerformed

    private void ofertaEmpregoBotaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ofertaEmpregoBotaoMenuActionPerformed
        mainPanel.add("ofertaEmprego",new AdicionarOfertaEmpregoPanel(client,this));
        ((CardLayout)mainPanel.getLayout()).show(mainPanel, "ofertaEmprego");
    }//GEN-LAST:event_ofertaEmpregoBotaoMenuActionPerformed

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
            java.util.logging.Logger.getLogger(ClientAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientAPP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu adicionarOfertaMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem ofertaEmpregoBotaoMenu;
    private javax.swing.JMenuItem ofertaRecursosBotaoMenu;
    private javax.swing.JMenu programaMenu;
    private javax.swing.JMenuItem sairBotaoMenu;
    // End of variables declaration//GEN-END:variables
}
