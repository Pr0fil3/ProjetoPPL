package DataClasses;

import java.io.Serializable;

/**
 * Classe abstracta Oferta contém o estado da oferta.
 */
public abstract class Oferta implements Serializable{
    public static enum ESTADO_OFERTA {POR_APROVAR, APROVACAO_PROVISORIA, TERMINADO};
    public static enum AREA_ATUACAO {REDE, DESENVOLVIMENTO, BASES_DADOS};

    protected ESTADO_OFERTA estadoOferta;
    protected AREA_ATUACAO areaAtuacao;

    protected Oferta(ESTADO_OFERTA estadoOferta, AREA_ATUACAO areaAtuacao) {
        this.estadoOferta = estadoOferta;
        this.areaAtuacao = areaAtuacao;
    }

    public static ESTADO_OFERTA parseEstadoOferta(String estado) {
        if(estado.equals(ESTADO_OFERTA.POR_APROVAR.toString()))
            return ESTADO_OFERTA.POR_APROVAR;
        else if(estado.equals(ESTADO_OFERTA.APROVACAO_PROVISORIA.toString()))
            return ESTADO_OFERTA.APROVACAO_PROVISORIA;
        else
            return ESTADO_OFERTA.TERMINADO;
    }
    
    public static AREA_ATUACAO parseAreaAtuacao(String estado) {
        if(estado.equals(AREA_ATUACAO.REDE.toString()))
            return AREA_ATUACAO.REDE;
        else if(estado.equals(AREA_ATUACAO.DESENVOLVIMENTO.toString()))
            return AREA_ATUACAO.DESENVOLVIMENTO;
        else
            return AREA_ATUACAO.BASES_DADOS;
    }

    public ESTADO_OFERTA getEstadoOferta() {
        return estadoOferta;
    }

    public AREA_ATUACAO getAreaAtuacao() {
        return areaAtuacao;
    }
}
