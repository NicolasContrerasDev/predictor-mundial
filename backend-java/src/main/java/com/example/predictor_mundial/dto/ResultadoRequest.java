package com.example.predictor_mundial.dto;

public class ResultadoRequest {

	private Long id;
	private int golesAFavor;
	private int golesEnContra;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getGolesAFavor() {
		return golesAFavor;
	}

	public void setGolesAFavor(int golesAFavor) {
		this.golesAFavor = golesAFavor;
	}

	public int getGolesEnContra() {
		return golesEnContra;
	}

	public void setGolesEnContra(int golesEnContra) {
		this.golesEnContra = golesEnContra;
	}
}