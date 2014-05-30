package LogicClasses;

import DataClasses.User;
import LogicClasses.Exceptions.UserNotFoundException;
import LogicClasses.Exceptions.WrongPasswordException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leandro on 29/05/2014.
 */
public class ManagerConexaoBD {
    private List<ConexaoBD> conexoes;
    private MessageDigest messageDigest;

    public ManagerConexaoBD() throws SQLException {
        conexoes = new ArrayList<ConexaoBD>();

        for(int i = 0; i < 5; i++)
            conexoes.add(new ConexaoBD());
    }

    public ManagerConexaoBD(String url, String user, String password) throws SQLException {
        conexoes = new ArrayList<ConexaoBD>();

        for(int i = 0; i < 5; i++)
            conexoes.add(new ConexaoBD(url, user, password));
    }

    private synchronized Connection getConnection() throws SQLException {
        while(true) {
            for (ConexaoBD conexao : conexoes) {
                if (!conexao.isaSerUsado()) {
                    notifyAll();
                    return conexao.getConexao();
                }
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private User getUser(int id) throws SQLException {
        Statement statement = getConnection().createStatement();
        /*
        falta a parte da tabela !!!!!!!!!!
         */
        ResultSet resultSet = statement.executeQuery(/*"select * from NOME_TABELA where NOME = " + nome*/);
        if (resultSet.next()) {
            User newUser = new User(resultSet.getInt("user_id"), resultSet.getString("user_name"), resultSet.getString("user_password"), resultSet.getBoolean("user_privilege"));
            statement.close();
            return newUser;
        } else {
            statement.close();
            return null;
        }
    }

    private String toMD5(String msg) throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("MD5");
        byte[] msgBytes = msg.getBytes();
        messageDigest.reset();
        byte[] newMsgBytes = messageDigest.digest(msgBytes);
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<newMsgBytes.length;i++){
            stringBuffer.append(Integer.toHexString(0xff & newMsgBytes[i]));
        }
        return stringBuffer.toString();
    }

    public User login(User user) throws SQLException, NoSuchAlgorithmException, UserNotFoundException, WrongPasswordException {
        if(user == null)
            throw new UserNotFoundException();

        User tempUser = getUser(user.getId());
        String userPassword = toMD5(user.getPassword());
        if(tempUser.getPassword().equals(userPassword)){
            return user;
        }
        else throw new WrongPasswordException();
    }
}
