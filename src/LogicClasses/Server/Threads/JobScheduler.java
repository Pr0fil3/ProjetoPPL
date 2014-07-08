/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicClasses.Server.Threads;

import DataClasses.Oferta;
import DataClasses.OfertaRecursos;
import LogicClasses.ConexoesBD.ConexaoBD;
import static LogicClasses.ConexoesBD.ManagerConexaoBD.*;
import LogicClasses.Server.IServerUI;
import LogicClasses.Server.ServerImplementation;
import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

/**
 *
 * @author -nikeiZprooo-
 */
public class JobScheduler extends TimerTask {

    private final ConexaoBD conexao;
    private final IServerUI serverUI;

    private final ThreadGestaoArea threadRedes, threadDesenvolvimento, threadBasesDados;

    public JobScheduler(ServerImplementation server, IServerUI serverUI, ConexaoBD conexao,
            ConexaoBD conexaoThreadRedes, ConexaoBD conexaoThreadBasesDados,
            ConexaoBD conexaoThreadDesenvolvimento, int tempoWaitVerificacoesLocais,
            int tempoWaitVerificacoesBD)
            throws SQLException {
        this.conexao = conexao;
        this.serverUI = serverUI;

        this.threadRedes = new ThreadGestaoArea(server, serverUI, 
                Oferta.AREA_ATUACAO.REDE, conexaoThreadRedes,
                tempoWaitVerificacoesLocais,tempoWaitVerificacoesBD);
        this.threadBasesDados = new ThreadGestaoArea(server, serverUI,
                Oferta.AREA_ATUACAO.BASES_DADOS, conexaoThreadBasesDados,
                tempoWaitVerificacoesLocais,tempoWaitVerificacoesBD);
        this.threadDesenvolvimento = new ThreadGestaoArea(server, serverUI,
                Oferta.AREA_ATUACAO.DESENVOLVIMENTO, conexaoThreadDesenvolvimento,
                tempoWaitVerificacoesLocais,tempoWaitVerificacoesBD);

        this.threadRedes.start();
        this.threadBasesDados.start();
        this.threadDesenvolvimento.start();
    }

    @Override
    public void run() {
        try {
            List<OfertaRecursos> ofertasNovas = recursosFromResultSet(conexao.getStatement().executeQuery("select * from " + NOME_TABELA_OFERTAS_RECURSOS
                    + " where " + RECURSOS_COLUNA_ESTADO_OFERTA + " = " + "'" + Oferta.ESTADO_OFERTA.APROVADO.name() + "'"
                    + " and " + RECURSOS_COLUNA_IS_NEW + " is true"));

            if(ofertasNovas.size() > 0)
                serverUI.sendMessage("JobScheduler - " + ofertasNovas.size() + " novas ofertas de recursos Aprovadas foram enviadas para as threads.");

            for (OfertaRecursos oferta : ofertasNovas) {
                if (oferta.getAreaAtuacao() == Oferta.AREA_ATUACAO.REDE) {
                    threadRedes.adicionarOfertaRecursosLivre(oferta.getId());
                } else if (oferta.getAreaAtuacao() == Oferta.AREA_ATUACAO.BASES_DADOS) {
                    threadBasesDados.adicionarOfertaRecursosLivre(oferta.getId());
                } else {
                    threadDesenvolvimento.adicionarOfertaRecursosLivre(oferta.getId());
                }

                conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_RECURSOS
                        + " set " + RECURSOS_COLUNA_IS_NEW + " = false"
                        + " where " + RECURSOS_COLUNA_ID + " = " + oferta.getId());
            }
        } catch (SQLException ex) {
            serverUI.sendMessage("Erro de SQL em JobScheduler.run()...");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        conexao.getStatement().executeUpdate("update " + NOME_TABELA_OFERTAS_RECURSOS
                + " set " + RECURSOS_COLUNA_IS_NEW + " = '1'"
                + " where " + RECURSOS_COLUNA_EMPREGO_ID + " is null");
    }
}
