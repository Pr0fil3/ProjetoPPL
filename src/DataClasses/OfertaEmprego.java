package DataClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe OfertaEmprego, extende Oferta
 */
public class OfertaEmprego extends Oferta implements Serializable{
    private String titulo;
    private String detalhesOferta;
    private int numeroCandidatosNecessarios;
    private String perfilCandidatos;
    private List<String> anexos;

    public OfertaEmprego(int id, String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos, List<String> anexos, AREA_ATUACAO areaAtuacao, ESTADO_OFERTA estadoOferta) {
        super(id, estadoOferta, areaAtuacao);
        this.titulo = titulo;
        this.detalhesOferta = detalhesOferta;
        this.numeroCandidatosNecessarios = numeroCandidatosNecessarios;
        this.perfilCandidatos = perfilCandidatos;
        this.anexos = anexos;
    }

    public OfertaEmprego(String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos, AREA_ATUACAO areaAtuacao, List<String> anexos){
        this(0,titulo,detalhesOferta,numeroCandidatosNecessarios,perfilCandidatos,anexos, areaAtuacao,ESTADO_OFERTA.POR_APROVAR);
    }

    public OfertaEmprego(int id, String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos,AREA_ATUACAO areaAtuacao, ESTADO_OFERTA estadoOferta) {
        this(id,titulo,detalhesOferta,numeroCandidatosNecessarios,perfilCandidatos,new ArrayList<String>(), areaAtuacao, estadoOferta);
    }

    public OfertaEmprego(int id, String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos, AREA_ATUACAO areaAtuacao) {
        this(id,titulo,detalhesOferta,numeroCandidatosNecessarios,perfilCandidatos, areaAtuacao, ESTADO_OFERTA.POR_APROVAR);
    }

    public OfertaEmprego(String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos, AREA_ATUACAO areaAtuacao) {
        this(0,titulo,detalhesOferta,numeroCandidatosNecessarios,perfilCandidatos, areaAtuacao,ESTADO_OFERTA.POR_APROVAR);
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDetalhesOferta() {
        return detalhesOferta;
    }

    public int getNumeroCandidatosNecessarios() {
        return numeroCandidatosNecessarios;
    }

    public String getPerfilCandidatos() {
        return perfilCandidatos;
    }

    public List<String> getAnexos() {
        return anexos;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDetalhesOferta(String detalhesOferta) {
        this.detalhesOferta = detalhesOferta;
    }

    public void setNumeroCandidatosNecessarios(int numeroCandidatosNecessarios) {
        this.numeroCandidatosNecessarios = numeroCandidatosNecessarios;
    }

    public void setPerfilCandidatos(String perfilCandidatos) {
        this.perfilCandidatos = perfilCandidatos;
    }

    public void setAnexos(List<String> anexos) {
        this.anexos = anexos;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        else if (o == this)
            return true;
        else if (o instanceof OfertaEmprego){
            OfertaEmprego ofertaEmprego = (OfertaEmprego) o;
            if(this.id == ofertaEmprego.id 
                    && this.titulo.equals(ofertaEmprego.titulo) 
                    && this.detalhesOferta.equals(ofertaEmprego.detalhesOferta)
                    && this.numeroCandidatosNecessarios == ofertaEmprego.numeroCandidatosNecessarios
                    && this.perfilCandidatos.equals(ofertaEmprego.perfilCandidatos))
                if (anexos.size() == ofertaEmprego.anexos.size())
                    return anexos.containsAll(ofertaEmprego.anexos);
            return false;
        } else return false;
    }
}
