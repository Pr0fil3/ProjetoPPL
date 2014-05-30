package LogicClasses.Server;

import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import LogicClasses.DBConnection.ManagerConexaoBD;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.UserNotFoundException;
import LogicClasses.Server.Exceptions.WrongPasswordException;

import javax.naming.NoPermissionException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by -nikeiZprooo- on 30/05/2014.
 */
public class ServerImplementation extends UnicastRemoteObject implements IServerForRegister, IServerForAdmin {
    private ManagerConexaoBD mConexaoBD;
    private MessageDigest messageDigest;

    public ServerImplementation() throws RemoteException, SQLException {
        mConexaoBD = new ManagerConexaoBD();
    }

    public ServerImplementation(String url, String user, String password) throws RemoteException, SQLException {
        this.mConexaoBD = new ManagerConexaoBD(url, user, password);
    }

    private String toMD5(String msg) {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] msgBytes = msg.getBytes();
            messageDigest.reset();
            byte[] newMsgBytes = messageDigest.digest(msgBytes);
            StringBuffer stringBuffer = new StringBuffer();
            for(int i=0;i<newMsgBytes.length;i++){
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
        if(user == null)
            throw new InvalidParameterException();

        User tempUser = mConexaoBD.getUser(user.getId());

        if(tempUser == null)
            throw new UserNotFoundException();

        String userPassword = toMD5(user.getPassword());
        if(tempUser.getPassword().equals(userPassword)){
            return user;
        }
        else throw new WrongPasswordException();
    }

    @Override
    public User newUser(User user, User newUser) throws RemoteException, NoPrivilegesException, SQLException {
        if (user == null || newUser == null)
            throw new InvalidParameterException();

        if (user.isPrivilegios()) {
            newUser.setPassword(toMD5(newUser.getPassword()));
            return mConexaoBD.createUser(newUser);
        } else throw new NoPrivilegesException();
    }

    public OfertaRecursos novaOferta(OfertaRecursos ofertaRecursos) throws RemoteException {
        return null;
    }

    @Override
    public OfertaRecursos novaOferta(OfertaEmprego ofertaEmprego) throws RemoteException {
        return null;
    }
}
