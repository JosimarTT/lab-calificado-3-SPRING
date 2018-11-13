package pe.tantahuilca.josimar.solicitudesapi.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pe.tantahuilca.josimar.solicitudesapi.models.ResponseMessage;
import pe.tantahuilca.josimar.solicitudesapi.models.Solicitud;
import pe.tantahuilca.josimar.solicitudesapi.services.SolicitudService;

@RestController
public class SolicitudController {

	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	private static final String FILEPATH = "/Files/SpringProjects/solicitudes-api/images";

	@Autowired
	private SolicitudService solicitudService;

	@GetMapping("/solicitudes")
	public List<Solicitud> solicitudes() {
		logger.info("call solicitudes");
		List<Solicitud> solicitudes = solicitudService.listar();
		return solicitudes;
	}

	@GetMapping("/solicitudes/images/{filename:.+}")
	public ResponseEntity<Resource> files(@PathVariable String filename) throws Exception {
		logger.info("call images: " + filename);

		Resource resource = new UrlResource(Paths.get(FILEPATH).resolve(filename).toUri());
		logger.info("Resource: " + resource);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Paths.get(FILEPATH).resolve(filename)))
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength())).body(resource);
	}

	@PostMapping("/solicitudes") // https://spring.io/guides/gs/uploading-files/
	public ResponseMessage crear(@RequestParam("correo") String correo,
			@RequestParam(name = "imagen", required = false) MultipartFile imagen, @RequestParam("tipo") String tipo,
			@RequestParam("motivo") String motivo) {
		logger.info("call crear(" + correo + ", " + tipo + ", " + motivo + ", " + imagen + ")");

		Solicitud solicitud = new Solicitud();
		solicitud.setCorreo(correo);
		solicitud.setTipo(tipo);
		solicitud.setMotivo(motivo);

		if (imagen != null && !imagen.isEmpty()) {
			try {

				solicitud.setImagen(imagen.getOriginalFilename());

				Files.copy(imagen.getInputStream(), Paths.get(FILEPATH).resolve(imagen.getOriginalFilename()));

			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		solicitudService.crear(solicitud);

		return ResponseMessage.success("Registro completo");
	}

}
