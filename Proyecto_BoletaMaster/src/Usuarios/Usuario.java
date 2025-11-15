package Usuarios;

/**
 * Clase abstracta que representa un usuario del sistema BoletaMaster.
 * Contiene la información común a clientes, organizadores y administradores.
 */
public abstract class Usuario{
	// Atributos 
	protected String login;
	protected String password;
	protected String nombre;
	
	
	// Ctor
    /**
     * Crea un usuario del sistema.
     * @param login Identificador único de inicio de sesión.
     * @param password Contraseña del usuario.
     * @param nombre Nombre visible del usuario.
     * @pre login, password y nombre son textos no vacíos.
     * @post El usuario queda inicializado con el login, la contraseña y el nombre indicados.
     */
	public Usuario(String login, String password, String nombre) {
		this.setLogin(login);
		this.setPassword(password);
		this.setNombre(nombre);
	}

	// Getters y Setters
	
	/**
     * Obtiene el login del usuario.
     * @return Login actual del usuario.
     */
	public String getLogin() {
		return login;
	}
	/**
     * Actualiza el login del usuario.
     * @param login Nuevo identificador.
     * @pre login es un texto no vacío.
     */
	public void setLogin(String login) {
		this.login = login;
	}

    /**
     * Obtiene la contraseña del usuario.
     * @return Contraseña actual.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Actualiza la contraseña del usuario.
     * @param password Nueva contraseña.
     * @pre password es un texto no vacío.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return Nombre actual.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Actualiza el nombre del usuario.
     * @param nombre Nuevo nombre visible.
     * @pre nombre es un texto no vacío.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    

    // Autenticación básica
    /**
     * Revisa que el login y el password sean coherentes.
     * @param login Login de usuario.
     * @param password Contraseña a autenticar.
     * @pre login  y password son un texto no vacío.
     */
    public boolean autenticar(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

}