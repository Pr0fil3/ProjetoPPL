package DataClasses;

/**
 * Created by Leandro on 29/05/2014.
 */
public class OfertaRecursos {
    public static enum ESTADO_OFERTA {POR_APROVAR, APROVACAO_PROVISORIA, TERMINADO};

    private int id;
    private String nome;
    private String contacto;
    private ESTADO_OFERTA estadoOferta;

    public OfertaRecursos(int id, String nome, String contacto, ESTADO_OFERTA estadoOferta) {
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
        this.estadoOferta = estadoOferta;
    }

    public OfertaRecursos(int id, String nome, String contacto) {
       this(id,nome,contacto,ESTADO_OFERTA.POR_APROVAR);
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public ESTADO_OFERTA getEstadoOferta() {
        return estadoOferta;
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
}
