package ProjetoPP_Fase1.LogicClasses.Server;

import DataClasses.Oferta;
import DataClasses.User;
import ProjetoPP_Fase1.LogicClasses.ClientAdmin.IClientAdmin;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.UserNotFoundException;
import ProjetoPP_Fase1.LogicClasses.Server.Exceptions.NoPrivilegesException;
import ProjetoPP_Fase1.LogicClasses.Server.Exceptions.WrongPasswordException;

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
