package ProjetoPP_Fase1.LogicClasses.Server;

import DataClasses.FileTransfer;
import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import ProjetoPP_Fase1.LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import ProjetoPP_Fase1.LogicClasses.ClientAdmin.IClientAdmin;
import ProjetoPP_Fase1.LogicClasses.DBConnection.ConexaoBD;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.UserNotFoundException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.ManagerConexaoBD;
import ProjetoPP_Fase1.LogicClasses.Server.Exceptions.NoPrivilegesException;
import ProjetoPP_Fase1.LogicClasses.Server.Exceptions.WrongPasswordException;
import ProjetoPP_Fase1.LogicClasses.Server.Threads.JobScheduler;
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
import java.util.Random;
import java.util.Timer;

/**
 * Created by -nikeiZprooo- on 30/05/2014.
 */
public class ServerImplementation extends UnicastRemoteObject implements IServerForRegister, IServerForAdmin {

    private final ManagerConexaoBD mConexaoBD;
    private final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    private final Timer jobScheduler;
    
    private final IServerUI serverUI;
    private final ArrayList<IClientAdmin> adminClients = new ArrayList<>();
    private final ArrayList<Oferta> ofertasEmAnálise = new ArrayList<>();
    private final Random random = new Random();

    public ServerImplementation(IServerUI serverUI) throws RemoteException, SQLException, NoSuchAlgorithmException {
        this.mConexaoBD = new ManagerConexaoBD();
        jobScheduler = new Timer();
        jobScheduler.scheduleAtFixedRate(new JobScheduler(this,serverUI,
                new ConexaoBD(), new ConexaoBD(),
                new ConexaoBD(), new ConexaoBD()), 10000, 30000);
        this.serverUI = serverUI;
    }

    public ServerImplementation(IServerUI serverUI, String url, String user, String password)
            throws RemoteException, SQLException, NoSuchAlgorithmException {
        this.mConexaoBD = new ManagerConexaoBD(url, user, password);
        jobScheduler = new Timer();
        jobScheduler.scheduleAtFixedRate(new JobScheduler(this,serverUI,
                new ConexaoBD(url, user, password), new ConexaoBD(url, user, password),
                new ConexaoBD(url, user, password), new ConexaoBD(url, user, password)), 10000, 30000);
        this.serverUI = serverUI;
    }

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
            adminClients.add(adminClient);
            return tempUser;
        } else {
            throw new WrongPasswordException();
        }
    }

    @Override
    public boolean logout(IClientAdmin adminClient) throws RemoteException {
        return adminClients.remove(adminClient);
    }

    @Override
    public void newUser(User user, User newUser)
            throws RemoteException, NoPrivilegesException, SQLException, UserNotFoundException, KeyNotReturnedException {
        if (user == null || newUser == null) {
            throw new InvalidParameterException();
        }

        if (user.isPrivilegios()) {
            newUser.setPassword(toMD5(newUser.getPassword()));
            mConexaoBD.newUser(newUser);
        } else {
            throw new NoPrivilegesException();
        }
    }

    @Override
    public void novaOferta(OfertaRecursos ofertaRecursos)
            throws RemoteException, SQLException {
        if (ofertaRecursos == null) {
            throw new InvalidParameterException();
        }
        mConexaoBD.newOfertaRecursos(ofertaRecursos);
        try {
            existemNovasOfertas();
        } catch (OfertaEmpregoNotFoundException | NoPrivilegesException | OfertaRecursosNotFoundException | UserNotLoggedInException ex) {
            serverUI.sendMessage("Tentativa de notificação dos clientes dos administradores falhada.");
        }
    }

    @Override
    public void novaOferta(OfertaEmprego ofertaEmprego, ArrayList<FileTransfer> transferencias)
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

    @Override
    public Oferta getNewReviewCase() throws RemoteException, SQLException, OfertaEmpregoNotFoundException, OfertaRecursosNotFoundException {
        if(random.nextBoolean()){
            try {
                return mConexaoBD.getRecursoToReview();
            } catch (OfertaRecursosNotFoundException ex) {
                return mConexaoBD.getEmpregoToReview();
            }
        } else {
            try {
                return mConexaoBD.getEmpregoToReview();
            } catch (OfertaEmpregoNotFoundException ex) {
                return mConexaoBD.getRecursoToReview();
            }
        }
    }
    
    public void existemNovasOfertas() throws RemoteException, SQLException, OfertaEmpregoNotFoundException, NoPrivilegesException, OfertaRecursosNotFoundException, UserNotLoggedInException{
        for (IClientAdmin ca : adminClients)
            ca.notificarNovosCasos();
    }
}
