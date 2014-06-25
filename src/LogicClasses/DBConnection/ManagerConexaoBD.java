package LogicClasses.DBConnection;

import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import LogicClasses.DBConnection.Exceptions.KeyNotReturnedException;
import LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.DBConnection.Exceptions.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe
 */
public class ManagerConexaoBD {
    private static String NOME_TABELA_USERS = "Users";
    private static String USERS_COLUNA_ID = "user_id";
    private static String USERS_COLUNA_NOME = "user_name";
    private static String USERS_COLUNA_PASSWORD = "user_password";
    private static String USERS_COLUNA_PRIVILEGIO = "user_privilege";

    private static String NOME_TABELA_OFERTAS_RECURSOS = "ofertas_recursos";
    private static String RECURSOS_COLUNA_ID = "recursos_id";
    private static String RECURSOS_COLUNA_NOME = "recursos_nome";
    private static String RECURSOS_COLUNA_CONTACTO = "recursos_contacto";
    private static String RECURSOS_COLUNA_AREA_ATUACAO = "recursos_area_atuacao";
    private static String RECURSOS_COLUNA_EMPREGO_ID = "emprego_id";
    private static String RECURSOS_COLUNA_ESTADO_OFERTA = "recursos_estado_oferta";

    private static String NOME_TABELA_OFERTAS_EMPREGO = "ofertas_emprego";
    private static String EMPREGO_COLUNA_ID = "emprego_id";
    private static String EMPREGO_COLUNA_TITULO = "emprego_titulo";
    private static String EMPREGO_COLUNA_DETALHES = "emprego_detalhes";
    private static String EMPREGO_COLUNA_CANDIDATOS_NECESSARIOS = "emprego_candidatos_necessarios";
    private static String EMPREGO_COLUNA_PERFIL = "emprego_perfil";
    private static String EMPREGO_COLUNA_ESTADO_OFERTA = "emprego_estado";

    private static String NOME_TABELA_ANEXOS = "anexos";
    private static String ANEXOS_COLUNA_EMPREGO_ID = "emprego_id";
    private static String ANEXOS_COLUNA_PATH = "anexos_path";

    private List<ConexaoBD> conexoes = new ArrayList<ConexaoBD>();

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
                e.printStackTrace();
            }
        }
    }

    private ResultSet getFromDB(Statement statement, String tabela, String condicao, String variavel) throws SQLException {
        return statement.executeQuery("select * from " + tabela + " where " + condicao + " = " + variavel);
    }

    /////////////////////
    // FROM RESULT SET //
    /////////////////////

    private List<User> usersFromResultSet(ResultSet resultSet) throws SQLException {
        List users = new ArrayList<User>();
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

    private List<OfertaRecursos> recursosFromResultSet(ResultSet resultSet) throws SQLException {
        List recursos = new ArrayList<OfertaRecursos>();
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

    private List<OfertaEmprego> empregoFromResultSet(ResultSet resultSet) throws SQLException {
        List emprego = new ArrayList<OfertaRecursos>();
        while (resultSet.next()){
            int id = resultSet.getInt(EMPREGO_COLUNA_ID);

            // Ir buscar os anexos do emprego
            ConexaoBD conexao = getConexao();
            List<String> anexos = new ArrayList<String>();
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
                    OfertaEmprego.parseEstadoOferta(resultSet.getString(EMPREGO_COLUNA_ESTADO_OFERTA)));
            emprego.add(newOfertaEmprego);
        }
        return emprego;
    }

    private List<String> anexosFromResultSet(ResultSet resultSet) throws SQLException {
        List<String> anexos = new ArrayList<String>();
        while (resultSet.next()){
            anexos.add(resultSet.getString(ANEXOS_COLUNA_PATH));
        }
        return anexos;
    }

    /////////
    // NEW //
    /////////

    public User newUser(User newUser) throws SQLException, KeyNotReturnedException, UserNotFoundException {
        ConexaoBD conexao = getConexao();
        try {
            conexao.getStatement().executeUpdate("insert into " + NOME_TABELA_USERS + " values (null,"
                    + newUser.getNome() + ","
                    + newUser.getPassword() + ","
                    + newUser.isPrivilegios());
            ResultSet keys = conexao.getStatement().getGeneratedKeys();
            if (keys.next()){
                return getUser(keys.getInt(1));
            } else throw new KeyNotReturnedException("New User didn't return key");
        }finally {
            conexao.libertar();
        }
    }

    public OfertaRecursos newOfertaRecursos(OfertaRecursos newOfertaRecursos) throws SQLException, KeyNotReturnedException, OfertaRecursosNotFoundException {
        ConexaoBD conexao = getConexao();
        try {
            conexao.getStatement().executeUpdate("insert into " + NOME_TABELA_OFERTAS_RECURSOS + " values (null,"
                    + newOfertaRecursos.getNome() + ","
                    + newOfertaRecursos.getContacto() + ","
                    + newOfertaRecursos.getAreaAtuacao().toString() + ","
                    + newOfertaRecursos.getEstadoOferta().toString()+ ","
                    + null);
            ResultSet keys = conexao.getStatement().getGeneratedKeys();
            if (keys.next()){
                return getOfertaRecursos(keys.getInt(1));
            } else throw new KeyNotReturnedException("New OfertaRecursos didn't return key");
        }finally {
            conexao.libertar();
        }
    }

    public OfertaEmprego newOfertaEmprego(OfertaEmprego newOfertaEmprego) throws SQLException, OfertaEmpregoNotFoundException, KeyNotReturnedException {
        ConexaoBD conexao = getConexao();
        try {
            conexao.getStatement().executeUpdate("insert into " + NOME_TABELA_OFERTAS_EMPREGO + " values (null,"
                    + newOfertaEmprego.getTitulo() + ","
                    + newOfertaEmprego.getDetalhesOferta() + ","
                    + newOfertaEmprego.getNumeroCandidatosNecessarios() + ","
                    + newOfertaEmprego.getPerfilCandidatos() + ","
                    + newOfertaEmprego.getEstadoOferta().toString());
            ResultSet keys = conexao.getStatement().getGeneratedKeys();
            if (keys.next()){
                int i = keys.getInt(1);
                for(String path : newOfertaEmprego.getAnexos()){
                    conexao.getStatement().executeUpdate("insert into " + NOME_TABELA_ANEXOS + " values ("
                            + i + ","
                            + path);
                }
                return getOfertaEmprego(i);
            } else throw new KeyNotReturnedException("New OfertaEmprego didn't return key");
        }finally {
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
            for (OfertaEmprego oE : empregoFromResultSet(getFromDB(conexao.getStatement(), NOME_TABELA_OFERTAS_EMPREGO, EMPREGO_COLUNA_ID, String.valueOf(id))))
                return oE;
            throw new OfertaEmpregoNotFoundException();
        } finally {
            conexao.libertar();
        }
    }
}