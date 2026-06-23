package com.example.predictor_mundial.model;

import jakarta.persistence.*;

@Entity
@Table(name = "partidos")
public class Partido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "equipo_local", nullable = false)
	private String equipoLocal;

	@Column(name = "equipo_visitante", nullable = false)
	private String equipoVisitante;

	@Column(name = "fase_torneo")
	private String faseTorneo;

	@Column(name = "cuota_local")
	private Double cuotaLocal;

	@Column(name = "cuota_empate")
	private Double cuotaEmpate;

	@Column(name = "cuota_visitante")
	private Double cuotaVisitante;

	@Column(name = "prediccion_resultado")
	private String prediccionResultado;

	@Column(name = "probabilidad_local")
	private Double probabilidadLocal;

	@Column(name = "probabilidad_empate")
	private Double probabilidadEmpate;

	@Column(name = "probabilidad_visitante")
	private Double probabilidadVisitante;

	public Partido() {
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEquipoLocal() {
		return equipoLocal;
	}

	public void setEquipoLocal(String equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	public String getEquipoVisitante() {
		return equipoVisitante;
	}

	public void setEquipoVisitante(String equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}

	public String getFaseTorneo() {
		return faseTorneo;
	}

	public void setFaseTorneo(String faseTorneo) {
		this.faseTorneo = faseTorneo;
	}

	public Double getCuotaLocal() {
		return cuotaLocal;
	}

	public void setCuotaLocal(Double cuotaLocal) {
		this.cuotaLocal = cuotaLocal;
	}

	public Double getCuotaEmpate() {
		return cuotaEmpate;
	}

	public void setCuotaEmpate(Double cuotaEmpate) {
		this.cuotaEmpate = cuotaEmpate;
	}

	public Double getCuotaVisitante() {
		return cuotaVisitante;
	}

	public void setCuotaVisitante(Double cuotaVisitante) {
		this.cuotaVisitante = cuotaVisitante;
	}

	public String getPrediccionResultado() {
		return prediccionResultado;
	}

	public void setPrediccionResultado(String prediccionResultado) {
		this.prediccionResultado = prediccionResultado;
	}

	public Double getProbabilidadLocal() {
		return probabilidadLocal;
	}

	public void setProbabilidadLocal(Double probabilidadLocal) {
		this.probabilidadLocal = probabilidadLocal;
	}

	public Double getProbabilidadEmpate() {
		return probabilidadEmpate;
	}

	public void setProbabilidadEmpate(Double probabilidadEmpate) {
		this.probabilidadEmpate = probabilidadEmpate;
	}

	public Double getProbabilidadVisitante() {
		return probabilidadVisitante;
	}

	public void setProbabilidadVisitante(Double probabilidadVisitante) {
		this.probabilidadVisitante = probabilidadVisitante;
	}
}