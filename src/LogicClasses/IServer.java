package LogicClasses;

import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;

/**
 * Created by Leandro on 29/05/2014.
 */
public interface IServer {
    public boolean login(User user);
    public boolean newUser(User user, User newUser);
    public boolean novaOferta(OfertaRecursos ofertaRecursos);
    public boolean novaOferta(OfertaEmprego ofertaEmprego);
}
