package LogicClasses.Server;

import DataClasses.FileTransfer;
import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import LogicClasses.ClientAdmin.IClientAdmin;
import LogicClasses.ConexoesBD.ConexaoBD;
import LogicClasses.ConexoesBD.Exceptions.KeyNotReturnedException;
import LogicClasses.ConexoesBD.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.UserNotFoundException;
import LogicClasses.ConexoesBD.ManagerConexaoBD;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.WrongPasswordException;
import LogicClasses.Server.Threads.JobScheduler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * Implementação do servidor.
 * Age como intermediário entre os clientes e o Manager da base de dados.
 * Faz também a gestão do sistema de ficheiros no caso dos anexos.
 */
public class ServerImplementation extends UnicastRemoteObject implements IServerForRegister, IServerForAdmin {

    private final ManagerConexaoBD mConexaoBD;
    private final MessageDigest messageDigest = MessageDigest.getInstance("MD5");

    private final IServerUI serverUI;
    private final ArrayList<IClientAdmin> adminClients = new ArrayList<>();
    private final ArrayList<Oferta> ofertasEmAnalise = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Inicializa o JobScheduler e as conexoes à base de dados.
     * @param serverUI
     * @param tempoWaitVerificacoesLocais
     * @param tempoWaitVerificacoesBD
     * @throws RemoteException
     * @throws SQLException
     * @throws NoSuchAlgorithmException 
     */
    public ServerImplementation(IServerUI serverUI,
            int tempoWaitVerificacoesLocais, int tempoWaitVerificacoesBD)
            throws RemoteException, SQLException, NoSuchAlgorithmException {
        this.mConexaoBD = new ManagerConexaoBD();
        this.serverUI = serverUI;
        new Timer().scheduleAtFixedRate(new JobScheduler(this, serverUI,
                new ConexaoBD(), new ConexaoBD(),
                new ConexaoBD(), new ConexaoBD(),
                tempoWaitVerificacoesLocais,tempoWaitVerificacoesBD), 
                tempoWaitVerificacoesBD, tempoWaitVerificacoesBD);
    }

    /**
     * Inicializa o JobScheduler e as conexoes à base de dados com
     * os parâmetros disponibilizados
     * @param serverUI
     * @param url
     * @param user
     * @param password
     * @param tempoWaitScheduler
     * @param tempoWaitVerificacoesLocais
     * @param tempoWaitVerificacoesBD
     * @throws RemoteException
     * @throws SQLException
     * @throws NoSuchAlgorithmException 
     */
    public ServerImplementation(IServerUI serverUI, String url,
            String user, String password, int tempoWaitScheduler,
            int tempoWaitVerificacoesLocais, int tempoWaitVerificacoesBD)
            throws RemoteException, SQLException, NoSuchAlgorithmException {
        this.mConexaoBD = new ManagerConexaoBD(url, user, password);
        this.serverUI = serverUI;
        new Timer().scheduleAtFixedRate(new JobScheduler(this, serverUI,
                new ConexaoBD(url, user, password), new ConexaoBD(url, user, password),
                new ConexaoBD(url, user, password), new ConexaoBD(url, user, password),
                tempoWaitVerificacoesLocais,tempoWaitVerificacoesBD), 
                tempoWaitScheduler, tempoWaitScheduler);
    }

