package LogicClasses.ConexoesBD;

import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import DataClasses.OfertaRecursos;
import DataClasses.User;
import LogicClasses.ConexoesBD.Exceptions.KeyNotReturnedException;
import LogicClasses.ConexoesBD.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.OfertaRecursosNotFoundException;
import LogicClasses.ConexoesBD.Exceptions.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe para gerir vários objectos da classe "ConexaoBD"
 * É esta classe que faz todas as operações na base de dados excepto as
 * realizadas pelo JobScheduler e as Threads
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

    private final List<ConexaoBD> conexoes = new ArrayList<>();

    /**
     * Cria 5 conexoes utilizando as opções por defeito
     * @throws SQLException 
     */
    public ManagerConexaoBD() throws SQLException {
        for(int i = 0; i < 5; i++)
            conexoes.add(new ConexaoBD());
    }

    /**
     * Cria 5 conexoes utilizando os parametros de entrada
     * @param url
     * @param user
     * @param password
     * @throws SQLException 
     */
    public ManagerConexaoBD(String url, String user, String password) throws SQLException {
        for(int i = 0; i < 5; i++)
            conexoes.add(new ConexaoBD(url, user, password));
    }

    /**
     * Procura uma conexao que não esteja a ser usada.
     * @return uma ConexaoBD, conexao à base de dados
     * @throws SQLException 
     */
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

    /**
     * Retorna um ResultSet para querys simples
     * @param statement para realizar uma Query
     * @param tabela Nome da tabela a pesquisa
     * @param condicao Condicao a procurar
     * @param variavel Variavel a testar com a condicao
     * @return ResultSet da Query
     * @throws SQLException 
     */
    private ResultSet getFromDB(Statement statement, String tabela, String condicao, String variavel) throws SQLException {
        return statement.executeQuery("select * from " + tabela + " where " + condicao + " = " + variavel);
    }

    /////////////////////
    // FROM RESULT SET //
    /////////////////////

    /**
     * 
     * @param resultSet
     * @return Lista de Users
     * @throws SQLException 
     */
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

    /**
     * 
     * @param resultSet
     * @return Lista de Ofertas de Recursos
     * @throws SQLException 
     */
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

    /**
     * 
     * @param resultSet
     * @return Lista de Ofertas de Emprego e seus Anexos
     * @throws SQLException 
     */
    public List<OfertaEmprego> empregoAndAnexosFromResultSet(ResultSet resultSet) throws SQLException {
        List emprego = new ArrayList<>();
        while (resultSet.next()){
            int id = resultSet.getInt(EMPREGO_COLUNA_ID);

            OfertaEmprego newOfertaEmprego = new OfertaEmprego(
                    id,
                    resultSet.getString(EMPREGO_COLUNA_TITULO),
                    resultSet.getString(EMPREGO_COLUNA_DETALHES),
                    resultSet.getInt(EMPREGO_COLUNA_CANDIDATOS_NECESSARIOS),
                    resultSet.getString(EMPREGO_COLUNA_PERFIL),
                    getAnexos(id),
                    OfertaEmprego.parseAreaAtuacao(resultSet.getString(EMPREGO_COLUNA_AREA_ATUACAO)),
                    OfertaEmprego.parseEstadoOferta(resultSet.getString(EMPREGO_COLUNA_ESTADO_OFERTA)));
            emprego.add(newOfertaEmprego);
        }
        return emprego;
    }
    
    /**
     * 
     * @param resultSet
     * @return Lista de Ofertas de Emprego sem os anexos
     * @throws SQLException 
     */
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

    /**
     * 
     * @param resultSet
     * @return Lista de anexos
     * @throws SQLException 
     */
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

    /**
     * Cria um novo utilizador na base de dados
     * 
     * Faz throw de "UserNotFoundException" ou "KeyNotReturnedException"
     * se não conseguir retornar o utilizador criado, incluindo o seu id,
     * criado pela base de dados em modo auto increment
     * @param newUser
     * @return O user criado
     * @throws SQLException
     * @throws UserNotFoundException
     * @throws KeyNotReturnedException 
     */
    public User newUser(User newUser) throws SQLException, UserNotFoundException, KeyNotReturnedException {
        ConexaoBD conexao = getConexao();
        try {
            conexao.getStatement().executeUpdate("insert into " + NOME_TABELA_USERS + " values (null,"
                    + "'" + newUser.getNome() + "'" + ","
                    + "'" + newUser.getPassword() + "'" + ","
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

    /**
     * Cria uma nova oferta de Recrursos
     * @param newOfertaRecursos
     * @throws SQLException 
     */
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
                    + "'" + newOfertaRecursos.getAreaAtuacao().name() + "'" + ","
                    + "'" + newOfertaRecursos.getEstadoOferta().name() + "');");
        }finally {
            conexao.libertar();
        }
    }

    /**
     * Cria uma nova oferta de emprego
     * Retorna o ID da oferta criada para ser utilizado para inserir os anexos.
     * 
     * @param newOfertaEmprego
     * @return O id da nova oferta
     * @throws SQLException
     * @throws KeyNotReturnedException 
     */
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
                    + "'" + newOfertaEmprego.getAreaAtuacao().name() + "'" + ","
                    + "'" + newOfertaEmprego.getEstadoOferta().name() + "');"
                    , Statement.RETURN_GENERATED_KEYS);
            ResultSet keys = conexao.getStatement().getGeneratedKeys();
            if(keys.next()){
                return keys.getInt(1);
            } throw new KeyNotReturnedException();
        }finally {
            conexao.libertar();
        }
    }
    
    /**
     * Insere os anexos da oferta com o ID "idEmprego"
     * @param anexos
     * @param idEmprego
     * @throws SQLException 
     */
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

    /**
     * Procura na base de dados um utilizador com o id fornecido
     * @param id
     * @return O utlizador se o encontrar
     * @throws SQLException
     * @throws UserNotFoundException Se não encontrar utilizador
     */
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

    /**
     * Procura na base de dados uma oferta de recursos com o id fornecido
     * @param id
     * @return A oferta se a encontrar
     * @throws SQLException
     * @throws OfertaRecursosNotFoundException Se não encontrar Oferta
     */
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

    /**
     * Procura na base de dados um oferta de emprego com o id fornecido
     * @param id
     * @return A oferta se a encontrar
     * @throws SQLException
     * @throws OfertaEmpregoNotFoundException Se não encontrar a oferta
     */
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
    
    /**
     * Procura na base de dados a lista de anexos da oferta com o id fornecido
     * @param id
     * @return A lista de anexos
     * @throws SQLException 
     */
    public List<String> getAnexos(int id) throws SQLException{
        ConexaoBD conexao = getConexao();
        List<String> anexos = new ArrayList<>();
        try{
            return anexosFromResultSet(getFromDB(conexao.getStatement(),NOME_TABELA_ANEXOS, ANEXOS_COLUNA_EMPREGO_ID, String.valueOf(id)));
        } finally {
            conexao.libertar();
        }
    }
    
    //////////////////////
    // GET REVIEW CASES //
    //////////////////////
    
    /**
     * Procura uma oferta de emprego que esteja pronta para ser aprovada
     * @param ofertasEmAnalise
     * @return OfertaEmprego
     * @throws SQLException
     * @throws OfertaEmpregoNotFoundException Se não encontrar nenhuma
     */
    public OfertaEmprego getEmpregoToReview(ArrayList<Oferta> ofertasEmAnalise) throws SQLException, OfertaEmpregoNotFoundException{
        ConexaoBD conexao = getConexao();
        try{
            for (OfertaEmprego oferta : empregoAndAnexosFromResultSet(conexao.getStatement().executeQuery("select *"
                    + " from " + NOME_TABELA_OFERTAS_EMPREGO
                    + " where " + EMPREGO_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.APROVACAO_PROVISORIA.name() + "'"))){
                if(!ofertasEmAnalise.contains(oferta))
                    return oferta;
            }
            throw new OfertaEmpregoNotFoundException();
        } finally {
            conexao.libertar();
        }
    }
    
    /**
     * Procura uma oferta de recursos que esteja pronta para ser aprovada
     * @param ofertasEmAnalise
     * @return OfertaRecursos
     * @throws SQLException
     * @throws OfertaRecursosNotFoundException Se não encontrar nenhuma
     */
    public OfertaRecursos getRecursoToReview(ArrayList<Oferta> ofertasEmAnalise) throws SQLException, OfertaRecursosNotFoundException{
        ConexaoBD conexao = getConexao();
        try {
            for (OfertaRecursos oferta : recursosFromResultSet(conexao.getStatement().executeQuery("select *"
                    + " from " + NOME_TABELA_OFERTAS_RECURSOS
                    + " where " + RECURSOS_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.POR_APROVAR.name() + "'"))){
                if(!ofertasEmAnalise.contains(oferta))
                    return oferta;
            }
            throw new OfertaRecursosNotFoundException();
        } finally {
            conexao.libertar();
        }
    }
    
    /////////////////
    // REVIEW CASE //
    /////////////////
    
    /**
     * Coloca na base de dados o resultado da análise de uma oferta de emprego 
     * @param id
     * @param analise
     * @throws SQLException 
     */
    public void reviewEmprego(int id, boolean analise) throws SQLException{
        ConexaoBD conexao = getConexao();
        try{
            if(analise)
                conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_EMPREGO
                        + " set " + EMPREGO_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.APROVADO.name() + "'"
                        + " where " + EMPREGO_COLUNA_ID + " = " + id);
            else {
                conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_EMPREGO
                        + " set " + EMPREGO_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.NAO_APROVADO.name() + "'"
                        + " where " + EMPREGO_COLUNA_ID + " = " + id);
                conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_RECURSOS
                        + " set " + RECURSOS_COLUNA_EMPREGO_ID + " = null, " + RECURSOS_COLUNA_IS_NEW + " = true"
                        + " where " + RECURSOS_COLUNA_EMPREGO_ID + " = " + id);
            }
        } finally {
            conexao.libertar();
        }
    }
    
    /**
     * Coloca na base de dados o resultado da análise de uma oferta de recursos
     * @param id
     * @param analise
     * @throws SQLException 
     */
    public void reviewRecurso(int id, boolean analise) throws SQLException{
        ConexaoBD conexao = getConexao();
        try{
            if(analise)
                conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_RECURSOS
                        + " set " + RECURSOS_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.APROVADO.name() + "'"
                        + " where " + RECURSOS_COLUNA_ID + " = " + id);
            else
                conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_RECURSOS
                        + " set " + RECURSOS_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.NAO_APROVADO.name() + "'"
                        + " where " + RECURSOS_COLUNA_ID + " = " + id);
        } finally {
            conexao.libertar();
        }
    }
}