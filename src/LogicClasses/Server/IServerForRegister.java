package LogicClasses.Server;

import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import LogicClasses.Server.Exceptions.UserNotFoundException;
import LogicClasses.Server.Exceptions.WrongPasswordException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by Leandro on 29/05/2014.
 */
public interface IServerForRegister extends Remote {
    public User login(User user) throws RemoteException, SQLException, NoSuchAlgorithmException, UserNotFoundException, WrongPasswordException;
    public OfertaRecursos novaOferta(OfertaRecursos ofertaRecursos) throws RemoteException;
    public OfertaRecursos novaOferta(OfertaEmprego ofertaEmprego) throws RemoteException;
}