    /**
     * Encodifica mensagens.
     * Utilizado para encodificar as passwords.
     * @param msg
     * @return String codificada
     */
    private String toMD5(String msg) {
        byte[] msgBytes = msg.getBytes();
        messageDigest.reset();
        byte[] newMsgBytes = messageDigest.digest(msgBytes);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < newMsgBytes.length; i++) {
            stringBuffer.append(Integer.toHexString(0xff & newMsgBytes[i]));
        }
        return stringBuffer.toString();
    }

    /**
     * Cria um novo utilizador
     * @param user
     * @param newUser
     * @return
     * @throws RemoteException
     * @throws NoPrivilegesException
     * @throws SQLException
     * @throws UserNotFoundException
     * @throws KeyNotReturnedException 
     */
    @Override
    public User newUser(User user, User newUser)
            throws RemoteException, NoPrivilegesException, SQLException, UserNotFoundException, KeyNotReturnedException {
        if (user == null || newUser == null) {
            throw new InvalidParameterException();
        }

        if (user.isPrivilegios()) {
            newUser.setPassword(toMD5(newUser.getPassword()));
            return mConexaoBD.newUser(newUser);
        } else {
            throw new NoPrivilegesException();
        }
    }

    /**
     * Faz login
     * @param adminClient
     * @param user
     * @return
     * @throws RemoteException
     * @throws SQLException
     * @throws WrongPasswordException
     * @throws UserNotFoundException 
     */
    @Override
    public User login(IClientAdmin adminClient, User user) throws RemoteException, SQLException, WrongPasswordException, UserNotFoundException {
        if (user == null) {
            throw new InvalidParameterException();
        }

        User tempUser = mConexaoBD.getUser(user.getId());

        if (tempUser == null) {
            throw new UserNotFoundException();
        }

        String userPassword = toMD5(user.getPassword());
        if (userPassword.equals(tempUser.getPassword())) {
            serverUI.sendMessage("O aprovador " + tempUser.getNome() + " fez login.");
            adminClients.add(adminClient);
            return tempUser;
        } else {
            throw new WrongPasswordException();
        }
    }

    /**
     * Faz logout
     * @param adminClient
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean logout(IClientAdmin adminClient) throws RemoteException {
        serverUI.sendMessage("O aprovador " + adminClient.getUser().getNome() + " fez logout.");
        return adminClients.remove(adminClient);
    }

    /**
     * Permite criar uma nova oferta de recursos
     * @param ofertaRecursos
     * @throws RemoteException
     * @throws SQLException 
     */
    @Override
    public void novaOferta(OfertaRecursos ofertaRecursos)
            throws RemoteException, SQLException {
        if (ofertaRecursos == null) {
            throw new InvalidParameterException();
        }
        mConexaoBD.newOfertaRecursos(ofertaRecursos);

        try {
            existemNovasOfertas();
        } catch (OfertaEmpregoNotFoundException | NoPrivilegesException | OfertaRecursosNotFoundException | UserNotLoggedInException | IOException ex) {
            serverUI.sendMessage("Foi adicionada um nova oferta de recursos mas a tentativa de notificar os clientes dos administradores falhou.");
        }
    }
    
    /**
     * Permite criar uma nova oferta de emprego e adiconar anexos.
     * @param ofertaEmprego
     * @param transferencias
     * @throws RemoteException
     * @throws SQLException
     * @throws KeyNotReturnedException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    @Override
    public void novaOferta(OfertaEmprego ofertaEmprego, List<FileTransfer> transferencias)
            throws RemoteException, SQLException, KeyNotReturnedException, FileNotFoundException, IOException {
        if (ofertaEmprego == null) {
            throw new InvalidParameterException();
        }

        int idNovaOferta = mConexaoBD.newOfertaEmprego(ofertaEmprego);

        if (transferencias == null) {
        } else {
            Random rand = new Random();
            ArrayList<String> anexoPaths = new ArrayList<>();
            for (FileTransfer anexoFile : transferencias) {
                File sitioGravacao = new File("AnexosProjetoPP" + File.separator + idNovaOferta + File.separator + rand.nextInt(Integer.MAX_VALUE) + File.separator + anexoFile.getTitulo());
                if (anexoFile.gravar(sitioGravacao)) {
                    anexoPaths.add(sitioGravacao.getCanonicalPath());
                }
            }
            mConexaoBD.newAnexos(anexoPaths, idNovaOferta);
        }
    }

    /**
     * Vai buscar aleatóriamente uma oferta para ser analisada.
     * Dá throw de "OfertaEmpregoNotFoundException" ou
     * "OfertaRecursosNotFoundException" se não existir nenhuma oferta
     * para ser analisada.
     * 
     * @return
     * @throws RemoteException
     * @throws SQLException
     * @throws OfertaEmpregoNotFoundException
     * @throws OfertaRecursosNotFoundException 
     */
    @Override
    public synchronized Oferta getNewReviewCase() throws RemoteException, SQLException, OfertaEmpregoNotFoundException, OfertaRecursosNotFoundException {
        Oferta tempOferta;
        if (random.nextBoolean()) {
            try {
                tempOferta = mConexaoBD.getRecursoToReview(ofertasEmAnalise);
                ofertasEmAnalise.add(tempOferta);
                serverUI.sendMessage("A oferta de recursos de ID " + tempOferta.getId() + " foi enviada para análise.");
                return tempOferta;
            } catch (OfertaRecursosNotFoundException ex) {
                tempOferta = mConexaoBD.getEmpregoToReview(ofertasEmAnalise);
                serverUI.sendMessage("A oferta de emprego de ID " + tempOferta.getId() + " foi enviada para análise.");
                ofertasEmAnalise.add(tempOferta);
                return tempOferta;
            }
        } else {
            try {
                tempOferta = mConexaoBD.getEmpregoToReview(ofertasEmAnalise);
                serverUI.sendMessage("A oferta de emprego de ID " + tempOferta.getId() + " foi enviada para análise.");
                ofertasEmAnalise.add(tempOferta);
                return tempOferta;
            } catch (OfertaEmpregoNotFoundException ex) {
                tempOferta = mConexaoBD.getRecursoToReview(ofertasEmAnalise);
                serverUI.sendMessage("A oferta de recursos de ID " + tempOferta.getId() + " foi enviada para análise.");
                ofertasEmAnalise.add(tempOferta);
                return tempOferta;
            }
        }
    }
    
    /**
     * Vai buscar à base de dados os anexos de uma certa oferta e envia-os
     * através de objetos da classe FileTransfer
     * @param id
     * @return Lista de FileTransfer
     * @throws RemoteException
     * @throws SQLException
     * @throws IOException 
     */
    @Override
    public List<FileTransfer> getAnexos(int id) throws RemoteException, SQLException, IOException{
        List<FileTransfer> ficheiros = new ArrayList<>();
        for(String path : mConexaoBD.getAnexos(id)){
            ficheiros.add(new FileTransfer(new File(path)));
        }
        return ficheiros;
    }

    /**
     * Envia para a base de dados a avaliação de uma oferta
     * @param oferta
     * @param avaliacao
     * @throws RemoteException
     * @throws SQLException 
     */
    @Override
    public synchronized void avaliarCaso(Oferta oferta, boolean avaliacao) 
            throws RemoteException, SQLException {
        if (oferta == null) {
            throw new InvalidParameterException();
        }

        if (oferta instanceof OfertaEmprego) {
            mConexaoBD.reviewEmprego(oferta.getId(), avaliacao);
            ofertasEmAnalise.remove(oferta);
            serverUI.sendMessage("A oferta de emprego de ID " + oferta.getId() + " recebeu avaliação " + (avaliacao?"positiva.":"negativa. As ofertas de emprego atribuídas serão reatribuídas."));
        } else if (oferta instanceof OfertaRecursos) {
            mConexaoBD.reviewRecurso(oferta.getId(), avaliacao);
            ofertasEmAnalise.remove(oferta);
            serverUI.sendMessage("A oferta de recursos de ID " + oferta.getId() + " recebeu avaliação " + (avaliacao?"positiva.":"negativa. As ofertas de emprego atribuídas serão reatribuídas."));
        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * Permite cancelar a análise de uma oferta
     * @param oferta
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean cancelReview(Oferta oferta) throws RemoteException{
        if(oferta != null){
            serverUI.sendMessage("Foi cancelada a avaliação da oferta de " + (oferta instanceof OfertaEmprego?"emprego":"recursos") + " com ID " + oferta.getId());
            return ofertasEmAnalise.remove(oferta);
        } else return false;
    }

    /**
     * Chamado pelo JobScheduler e pelas Threads quando há novas Ofertas
     * para serem analisadas.
     * Notifica todos os clientes dos aprovadores que estiverem logados no
     * servidor.
     * @throws RemoteException
     * @throws SQLException
     * @throws OfertaEmpregoNotFoundException
     * @throws NoPrivilegesException
     * @throws OfertaRecursosNotFoundException
     * @throws UserNotLoggedInException
     * @throws IOException 
     */
    public void existemNovasOfertas() throws RemoteException, SQLException,
            OfertaEmpregoNotFoundException, NoPrivilegesException,
            OfertaRecursosNotFoundException, UserNotLoggedInException, IOException {
        for (IClientAdmin ca : adminClients) {
            ca.notificarNovosCasos();
        }
    }
}
