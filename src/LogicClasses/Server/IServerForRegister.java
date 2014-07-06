package LogicClasses.Server;

import DataClasses.FileTransfer;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Leandro on 29/05/2014.
 */
public interface IServerForRegister extends Remote {
    public void novaOferta(OfertaRecursos ofertaRecursos) throws RemoteException, SQLException;
    public void novaOferta(OfertaEmprego ofertaEmprego, ArrayList<FileTransfer> transferencias) 
            throws RemoteException, SQLException, KeyNotReturnedException, FileNotFoundException, IOException;
}
