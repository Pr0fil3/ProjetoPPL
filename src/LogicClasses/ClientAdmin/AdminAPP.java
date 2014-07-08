/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicClasses.ClientAdmin;

import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import LogicClasses.ClientAdmin.AdminPanels.LoginPanel;
import LogicClasses.ClientAdmin.AdminPanels.VerOfertaEmpregoPanel;
import LogicClasses.ClientAdmin.AdminPanels.VerOfertaRecursosPanel;
import LogicClasses.ClientAdmin.Exceptions.PasswordNotValidException;
import LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import LogicClasses.ClientAdmin.Exceptions.UserNotValidException;
import LogicClasses.ConexoesBD.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.UserNotFoundException;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.WrongPasswordException;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author -nikeiZprooo-
 */
public class AdminAPP extends javax.swing.JFrame {

    private ClientAdminImplementation admin;
    private Component painelReviewAtual;

    /**
     * Creates new form AdminAPP
     */
    public AdminAPP() {
        initComponents();

        try {
            admin = new ClientAdminImplementation(this);
        } catch (RemoteException | NotBoundException ex) {
            JOptionPane.showMessageDialog(this, "Não foi possivel conectar ao servidor... a sair...");
            System.exit(0);
        }

        refreshAPP();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                logout();
                System.exit(0);
            }
        });
    }

    public void apresentarCaso(Oferta oferta) throws SQLException, IOException {
        if (oferta instanceof OfertaEmprego) {
            painelReviewAtual = new VerOfertaEmpregoPanel((OfertaEmprego) oferta, admin.getAnexos(oferta.getId()));
            mainPanel.add(painelReviewAtual,"caso");
            ((CardLayout)mainPanel.getLayout()).show(mainPanel, "caso");
            refreshAPP();
            pack();
        } else if (oferta instanceof OfertaRecursos){
            painelReviewAtual = new VerOfertaRecursosPanel((OfertaRecursos) oferta);
            mainPanel.add(painelReviewAtual,"caso");
            ((CardLayout)mainPanel.getLayout()).show(mainPanel, "caso");
            refreshAPP();
            pack();
        }
    }

    public void refreshAPP() {
        if (admin.isLoggedIn()) {
            loginMenuItem.setEnabled(false);
            logoutMenuItem.setEnabled(true);
            if (painelReviewAtual != null) {
                aprovarOfertaButton.setEnabled(true);
                rejeitarOfertaButton.setEnabled(true);
            } else {
                aprovarOfertaButton.setEnabled(false);
                rejeitarOfertaButton.setEnabled(false);
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "loggedInCard");
            }
        } else {
            loginMenuItem.setEnabled(true);
            logoutMenuItem.setEnabled(false);
            aprovarOfertaButton.setEnabled(false);
            rejeitarOfertaButton.setEnabled(false);
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "loggedOutCard");
        }
    }

    private void login() {
        if (testarLogin("", "")) {
            refreshAPP();
            try {
                apresentarCaso(admin.receberNovoCaso());
            } catch (OfertaEmpregoNotFoundException | OfertaRecursosNotFoundException | UserNotLoggedInException | NoPrivilegesException ex) {
                // Erro irrelevante
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Não foi possível contactar a base de dados. Faça logout e volte a fazer login.\nSe o erro persistir contacte um administrador.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro no sistema de ficheiros do servidor. Por favor contacte um administrador.");
            }
        }
    }
    
    private void removerPainelCaso(){
        if(painelReviewAtual != null) {
            mainPanel.remove(painelReviewAtual);
            painelReviewAtual = null;
        }
        refreshAPP();
        pack();
    }

    //<editor-fold defaultstate="collapsed" desc="Código responsável por logins">
    private User showLoginPanel(String user, String password) {
        LoginPanel loginPanel = new LoginPanel(user, password);
        if (JOptionPane.showConfirmDialog(this, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            return loginPanel.getUser();
        } else {
            return null;
        }
    }

    private boolean testarLogin(String userName, String password) {
        User user = showLoginPanel(userName, password);

        if (user != null) {
            try {
                user = admin.login(Integer.parseInt(user.getNome()), user.getPassword());
                JOptionPane.showMessageDialog(this, "Bem-vindo, aprovador " + user.getNome() + ".");
                return true;
            } catch (NumberFormatException | UserNotValidException ex) {
                JOptionPane.showMessageDialog(this, "O campo \"Login\" não foi preenchido devidamente.");
                return testarLogin(user.getNome(), user.getPassword());
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro de RMI... Falha ao contactar o servidor. Por favor, tente novamente.");
                return testarLogin(user.getNome(), user.getPassword());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro de SQL... Falha ao contactar a base de dados. Por favor, tente novamente.");
                return testarLogin(user.getNome(), user.getPassword());
            } catch (WrongPasswordException ex) {
                JOptionPane.showMessageDialog(this, "A password está errada.");
                return testarLogin(user.getNome(), user.getPassword());
            } catch (UserNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "O utilizador não existe ou não foi encontrado.");
                return testarLogin(user.getNome(), user.getPassword());
            } catch (PasswordNotValidException ex) {
                JOptionPane.showMessageDialog(this, "O campo \"Password\" não foi preenchido devidamente.");
                return testarLogin(user.getNome(), user.getPassword());
            }
        } else {
            return false;
        }
    }
    //</editor-fold>

    private void logout() {
        try {
            admin.logout();
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível contactar o servidor. Por favor contacte um administrador.");
        } finally {
            removerPainelCaso();
        }
    }

    private void avaliarCaso(boolean avaliacao) {
        try {
            admin.avaliarCaso(avaliacao);
            removerPainelCaso();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar a base de dados... Por favor contacte um administrador.\nA sair do programa...");
            System.exit(0);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao contactar o servidor... Tente novamento.\nSe o erro persistir contacte um administrador");
        } 
        
        try {
            apresentarCaso(admin.receberNovoCaso());
        }catch (OfertaEmpregoNotFoundException | OfertaRecursosNotFoundException | UserNotLoggedInException | NoPrivilegesException ex) {
            
        } catch (RemoteException ex) {
            Logger.getLogger(AdminAPP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao contactar a base de dados... Tente novamente. Se o erro persistir contacte um administrador.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro no sistema de ficheiros do servidor. Por favor, contacte um administrador.");
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

        jPanel1 = new javax.swing.JPanel();
        rejeitarOfertaButton = new javax.swing.JButton();
        aprovarOfertaButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        panelLoggedIn = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelLoggedOut = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        loginMenuItem = new javax.swing.JMenuItem();
        logoutMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        sairMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Projeto PP - Client Admin");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        rejeitarOfertaButton.setText("Rejeitar Oferta");
        rejeitarOfertaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejeitarOfertaButtonActionPerformed(evt);
            }
        });

        aprovarOfertaButton.setText("Aprovar Oferta");
        aprovarOfertaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aprovarOfertaButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aprovarOfertaButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rejeitarOfertaButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rejeitarOfertaButton)
                    .addComponent(aprovarOfertaButton))
                .addContainerGap())
        );

        mainPanel.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel1.setText("Cliente de Aprovação de Ofertas da e-Trabalho");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel2.setText("Neste momento não há Ofertas para analisar...");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel3.setText("O cliente de Aprovação apresentará Ofertas para analisar assim que elas chegarem ao servidor!");

        javax.swing.GroupLayout panelLoggedInLayout = new javax.swing.GroupLayout(panelLoggedIn);
        panelLoggedIn.setLayout(panelLoggedInLayout);
        panelLoggedInLayout.setHorizontalGroup(
            panelLoggedInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoggedInLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLoggedInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLoggedInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1))
                    .addGroup(panelLoggedInLayout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jLabel3)))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        panelLoggedInLayout.setVerticalGroup(
            panelLoggedInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoggedInLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(197, 197, 197)
                .addComponent(jLabel3)
                .addContainerGap(245, Short.MAX_VALUE))
        );

        mainPanel.add(panelLoggedIn, "loggedInCard");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel4.setText("Cliente de Aprovação de Ofertas da e-Trabalho");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel5.setText("Faça login para poder utilizar este software.");

        javax.swing.GroupLayout panelLoggedOutLayout = new javax.swing.GroupLayout(panelLoggedOut);
        panelLoggedOut.setLayout(panelLoggedOutLayout);
        panelLoggedOutLayout.setHorizontalGroup(
            panelLoggedOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoggedOutLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLoggedOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addContainerGap(410, Short.MAX_VALUE))
        );
        panelLoggedOutLayout.setVerticalGroup(
            panelLoggedOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoggedOutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(461, Short.MAX_VALUE))
        );

        mainPanel.add(panelLoggedOut, "loggedOutCard");

        jMenu1.setText("Programa");

        loginMenuItem.setText("Login");
        loginMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(loginMenuItem);

        logoutMenuItem.setText("Logout");
        logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(logoutMenuItem);
        jMenu1.add(jSeparator1);

        sairMenuItem.setText("Sair");
        sairMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sairMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(sairMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginMenuItemActionPerformed
        login();
    }//GEN-LAST:event_loginMenuItemActionPerformed

    private void logoutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutMenuItemActionPerformed
        logout();
    }//GEN-LAST:event_logoutMenuItemActionPerformed

    private void sairMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sairMenuItemActionPerformed
        logout();
        System.exit(0);
    }//GEN-LAST:event_sairMenuItemActionPerformed

    private void aprovarOfertaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aprovarOfertaButtonActionPerformed
        avaliarCaso(true);
    }//GEN-LAST:event_aprovarOfertaButtonActionPerformed

    private void rejeitarOfertaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejeitarOfertaButtonActionPerformed
        avaliarCaso(false);
    }//GEN-LAST:event_rejeitarOfertaButtonActionPerformed

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
            java.util.logging.Logger.getLogger(AdminAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminAPP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aprovarOfertaButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem loginMenuItem;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel panelLoggedIn;
    private javax.swing.JPanel panelLoggedOut;
    private javax.swing.JButton rejeitarOfertaButton;
    private javax.swing.JMenuItem sairMenuItem;
    // End of variables declaration//GEN-END:variables
}
