package DataClasses;

/**
 * Classe abstracta Oferta contém o estado da oferta.
 */
public abstract class Oferta {
    public static enum ESTADO_OFERTA {POR_APROVAR, APROVACAO_PROVISORIA, TERMINADO};

    protected ESTADO_OFERTA estadoOferta;

    protected Oferta() {
        this.estadoOferta = ESTADO_OFERTA.POR_APROVAR;
    }

    protected Oferta(ESTADO_OFERTA estadoOferta) {
        this.estadoOferta = estadoOferta;
    }

    public static ESTADO_OFERTA parseEstadoOferta(String estado) {
        if(estado.equals(ESTADO_OFERTA.POR_APROVAR.toString()))
            return ESTADO_OFERTA.POR_APROVAR;
        else if(estado.equals(ESTADO_OFERTA.APROVACAO_PROVISORIA.toString()))
            return ESTADO_OFERTA.APROVACAO_PROVISORIA;
        else
            return ESTADO_OFERTA.TERMINADO;
    }

    public ESTADO_OFERTA getEstadoOferta() {
        return estadoOferta;
    }

    public void setEstadoOferta(ESTADO_OFERTA estadoOferta) {
        this.estadoOferta = estadoOferta;
    }
}
