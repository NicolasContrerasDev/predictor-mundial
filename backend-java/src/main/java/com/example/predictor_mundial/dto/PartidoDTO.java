package com.example.predictor_mundial.dto;

public class PartidoDTO {
    private String equipoLocal;
    private String equipoVisitante;
    private String faseTorneo;
    private Double cuotaLocal;
    private Double cuotaEmpate;
    private Double cuotaVisitante;
    private String prediccionResultado;
    private Double probabilidadLocal;
    private Double probabilidadEmpate;
    private Double probabilidadVisitante;

    public PartidoDTO() {}

    // Getters y Setters
    public String getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(String equipoLocal) { this.equipoLocal = equipoLocal; }

    public String getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(String equipoVisitante) { this.equipoVisitante = equipoVisitante; }

    public String getFaseTorneo() { return faseTorneo; }
    public void setFaseTorneo(String faseTorneo) { this.faseTorneo = faseTorneo; }

    public Double getCuotaLocal() { return cuotaLocal; }
    public void setCuotaLocal(Double cuotaLocal) { this.cuotaLocal = cuotaLocal; }

    public Double getCuotaEmpate() { return cuotaEmpate; }
    public void setCuotaEmpate(Double cuotaEmpate) { this.cuotaEmpate = cuotaEmpate; }

    public Double getCuotaVisitante() { return cuotaVisitante; }
    public void setCuotaVisitante(Double cuotaVisitante) { this.cuotaVisitante = cuotaVisitante; }

    public String getPrediccionResultado() { return prediccionResultado; }
    public void setPrediccionResultado(String prediccionResultado) { this.prediccionResultado = prediccionResultado; }

    public Double getProbabilidadLocal() { return probabilidadLocal; }
    public void setProbabilidadLocal(Double probabilidadLocal) { this.probabilidadLocal = probabilidadLocal; }

    public Double getProbabilidadEmpate() { return probabilidadEmpate; }
    public void setProbabilidadEmpate(Double probabilidadEmpate) { this.probabilidadEmpate = probabilidadEmpate; }

    public Double getProbabilidadVisitante() { return probabilidadVisitante; }
    public void setProbabilidadVisitante(Double probabilidadVisitante) { this.probabilidadVisitante = probabilidadVisitante; }
}