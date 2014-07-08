package DataClasses;

import java.io.Serializable;

/**
 * Classe OfertaRecursos extende Oferta
 */
public class OfertaRecursos extends Oferta implements Serializable{
    private String nome;
    private String contacto;
    private int empregoId;

    public OfertaRecursos(int id, String nome, String contacto, int empregoId, AREA_ATUACAO areaAtuacao, ESTADO_OFERTA estadoOferta) {
        super(id, estadoOferta, areaAtuacao);
        this.nome = nome;
        this.contacto = contacto;
        this.empregoId = empregoId;
    }

    public OfertaRecursos(int id, String nome, String contacto, AREA_ATUACAO areaAtuacao) {
        this(id,nome,contacto,0,areaAtuacao,ESTADO_OFERTA.POR_APROVAR);
    }

    public OfertaRecursos(String nome, String contacto, AREA_ATUACAO areaAtuacao) {
        this(0,nome,contacto,0,areaAtuacao,ESTADO_OFERTA.POR_APROVAR);
    }

    public String getNome() {
        return nome;
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

    public int getEmpregoId() {
        return empregoId;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        else if (o == this)
            return true;
        else if (o instanceof OfertaRecursos){
            OfertaRecursos ofertaRecursos = (OfertaRecursos) o;
            return this.id == ofertaRecursos.id 
                    && this.nome.equals(ofertaRecursos.nome)
                    && this.contacto.equals(ofertaRecursos.contacto);
        } else return false;
    }
}
