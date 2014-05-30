package LogicClasses.Server;

import DataClasses.User;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.UserNotFoundException;
import LogicClasses.Server.Exceptions.WrongPasswordException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Created by -nikeiZprooo- on 30/05/2014.
 */
public interface IServerForAdmin extends Remote {
    public User login(User user) throws RemoteException, SQLException, UserNotFoundException, WrongPasswordException;
    public User newUser(User user, User newUser) throws RemoteException, NoPrivilegesException, SQLException;
}
