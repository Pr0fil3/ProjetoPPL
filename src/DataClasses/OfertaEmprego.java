package DataClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * OfertaEmprego
 */
public class OfertaEmprego extends Oferta {
    private int id;
    private String titulo;
    private String detalhesOferta;
    private int numeroCandidatosNecessarios;
    private String perfilCandidatos;
    private List<String> anexos;

    public OfertaEmprego(int id, String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos, List<String> anexos, ESTADO_OFERTA estadoOferta) {
        super(estadoOferta);
        this.id = id;
        this.titulo = titulo;
        this.detalhesOferta = detalhesOferta;
        this.numeroCandidatosNecessarios = numeroCandidatosNecessarios;
        this.perfilCandidatos = perfilCandidatos;
        this.anexos = anexos;
    }

    public OfertaEmprego(int id, String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos, ESTADO_OFERTA estadoOferta) {
        this(id,titulo,detalhesOferta,numeroCandidatosNecessarios,perfilCandidatos,new ArrayList<String>(), estadoOferta);
    }

    public OfertaEmprego(int id, String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos) {
        this(id,titulo,detalhesOferta,numeroCandidatosNecessarios,perfilCandidatos,ESTADO_OFERTA.POR_APROVAR);
    }

    public OfertaEmprego(String titulo, String detalhesOferta, int numeroCandidatosNecessarios,
                         String perfilCandidatos) {
        this(0,titulo,detalhesOferta,numeroCandidatosNecessarios,perfilCandidatos,ESTADO_OFERTA.POR_APROVAR);
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
}
