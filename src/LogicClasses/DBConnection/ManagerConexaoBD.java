package LogicClasses.DBConnection;

import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import com.sun.corba.se.spi.monitoring.StatisticMonitoredAttribute;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leandro on 29/05/2014.
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

    private static String NOME_TABELA_OFERTAS_EMPREGO = "ofertas_emprego";
    private static String EMPREGO_COLUNA_ID = "emprego_id";
    private static String EMPREGO_COLUNA_TITULO = "emprego_titulo";
    private static String EMPREGO_COLUNA_DETALHES = "emprego_detalhes";
    private static String EMPREGO_COLUNA_CANDIDATOS_NECESSARIOS = "emprego_candidatos_necessarios";
    private static String EMPREGO_COLUNA_PERFIL = "emprego_perfil";

    private static String NOME_TABELA_ANEXOS = "anexos";
    private static String ANEXOS_COLUNA_EMPREGO_ID = "emprego_id";
    private static String ANEXOS_COLUNA_PATH = "anexos_path";

    public static enum TABELA {USERS, OFERTAS_RECURSOS, OFERTAS_EMPREGO, TABELA_ANEXOS}

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

    private User userFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            User newUser = new User(
                    resultSet.getInt(USERS_COLUNA_ID),
                    resultSet.getString(USERS_COLUNA_NOME),
                    resultSet.getString(USERS_COLUNA_PASSWORD),
                    resultSet.getBoolean(USERS_COLUNA_PRIVILEGIO));
            return newUser;
        } else {
            return null;
        }
    }

    private OfertaRecursos recursosFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            OfertaRecursos newOfertaRecursos = new OfertaRecursos(
                    resultSet.getInt(RECURSOS_COLUNA_ID),
                    resultSet.getString(RECURSOS_COLUNA_NOME),
                    resultSet.getString(RECURSOS_COLUNA_CONTACTO),
                    OfertaRecursos.parseAreaAtuacao(resultSet.getString(RECURSOS_COLUNA_AREA_ATUACAO)));
            return newOfertaRecursos;
        } else {
            return null;
        }
    }

    public User getUser(int id) throws SQLException {
        ConexaoBD conexao = getConexao();
        try {
            return userFromResultSet(getFromDB(conexao.getStatement(), NOME_TABELA_USERS, USERS_COLUNA_ID, String.valueOf(id)));
        } finally {
            conexao.libertar();
        }
    }

    public User getUser(String name) throws SQLException {
        ConexaoBD conexao = getConexao();
        try {
            return userFromResultSet(getFromDB(getConexao().getStatement(), NOME_TABELA_USERS, USERS_COLUNA_NOME, USERS_COLUNA_NOME));
        }finally {
            conexao.libertar();
        }
    }

    public User createUser(User newUser) throws SQLException {
        ConexaoBD conexao = getConexao();
        try {
            conexao.getStatement().executeUpdate("insert into " + NOME_TABELA_USERS + " values (null,"
                    + newUser.getNome() + ","
                    + newUser.getPassword() + ","
                    + newUser.isPrivilegios());
            return getUser(newUser.getNome());
        }finally {
            conexao.libertar();
        }
    }

    public OfertaRecursos getOfertaRecursos(int id) throws SQLException {
        ConexaoBD conexao = getConexao();
        try {
            return recursosFromResultSet(getFromDB(conexao.getStatement(), NOME_TABELA_OFERTAS_RECURSOS, RECURSOS_COLUNA_ID, String.valueOf(id)));
        } finally {
            conexao.libertar();
        }
    }

    public OfertaRecursos getOfertaRecursos(String nome) throws SQLException {
        ConexaoBD conexao = getConexao();
        try {
            return recursosFromResultSet(getFromDB(conexao.getStatement(), NOME_TABELA_OFERTAS_RECURSOS, RECURSOS_COLUNA_NOME,nome));
        } finally {
            conexao.libertar();
        }
    }

    // PAREI AQUIIIII !!!!!

    public OfertaRecursos newOfertaRecursos(OfertaRecursos ofertaRecursos) throws SQLException {
        ConexaoBD conexaoBD = getConnection();
        Statement statement = conexaoBD.getConnection().createStatement();
        try {
            statement.executeUpdate("insert into users values (null," + ofertaRecursos.getNome() + ","
                    + ofertaRecursos.getContacto() + ","
                    + ofertaRecursos.getAreaAtuacao().toString() + ","
                    + ofertaRecursos.getEstadoOferta().toString());
            return getOfertaRecursos(ofertaRecursos.getNome());
        }finally {
            conexaoBD.libertar();
            statement.close();
        }
    }

    public OfertaEmprego getOfertaEmprego(int id) throws SQLException {
        ConexaoBD conexaoBD = getConnection();
        Statement statement = conexaoBD.getConnection().createStatement();
        try {
            ResultSet resultSet = statement.executeQuery("select * from "
                    + NOME_TABELA_OFERTAS_EMPREGO + " left join " + NOME_TABELA_ANEXOS
                    + " ON " + NOME_TABELA_OFERTAS_EMPREGO + "." + EMPREGO_COLUNA_ID
                    + " = " + NOME_TABELA_ANEXOS + "." + ANEXOS_COLUNA_EMPREGO_ID
                    + " where " + NOME_TABELA_OFERTAS_EMPREGO + "." + EMPREGO_COLUNA_ID + " = " + id);
            if (resultSet.next()) {
                OfertaEmprego ofertaEmprego = new OfertaEmprego(
                        resultSet.getInt(EMPREGO_COLUNA_ID),
                        resultSet.getString(EMPREGO_COLUNA_TITULO),
                        resultSet.getString(EMPREGO_COLUNA_DETALHES),
                        resultSet.getInt(EMPREGO_COLUNA_CANDIDATOS_NECESSARIOS),
                        resultSet.getString(EMPREGO_COLUNA_PERFIL));
                // ADICIONAR OS ANEXOS AQUI
                return ofertaEmprego;
            } else {
                return null;
            }
        } finally {
            conexaoBD.libertar();
            statement.close();
        }
    }

    public OfertaEmprego getOfertaEmprego(String titulo) throws SQLException {
        ConexaoBD conexaoBD = getConnection();
        Statement statement = conexaoBD.getConnection().createStatement();
        try {
            ResultSet resultSet = statement.executeQuery("select * from "
                    + NOME_TABELA_OFERTAS_EMPREGO + " where " + EMPREGO_COLUNA_TITULO + " = " + titulo);
            if (resultSet.next()) {
                return getOfertaEmprego(resultSet.getInt(EMPREGO_COLUNA_ID));
            } else {
                return null;
            }
        } finally {
            conexaoBD.libertar();
            statement.close();
        }
    }

    public OfertaEmprego newOfertaEmprego(OfertaEmprego ofertaEmprego) throws SQLException {
        ConexaoBD conexaoBD = getConnection();
        Statement statement = conexaoBD.getConnection().createStatement();
        try {
            statement.executeUpdate("insert into users values (null," + ofertaEmprego.getTitulo() + ","
                    + ofertaEmprego.getDetalhesOferta() + ","
                    + ofertaEmprego.getNumeroCandidatosNecessarios() + ","
                    + ofertaEmprego.getPerfilCandidatos());
            return getOfertaEmprego(ofertaEmprego.getTitulo());
        }finally {
            conexaoBD.libertar();
            statement.close();
        }
    }
}