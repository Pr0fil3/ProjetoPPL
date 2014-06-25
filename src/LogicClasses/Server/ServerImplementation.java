package LogicClasses.Server;

import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.DBConnection.Exceptions.UserNotFoundException;
import LogicClasses.DBConnection.ManagerConexaoBD;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.WrongPasswordException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.sql.SQLException;

/**
 * Created by -nikeiZprooo- on 30/05/2014.
 */
public class ServerImplementation extends UnicastRemoteObject implements IServerForRegister, IServerForAdmin {
    private ManagerConexaoBD mConexaoBD;
    private MessageDigest messageDigest;
    private IServerUI serverUI;

    public ServerImplementation(IServerUI serverUI) throws RemoteException, SQLException {
        this.mConexaoBD = new ManagerConexaoBD();
        this.serverUI = serverUI;
    }

    public ServerImplementation(IServerUI serverUI, String url, String user, String password) throws RemoteException, SQLException {
        this.mConexaoBD = new ManagerConexaoBD(url, user, password);
        this.serverUI = serverUI;
    }

    private String toMD5(String msg) {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] msgBytes = msg.getBytes();
            messageDigest.reset();
            byte[] newMsgBytes = messageDigest.digest(msgBytes);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < newMsgBytes.length; i++) {
                stringBuffer.append(Integer.toHexString(0xff & newMsgBytes[i]));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
     public User login(User user) throws SQLException, UserNotFoundException, WrongPasswordException {
        if (user == null)
            throw new InvalidParameterException();

        User tempUser = mConexaoBD.getUser(user.getId());

        if (tempUser == null)
            throw new UserNotFoundException();

        String userPassword = toMD5(user.getPassword());
        if (userPassword.equals(tempUser.getPassword())) {
            return tempUser;
        } else throw new WrongPasswordException();
    }

    @Override
    public User newUser(User user, User newUser) throws RemoteException, NoPrivilegesException, SQLException, KeyNotReturnedException, UserNotFoundException {
        if (user == null || newUser == null)
            throw new InvalidParameterException();

        if (user.isPrivilegios()) {
            newUser.setPassword(toMD5(newUser.getPassword()));
            return mConexaoBD.newUser(newUser);
        } else throw new NoPrivilegesException();
    }

    @Override
    public Oferta novaOferta(OfertaRecursos ofertaRecursos) throws RemoteException, SQLException, KeyNotReturnedException, OfertaRecursosNotFoundException {
        if (ofertaRecursos == null)
            throw new InvalidParameterException();
        return mConexaoBD.newOfertaRecursos(ofertaRecursos);
    }

    @Override
    public Oferta novaOferta(OfertaEmprego ofertaEmprego) throws RemoteException, SQLException, KeyNotReturnedException, OfertaEmpregoNotFoundException {
        if (ofertaEmprego == null)
            throw new InvalidParameterException();
        return mConexaoBD.newOfertaEmprego(ofertaEmprego);
    }
}
