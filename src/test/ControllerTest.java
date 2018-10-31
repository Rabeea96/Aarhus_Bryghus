package test;

import static org.junit.Assert.*;

import org.junit.Before;

import model.*;
import controller.Controller;
import org.junit.Test;

public class ControllerTest {

    private Controller controller;
    private Produktgruppe pg;
    private Produkt p;
    private Prisliste pl;

    @Before
    public void setUp() throws Exception {
        controller = Controller.getInstance();
        pg = controller.createProduktgruppe("Produktgruppe");
        p = controller.createSimpel_produkt("Produkt", pg);
        pl = controller.createPrisliste("Prisliste");
    }

    // test af 3 ikke-trivielle metoder

    // createProduktgruppe
    @Test
    public void createProduktgruppe_Test1() {
        assertNotNull(pg);
    }

    @Test
    public void createProduktgruppe_Test2() {
        assertEquals("Produktgruppe", pg.getNavn());
    }

    @Test
    public void createProduktgruppe_Test3() {
        assertTrue(controller.getProduktgrupper().contains(pg));
    }

    // createSimpelProdukt
    @Test
    public void createSimpelProdukt_Test1() {
        assertNotNull(p);
    }

    @Test
    public void createSimpelProdukt_Test2() {
        assertEquals("Produkt", p.getNavn());
    }

    @Test
    public void createSimpelProdukt_Test3() {
        assertTrue(controller.getProdukter().contains(p));
    }

    @Test
    public void createSimpelProdukt_Test4() {
        assertTrue(pg.getProdukter().contains(p));
    }

    // createPrisliste
    @Test
    public void createPrisliste_Test1() {
        assertNotNull(pl);
    }

    @Test
    public void createPrisliste_Test2() {
        assertEquals("Prisliste", pl.getNavn());
    }

    @Test
    public void createPrisliste_Test3() {
        assertTrue(controller.getPrislister().contains(pl));
    }

    // test af en central use case - UC1: Registrere et produktsalg
    @Test
    public void registrereSalg_Test1() {
    }

}
