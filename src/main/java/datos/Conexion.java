package datos;

import java.sql.*;

/**
 * Esta clase se encarga de la conexion a la BD y posteriorment cerrar las conexiones.
 *
 * @author Edwin Jaldin
 */
public class Conexion {

	//cadena de conexion a la BD de MySql.
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	//usuario de acceso de MySql
	private static final String JDBC_USER = "root";
	//contraseña de acceso de MySql
	private static final String JDBC_PASSWORD = "admin";

	/**
	 * Creamos la conexion a la BD.
	 *
	 * @return Retorna una conextion.
	 * @throws SQLException Se propaga para posteriormente implementarla.
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	/**
	 * Sobrecarga de metodo para cerray las conexiones abiertas.
	 *
	 * @param resultado Recibe un tipo ResultSet (resultado).
	 * @throws SQLException
	 */
	public static void close(ResultSet resultado) throws SQLException {
		resultado.close();
	}

	/**
	 * Sobrecarga de metodo para cerray las conexiones abiertas.
	 *
	 * @param instruccion Recibe un tipo Statement (Instruccion).
	 * @throws SQLException
	 */
	public static void close(Statement instruccion) throws SQLException {
		instruccion.close();
	}

	/**
	 * Sobrecarga de metodo para cerray las conexiones abiertas.
	 *
	 * @param instruccion Recibe un tipo PreparedStatement (instruccion mas eficiente para consultas)
	 * @throws SQLException
	 */
	public static void close(PreparedStatement instruccion) throws SQLException {
		instruccion.close();
	}

	/**
	 * Sobrecarga de metodo para cerray las conexiones abiertas.
	 *
	 * @param conexion Recibe un tipo Connection (conexion)
	 * @throws SQLException
	 */
	public static void close(Connection conexion) throws SQLException {
		conexion.close();
	}
}
