/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicClasses.ClientAdmin.AdminPanels;

import DataClasses.FileTransfer;
import DataClasses.OfertaEmprego;
import java.util.List;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author -nikeiZprooo-
 */
public class VerOfertaEmpregoPanel extends javax.swing.JPanel {

    /**
     * Creates new form VerOfertaEmpregoPanel
     */
    public VerOfertaEmpregoPanel(OfertaEmprego ofertaEmprego, List<FileTransfer> anexos) {
        initComponents();
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        areaOfertaLabel.setText(ofertaEmprego.getAreaAtuacao().toString());
        detalhesOfertaLabel.setText(ofertaEmprego.getDetalhesOferta());
        numeroCandidatosNecessariosLabel.setText(String.valueOf(ofertaEmprego.getNumeroCandidatosNecessarios()));
        perfilCandidatosLabel.setText(ofertaEmprego.getPerfilCandidatos());
        tituloLabel.setText(ofertaEmprego.getTitulo());

        for (FileTransfer ficheiro : anexos) {
            anexosPanel.add(new VerAnexoPanel(ficheiro));
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

        label1 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        label5 = new javax.swing.JLabel();
        jScrollPane = new javax.swing.JScrollPane();
        anexosPanel = new javax.swing.JPanel();
        numeroCandidatosNecessariosLabel = new javax.swing.JLabel();
        tituloLabel = new javax.swing.JLabel();
        detalhesOfertaLabel = new javax.swing.JLabel();
        perfilCandidatosLabel = new javax.swing.JLabel();
        areaOfertaLabel = new javax.swing.JLabel();

        label1.setText("Titulo:");

        label3.setText("Detalhes da Oferta:");

        label2.setText("Número de Candidatos Necessários:");

        label4.setText("Perfil dos Candidatos:");

        label5.setText("Área da Oferta:");

        anexosPanel.setLayout(new javax.swing.BoxLayout(anexosPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane.setViewportView(anexosPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label2)
                    .addComponent(label1)
                    .addComponent(label3)
                    .addComponent(label4)
                    .addComponent(label5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numeroCandidatosNecessariosLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(detalhesOfertaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(perfilCandidatosLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(areaOfertaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jScrollPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label1)
                    .addComponent(tituloLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label3)
                    .addComponent(detalhesOfertaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label2)
                    .addComponent(numeroCandidatosNecessariosLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label4)
                    .addComponent(perfilCandidatosLabel))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label5)
                    .addComponent(areaOfertaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel anexosPanel;
    private javax.swing.JLabel areaOfertaLabel;
    private javax.swing.JLabel detalhesOfertaLabel;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel label5;
    private javax.swing.JLabel numeroCandidatosNecessariosLabel;
    private javax.swing.JLabel perfilCandidatosLabel;
    private javax.swing.JLabel tituloLabel;
    // End of variables declaration//GEN-END:variables
}