package DataClasses;

/**
 * Created by Leandro on 29/05/2014.
 */
public class OfertaRecursos extends Oferta {
    public static enum AREA_ATUACAO {REDE, DESENVOLVIMENTO, BASES_DADOS};

    private int id;
    private String nome;
    private String contacto;
    private AREA_ATUACAO areaAtuacao;

    public OfertaRecursos(int id, String nome, String contacto,AREA_ATUACAO areaAtuacao, ESTADO_OFERTA estadoOferta) {
        super(estadoOferta);
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
        this.areaAtuacao = areaAtuacao;
    }

    public OfertaRecursos(int id, String nome, String contacto, AREA_ATUACAO areaAtuacao) {
        this(id,nome,contacto,areaAtuacao,ESTADO_OFERTA.POR_APROVAR);
    }

    public OfertaRecursos(String nome, String contacto, AREA_ATUACAO areaAtuacao) {
        this(0,nome,contacto,areaAtuacao,ESTADO_OFERTA.POR_APROVAR);
    }

    public static AREA_ATUACAO parseAreaAtuacao(String estado) {
        if(estado.equals(AREA_ATUACAO.REDE.toString()))
            return AREA_ATUACAO.REDE;
        else if(estado.equals(AREA_ATUACAO.DESENVOLVIMENTO.toString()))
            return AREA_ATUACAO.DESENVOLVIMENTO;
        else
            return AREA_ATUACAO.BASES_DADOS;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public AREA_ATUACAO getAreaAtuacao() {
        return areaAtuacao;
    }
}
