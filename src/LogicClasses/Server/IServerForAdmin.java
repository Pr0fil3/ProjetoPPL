package LogicClasses.Server;

import DataClasses.FileTransfer;
import DataClasses.Oferta;
import DataClasses.User;
import LogicClasses.ClientAdmin.IClientAdmin;
import LogicClasses.ConexoesBD.Exceptions.KeyNotReturnedException;
import LogicClasses.ConexoesBD.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.UserNotFoundException;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.WrongPasswordException;
import java.io.IOException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by -nikeiZprooo- on 30/05/2014.
 */
public interface IServerForAdmin extends Remote {
    public User login(IClientAdmin adminClient, User user) throws RemoteException, SQLException, WrongPasswordException, UserNotFoundException;
    public boolean logout(IClientAdmin adminClient) throws RemoteException;
    public User newUser(User user, User newUser) throws RemoteException, NoPrivilegesException, SQLException, UserNotFoundException, KeyNotReturnedException;
    public Oferta getNewReviewCase() throws RemoteException, SQLException, OfertaEmpregoNotFoundException, OfertaRecursosNotFoundException;
    public boolean cancelReview(Oferta oferta) throws RemoteException;
    public void avaliarCaso(Oferta oferta, boolean avaliacao) throws RemoteException, SQLException;
    public List<FileTransfer> getAnexos(int id) throws RemoteException, SQLException, IOException;
}
