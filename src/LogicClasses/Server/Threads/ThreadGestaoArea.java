/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicClasses.Server.Threads;

import DataClasses.Oferta;
import DataClasses.OfertaEmprego;
import LogicClasses.ClientAdmin.Exceptions.UserNotLoggedInException;
import LogicClasses.DBConnection.ConexaoBD;
import LogicClasses.DBConnection.Exceptions.OfertaEmpregoNotFoundException;
import LogicClasses.DBConnection.Exceptions.OfertaRecursosNotFoundException;
import static LogicClasses.DBConnection.ManagerConexaoBD.*;
import LogicClasses.Server.Exceptions.NoPrivilegesException;
import LogicClasses.Server.IServerUI;
import LogicClasses.Server.ServerImplementation;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author -nikeiZprooo-
 */
public class ThreadGestaoArea extends Thread {

    private final Oferta.AREA_ATUACAO areaAtuacao;
    private final ConexaoBD conexao;
    private final IServerUI serverUI;
    private final ServerImplementation server;

    private final Queue<Integer> ofertasRecursosLivres;

    public ThreadGestaoArea(ServerImplementation server, IServerUI serverUI, Oferta.AREA_ATUACAO areaAtuacao, ConexaoBD conexao) {
        this.areaAtuacao = areaAtuacao;
        this.conexao = conexao;
        this.serverUI = serverUI;
        this.server = server;
        this.ofertasRecursosLivres = new LinkedList<>();
    }

    public void adicionarOfertaRecursosLivre(int id) {
        ofertasRecursosLivres.add(id);
    }

    private OfertaEmprego buscarOfertaEmpregoLivre() {
        try {
            List<OfertaEmprego> listaEmpregoLivre = empregoFromResultSet(conexao.getStatement().executeQuery("select * from " + NOME_TABELA_OFERTAS_EMPREGO
                    + " where " + EMPREGO_COLUNA_AREA_ATUACAO + " = " + "'" + areaAtuacao.toString() + "'"
                    + " and " + EMPREGO_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.POR_APROVAR.toString() + "'"));
            for (OfertaEmprego emprego : listaEmpregoLivre) {
                return emprego;
            }
        } catch (SQLException ex) {
            serverUI.sendMessage("Erro de SQL em ThreadGestaoArea.buscarOfertaEmpregoLivre()...");
        }
        return null;
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (true) {
                    while (ofertasRecursosLivres.isEmpty()) {
                        wait(1000);
                    }
                    OfertaEmprego empregoLivre = buscarOfertaEmpregoLivre();
                    while (empregoLivre == null) {
                        wait(5000);
                        empregoLivre = buscarOfertaEmpregoLivre();
                    }
                    while (empregoLivre.getNumeroCandidatosNecessarios() >= ofertasRecursosLivres.size()) {
                        wait(1000);
                    }
                    try {
                        for (int i = 0; i < empregoLivre.getNumeroCandidatosNecessarios(); i++) {
                            serverUI.sendMessage("O recurso de ID "
                                    + ofertasRecursosLivres.peek() + " da área "
                                    + areaAtuacao.toString()
                                    + " foi vinculada ao emprego de ID "
                                    + empregoLivre.getId() + ".");

                            conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_RECURSOS
                                    + " set " + RECURSOS_COLUNA_EMPREGO_ID + " = " + "'" + empregoLivre.getId() + "'"
                                    + " where " + RECURSOS_COLUNA_ID + " = " + "'" + ofertasRecursosLivres.poll() + "'");
                        }

                        serverUI.sendMessage("O emprego de ID " + empregoLivre.getId() + " foi dado como aprovado provisoriamente.");

                        conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_EMPREGO
                                + " set " + EMPREGO_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.APROVACAO_PROVISORIA.toString() + "'"
                                + " where " + EMPREGO_COLUNA_ID + " = " + "'" + empregoLivre.getId() + "'");

                        server.existemNovasOfertas();
                    } catch (SQLException ex) {
                        serverUI.sendMessage("Erro de SQL em ThreadGestaoArea.run()...");
                    } catch (RemoteException ex) {
                        serverUI.sendMessage("Não foi possivel contactar o client sobre novas ofertas.");
                    } catch (OfertaEmpregoNotFoundException ex) {
                        // Exceptions não relevantes para esta classe.
                    } catch (NoPrivilegesException ex) {
                        // Exceptions não relevantes para esta classe.
                    } catch (OfertaRecursosNotFoundException ex) {
                        // Exceptions não relevantes para esta classe.
                    } catch (UserNotLoggedInException ex) {
                        // Exceptions não relevantes para esta classe.
                    }
                }
            }
        } catch (InterruptedException e) {
            serverUI.sendMessage("Erro no wait() em ThreadGestaoArea.run()...");
        }
    }
}
