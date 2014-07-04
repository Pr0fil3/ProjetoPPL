package ProjetoPP_Fase1.LogicClasses.DBConnection;

import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import ProjetoPP_Fase1.LogicClasses.DBConnection.Exceptions.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe
 */
public class ManagerConexaoBD {
    public static String NOME_TABELA_USERS = "Users";
    public static String USERS_COLUNA_ID = "user_id";
    public static String USERS_COLUNA_NOME = "user_name";
    public static String USERS_COLUNA_PASSWORD = "user_password";
    public static String USERS_COLUNA_PRIVILEGIO = "user_privilege";

    public static String NOME_TABELA_OFERTAS_RECURSOS = "ofertas_recursos";
    public static String RECURSOS_COLUNA_ID = "recursos_id";
    public static String RECURSOS_COLUNA_NOME = "recursos_nome";
    public static String RECURSOS_COLUNA_CONTACTO = "recursos_contacto";
    public static String RECURSOS_COLUNA_AREA_ATUACAO = "recursos_area_atuacao";
    public static String RECURSOS_COLUNA_EMPREGO_ID = "emprego_id";
    public static String RECURSOS_COLUNA_ESTADO_OFERTA = "recursos_estado_oferta";
    public static String RECURSOS_COLUNA_IS_NEW = "is_new";

    public static String NOME_TABELA_OFERTAS_EMPREGO = "ofertas_emprego";
    public static String EMPREGO_COLUNA_ID = "emprego_id";
    public static String EMPREGO_COLUNA_TITULO = "emprego_titulo";
    public static String EMPREGO_COLUNA_DETALHES = "emprego_detalhes";
    public static String EMPREGO_COLUNA_CANDIDATOS_NECESSARIOS = "emprego_candidatos_necessarios";
    public static String EMPREGO_COLUNA_PERFIL = "emprego_perfil";
    public static String EMPREGO_COLUNA_AREA_ATUACAO = "emprego_area_atuacao";
    public static String EMPREGO_COLUNA_ESTADO_OFERTA = "emprego_estado";

    public static String NOME_TABELA_ANEXOS = "anexos";
    public static String ANEXOS_COLUNA_EMPREGO_ID = "emprego_id";
    public static String ANEXOS_COLUNA_PATH = "anexo_path";

    private List<ConexaoBD> conexoes = new ArrayList<>();

    public ManagerConexaoBD() throws SQLException {
        for(int i = 0; i < 5; i++)
            conexoes.add(new ConexaoBD());
    }

    public ManagerConexaoBD(String url, String user, String password) throws SQLException {
        for(int i = 0; i < 5; i++)
            conexoes.add(new ConexaoBD(url, user, password));
    }

