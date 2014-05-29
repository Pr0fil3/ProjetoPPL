package LogicClasses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leandro on 29/05/2014.
 */
public class ManagerConexaoBD {
    private List<ConexaoBD> conexoes;

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
        while(true){
            for (ConexaoBD conexao : conexoes)
                if (!conexao.isaSerUsado()) {
                    notifyAll();
                    return conexao.getConexao();
                }

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private User getUser(String nome) throws SQLException {
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from NOME_TABELA where NOME = " + nome);
        // FAZER
    }

    public boolean login(User user){

    }
}
