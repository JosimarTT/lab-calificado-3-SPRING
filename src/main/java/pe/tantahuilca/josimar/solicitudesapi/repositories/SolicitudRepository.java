package pe.tantahuilca.josimar.solicitudesapi.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import pe.tantahuilca.josimar.solicitudesapi.models.Solicitud;

@Repository
public class SolicitudRepository {
	private static final Logger logger = LoggerFactory.getLogger(SolicitudRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Solicitud> listar() {
		logger.info("call listar()");

		String sql = "select * from solicitudes";

		List<Solicitud> solicitudes = jdbcTemplate.query(sql, new RowMapper<Solicitud>() {
			public Solicitud mapRow(ResultSet rs, int rowNum) throws SQLException {
				Solicitud solicitud = new Solicitud();
				solicitud.setId(rs.getInt("id"));
				solicitud.setCorreo(rs.getString("correo"));
				solicitud.setTipo(rs.getString("tipo"));
				solicitud.setMotivo(rs.getString("motivo"));
				solicitud.setImagen(rs.getString("imagen"));
				return solicitud;
			}
		});
		logger.info("solicitudes: " + solicitudes);
		return solicitudes;
	}

	public void crear(Solicitud solicitud) {
		logger.info("crear " + solicitud);

		String sql = "insert into solicitudes (correo, tipo, motivo, imagen) values (?, ?, ?, ?)";

		jdbcTemplate.update(sql, solicitud.getCorreo(), solicitud.getTipo(), solicitud.getMotivo(),
				solicitud.getImagen());
	}

}