    private synchronized ConexaoBD getConexao() throws SQLException {
        while(true) {
            for (ConexaoBD conexao : conexoes) {
                if (!conexao.isaSerUsado()) {
                    notifyAll();
                    return conexao;
                }
            }
            try {
                wait();
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "erro em getConnection()");
            }
        }
    }

    private ResultSet getFromDB(Statement statement, String tabela, String condicao, String variavel) throws SQLException {
        return statement.executeQuery("select * from " + tabela + " where " + condicao + " = " + variavel);
    }

    /////////////////////
    // FROM RESULT SET //
    /////////////////////

    public static List<User> usersFromResultSet(ResultSet resultSet) throws SQLException {
        List users = new ArrayList<>();
        while (resultSet.next()) {
            User newUser = new User(
                    resultSet.getInt(USERS_COLUNA_ID),
                    resultSet.getString(USERS_COLUNA_NOME),
                    resultSet.getString(USERS_COLUNA_PASSWORD),
                    resultSet.getBoolean(USERS_COLUNA_PRIVILEGIO));
            users.add(newUser);
        }
        return users;
    }

    public static List<OfertaRecursos> recursosFromResultSet(ResultSet resultSet) throws SQLException {
        List recursos = new ArrayList<>();
        while (resultSet.next()) {
            OfertaRecursos newOfertaRecursos = new OfertaRecursos(
                    resultSet.getInt(RECURSOS_COLUNA_ID),
                    resultSet.getString(RECURSOS_COLUNA_NOME),
                    resultSet.getString(RECURSOS_COLUNA_CONTACTO),
                    resultSet.getInt(RECURSOS_COLUNA_EMPREGO_ID),
                    OfertaRecursos.parseAreaAtuacao(resultSet.getString(RECURSOS_COLUNA_AREA_ATUACAO)),
                    OfertaRecursos.parseEstadoOferta(resultSet.getString(RECURSOS_COLUNA_ESTADO_OFERTA)));
            recursos.add(newOfertaRecursos);
        }
        return recursos;
    }

    public List<OfertaEmprego> empregoAndAnexosFromResultSet(ResultSet resultSet) throws SQLException {
        List emprego = new ArrayList<>();
        while (resultSet.next()){
            int id = resultSet.getInt(EMPREGO_COLUNA_ID);

            // Ir buscar os anexos do emprego
            ConexaoBD conexao = getConexao();
            List<String> anexos = new ArrayList<>();
            try{
                anexos = anexosFromResultSet(getFromDB(conexao.getStatement(),NOME_TABELA_ANEXOS, ANEXOS_COLUNA_EMPREGO_ID, String.valueOf(id)));
            } finally {
                conexao.libertar();
            }

            OfertaEmprego newOfertaEmprego = new OfertaEmprego(
                    id,
                    resultSet.getString(EMPREGO_COLUNA_TITULO),
                    resultSet.getString(EMPREGO_COLUNA_DETALHES),
                    resultSet.getInt(EMPREGO_COLUNA_CANDIDATOS_NECESSARIOS),
                    resultSet.getString(EMPREGO_COLUNA_PERFIL),
                    anexos,
                    OfertaEmprego.parseAreaAtuacao(resultSet.getString(EMPREGO_COLUNA_AREA_ATUACAO)),
                    OfertaEmprego.parseEstadoOferta(resultSet.getString(EMPREGO_COLUNA_ESTADO_OFERTA)));
            emprego.add(newOfertaEmprego);
        }
        return emprego;
    }
    
    public static List<OfertaEmprego> empregoFromResultSet(ResultSet resultSet) throws SQLException {
        List emprego = new ArrayList<>();
        while (resultSet.next()){
            OfertaEmprego newOfertaEmprego = new OfertaEmprego(
                    resultSet.getInt(EMPREGO_COLUNA_ID),
                    resultSet.getString(EMPREGO_COLUNA_TITULO),
                    resultSet.getString(EMPREGO_COLUNA_DETALHES),
                    resultSet.getInt(EMPREGO_COLUNA_CANDIDATOS_NECESSARIOS),
                    resultSet.getString(EMPREGO_COLUNA_PERFIL),
                    new ArrayList<String>(),
                    OfertaEmprego.parseAreaAtuacao(resultSet.getString(EMPREGO_COLUNA_AREA_ATUACAO)),
                    OfertaEmprego.parseEstadoOferta(resultSet.getString(EMPREGO_COLUNA_ESTADO_OFERTA)));
            emprego.add(newOfertaEmprego);
        }
        return emprego;
    }

    private List<String> anexosFromResultSet(ResultSet resultSet) throws SQLException {
        List<String> anexos = new ArrayList<>();
        while (resultSet.next()){
            anexos.add(resultSet.getString(ANEXOS_COLUNA_PATH));
        }
        return anexos;
    }

    /////////
    // NEW //
    /////////

    public User newUser(User newUser) throws SQLException, UserNotFoundException, KeyNotReturnedException {
        ConexaoBD conexao = getConexao();
        try {
            conexao.getStatement().executeUpdate("insert into " + NOME_TABELA_USERS + " values (null,"
                    + newUser.getNome() + ","
                    + newUser.getPassword() + ","
                    + newUser.isPrivilegios() + ")"
                    , Statement.RETURN_GENERATED_KEYS);
            ResultSet keys = conexao.getStatement().getGeneratedKeys();
            if(keys.next()){
                return getUser(keys.getInt(1));
            } throw new KeyNotReturnedException();
        }finally {
            conexao.libertar();
        }
    }

    public void newOfertaRecursos(OfertaRecursos newOfertaRecursos) 
            throws SQLException {
        ConexaoBD conexao = getConexao();
        try {
            conexao.getStatement().executeUpdate("INSERT INTO "
                    + NOME_TABELA_OFERTAS_RECURSOS + "(" 
                    + RECURSOS_COLUNA_NOME + ","
                    + RECURSOS_COLUNA_CONTACTO + ","
                    + RECURSOS_COLUNA_AREA_ATUACAO + ","
                    + RECURSOS_COLUNA_ESTADO_OFERTA 
                    + ") VALUES ("
                    + "'" + newOfertaRecursos.getNome() + "'"  + ","
                    + "'" + newOfertaRecursos.getContacto() + "'" + ","
                    + "'" + newOfertaRecursos.getAreaAtuacao().toString() + "'" + ","
                    + "'" + newOfertaRecursos.getEstadoOferta().toString() + "');");
        }finally {
            conexao.libertar();
        }
    }

    public int newOfertaEmprego(OfertaEmprego newOfertaEmprego) 
            throws SQLException, KeyNotReturnedException {
        ConexaoBD conexao = getConexao();
        try {
            conexao.getStatement().executeUpdate("INSERT INTO " 
                    + NOME_TABELA_OFERTAS_EMPREGO + "("
                    + EMPREGO_COLUNA_TITULO + ","
                    + EMPREGO_COLUNA_DETALHES + ","
                    + EMPREGO_COLUNA_CANDIDATOS_NECESSARIOS + ","
                    + EMPREGO_COLUNA_PERFIL + ","
                    + EMPREGO_COLUNA_AREA_ATUACAO + ","
                    + EMPREGO_COLUNA_ESTADO_OFERTA
                    + ") values ("
                    + "'" + newOfertaEmprego.getTitulo() + "'" + ","
                    + "'" + newOfertaEmprego.getDetalhesOferta() + "'" + ","
                    + "'" + newOfertaEmprego.getNumeroCandidatosNecessarios() + "'" + ","
                    + "'" + newOfertaEmprego.getPerfilCandidatos() + "'" + ","
                    + "'" + newOfertaEmprego.getAreaAtuacao().toString() + "'" + ","
                    + "'" + newOfertaEmprego.getEstadoOferta().toString() + "');"
                    , Statement.RETURN_GENERATED_KEYS);
            ResultSet keys = conexao.getStatement().getGeneratedKeys();
            if(keys.next()){
                return keys.getInt(1);
            } throw new KeyNotReturnedException();
        }finally {
            conexao.libertar();
        }
    }
    
    public void newAnexos(ArrayList<String> anexos, int idEmprego) 
            throws SQLException{
        ConexaoBD conexao = getConexao();
        try {
            for(String anexoPath : anexos) {
                String tempPath = anexoPath.replace("\\", "\\\\");
                conexao.getStatement().executeUpdate("INSERT INTO "
                        + NOME_TABELA_ANEXOS + "("
                        + ANEXOS_COLUNA_EMPREGO_ID + ","
                        + ANEXOS_COLUNA_PATH
                        + ") values ("
                        + "'" + idEmprego + "'" + ","
                        + "'" + tempPath + "'" + ");");
            }
        } finally {
            conexao.libertar();
        }
    }

    /////////////////
    // GET FROM BD //
    /////////////////

    public User getUser(int id) throws SQLException, UserNotFoundException {
        ConexaoBD conexao = getConexao();
        try {
            for(User u : usersFromResultSet(getFromDB(conexao.getStatement(), NOME_TABELA_USERS, USERS_COLUNA_ID, String.valueOf(id))))
                return u;
            throw new UserNotFoundException();
        } finally {
            conexao.libertar();
        }
    }

    public OfertaRecursos getOfertaRecursos(int id) throws SQLException, OfertaRecursosNotFoundException {
        ConexaoBD conexao = getConexao();
        try {
            for(OfertaRecursos oR : recursosFromResultSet(getFromDB(conexao.getStatement(), NOME_TABELA_OFERTAS_RECURSOS, RECURSOS_COLUNA_ID, String.valueOf(id))))
                return oR;
            throw new OfertaRecursosNotFoundException();
        } finally {
            conexao.libertar();
        }
    }

    public OfertaEmprego getOfertaEmprego(int id) throws SQLException, OfertaEmpregoNotFoundException {
        ConexaoBD conexao = getConexao();
        try {
            for (OfertaEmprego oE : empregoAndAnexosFromResultSet(getFromDB(conexao.getStatement(), NOME_TABELA_OFERTAS_EMPREGO, EMPREGO_COLUNA_ID, String.valueOf(id))))
                return oE;
            throw new OfertaEmpregoNotFoundException();
        } finally {
            conexao.libertar();
        }
    }
    
    //////////////////////
    // GET REVIEW CASES //
    //////////////////////
    
    public OfertaEmprego getEmpregoToReview() throws SQLException, OfertaEmpregoNotFoundException{
        ConexaoBD conexao = getConexao();
        try{
            for (OfertaEmprego oferta : empregoAndAnexosFromResultSet(conexao.getStatement().executeQuery("select *"
                    + " from " + NOME_TABELA_OFERTAS_EMPREGO
                    + " where " + EMPREGO_COLUNA_ESTADO_OFERTA + " = " + Oferta.ESTADO_OFERTA.APROVACAO_PROVISORIA.toString()))){
                return oferta;
            }
            throw new OfertaEmpregoNotFoundException();
        } finally {
            conexao.libertar();
        }
    }
    
    public OfertaRecursos getRecursoToReview() throws SQLException, OfertaRecursosNotFoundException{
        ConexaoBD conexao = getConexao();
        try {
            for (OfertaRecursos oferta : recursosFromResultSet(conexao.getStatement().executeQuery("select *"
                    + " from " + NOME_TABELA_OFERTAS_RECURSOS
                    + " where " + RECURSOS_COLUNA_ESTADO_OFERTA + " = " + Oferta.ESTADO_OFERTA.TERMINADO.toString()))){
                return oferta;
            }
            throw new OfertaRecursosNotFoundException();
        } finally {
            conexao.libertar();
        }
    }
}