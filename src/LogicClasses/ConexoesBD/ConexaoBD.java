package LogicClasses.ConexoesBD;

import java.sql.*;

/**
 * A instaciação desta classe cria uma ligação à base de dados e respetiva statment
 */
public class ConexaoBD {
    private static String DEFAULT_URL = "jdbc:mysql://localhost:3306/projeto_ppl";
    private static String DEFAULT_USER = "root";

    private Connection connection;
    private Statement statement;
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
        this.statement = connection.createStatement();
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
        super.finalize();
        if (this.statement != null)
            this.statement.close();
        if (this.connection != null)
            this.connection.close();
    }

    public Statement getStatement() {
        this.aSerUsado = true;
        return statement;
    }

    /**
     * É chamado quando esta statment não é mais necessária, e pode ser utilizada por outro user.
     */
    public void libertar() {
        aSerUsado = false;
    }

    public boolean isaSerUsado() {
        return aSerUsado;
    }
}
