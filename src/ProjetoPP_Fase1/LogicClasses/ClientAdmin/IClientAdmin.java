package ProjetoPP_Fase1.LogicClasses.ClientAdmin;

import ProjetoPP_Fase1.LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import ProjetoPP_Fase1.LogicClasses.Server.Exceptions.NoPrivilegesException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Created by -nikeiZprooo- on 22/06/2014.
 */
public interface IClientAdmin extends Remote {
    public void notificarNovosCasos() throws RemoteException, SQLException, OfertaEmpregoNotFoundException,NoPrivilegesException,OfertaRecursosNotFoundException, UserNotLoggedInException;
}
