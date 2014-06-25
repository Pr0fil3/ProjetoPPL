package LogicClasses.Server;

import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Created by Leandro on 29/05/2014.
 */
public interface IServerForRegister extends Remote {
    public Oferta novaOferta(OfertaRecursos ofertaRecursos) throws RemoteException, OfertaRecursosNotFoundException, SQLException, KeyNotReturnedException;
    public Oferta novaOferta(OfertaEmprego ofertaEmprego) throws RemoteException, KeyNotReturnedException, SQLException, OfertaEmpregoNotFoundException;
}
