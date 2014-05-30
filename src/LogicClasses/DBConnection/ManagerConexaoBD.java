package LogicClasses.DBConnection;

import DataClasses.OfertaRecursos;
import DataClasses.User;

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

    private List<ConexaoBD> conexoes = new ArrayList<ConexaoBD>();

    public ManagerConexaoBD() throws SQLException {
        for(int i = 0; i < 5; i++)
            conexoes.add(new ConexaoBD());
    }

    public ManagerConexaoBD(String url, String user, String password) throws SQLException {
        for(int i = 0; i < 5; i++)
            conexoes.add(new ConexaoBD(url, user, password));
    }

    private synchronized ConexaoBD getConnection() throws SQLException {
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

    public User getUser(int id) throws SQLException {
        ConexaoBD conexaoBD = getConnection();
        Statement statement = conexaoBD.getConnection().createStatement();
        try {
            ResultSet resultSet = statement.executeQuery("select * from "
                    + NOME_TABELA_USERS + " where " + USERS_COLUNA_ID + " = " + id);
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
        } finally {
            conexaoBD.libertar();
            statement.close();
        }
    }

    public User getUser(String name) throws SQLException {
        ConexaoBD conexaoBD = getConnection();
        Statement statement = conexaoBD.getConnection().createStatement();
        try {
            ResultSet resultSet = statement.executeQuery("select * from "
                    + NOME_TABELA_USERS + " where " + USERS_COLUNA_NOME + " = " + name);
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
        }finally {
            conexaoBD.libertar();
            statement.close();
        }

    }

    public User createUser(User newUser) throws SQLException {
        ConexaoBD conexaoBD = getConnection();
        Statement statement = conexaoBD.getConnection().createStatement();
        try {
            statement.executeUpdate("insert into users values (null," + newUser.getNome() + ","
                    + newUser.getPassword() + "," + newUser.isPrivilegios());
            return getUser(newUser.getNome());
        }finally {
            conexaoBD.libertar();
            statement.close();
        }
    }

    public OfertaRecursos getOfertaRecursos(int id){

    }
}