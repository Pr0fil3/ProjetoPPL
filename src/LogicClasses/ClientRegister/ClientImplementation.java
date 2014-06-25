package LogicClasses.ClientRegister;

import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import LogicClasses.ClientRegister.Exceptions.*;
import LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.Server.IServerForRegister;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by -nikeiZprooo- on 24/06/2014.
 */
public class ClientImplementation extends UnicastRemoteObject implements IClientRegister {
    private IServerForRegister server;

    public ClientImplementation() throws RemoteException {
        Registry reg = LocateRegistry.getRegistry();
        try {
            this.server = (IServerForRegister) reg.lookup("server");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void novaOfertaRecursos(String nome, String contacto, OfertaRecursos.AREA_ATUACAO areaAtuacao)
            throws NameNotValidException, ContactNotValidException, AreaAtuacaoNotValidException,
            RemoteException, SQLException, KeyNotReturnedException, OfertaRecursosNotFoundException {
        if(nome == null || nome.length() <= 0)
            throw new NameNotValidException();
        if(contacto == null || contacto.length() <= 0)
            throw new ContactNotValidException();
        if(areaAtuacao == null){
            throw new AreaAtuacaoNotValidException();
        }
        server.novaOferta(new OfertaRecursos(nome,contacto,areaAtuacao));
    }

    public void novaOfertaEmprego(String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                                  String perfilCandidatos, List<String> anexos)
            throws TituloNotValidException, DetalhesOfertaNotValidException, NumeroCandidatosNotValidException,
            PerfilCandidatosNotValidException, RemoteException, KeyNotReturnedException,
            OfertaEmpregoNotFoundException, SQLException {
        if(titulo == null || titulo.length() <= 0)
            throw new TituloNotValidException();
        if(detalhesOferta == null || detalhesOferta.length() <= 0)
            throw new DetalhesOfertaNotValidException();
        if(numeroCandidatosNecessarios < 1)
            throw new NumeroCandidatosNotValidException();
        if(perfilCandidatos == null || perfilCandidatos.length() <= 0)
            throw new PerfilCandidatosNotValidException();
        if(anexos == null){
            anexos = new ArrayList<String>();
        }
        server.novaOferta(new OfertaEmprego(titulo,detalhesOferta,numeroCandidatosNecessarios,perfilCandidatos,anexos));
    }
}
