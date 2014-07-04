package DataClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe OfertaEmprego, extende Oferta
 */
public class OfertaEmprego extends Oferta implements Serializable{
    private int id;
    private String titulo;
    private String detalhesOferta;
    private int numeroCandidatosNecessarios;
    private String perfilCandidatos;
    private List<String> anexos;

    public OfertaEmprego(int id, String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos, List<String> anexos, AREA_ATUACAO areaAtuacao, ESTADO_OFERTA estadoOferta) {
        super(estadoOferta, areaAtuacao);
        this.id = id;
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

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }
}
