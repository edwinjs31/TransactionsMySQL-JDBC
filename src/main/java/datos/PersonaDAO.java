/*
 *DAO=Acces Data Object 
 * Al cerrar la conexion se realiza un commit aunque esté deshabilitado el autocommit.
 * y se se podrá revertir con rollback.
 */
package datos;

import static datos.Conexion.close;
import dominio.Persona;
import java.sql.*;
import java.util.*;

/**
 * Clase de acceso a datos: recuperacion de datos y creacion de objetos 'Persona'.
 *
 * @author Edwin Jaldin
 */
public class PersonaDAO {

	private Connection conexionTransaccional;
	private static final String SQL_SELECT = "SELECT id_persona, nombre, apellido, email, telefono FROM persona";
	private static final String SQL_INSERT = "INSERT INTO persona(nombre, apellido, email, telefono) VALUES(?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE persona SET nombre=?, apellido=?, email=?, telefono=? WHERE id_persona=?";
	private static final String SQL_DELETE = "DELETE FROM persona WHERE id_persona=?";

	public PersonaDAO() {
	}

	public PersonaDAO(Connection conexionTransaccional) {
		this.conexionTransaccional = conexionTransaccional;
	}

	/**
	 * Esta funcion llamando a los metodos de la clase conexion, se encarga de leer (consultar-SELECT) los registros de la BD y agregarlos en una lista de personas.
	 *
	 * @return Retorna una lista de objetos de tipo 'Persona' extraidos de la BD.
	 * @throws java.sql.SQLException 
	 * Propagamos la excepcion, para que pueda ejecutar el rollback mas adelande
	 */
	public List<Persona> listaPersonas() throws SQLException {
		Connection conexion = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Persona persona = null;
		List<Persona> personas = new ArrayList<>();

		try {
			/*
			Si el objeto conexion transaccional es diferente a null usaremos esa conexion
			y no debe cerrarse en este metodo, para no cerrar toda la transaccion.
			Caso contrario obtenemos una nueva conexion y debe cerrarse en este metodo.
			 */
			conexion = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
			stmt = conexion.prepareStatement(SQL_SELECT);
			rs = stmt.executeQuery();//ejecucion de la consulta
			while (rs.next()) {
				int idPersona = rs.getInt("id_persona");
				String nombre = rs.getString("nombre");
				String apellido = rs.getString("apellido");
				String email = rs.getString("email");
				String telefono = rs.getString("telefono");

				persona = new Persona(idPersona, nombre, apellido, email, telefono);
				personas.add(persona);

			}

		} finally {
			try {
				//cerramos conexiones llamando a los metodos estaticos de la clase 'Conexion'
				Conexion.close(rs);
				Conexion.close(stmt);
				//si la conexion fue creada en este metodo,entonces debe cerrarse tambien en el mismo
				if (this.conexionTransaccional == null) {
					Conexion.close(conexion);
				}

			} catch (SQLException ex) {
				ex.printStackTrace(System.out);
			}
		}

		return personas;
	}

	/**
	 * Con esta funcion podemos hacer un INSERT de un nuevo objeto 'Persona'.
	 *
	 * @param persona Recibe como parametro un objeto 'Persona', al que se le agregara a la BD.
	 * @return Retorna el numero de registros modificados.
	 */
	public int insertar(Persona persona) throws SQLException {
		Connection conexion = null;
		PreparedStatement stmt = null;
		int registros = 0;
		try {
			/*
			Si el objeto conexion transaccional es diferente a null usaremos esa conexion
			y no debe cerrarse en este metodo, para no cerrar toda la transaccion.
			Caso contrario obtenemos una nueva conexion y debe cerrarse en este metodo.
			 */
			conexion = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
			stmt = conexion.prepareStatement(SQL_INSERT);
			//como vamos hacer un 'INSERT' asignamos los valores a insertar.
			//el indice corresponde a la posicion dentro del parentecis de 'VALUES' 
			stmt.setString(1, persona.getNombre());
			stmt.setString(2, persona.getApellido());
			stmt.setString(3, persona.getEmail());
			stmt.setString(4, persona.getTelefono());

			//significa los cambios en la BD, valido para UPDATE, INSERT, DELETE.
			//ejecuta la sentencia INSERT y devuelve el numero de registros afectados.
			registros = stmt.executeUpdate();

		} finally {
			try {
				//al importar la clase conexion y el metodo estatico se puede llamar directamente,
				close(stmt);
				//si la conexion fue creada en este metodo,entonces debe cerrarse tambien en el mismo
				if (this.conexionTransaccional == null) {
					Conexion.close(conexion);
				}
			} catch (SQLException ex) {
				ex.printStackTrace(System.out);
			}

		}
		return registros;
	}

	public int actualizar(Persona persona) throws SQLException {

		Connection conexion = null;
		PreparedStatement stmt = null;
		int registros = 0;

		try {
			/*
			Si el objeto conexion transaccional es diferente a null usaremos esa conexion
			y no debe cerrarse en este metodo, para no cerrar toda la transaccion.
			Caso contrario obtenemos una nueva conexion y debe cerrarse en este metodo.
			 */
			conexion = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
			stmt = conexion.prepareStatement(SQL_UPDATE);

			stmt.setString(1, persona.getNombre());
			stmt.setString(2, persona.getApellido());
			stmt.setString(3, persona.getEmail());
			stmt.setString(4, persona.getTelefono());
			stmt.setInt(5, persona.getIdPersona());

			registros = stmt.executeUpdate();

		} finally {
			try {
				close(stmt);
				//si la conexion fue creada en este metodo,entonces debe cerrarse tambien en el mismo
				if (this.conexionTransaccional == null) {
					Conexion.close(conexion);
				}
			} catch (SQLException ex) {
				ex.printStackTrace(System.out);
			}
		}
		return registros;
	}

	public int eliminar(Persona persona) throws SQLException {
		Connection conexion = null;
		PreparedStatement stmt = null;
		int registros = 0;

		try {
			/*
			Si el objeto conexion transaccional es diferente a null usaremos esa conexion
			y no debe cerrarse en este metodo, para no cerrar toda la transaccion.
			Caso contrario obtenemos una nueva conexion y debe cerrarse en este metodo.
			 */
			conexion = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
			stmt = conexion.prepareStatement(SQL_DELETE);

			stmt.setInt(1, persona.getIdPersona());

			registros = stmt.executeUpdate();

		} finally {
			try {
				close(stmt);
				//si la conexion fue creada en este metodo,entonces debe cerrarse tambien en el mismo
				if (this.conexionTransaccional == null) {
					Conexion.close(conexion);
				}
			} catch (SQLException ex) {
				ex.printStackTrace(System.out);
			}
		}
		return registros;
	}
}
