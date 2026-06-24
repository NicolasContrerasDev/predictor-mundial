package com.example.predictor_mundial.model;

import jakarta.persistence.*;

@Entity
@Table(name = "partidos_mundial")
public class Partido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String equipoLocal;
	private String equipoVisita;

	private Integer historialVictoriasLocal;
	private Integer historialVictoriasVisita;
	private Integer empatesHistoricos;

	private Integer tarjetasAmarillasLocal;
	private Integer tarjetasAmarillasVisita;
	private Integer tarjetasRojasLocal;
	private Integer tarjetasRojasVisita;

	private Double probVictoriaLocal;
	private Double probEmpate;
	private Double probVictoriaVisita;

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

	public String getEquipoVisita() {
		return equipoVisita;
	}

	public void setEquipoVisita(String equipoVisita) {
		this.equipoVisita = equipoVisita;
	}

	public Integer getHistorialVictoriasLocal() {
		return historialVictoriasLocal;
	}

	public void setHistorialVictoriasLocal(Integer historialVictoriasLocal) {
		this.historialVictoriasLocal = historialVictoriasLocal;
	}

	public Integer getHistorialVictoriasVisita() {
		return historialVictoriasVisita;
	}

	public void setHistorialVictoriasVisita(Integer historialVictoriasVisita) {
		this.historialVictoriasVisita = historialVictoriasVisita;
	}

	public Integer getEmpatesHistoricos() {
		return empatesHistoricos;
	}

	public void setEmpatesHistoricos(Integer empatesHistoricos) {
		this.empatesHistoricos = empatesHistoricos;
	}

	public Integer getTarjetasAmarillasLocal() {
		return tarjetasAmarillasLocal;
	}

	public void setTarjetasAmarillasLocal(Integer tarjetasAmarillasLocal) {
		this.tarjetasAmarillasLocal = tarjetasAmarillasLocal;
	}

	public Integer getTarjetasAmarillasVisita() {
		return tarjetasAmarillasVisita;
	}

	public void setTarjetasAmarillasVisita(Integer tarjetasAmarillasVisita) {
		this.tarjetasAmarillasVisita = tarjetasAmarillasVisita;
	}

	public Integer getTarjetasRojasLocal() {
		return tarjetasRojasLocal;
	}

	public void setTarjetasRojasLocal(Integer tarjetasRojasLocal) {
		this.tarjetasRojasLocal = tarjetasRojasLocal;
	}

	public Integer getTarjetasRojasVisita() {
		return tarjetasRojasVisita;
	}

	public void setTarjetasRojasVisita(Integer tarjetasRojasVisita) {
		this.tarjetasRojasVisita = tarjetasRojasVisita;
	}

	public Double getProbVictoriaLocal() {
		return probVictoriaLocal;
	}

	public void setProbVictoriaLocal(Double probVictoriaLocal) {
		this.probVictoriaLocal = probVictoriaLocal;
	}

	public Double getProbEmpate() {
		return probEmpate;
	}

	public void setProbEmpate(Double probEmpate) {
		this.probEmpate = probEmpate;
	}

	public Double getProbVictoriaVisita() {
		return probVictoriaVisita;
	}

	public void setProbVictoriaVisita(Double probVictoriaVisita) {
		this.probVictoriaVisita = probVictoriaVisita;
	}
}