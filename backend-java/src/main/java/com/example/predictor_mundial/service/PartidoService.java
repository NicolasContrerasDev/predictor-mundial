package com.example.predictor_mundial.service;

import com.example.predictor_mundial.dto.PartidoDTO;
import com.example.predictor_mundial.model.Partido;
import com.example.predictor_mundial.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PartidoService {

	private final PartidoRepository partidoRepository;

	@Autowired
	public PartidoService(PartidoRepository partidoRepository) {
		this.partidoRepository = partidoRepository;
	}

	public List<PartidoDTO> obtenerTodos() {
		return partidoRepository.findAll().stream().map(partido -> {
			PartidoDTO dto = new PartidoDTO();
			dto.setEquipoLocal(partido.getEquipoLocal());
			dto.setEquipoVisitante(partido.getEquipoVisitante());
			dto.setPrediccionResultado(partido.getPrediccionResultado());
			dto.setProbabilidadLocal(partido.getProbabilidadLocal());
			dto.setProbabilidadEmpate(partido.getProbabilidadEmpate());
			dto.setProbabilidadVisitante(partido.getProbabilidadVisitante());
			return dto;
		}).collect(Collectors.toList());
	}

	public PartidoDTO procesarYGuardar(PartidoDTO dto) {
		Partido partido = new Partido();
		partido.setEquipoLocal(dto.getEquipoLocal());
		partido.setEquipoVisitante(dto.getEquipoVisitante());
		partido.setFaseTorneo(dto.getFaseTorneo());
		partido.setCuotaLocal(dto.getCuotaLocal());
		partido.setCuotaEmpate(dto.getCuotaEmpate());
		partido.setCuotaVisitante(dto.getCuotaVisitante());

		RestTemplate restTemplate = new RestTemplate();
		String urlPython = "http://localhost:8000/predecir";

		Map<String, Object> requestData = new HashMap<>();
		requestData.put("equipoLocal", dto.getEquipoLocal());
		requestData.put("equipoVisitante", dto.getEquipoVisitante());
		requestData.put("cuotaLocal", dto.getCuotaLocal());
		requestData.put("cuotaEmpate", dto.getCuotaEmpate());
		requestData.put("cuotaVisitante", dto.getCuotaVisitante());

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> respuestaPython = restTemplate.postForObject(urlPython, requestData, Map.class);

			if (respuestaPython != null) {
				partido.setPrediccionResultado((String) respuestaPython.get("prediccion"));

				partido.setProbabilidadLocal(extraerDouble(respuestaPython.get("probabilidadLocal")));
				partido.setProbabilidadEmpate(extraerDouble(respuestaPython.get("probabilidadEmpate")));
				partido.setProbabilidadVisitante(extraerDouble(respuestaPython.get("probabilidadVisitante")));
			}
		} catch (Exception e) {
			System.err.println("Error conectando a Python: " + e.getMessage());
			partido.setPrediccionResultado("No Calculado");
			partido.setProbabilidadLocal(0.0);
			partido.setProbabilidadEmpate(0.0);
			partido.setProbabilidadVisitante(0.0);
		}

		Partido guardado = partidoRepository.save(partido);

		PartidoDTO responseDto = new PartidoDTO();
		responseDto.setEquipoLocal(guardado.getEquipoLocal());
		responseDto.setEquipoVisitante(guardado.getEquipoVisitante());
		responseDto.setPrediccionResultado(guardado.getPrediccionResultado());
		responseDto.setProbabilidadLocal(guardado.getProbabilidadLocal());
		responseDto.setProbabilidadEmpate(guardado.getProbabilidadEmpate());
		responseDto.setProbabilidadVisitante(guardado.getProbabilidadVisitante());

		return responseDto;
	}

	private Double extraerDouble(Object objeto) {
		return objeto != null ? Double.valueOf(objeto.toString()) : 0.0;
	}
}