package LogicClasses.DBConnection;

import java.sql.*;

/**
 * A instaciação desta classe cria uma ligação à base de dados
 */
public class ConexaoBD {
    private static String DEFAULT_URL = "jdbc:mysql://localhost:3306/projeto_ppl";
    private static String DEFAULT_USER = "root";

    private Connection connection;
    private boolean aSerUsado;

    /**
     * Permite escolher a base de dados
     * @param url da BD
     * @param user de login da BD
     * @param password de login da BD
     * @throws SQLException
     */
    public ConexaoBD(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
        this.aSerUsado = false;
    }

    /**
     * Conecta-se à base de dados usando DEFAULT_URL e DEFAULT_USER
     * @throws SQLException
     */
    public ConexaoBD() throws SQLException {
        this(DEFAULT_URL, DEFAULT_USER, "");
    }

    /**
     * Fecha a conexão e liberta recursos na BD
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        if (connection != null) {
            connection.close();
        }
    }

    public Connection getConnection() {
        aSerUsado = true;
        return connection;
    }

    /**
     * É chamado quando esta conexão não é mais necessária, e poder ser utilizado por outro user.
     */
    public void libertar() {
        aSerUsado = false;
    }

    public boolean isaSerUsado() {
        return aSerUsado;
    }
}
