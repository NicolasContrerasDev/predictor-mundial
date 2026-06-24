package com.example.predictor_mundial.controller;

import com.example.predictor_mundial.dto.PartidoDTO;
import com.example.predictor_mundial.dto.ResultadoRequest;
import com.example.predictor_mundial.enums.ResultadoPartido;
import com.example.predictor_mundial.enums.SeleccionMundial;
import com.example.predictor_mundial.service.PartidoService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
	public ResponseEntity<PartidoDTO> crearPartido(
			@Parameter(description = "Equipo local") @RequestParam("equipoLocal") SeleccionMundial equipoLocal,
			@Parameter(description = "Equipo visita") @RequestParam("equipoVisita") SeleccionMundial equipoVisita) {

		if (equipoLocal == equipoVisita) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un equipo no puede jugar contra sí mismo.");
		}

		return ResponseEntity.ok(partidoService.procesarYGuardarAutomatizado(equipoLocal, equipoVisita));
	}

	@GetMapping
	public ResponseEntity<List<PartidoDTO>> obtenerPartidos() {
		return ResponseEntity.ok(partidoService.obtenerTodos());
	}

	@GetMapping("/descargar-csv")
	public ResponseEntity<byte[]> descargarCSV() {

		String csv = partidoService.generarDatasetPredictivo();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=partidos.csv")
				.contentType(MediaType.TEXT_PLAIN).body(csv.getBytes());
	}

	@GetMapping("/descargar-xml")
	public ResponseEntity<String> descargarXML() {

		StringBuilder xml = new StringBuilder();
		xml.append("<partidos>\n");

		partidoService.obtenerTodos().forEach(p -> {
			xml.append("  <partido>\n").append("    <local>").append(p.getEquipoLocal()).append("</local>\n")
					.append("    <visita>").append(p.getEquipoVisita()).append("</visita>\n").append("    <probLocal>")
					.append(p.getProbVictoriaLocal()).append("</probLocal>\n").append("    <probEmpate>")
					.append(p.getProbEmpate()).append("</probEmpate>\n").append("    <probVisita>")
					.append(p.getProbVictoriaVisita()).append("</probVisita>\n").append("  </partido>\n");
		});

		xml.append("</partidos>");

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=partidos.xml")
				.contentType(MediaType.APPLICATION_XML).body(xml.toString());
	}

	@GetMapping("/demo")
	public ResponseEntity<String> demoResultados() {

		List<PartidoDTO> partidos = partidoService.obtenerTodos();

		StringBuilder html = new StringBuilder();

		html.append("<html><body style='font-family:Arial'>");
		html.append("<h2>⚽ Predictor Mundial - Resultados</h2>");

		for (PartidoDTO p : partidos) {
			html.append("<div style='margin-bottom:10px;padding:10px;border:1px solid #ccc'>").append("<b>")
					.append(p.getEquipoLocal()).append("</b> vs <b>").append(p.getEquipoVisita()).append("</b><br>")
					.append("Local: ").append(p.getProbVictoriaLocal()).append(" | ").append("Empate: ")
					.append(p.getProbEmpate()).append(" | ").append("Visita: ").append(p.getProbVictoriaVisita())
					.append("</div>");
		}

		html.append("</body></html>");

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/html").body(html.toString());
	}

	@GetMapping("/dashboard")
	public ResponseEntity<String> dashboard() {
		return ResponseEntity.ok(partidoService.generarDashboard());
	}

	@PutMapping("/resultado")
	public ResponseEntity<String> registrarResultado(
			@Parameter(description = "Resultado desde la perspectiva del local") @RequestParam("resultado") ResultadoPartido resultado,
			@RequestBody ResultadoRequest request) {

		String msg = partidoService.registrarResultado(resultado, request);
		return ResponseEntity.ok(msg);
	}

}