/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LogicClasses.ClientAdmin;

import DataClasses.Oferta;
import DataClasses.User;
import LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.DBConnection.Exceptions.UserNotFoundException;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.Exceptions.WrongPasswordException;
import LogicClasses.Server.IServerForAdmin;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

/**
 *
 * @author -nikeiZprooo-
 */
public class ClientAdminImplementation implements IClientAdmin{
    private boolean isIdle = true;
    
    private final IServerForAdmin server;
    private final AdminAPP adminUI;
    private User userLogado;

    public ClientAdminImplementation(AdminAPP admin) throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry();
        this.server = (IServerForAdmin) reg.lookup("server");
        this.adminUI = admin;
    }
    
    public User login(String user, String password) throws RemoteException,
            SQLException, WrongPasswordException, UserNotFoundException{
        userLogado = server.login(this, new User(user, password));
        return userLogado;
    }
    
    public boolean logout() throws RemoteException{
        userLogado = null;
        return server.logout(this);
    }
    
    public Oferta analisarCaso() throws RemoteException, SQLException,
            OfertaEmpregoNotFoundException, OfertaRecursosNotFoundException,
            UserNotLoggedInException, NoPrivilegesException{
        if(userLogado == null)
            throw new UserNotLoggedInException();
        else if (userLogado.isPrivilegios()){
            isIdle = false;
            return server.getNewReviewCase();
        } else
            throw new NoPrivilegesException();
    }
    
    @Override
    public void notificarNovosCasos() throws RemoteException, SQLException, OfertaEmpregoNotFoundException,NoPrivilegesException,OfertaRecursosNotFoundException, UserNotLoggedInException {
        if (userLogado != null && userLogado.isPrivilegios() && isIdle){
            adminUI.apresentarCaso(analisarCaso());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        logout();
    }
}
