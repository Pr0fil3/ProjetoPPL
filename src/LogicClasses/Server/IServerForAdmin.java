package LogicClasses.Server;

import DataClasses.Oferta;
import DataClasses.User;
import LogicClasses.ClientAdmin.IClientAdmin;
import LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.DBConnection.Exceptions.UserNotFoundException;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.WrongPasswordException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Created by -nikeiZprooo- on 30/05/2014.
 */
public interface IServerForAdmin extends Remote {
    public User login(IClientAdmin adminClient, User user) throws RemoteException, SQLException, WrongPasswordException, UserNotFoundException;
    public boolean logout(IClientAdmin adminClient) throws RemoteException;
    public void newUser(User user, User newUser) throws RemoteException, NoPrivilegesException, SQLException, UserNotFoundException, KeyNotReturnedException;
    public Oferta getNewReviewCase() throws RemoteException, SQLException, OfertaEmpregoNotFoundException, OfertaRecursosNotFoundException;
}
