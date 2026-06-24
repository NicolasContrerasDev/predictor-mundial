package com.example.predictor_mundial.dto;

import com.example.predictor_mundial.enums.SeleccionMundial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

public class PartidoDTO {

	@Schema(description = "Selección que juega en casa", implementation = SeleccionMundial.class, example = "ARGENTINA")
	@NotNull(message = "El equipo local es obligatorio")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private SeleccionMundial equipoLocal;

	@Schema(description = "Selección que juega de visita", implementation = SeleccionMundial.class, example = "MEXICO")
	@NotNull(message = "El equipo visitante es obligatorio")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private SeleccionMundial equipoVisita;

	private Integer historialVictoriasLocal = 0;
	private Integer historialVictoriasVisita = 0;
	private Integer empatesHistoricos = 0;
	private Integer tarjetasAmarillasLocal = 0;
	private Integer tarjetasAmarillasVisita = 0;
	private Integer tarjetasRojasLocal = 0;
	private Integer tarjetasRojasVisita = 0;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Double probVictoriaLocal;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Double probEmpate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Double probVictoriaVisita;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@AssertTrue(message = "Un equipo no puede jugar contra sí mismo. Elige selecciones distintas.")
	@JsonIgnore
	public boolean isEquiposDiferentes() {
		if (equipoLocal == null || equipoVisita == null) {
			return true;
		}
		return !equipoLocal.equals(equipoVisita);
	}

	public SeleccionMundial getEquipoLocal() {
		return equipoLocal;
	}

	public void setEquipoLocal(SeleccionMundial equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	public SeleccionMundial getEquipoVisita() {
		return equipoVisita;
	}

	public void setEquipoVisita(SeleccionMundial equipoVisita) {
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