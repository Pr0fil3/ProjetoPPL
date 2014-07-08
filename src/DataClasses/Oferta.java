package DataClasses;

import java.io.Serializable;

/**
 * Classe abstracta Oferta contém o estado da oferta.
 */
public abstract class Oferta implements Serializable{
    public static enum ESTADO_OFERTA {POR_APROVAR, APROVACAO_PROVISORIA, APROVADO, NAO_APROVADO};
    public static enum AREA_ATUACAO {REDE, DESENVOLVIMENTO, BASES_DADOS;
        @Override
        public String toString() {
            switch(this){
                case REDE: return "Rede";
                case DESENVOLVIMENTO: return "Desenvolvimento";
                case BASES_DADOS: return "Bases de Dados";
                default: throw new IllegalArgumentException();
            }
        }
 };

    protected int id;
    protected ESTADO_OFERTA estadoOferta;
    protected AREA_ATUACAO areaAtuacao;

    protected Oferta(int id, ESTADO_OFERTA estadoOferta, AREA_ATUACAO areaAtuacao) {
        this.id = id;
        this.estadoOferta = estadoOferta;
        this.areaAtuacao = areaAtuacao;
    }

    public static ESTADO_OFERTA parseEstadoOferta(String estado) {
        if(estado.equals(ESTADO_OFERTA.POR_APROVAR.name()))
            return ESTADO_OFERTA.POR_APROVAR;
        else if(estado.equals(ESTADO_OFERTA.APROVACAO_PROVISORIA.name()))
            return ESTADO_OFERTA.APROVACAO_PROVISORIA;
        else
            return ESTADO_OFERTA.APROVADO;
    }
    
    public static AREA_ATUACAO parseAreaAtuacao(String estado) {
        if(estado.equals(AREA_ATUACAO.REDE.name()))
            return AREA_ATUACAO.REDE;
        else if(estado.equals(AREA_ATUACAO.DESENVOLVIMENTO.name()))
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

    public int getId() {
        return id;
    }
}
