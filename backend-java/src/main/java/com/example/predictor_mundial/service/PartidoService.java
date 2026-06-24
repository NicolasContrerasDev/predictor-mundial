package com.example.predictor_mundial.service;

import com.example.predictor_mundial.dto.PartidoDTO;
import com.example.predictor_mundial.dto.ResultadoRequest;
import com.example.predictor_mundial.enums.ResultadoPartido;
import com.example.predictor_mundial.enums.SeleccionMundial;
import com.example.predictor_mundial.model.Partido;
import com.example.predictor_mundial.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PartidoService {

	private final PartidoRepository partidoRepository;
	private final RestTemplate restTemplate;

	@Value("${python.api.url:http://localhost:8000/predecir}")
	private String urlPython;

	private static class TeamStats {
		int ranking;
		int elo;
		double golesFavor;
		double golesContra;
		int forma;
		double amarillasProm;
		double rojasProm;

		TeamStats(int ranking, int elo, double golesFavor, double golesContra, int forma, double amarillasProm,
				double rojasProm) {
			this.ranking = ranking;
			this.elo = elo;
			this.golesFavor = golesFavor;
			this.golesContra = golesContra;
			this.forma = forma;
			this.amarillasProm = amarillasProm;
			this.rojasProm = rojasProm;
		}
	}

	private final Map<String, TeamStats> stats = new HashMap<>();

	@Autowired
	public PartidoService(PartidoRepository partidoRepository) {
		this.partidoRepository = partidoRepository;
		this.restTemplate = new RestTemplate();
		cargarCSV();
	}

	private void cargarCSV() {
		try {
			ClassPathResource resource = new ClassPathResource("team_stats.csv");
			InputStream input = resource.getInputStream();
			Scanner sc = new Scanner(input);

			if (sc.hasNextLine())
				sc.nextLine();

			while (sc.hasNextLine()) {
				String line = sc.nextLine().trim();
				if (line.isEmpty())
					continue;

				String[] d = line.split(",");
				if (d.length < 8)
					continue;

				stats.put(d[0],
						new TeamStats(Integer.parseInt(d[1]), Integer.parseInt(d[2]), Double.parseDouble(d[3]),
								Double.parseDouble(d[4]), Integer.parseInt(d[5]), Double.parseDouble(d[6]),
								Double.parseDouble(d[7])));
			}
			sc.close();

		} catch (Exception e) {
			throw new RuntimeException("Error cargando team_stats.csv", e);
		}
	}

	public List<PartidoDTO> obtenerTodos() {
		return partidoRepository.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
	}

	public PartidoDTO procesarYGuardarAutomatizado(SeleccionMundial local, SeleccionMundial visita) {

		TeamStats statsLocal = stats.getOrDefault(local.name(), new TeamStats(999, 1300, 1.0, 1.0, 1, 2.5, 0.2));
		TeamStats statsVisita = stats.getOrDefault(visita.name(), new TeamStats(999, 1300, 1.0, 1.0, 1, 2.5, 0.2));

		Map<String, Object> request = new HashMap<>();
		request.put("equipoLocal", local.name());
		request.put("equipoVisita", visita.name());
		request.put("rankingLocal", statsLocal.ranking);
		request.put("rankingVisita", statsVisita.ranking);
		request.put("eloLocal", statsLocal.elo);
		request.put("eloVisita", statsVisita.elo);
		request.put("golesFavorLocal", statsLocal.golesFavor);
		request.put("golesFavorVisita", statsVisita.golesFavor);
		request.put("golesContraLocal", statsLocal.golesContra);
		request.put("golesContraVisita", statsVisita.golesContra);
		request.put("formaLocal", statsLocal.forma);
		request.put("formaVisita", statsVisita.forma);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

		Partido partido = new Partido();
		partido.setEquipoLocal(local.name());
		partido.setEquipoVisita(visita.name());

		try {
			Map<String, Object> response = restTemplate.postForObject(urlPython, entity, Map.class);
			if (response != null) {
				partido.setProbVictoriaLocal(extract(response.get("probabilidadLocal")));
				partido.setProbEmpate(extract(response.get("probabilidadEmpate")));
				partido.setProbVictoriaVisita(extract(response.get("probabilidadVisita")));
			}
		} catch (Exception e) {
			System.err.println("Error Python API: " + e.getMessage());
		}

		partido.setTarjetasAmarillasLocal((int) Math.round(statsLocal.amarillasProm));
		partido.setTarjetasAmarillasVisita((int) Math.round(statsVisita.amarillasProm));
		partido.setTarjetasRojasLocal((int) Math.round(statsLocal.rojasProm));
		partido.setTarjetasRojasVisita((int) Math.round(statsVisita.rojasProm));

		List<Partido> historial = partidoRepository.findAll().stream()
				.filter(p -> p.getEquipoLocal().equals(local.name()) && p.getEquipoVisita().equals(visita.name()))
				.collect(Collectors.toList());

		int victoriasLocal = (int) historial.stream().filter(p -> p.getProbVictoriaLocal() > p.getProbVictoriaVisita()
				&& p.getProbVictoriaLocal() > p.getProbEmpate()).count();
		int victoriasVisita = (int) historial.stream().filter(p -> p.getProbVictoriaVisita() > p.getProbVictoriaLocal()
				&& p.getProbVictoriaVisita() > p.getProbEmpate()).count();
		int empates = (int) historial.stream().filter(
				p -> p.getProbEmpate() > p.getProbVictoriaLocal() && p.getProbEmpate() > p.getProbVictoriaVisita())
				.count();

		partido.setHistorialVictoriasLocal(victoriasLocal);
		partido.setHistorialVictoriasVisita(victoriasVisita);
		partido.setEmpatesHistoricos(empates);

		return mapearADto(partidoRepository.save(partido));
	}

	private Double extract(Object value) {
		return value == null ? 0.0 : Double.parseDouble(value.toString());
	}

	private PartidoDTO mapearADto(Partido partido) {
		PartidoDTO dto = new PartidoDTO();
		dto.setId(partido.getId());
		dto.setEquipoLocal(SeleccionMundial.valueOf(partido.getEquipoLocal()));
		dto.setEquipoVisita(SeleccionMundial.valueOf(partido.getEquipoVisita()));

		dto.setProbVictoriaLocal(partido.getProbVictoriaLocal());
		dto.setProbEmpate(partido.getProbEmpate());
		dto.setProbVictoriaVisita(partido.getProbVictoriaVisita());

		dto.setTarjetasAmarillasLocal(partido.getTarjetasAmarillasLocal());
		dto.setTarjetasAmarillasVisita(partido.getTarjetasAmarillasVisita());
		dto.setTarjetasRojasLocal(partido.getTarjetasRojasLocal());
		dto.setTarjetasRojasVisita(partido.getTarjetasRojasVisita());

		dto.setHistorialVictoriasLocal(partido.getHistorialVictoriasLocal());
		dto.setHistorialVictoriasVisita(partido.getHistorialVictoriasVisita());
		dto.setEmpatesHistoricos(partido.getEmpatesHistoricos());

		return dto;
	}

	public String generarDatasetPredictivo() {
		StringBuilder csv = new StringBuilder();
		csv.append("equipo_local,equipo_visita,prob_local,prob_empate,prob_visita\n");
		for (Partido p : partidoRepository.findAll()) {
			csv.append(p.getEquipoLocal()).append(",").append(p.getEquipoVisita()).append(",")
					.append(p.getProbVictoriaLocal()).append(",").append(p.getProbEmpate()).append(",")
					.append(p.getProbVictoriaVisita()).append("\n");
		}
		return csv.toString();
	}

	public String generarDashboard() {
		List<Partido> partidos = partidoRepository.findAll();
		if (partidos.isEmpty())
			return "<h2>No hay partidos aún</h2>";

		StringBuilder html = new StringBuilder();
		html.append("<html><body style='font-family:Arial;background:#111;color:#fff;padding:20px'>");
		html.append("<h1>⚽ Dashboard Predictor Mundial</h1>");

		double avgLocal = partidos.stream().mapToDouble(Partido::getProbVictoriaLocal).average().orElse(0);
		double avgEmpate = partidos.stream().mapToDouble(Partido::getProbEmpate).average().orElse(0);
		double avgVisita = partidos.stream().mapToDouble(Partido::getProbVictoriaVisita).average().orElse(0);

		html.append("<h3>📊 Promedios globales</h3>");
		html.append("<p>Local: ").append(String.format("%.2f", avgLocal)).append("</p>");
		html.append("<p>Empate: ").append(String.format("%.2f", avgEmpate)).append("</p>");
		html.append("<p>Visita: ").append(String.format("%.2f", avgVisita)).append("</p>");

		html.append("<h3>🔥 Top partidos dominantes (LOCAL)</h3>");
		partidos.stream().sorted((a, b) -> Double.compare(b.getProbVictoriaLocal(), a.getProbVictoriaLocal())).limit(5)
				.forEach(p -> html.append(renderMatch(p)));

		html.append("<h3>⚔️ Partidos más parejos</h3>");
		partidos.stream().sorted((a, b) -> Double.compare(distance(a), distance(b))).limit(5)
				.forEach(p -> html.append(renderMatch(p)));

		html.append("</body></html>");
		return html.toString();
	}

	private String renderMatch(Partido p) {
		return "<div style='margin:10px;padding:10px;border:1px solid #444;border-radius:8px'>" + "<b>"
				+ p.getEquipoLocal() + "</b> vs <b>" + p.getEquipoVisita() + "</b><br>" + "🏠 Local: "
				+ String.format("%.2f", p.getProbVictoriaLocal()) + "<br>" + "🤝 Empate: "
				+ String.format("%.2f", p.getProbEmpate()) + "<br>" + "🚀 Visita: "
				+ String.format("%.2f", p.getProbVictoriaVisita()) + "</div>";
	}

	private double distance(Partido p) {
		return Math.abs(p.getProbVictoriaLocal() - p.getProbVictoriaVisita());
	}

	public String registrarResultado(ResultadoPartido resultadoEnum, ResultadoRequest request) {

		Long id = request.getId();

		Partido partido = partidoRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partido no encontrado con id: " + id));

		String nombreLocal = partido.getEquipoLocal();
		String nombreVisita = partido.getEquipoVisita();

		TeamStats sLocal = stats.get(nombreLocal);
		TeamStats sVisita = stats.get(nombreVisita);

		int golesLocal = request.getGolesAFavor();
		int golesVisita = request.getGolesEnContra();

		switch (resultadoEnum) {
		case VICTORIA -> {
			if (sLocal != null)
				sLocal.forma = Math.min(5, sLocal.forma + 1);
			if (sVisita != null)
				sVisita.forma = Math.max(1, sVisita.forma - 1);
		}
		case DERROTA -> {
			if (sLocal != null)
				sLocal.forma = Math.max(1, sLocal.forma - 1);
			if (sVisita != null)
				sVisita.forma = Math.min(5, sVisita.forma + 1);
		}
		case EMPATE -> {
		}
		}

		if (sLocal != null) {
			sLocal.golesFavor = (sLocal.golesFavor + golesLocal) / 2.0;
			sLocal.golesContra = (sLocal.golesContra + golesVisita) / 2.0;
		}
		if (sVisita != null) {
			sVisita.golesFavor = (sVisita.golesFavor + golesVisita) / 2.0;
			sVisita.golesContra = (sVisita.golesContra + golesLocal) / 2.0;
		}

		return String.format("✅ Resultado registrado: %s %d-%d %s (%s)", nombreLocal, golesLocal, golesVisita,
				nombreVisita, resultadoEnum);
	}
}