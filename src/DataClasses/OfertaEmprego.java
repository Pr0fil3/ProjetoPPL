package DataClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leandro on 29/05/2014.
 */
public class OfertaEmprego {
    private int id;
    private String titulo;
    private String detalhesOferta;
    private int numeroCandidatosNecessarios;
    private String perfilCandidatos;
    private List<String> anexos;

    public OfertaEmprego(int id, String titulo, String detalhesOferta, int numeroCandidatosNecessarios, String perfilCandidatos) {
        this.id = id;
        this.titulo = titulo;
        this.detalhesOferta = detalhesOferta;
        this.numeroCandidatosNecessarios = numeroCandidatosNecessarios;
        this.perfilCandidatos = perfilCandidatos;
        this.anexos = new ArrayList<String>();
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
