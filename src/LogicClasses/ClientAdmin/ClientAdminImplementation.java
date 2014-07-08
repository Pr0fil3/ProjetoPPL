/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicClasses.ClientAdmin;

import DataClasses.FileTransfer;
import DataClasses.Oferta;
import DataClasses.User;
import LogicClasses.ClientAdmin.Exceptions.PasswordNotValidException;
import LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import LogicClasses.ClientAdmin.Exceptions.UserNotValidException;
import LogicClasses.ConexoesBD.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.UserNotFoundException;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.WrongPasswordException;
import LogicClasses.Server.IServerForAdmin;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author -nikeiZprooo-
 */
public class ClientAdminImplementation extends UnicastRemoteObject implements IClientAdmin {

    private final IServerForAdmin server;
    private final AdminAPP adminUI;
    private User userLogado;
    private Oferta reviewAtual;

    public ClientAdminImplementation(AdminAPP admin) throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry();
        this.server = (IServerForAdmin) reg.lookup("server");
        this.adminUI = admin;
    }

    public boolean isLoggedIn() {
        return userLogado != null;
    }

    public User login(int userID, String password) throws RemoteException,
            SQLException, WrongPasswordException, UserNotFoundException,
            PasswordNotValidException, UserNotValidException {
        if (userID < 1) {
            throw new UserNotValidException();
        }
        if (password == null || password.length() < 1) {
            throw new PasswordNotValidException();
        }

        userLogado = server.login(this, new User(userID, password));
        return userLogado;
    }

    public boolean logout() throws RemoteException {
        if (userLogado != null) {
            if (reviewAtual != null) {
                cancelarReview();
            }
            boolean b = server.logout(this);
            userLogado = null;
            return b;
        }
        return false;
    }

    public List<FileTransfer> getAnexos(int id) throws SQLException, IOException {
        return server.getAnexos(id);
    }

    public Oferta receberNovoCaso() throws RemoteException, SQLException,
            OfertaEmpregoNotFoundException, OfertaRecursosNotFoundException,
            UserNotLoggedInException, NoPrivilegesException {
        if (userLogado == null) {
            throw new UserNotLoggedInException();
        } else if (userLogado.isPrivilegios()) {
            reviewAtual = server.getNewReviewCase();
            return reviewAtual;
        } else {
            throw new NoPrivilegesException();
        }
    }

    public void avaliarCaso(boolean avaliacao) throws SQLException, RemoteException {
        server.avaliarCaso(reviewAtual, avaliacao);
        reviewAtual = null;
    }

    private boolean cancelarReview() throws RemoteException {
        boolean b = server.cancelReview(reviewAtual);
        reviewAtual = null;
        return b;
    }

    @Override
    public void notificarNovosCasos() throws RemoteException,
            SQLException, OfertaEmpregoNotFoundException,
            NoPrivilegesException, OfertaRecursosNotFoundException,
            UserNotLoggedInException, IOException {
        if (reviewAtual == null) {
            adminUI.apresentarCaso(receberNovoCaso());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.logout();
    }

    @Override
    public User getUser() throws RemoteException {
        return userLogado;
    }
}
