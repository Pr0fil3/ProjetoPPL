package DataClasses;

/**
 * Classe OfertaRecursos extende Oferta
 */
public class OfertaRecursos extends Oferta {
    public static enum AREA_ATUACAO {REDE, DESENVOLVIMENTO, BASES_DADOS};

    private int id;
    private String nome;
    private String contacto;
    private AREA_ATUACAO areaAtuacao;
    private int empregoId;

    public OfertaRecursos(int id, String nome, String contacto, int empregoId, AREA_ATUACAO areaAtuacao, ESTADO_OFERTA estadoOferta) {
        super(estadoOferta);
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
        this.areaAtuacao = areaAtuacao;
        this.empregoId = empregoId;
    }

    public OfertaRecursos(int id, String nome, String contacto, AREA_ATUACAO areaAtuacao) {
        this(id,nome,contacto,0,areaAtuacao,ESTADO_OFERTA.POR_APROVAR);
    }

    public OfertaRecursos(String nome, String contacto, AREA_ATUACAO areaAtuacao) {
        this(0,nome,contacto,0,areaAtuacao,ESTADO_OFERTA.POR_APROVAR);
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

    public int getEmpregoId() {
        return empregoId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
