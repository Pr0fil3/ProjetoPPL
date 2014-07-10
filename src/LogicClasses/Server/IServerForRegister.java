package LogicClasses.Server;

import DataClasses.FileTransfer;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import LogicClasses.ConexoesBD.Exceptions.KeyNotReturnedException;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface utilizada pelos clientes dos registers para se ligarem ao servidor.
 */
public interface IServerForRegister extends Remote {
    public void novaOferta(OfertaRecursos ofertaRecursos) throws RemoteException, SQLException;
    public void novaOferta(OfertaEmprego ofertaEmprego, List<FileTransfer> transferencias) 
            throws RemoteException, SQLException, KeyNotReturnedException, FileNotFoundException, IOException;
}
