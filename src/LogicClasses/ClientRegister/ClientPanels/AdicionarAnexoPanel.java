package LogicClasses.ClientRegister.ClientPanels;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Painel que permite adicionar anexos a uma oferta de Emprego
 */
public class AdicionarAnexoPanel extends javax.swing.JPanel {    
    /**
     * Creates new form AdicionarAnexoForm
     */
    public AdicionarAnexoPanel() {
        initComponents();
    }

    public File getAnexo() throws NullPointerException{
        if(anexoPathTextField.getText().length() > 0) {
            return new File(anexoPathTextField.getText());
        } else return null;        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        anexoPathTextField = new javax.swing.JTextField();
        procurarAnexoButon = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(800, 47));
        setMinimumSize(new java.awt.Dimension(800, 47));

        jLabel1.setText("Anexo:");

        procurarAnexoButon.setText("Procurar");
        procurarAnexoButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                procurarAnexoButonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(anexoPathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(procurarAnexoButon)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(anexoPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(procurarAnexoButon))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void procurarAnexoButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_procurarAnexoButonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar anexo");
        fileChooser.setApproveButtonText("Selecionar");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            try {
                anexoPathTextField.setText(fileChooser.getSelectedFile().getCanonicalPath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro.");
            }
        }
    }//GEN-LAST:event_procurarAnexoButonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anexoPathTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton procurarAnexoButon;
    // End of variables declaration//GEN-END:variables
}
