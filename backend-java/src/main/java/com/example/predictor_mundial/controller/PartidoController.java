package com.example.predictor_mundial.controller;

import com.example.predictor_mundial.dto.PartidoDTO;
import com.example.predictor_mundial.service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

	private final PartidoService partidoService;

	@Autowired
	public PartidoController(PartidoService partidoService) {
		this.partidoService = partidoService;
	}

	@PostMapping
	public ResponseEntity<PartidoDTO> crearPartido(@RequestBody PartidoDTO dto) {
		PartidoDTO resultado = partidoService.procesarYGuardar(dto);
		return ResponseEntity.ok(resultado);
	}

	@GetMapping
	public ResponseEntity<List<PartidoDTO>> obtenerPartidos() {
		List<PartidoDTO> listaDePartidos = partidoService.obtenerTodos();
		return ResponseEntity.ok(listaDePartidos);
	}
}