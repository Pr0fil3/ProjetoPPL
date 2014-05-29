package LogicClasses;

import java.sql.*;

/**
 * Created by Leandro on 29/05/2014.
 */
public class ConexaoBD {
    private static String DEFAULT_URL = "jdbc:mysql://localhost:3306/projeto_ppl";
    private static String DEFAULT_USER = "root";

    private Connection conexao;
    private boolean aSerUsado;

    public ConexaoBD(String url, String user, String password) throws SQLException {
        this.conexao = DriverManager.getConnection(url, user, password);
        this.aSerUsado = false;
    }

    public ConexaoBD() throws SQLException {
        this(DEFAULT_URL, DEFAULT_USER, "");
    }

    @Override
    protected void finalize() throws Throwable {
        if (conexao != null) {
            conexao.close();
        }
    }

    public Connection getConexao() {
        return conexao;
    }

    public boolean isaSerUsado() {
        return aSerUsado;
    }

    public void setaSerUsado(boolean aSerUsado) {
        this.aSerUsado = aSerUsado;
    }
}
