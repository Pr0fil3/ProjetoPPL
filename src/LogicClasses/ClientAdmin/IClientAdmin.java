package LogicClasses.ClientAdmin;

import DataClasses.User;
import LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import LogicClasses.ConexoesBD.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Interface entre a Implementação do cliente e o servidor
 */
public interface IClientAdmin extends Remote {
    public void notificarNovosCasos() throws RemoteException, SQLException,
            OfertaEmpregoNotFoundException,NoPrivilegesException,
            OfertaRecursosNotFoundException, UserNotLoggedInException,
            IOException;
    public User getUser() throws RemoteException;
}
