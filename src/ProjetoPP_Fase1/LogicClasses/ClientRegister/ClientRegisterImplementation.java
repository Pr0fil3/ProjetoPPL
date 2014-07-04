package ProjetoPP_Fase1.LogicClasses.ClientRegister;

import DataClasses.FileTransfer;
import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import ProjetoPP_Fase1.LogicClasses.ClientRegister.Exceptions.AreaAtuacaoNotValidException;
import ProjetoPP_Fase1.LogicClasses.ClientRegister.Exceptions.ContactNotValidException;
import ProjetoPP_Fase1.LogicClasses.ClientRegister.Exceptions.DetalhesOfertaNotValidException;
import ProjetoPP_Fase1.LogicClasses.ClientRegister.Exceptions.NameNotValidException;
import ProjetoPP_Fase1.LogicClasses.ClientRegister.Exceptions.NumeroCandidatosNotValidException;
import ProjetoPP_Fase1.LogicClasses.ClientRegister.Exceptions.PerfilCandidatosNotValidException;
import ProjetoPP_Fase1.LogicClasses.ClientRegister.Exceptions.TituloNotValidException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import ProjetoPP_Fase1.LogicClasses.Server.IServerForRegister;
import java.io.File;
import java.io.IOException;

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
public class ClientRegisterImplementation extends UnicastRemoteObject {

    private IServerForRegister server;

    public ClientRegisterImplementation() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry();
        this.server = (IServerForRegister) reg.lookup("server");
    }

    public void novaOfertaRecursos(String nome, String contacto, OfertaRecursos.AREA_ATUACAO areaAtuacao)
            throws NameNotValidException, ContactNotValidException, AreaAtuacaoNotValidException,
            RemoteException, SQLException {
        if (nome == null || nome.length() <= 0) 
            throw new NameNotValidException();
        if (contacto == null || contacto.length() <= 0)
            throw new ContactNotValidException();
        if (areaAtuacao == null)
            throw new AreaAtuacaoNotValidException();
        server.novaOferta(new OfertaRecursos(nome, contacto, areaAtuacao));
    }

    public void novaOfertaEmprego(String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
            String perfilCandidatos, List<File> anexos, Oferta.AREA_ATUACAO areaAtuacao)
            throws TituloNotValidException, DetalhesOfertaNotValidException, NumeroCandidatosNotValidException,
            PerfilCandidatosNotValidException, RemoteException, SQLException, IOException, KeyNotReturnedException, AreaAtuacaoNotValidException {
        if (titulo == null || titulo.length() <= 0)
            throw new TituloNotValidException();
        if (detalhesOferta == null || detalhesOferta.length() <= 0)
            throw new DetalhesOfertaNotValidException();
        if (numeroCandidatosNecessarios < 1)
            throw new NumeroCandidatosNotValidException();
        if (perfilCandidatos == null || perfilCandidatos.length() <= 0)
            throw new PerfilCandidatosNotValidException();
        if (areaAtuacao == null)
            throw new AreaAtuacaoNotValidException();
        
        if (anexos == null) {
            server.novaOferta(new OfertaEmprego(titulo, detalhesOferta, numeroCandidatosNecessarios, perfilCandidatos, areaAtuacao, new ArrayList<String>()), null);
        } else {
            ArrayList<FileTransfer> listaTransferencias = new ArrayList<>();
            for (File ficheiro : anexos)
                if (ficheiro.isFile() && ficheiro.canRead()) {
                    FileTransfer fileTransfer = new FileTransfer(ficheiro);
                    listaTransferencias.add(fileTransfer);
                }
            server.novaOferta(new OfertaEmprego(titulo, detalhesOferta, numeroCandidatosNecessarios, perfilCandidatos, areaAtuacao, new ArrayList<String>()), listaTransferencias);
        }
    }
}
