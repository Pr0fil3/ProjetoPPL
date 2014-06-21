package LogicClasses.Server;

import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Leandro on 29/05/2014.
 */
public interface IServerForRegister extends Remote {
    public OfertaRecursos novaOferta(OfertaRecursos ofertaRecursos) throws RemoteException;
    public OfertaRecursos novaOferta(OfertaEmprego ofertaEmprego) throws RemoteException;
}
