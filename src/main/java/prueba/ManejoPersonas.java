package prueba;

import datos.*;
import dominio.Persona;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Edwin Jaldin
 */
public class ManejoPersonas {

	public static void main(String[] args) {

		Connection conexion = null;
		try {
			//conexion externa (conexion de transaccion) para mantener abierta la conexion
			conexion = Conexion.getConnection();
			if (conexion.getAutoCommit()) {
				conexion.setAutoCommit(false);
			}
			//le pasamos la conexion
			PersonaDAO personaDao = new PersonaDAO(conexion);
			gestionPersonal(personaDao);
			//si no hay ningun error al ejecutar las sentencias(transaccion),se podrá guardar los cambios
			//por eso procesamos las excepciones en este punto y no en los metodos correspondientes.
			conexion.commit();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex);
			JOptionPane.showMessageDialog(null, "Ejecutando rollback");
			try {
				//si hay algun error por ejemplo; si supera los caracteres del nombre o apellido
				//se revertiran los cambios al ejecutar rollback.
				conexion.rollback();
			} catch (SQLException ex1) {
				JOptionPane.showMessageDialog(null, ex1);
			}
		}

	}//----FIN DEL MAIN------

	//*************************************** GESTION PERSONAS ****************************
	/**
	 * En este metodo podemos realizar:SELECT,INSERT,UPDATE Y DELETE alguna de estas sentencias modifican el estado de la BD(transaccion) Todas estas sentencias formaran una transaccion y si algo falla no se guardara ninguna sentencia.
	 *
	 * @param personaDao la clase que se encarga del acceso a datos (JDBC).
	 * @throws SQLException Las excepciones de deben propagar para poder utilizar el commit o rollbac en el main
	 */
	public static void gestionPersonal(PersonaDAO personaDao) throws SQLException {
		int opc = 0;
		boolean salir = false;
		String menu = "1. Listar Personas\n"
				+ "2. Insertar Persona\n"
				+ "3. Actualizar Persona\n"
				+ "4. Eliminar Persona\n"
				+ "5. Volver";
		try {

			while (salir != true) {
				opc = Integer.valueOf(JOptionPane.showInputDialog(null, menu, JOptionPane.QUESTION_MESSAGE));
				switch (opc) {
					case 1://LISTAMOS LOS OBJETOS PERSONA DE LA BD.(SELECT)
						imprimirPersonas(personaDao);
						break;
					case 2://INSERTAMOS UN NUEVO OBJETO DE TIPO PERSONA (INSERT)
						insertarPersona(personaDao);
						break;
					case 3://ACTUALIZAMOS UN REGISTRO (UPDATE)
						actualizarPersona(personaDao);
						break;
					case 4://ELIMINAMOS UN REGISTRO (DELETE)
						eliminarPersona(personaDao);
						break;
					case 5://VOLVER
						salir = true;
						break;
					default:
						JOptionPane.showMessageDialog(null, "Opcion incorrecta!!", "Error", JOptionPane.WARNING_MESSAGE);
						break;
				}
			}
		} catch (HeadlessException | NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Error al ingresar opcion", "Error de entrada", JOptionPane.WARNING_MESSAGE);
		}
	}
//propagamos las excepciones para controlar la transaccion en el main

	public static void imprimirPersonas(PersonaDAO personaDao) throws SQLException {
		List<Persona> personas = new ArrayList<>();

		personas = personaDao.listaPersonas();
		String res = new String();
		for (Persona persona : personas) {
			res += persona;
		}
		JOptionPane.showMessageDialog(null, res, "PERSONAS", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void insertarPersona(PersonaDAO personaDao) throws SQLException {
		int registros = 0;
		String nombre, apellido, email, tlfno;
		nombre = JOptionPane.showInputDialog("Nombre:");
		apellido = JOptionPane.showInputDialog("Apellido:");
		email = JOptionPane.showInputDialog("Email:");
		tlfno = JOptionPane.showInputDialog("Telefono:");

		Persona personaNueva = new Persona(nombre, apellido, email, tlfno);
		registros = personaDao.insertar(personaNueva);
		JOptionPane.showMessageDialog(null, registros, "REGISTROS INSERTADOS", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void actualizarPersona(PersonaDAO personaDao) throws SQLException {
		int idPersona, registros = 0;
		String nombre, apellido, email, tlfno;
		idPersona = Integer.valueOf(JOptionPane.showInputDialog("Id Persona a modificar: "));
		nombre = JOptionPane.showInputDialog("Nombre:");
		apellido = JOptionPane.showInputDialog("Apellido:");
		email = JOptionPane.showInputDialog("Email:");
		tlfno = JOptionPane.showInputDialog("Telefono:");

		Persona persona = new Persona(idPersona, nombre, apellido, email, tlfno);
		registros = personaDao.actualizar(persona);
		JOptionPane.showMessageDialog(null, registros, "REGISTROS ACTUALIZADOS", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void eliminarPersona(PersonaDAO personaDao) throws SQLException {
		int idPersona, registros = 0;
		idPersona = Integer.valueOf(JOptionPane.showInputDialog("Id Persona a eliminar: "));
		Persona persona = new Persona(idPersona);
		registros = personaDao.eliminar(persona);
		JOptionPane.showMessageDialog(null, registros, "REGISTROS ELIMINADOS", JOptionPane.INFORMATION_MESSAGE);
	}

}
