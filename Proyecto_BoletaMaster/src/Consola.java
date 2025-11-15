import Eventos.*;
import Usuarios.*;
import Tiquetes.*;


import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Consola{
	
	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);
	    	System.out.println("==============================================");
	        System.out.println("     ¡Bienvenido a BoletaMaster!              ");
	        System.out.println("==============================================");

	        // ---------------------------------------------------------------------
	        // Creación de usuarios base
	        // ---------------------------------------------------------------------
	        System.out.print("Crea el Administrador.\nLogin: ");
	        String loginAdmin = sc.nextLine();
	        System.out.print("Contraseña: ");
	        String passwordAdmin = sc.nextLine();
	        System.out.print("Nombre: ");
	        String nombreAdmin = sc.nextLine();
	        Administrador admin = new Administrador(loginAdmin, passwordAdmin, nombreAdmin, 1);
	        System.out.println("Administrador creado exitosamente.\n");

	        System.out.print("Crea el Organizador.\nLogin: ");
	        String loginOrg = sc.nextLine();
	        System.out.print("Contraseña: ");
	        String passwordOrg = sc.nextLine();
	        System.out.print("Nombre: ");
	        String nombreOrg = sc.nextLine();
	        System.out.print("Nombre Organización: ");
	        String nombreOrgan = sc.nextLine();
	        Organizador org = new Organizador(loginOrg, passwordOrg, nombreOrg, 2, 0.0, nombreOrgan);
	        System.out.println("Organizador creado exitosamente.\n");

	        System.out.print("Crea el Cliente.\nLogin: ");
	        String loginCli = sc.nextLine();
	        System.out.print("Contraseña: ");
	        String passwordCli = sc.nextLine();
	        System.out.print("Nombre: ");
	        String nombreCli = sc.nextLine();
	        Cliente cliente = new Cliente(loginCli, passwordCli, nombreCli, 3);
	        System.out.println("Cliente creado exitosamente.\n");

	        // ---------------------------------------------------------------------
	        // Creación de evento y localidad
	        // ---------------------------------------------------------------------
	        System.out.println("Creando un evento de prueba...");
	        Venue venue = new Venue(10, "Estadio Nacional", "Bogotá", 50000, "");
	        Evento evento = new Evento("Concierto BoletaMaster", 100, venue, LocalDateTime.now().plusDays(5), org, "Concierto", admin);
	        Localidad localidad = new Localidad("VIP", true, 100000, 120.5, evento, 100, 0, 500);
	        evento.crearLocalidad("Platea", false, 10000, 200, 150, 0, 400);

	        System.out.println("Evento y localidad creados exitosamente.\n");

	        // ---------------------------------------------------------------------
	        // Compra de tiquetes
	        // ---------------------------------------------------------------------
	        System.out.println("Simulando compra de tiquetes individuales...");
	        try {
				Tiquete individual = cliente.precompraIndividualNumerada(001, evento, localidad, 25);
				Tiquete individual2 = cliente.precompraIndividualNumerada(002, evento, localidad, 25);
				ArrayList<Integer> numerosAsientos = new ArrayList<>();
				numerosAsientos.add(1);

				PaqueteTiquetes paquete1 = cliente.precompraPaqueteMultipleMismoEvento(003, evento, localidad, 1, false, numerosAsientos);
				ArrayList<Tiquete> carritoComprasIndividuales = new ArrayList<>();
				ArrayList<PaqueteTiquetes> carritComprasPaquetes = new ArrayList<>();
				carritoComprasIndividuales.add(individual);
				carritoComprasIndividuales.add(individual2);
				carritComprasPaquetes.add(paquete1);
	            Compra compra = cliente.hacerCompra(carritoComprasIndividuales, carritComprasPaquetes, false);
	            System.out.println("Compra realizada exitosamente. Total pagado: $" + compra.getvalorCompra());
	        } catch (Exception e) {
	            System.out.println("Error en la compra: " + e.getMessage());
	        }

	        // ---------------------------------------------------------------------
	        // Solicitud de reembolso
	        // ---------------------------------------------------------------------
	        System.out.println("\nProbando solicitud de reembolso...");
	        if (!cliente.getCompras().isEmpty()) {
	            Tiquete tiquete = cliente.getCompras().get(0).getTiquetesComprados().get(0);
	            cliente.solicitarReembolsoPorCalamidadTiquete(tiquete, admin);
	            System.out.println("Solicitud enviada al administrador.");
	        }

	        // ---------------------------------------------------------------------
	        // Procesamiento de reembolsos
	        // ---------------------------------------------------------------------
	        System.out.println("\nProcesando reembolsos...");
	        if (!admin.getSolicitudesReembolsoTiquete().isEmpty()) {
	            admin.aprobarReembolsoPorCalamidadTiquete(cliente, admin.getSolicitudesReembolsoTiquete().get(cliente));
				admin.rechazarReembolsoPorCalamidadPaquete(cliente);
	            System.out.println("Reembolso autorizado al cliente. Nuevo saldo: $" + cliente.getSaldo());
	        }

	        // ---------------------------------------------------------------------
	        // Fin de pruebas
	        // ---------------------------------------------------------------------
	        System.out.println("\n==============================================");
	        System.out.println("   Fin de la simulación BoletaMaster.");
	        System.out.println("==============================================");
	        sc.close();
		
		
	
		}


}

	
		

