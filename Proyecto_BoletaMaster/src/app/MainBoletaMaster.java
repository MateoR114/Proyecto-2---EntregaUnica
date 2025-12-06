package app;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Venue;
import GUI.VentanaClienteBoleta;
import Tiquetes.Tiquete;
import Tiquetes.PaqueteTiquetes;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;

public class MainBoletaMaster {

    public static void main(String[] args) {
        MainBoletaMaster app = new MainBoletaMaster();
        app.iniciar();
    }

    private void iniciar() {
        Administrador admin = new Administrador("admin", "admin1", "Administrador", 1);
        admin.establecerCargoServicioPorTipo("Festival", 0.10);
        admin.establecerCargoServicioPorTipo("Concierto", 0.12);

        Organizador orgRock = new Organizador("orgRock", "SeArmoElPogo", "Rock al Parque", 10, 0.0, "IDARTES");
        Organizador orgVallenato = new Organizador("orgVallenato", "SeJodioElFinDeSemana", "Silvestre Producciones", 11, 0.0, "Silvestre Producciones");
        Organizador orgPop = new Organizador("orgPop", "MaicolJakzon", "Pop Global", 12, 0.0, "Pop Global");
        Organizador orgFolclor = new Organizador("orgFolclor", "LaVamoATumba", "Tradición Caribe", 13, 0.0, "Tradición Caribe");

        Venue parqueSimonBolivar = new Venue(100, "Parque Simón Bolívar", "Bogotá", 50000, "Aforo masivo al aire libre");
        parqueSimonBolivar.setAprobado(true);

        Venue movistarArena = new Venue(101, "Movistar Arena", "Bogotá", 14000, "Escenario cubierto");
        movistarArena.setAprobado(true);

        Venue estadioMetropolitano = new Venue(102, "Estadio Metropolitano", "Barranquilla", 45000, "Escenario de gran formato");
        estadioMetropolitano.setAprobado(true);

        Venue plazaToros = new Venue(103, "Plaza de Toros La Santamaría", "Bogotá", 15000, "Escenario circular");
        plazaToros.setAprobado(true);

        LocalDateTime fechaRockParque = LocalDateTime.now().plusDays(30);
        LocalDateTime fechaSilvestre = LocalDateTime.now().plusDays(40);
        LocalDateTime fechaShakira = LocalDateTime.now().plusDays(50);
        LocalDateTime fechaJBalvin = LocalDateTime.now().plusDays(60);
        LocalDateTime fechaGaiteros = LocalDateTime.now().plusDays(35);

        Evento rockAlParque = new Evento("Rock al Parque", 1000, parqueSimonBolivar, fechaRockParque, orgRock, "Festival", admin);
        Evento silvestreDangond = new Evento("Silvestre Dangond - El ultimo Baile", 1001, estadioMetropolitano, fechaSilvestre, orgVallenato, "Concierto", admin);
        Evento shakiraShow = new Evento("Shakira - Tour Las Mujeres Ya No Lloran", 1002, movistarArena, fechaShakira, orgPop, "Concierto", admin);
        Evento jBalvinShow = new Evento("J Balvin - Colores Album", 1003, movistarArena, fechaJBalvin, orgPop, "Concierto", admin);
        Evento gaiterosShow = new Evento("Los Gaiteros de San Jacinto ", 1004, plazaToros, fechaGaiteros, orgFolclor, "Concierto", admin);

        Localidad rockGeneral = rockAlParque.crearLocalidad("General", false, 30000, 80000.0, 70000.0, 60000.0, 120000.0);
        Localidad rockVip = rockAlParque.crearLocalidad("VIP", true, 5000, 180000.0, 160000.0, 140000.0, 250000.0);

        Localidad silvestreGeneral = silvestreDangond.crearLocalidad("Gradería General", false, 20000, 90000.0, 80000.0, 70000.0, 130000.0);
        Localidad silvestreVip = silvestreDangond.crearLocalidad("Palco VIP", true, 3000, 220000.0, 200000.0, 180000.0, 300000.0);

        Localidad shakiraPlatea = shakiraShow.crearLocalidad("Platea", true, 4000, 250000.0, 230000.0, 210000.0, 320000.0);
        Localidad shakiraGeneral = shakiraShow.crearLocalidad("General", false, 8000, 150000.0, 130000.0, 120000.0, 220000.0);

        Localidad jBalvinCancha = jBalvinShow.crearLocalidad("Cancha", false, 7000, 140000.0, 120000.0, 110000.0, 210000.0);
        Localidad jBalvinVip = jBalvinShow.crearLocalidad("VIP", true, 2000, 260000.0, 240000.0, 220000.0, 330000.0);

        Localidad gaiterosGeneral = gaiterosShow.crearLocalidad("General", false, 6000, 60000.0, 50000.0, 45000.0, 100000.0);
        Localidad gaiterosPreferencial = gaiterosShow.crearLocalidad("Preferencial", true, 2000, 110000.0, 100000.0, 90000.0, 160000.0);

        Cliente clientePrincipal = new Cliente("mateo", "201916012 ", "Mateo Reales", 500);
        clientePrincipal.setSaldo(2000000.0);

        ArrayList<Tiquete> tiquetesCompra = new ArrayList<Tiquete>();
        ArrayList<PaqueteTiquetes> paquetesCompra = new ArrayList<PaqueteTiquetes>();

        Tiquete tiqRockGeneral = clientePrincipal.precompraIndividualNoNumerada(1, rockAlParque, rockGeneral, false);
        tiquetesCompra.add(tiqRockGeneral);

        Tiquete tiqSilvestreVip = clientePrincipal.precompraIndividualNumerada(2, silvestreDangond, silvestreVip, 10);
        tiquetesCompra.add(tiqSilvestreVip);

        Tiquete tiqShakiraPlatea = clientePrincipal.precompraIndividualNumerada(3, shakiraShow, shakiraPlatea, 25);
        tiquetesCompra.add(tiqShakiraPlatea);

        Tiquete tiqJBalvinCancha = clientePrincipal.precompraIndividualNoNumerada(4, jBalvinShow, jBalvinCancha, false);
        tiquetesCompra.add(tiqJBalvinCancha);

        Tiquete tiqGaiterosPreferencial = clientePrincipal.precompraIndividualNumerada(5, gaiterosShow, gaiterosPreferencial, 5);
        tiquetesCompra.add(tiqGaiterosPreferencial);

        clientePrincipal.hacerCompra(tiquetesCompra, paquetesCompra, false);

        VentanaClienteBoleta ventana = new VentanaClienteBoleta(clientePrincipal);
        ventana.setVisible(true);
    }
}
