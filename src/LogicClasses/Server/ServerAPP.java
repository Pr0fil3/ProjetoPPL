package LogicClasses.Server;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Aplicação do servidor.
 * Inicializa a Implementação e mantém um log.
 */
public class ServerAPP extends javax.swing.JFrame implements IServerUI{
    private ServerImplementation server;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss - ");
    
    /**
     * Lê o ficheiro XML de configuração e inicializa o servidor.
     */
    public ServerAPP() {
        initComponents();
        System.runFinalizersOnExit(true);
        
        try {
            sendMessage("### --- ### --- ### ---   --- ### --- ### --- ###");
            sendMessage("A abrir ficheiro de configuração...");
            File XMLConfig = new File("src\\LogicClasses\\Server\\ConfigFiles\\Config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLConfig);
            sendMessage("A lêr os parâmetros...");
            doc.getDocumentElement().normalize();
            Element dataBaseElement = (Element) doc.getElementsByTagName("database").item(0);
            Element accessIntervalsElement = (Element) doc.getElementsByTagName("accessIntervals").item(0);
            
            String url = dataBaseElement.getElementsByTagName("url").item(0).getTextContent();
            int tempoScheduler = Integer.parseInt(accessIntervalsElement.getElementsByTagName("scheduler").item(0).getTextContent());
            int tempoChecksLocais = Integer.parseInt(accessIntervalsElement.getElementsByTagName("ramData").item(0).getTextContent());
            int tempoChecksBD = Integer.parseInt(accessIntervalsElement.getElementsByTagName("dbData").item(0).getTextContent());
            
            sendMessage("A criar ligações à base de dados em " + url + "...");
            sendMessage("Os delays serão (em Milisegundos):"
                    + " Scheduler: " + tempoScheduler 
                    + " | Checks Locais: " + tempoChecksLocais 
                    + " | Checks à BD: " + tempoChecksBD);
            
            this.server = new ServerImplementation(this, 
                    url,
                    dataBaseElement.getElementsByTagName("user").item(0).getTextContent(),
                    dataBaseElement.getElementsByTagName("password").item(0).getTextContent(),
                    tempoScheduler,
                    tempoChecksLocais,
                    tempoChecksBD);
            sendMessage("Ligações criadas!");
            sendMessage("A criar o servidor de RMI...");
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("server", server);
            sendMessage("### --- ### --- Servidor pronto --- ### --- ###");
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar o servidor de RMI... a fechar...");
            System.exit(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar as ligações à base de dados... a fechar...");
            System.exit(0);
        } catch (NoSuchAlgorithmException ex) {
            // Erro que nunca acontece...
            JOptionPane.showMessageDialog(this, "Erro... A sair");
            System.exit(0);
        } catch (ParserConfigurationException | SAXException ex) {
            JOptionPane.showMessageDialog(this, "O ficheiro de configuração não está bem formatado... a fechar...");
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "O ficheiro de configuração não foi encontrado... a fechar...");
            System.exit(0);
        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Os tempos de espera do ficheiro de configuração não são válidos... a fechar...");
        }
    }
    
    @Override
    public synchronized void sendMessage(String msg) {
        consolaTextArea.append(dateFormat.format(System.currentTimeMillis()) + msg + "\n");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        consolaTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Projeto PP - Server");
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        consolaTextArea.setEditable(false);
        consolaTextArea.setColumns(20);
        consolaTextArea.setRows(5);
        consolaTextArea.setPreferredSize(new java.awt.Dimension(200, 300));
        jScrollPane1.setViewportView(consolaTextArea);

        getContentPane().add(jScrollPane1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(ServerAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerAPP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerAPP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea consolaTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
