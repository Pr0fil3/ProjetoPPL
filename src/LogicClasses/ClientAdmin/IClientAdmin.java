package LogicClasses.ClientAdmin;

import LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Created by -nikeiZprooo- on 22/06/2014.
 */
public interface IClientAdmin extends Remote {
    public void notificarNovosCasos() throws RemoteException, SQLException, OfertaEmpregoNotFoundException,NoPrivilegesException,OfertaRecursosNotFoundException, UserNotLoggedInException;
}
