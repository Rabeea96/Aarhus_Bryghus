package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import controller.Controller;
import model.Betalingsmiddel;
import model.Giv_rabat_i_kroner;
import model.Giv_rabat_i_procent;
import model.Ordre;
import model.Ordrelinje;
import model.Prisliste;
import model.Produkt;
import model.Produktgruppe;
import model.Produktpris;
import model.Strategy_giv_rabat;

public class OrdreTest {
	Controller controller = Controller.getInstance();
	private Produktgruppe pg, pg2;
    private Produkt p, rundvisning;
    private Prisliste pl, pl2;
    private Ordre ordre1, ordre2, ordre3, ordre4, ordre5;
    private Betalingsmiddel dankort, mobilepay;
    private boolean studierabat;
    private Strategy_giv_rabat strategy; 
    private Ordrelinje ordrelinje1;
    private Produktpris produktpris, produktprisRundvisning;
	
	@Before
    public void setUp() throws Exception {
        pg = controller.createProduktgruppe("Flaskeøl");
        p = controller.createSimpel_produkt("Efterårsøl", pg);
        pl = controller.createPrisliste("Butik");
        pl2 = controller.createPrisliste("Fredagsbar");
        pg2 = controller.createProduktgruppe("Rundvisning");
        rundvisning = controller.createRundvisning("Eftermiddagesrundvisning", pg2);
        produktpris = controller.createProduktpris(pl, 20.0, p);
        produktprisRundvisning = controller.createProduktpris(pl, 100, rundvisning);
        //ordre uden rabat
        ordre1 = new Ordre(dankort, LocalDate.of(2018, 10, 25), pl);
        //rundvisning uden rabat
        ordre2 = new Ordre(dankort, LocalDate.of(2018, 10, 26), pl, LocalTime.of(13, 00), studierabat);
        //ordre med rabat
        ordre3 = new Ordre(dankort, LocalDate.of(2018, 10, 27), pl, strategy, 50);
        //rundvisning med rabat
        ordre4 = new Ordre(dankort, LocalDate.of(2018, 10, 28), LocalTime.of(13, 00), studierabat, pl, strategy, 50.0);
        //udlejning
        ordre5 = new Ordre(dankort, LocalTime.of(18, 00), LocalDate.of(2018, 10, 29), LocalDate.of(2018, 10, 31), pl);
        
        //opretter en ordrelinje
        ordrelinje1 = new Ordrelinje(5, produktpris, ordre1);
        new Ordrelinje(10, produktprisRundvisning, ordre2);
       }
	
	//constructor 1
	@Test
	public void ordre1_tc1() {
		assertEquals(dankort, ordre1.getBetalingsmiddel());
	}
	
	@Test
	public void ordre1_tc2() {
		assertEquals(LocalDate.of(2018, 10, 25), ordre1.getDato());
	}
	
	@Test
	public void ordre1_tc3() {
		assertEquals(pl, ordre1.getPrisliste());
	}
	
	//constructor 2
	@Test
	public void ordre2_tc1() {
		assertEquals(dankort, ordre2.getBetalingsmiddel());
	}
	
	@Test
	public void ordre2_tc2() {
		assertEquals(LocalDate.of(2018, 10, 26), ordre2.getDato());
	}
	
	@Test
	public void ordre2_tc3() {
		assertEquals(pl, ordre2.getPrisliste());
	}
	
	@Test
	public void ordre2_tc4() {
		assertEquals(LocalTime.of(13, 00), ordre2.getTidspunkt());
	}
	
	@Test
	public void ordre2_tc5() {
		assertEquals(studierabat, ordre2.isStudierabat());
	}
	//constructor 3
	@Test
	public void ordre3_tc1() {
		assertEquals(dankort, ordre3.getBetalingsmiddel());
	}
	
	@Test
	public void ordre3_tc2() {
		assertEquals(LocalDate.of(2018, 10, 27), ordre3.getDato());
	}
	
	@Test
	public void ordre3_tc3() {
		assertEquals(pl, ordre1.getPrisliste());
	}
	
	@Test
	public void ordre3_tc4() {
		assertEquals(strategy, ordre3.getStrategy());
	}
	
	@Test
	public void ordre3_tc5() {
		assertEquals(50.0, ordre3.getRabat(), 0.01);
	}
	//constructor 4
	@Test
	public void ordre4_tc1() {
		assertEquals(dankort, ordre4.getBetalingsmiddel());
	}
	
	@Test
	public void ordre4_tc2() {
		assertEquals(LocalDate.of(2018, 10, 28), ordre4.getDato());
	}
	
	@Test
	public void ordre4_tc3() {
		assertEquals(LocalTime.of(13, 00), ordre4.getTidspunkt());
	}
	
	@Test
	public void ordre4_tc4() {
		assertEquals(studierabat, ordre4.isStudierabat());
	}
	
	@Test
	public void ordre4_tc5() {
		assertEquals(50.0, ordre3.getRabat(), 0.01);
	}
	//constructor 5
	@Test
	public void ordre5_tc1() {
		assertEquals(dankort, ordre5.getBetalingsmiddel());
	}
	
	@Test
	public void ordre5_tc2() {
		assertEquals(LocalTime.of(18, 00), ordre5.getTidspunkt());
	}
	
	@Test
	public void ordre5_tc3() {
		assertEquals(LocalDate.of(2018, 10, 29), ordre5.getStartDato());
	}
	
	@Test
	public void ordre5_tc4() {
		assertEquals(LocalDate.of(2018, 10, 31), ordre5.getSlutDato());
	}
	
	@Test
	public void ordre5_tc5() {
		assertEquals(pl, ordre5.getPrisliste());
	}
	
	//test af isStatus
	@Test
	public void isStatus_tc1(){
		assertTrue(ordre5.isStatus());
	}
	//test af setStatus
	@Test
	public void setStatus_tc1() {
		ordre5.setStatus(false);
		assertFalse(ordre5.isStatus());
	}
	//test af getBetalingsmiddel er udført ovenfor, test af setBetalingsmiddel
	@Test
	public void setBetalingsmiddel_tc1() {
		ordre1.setBetalingsmiddel(mobilepay);
		assertEquals(mobilepay, ordre1.getBetalingsmiddel());
	}
	//test på om ordren bliver oprettet som en rundvisning
	@Test
	public void isRundvisning_tc1() {
		assertTrue(ordre4.isRundvisning());
	}
	//test på setRundvisning
	@Test
	public void setRundvisning_tc1() {
		ordre4.setRundvisning(false);
		assertFalse(ordre4.isRundvisning());
	}
	//test af setDato
	@Test
	public void setDate_tc1() {
		ordre1.setDato(LocalDate.of(2001, 01, 01));
		assertEquals(LocalDate.of(2001, 01, 01), ordre1.getDato());
	}
	//test af setTidspunkt
	@Test
	public void setTidspunkt_tc1() {
		ordre2.setTidspunkt(LocalTime.of(10, 00));
		assertEquals(LocalTime.of(10, 00), ordre2.getTidspunkt());
	}
	//test af setStudierabat
	@Test
	public void setStudierabat_tc1() {
		ordre2.setStudierabat(true);
		assertTrue(ordre2.isStudierabat());
	}
	//test af setStartDato
	@Test
	public void setStartDato_tc1() {
		ordre5.setStartDato(LocalDate.of(2002, 02, 02));
		assertEquals(LocalDate.of(2002, 02, 02), ordre5.getStartDato());
	}
	//test af setSlutDato
	@Test
	public void setSlutDato_tc1() {
		ordre5.setSlutDato(LocalDate.of(2019, 01, 01));
		assertEquals(LocalDate.of(2019, 01, 01), ordre5.getSlutDato());
	}
	//test af getOrdrelinje
	@Test
	public void getOrdrelinjer_tc1() {
		assertEquals(1, ordre1.getOrdrelinjer().size());
	}
	//test af removeOrdrelinje
	@Test
	public void removeOrdrelinje_tc1() {
	ordre1.removeOrdrelinje(ordrelinje1);
	assertEquals(0, ordre1.getOrdrelinjer().size());
	}
	//test af setPrisliste
	@Test
	public void setPrisliste_tc1() {
		ordre1.setPrisliste(pl2);
		assertEquals("Fredagsbar", ordre1.getPrisliste().getNavn());
	}
	//test af setStrategy og getStrategy
	@Test
	public void setStrategy_tc1() {
		
		ordre1.setStrategy(new Giv_rabat_i_kroner());
		assertTrue(ordre1.getStrategy() instanceof Giv_rabat_i_kroner);
	}
	//test af isRabat_angivet og setRabat_angivet
	@Test
	public void isRabat_angivet_tc1() {
		ordre1.setRabat_angivet(true);
		assertTrue(ordre1.isRabat_angivet());
	}
	//test af getRabat
	@Test
	public void getRabat_tc1() {
		assertEquals(50.0, ordre4.getRabat(), 0.01);
	}
	//test af setRabat
	@Test
	public void setRabat_tc1() {
		ordre1.setRabat(100);
		assertEquals(100.0, ordre1.getRabat(), 0.01);
	}
	//test af getOrdreCounter og setOrdreCounter
	@Test
	public void setCounter_tc1() {
		ordre1.setOrdreCounter(3);
		assertEquals(3, ordre1.getOrdreCounter());
	}
	//test af beregnPris_rundvisning
	@Test
	public void beregnPris_rundvisning_tc1() {
		assertTrue(ordre2.isRundvisning());
	}
	//test af beregnPris_rundvisning if (efter 16) metoden kaldes i samletpris()
	@Test
	public void BeregnPris_rundvisning_tc2() {
		ordre2.setTidspunkt(LocalTime.of(16, 00));
		assertEquals(1200.0, ordre2.samletpris(), 0.01);
	}
	//test af beregnPris_rundvsining, if (studierabat == true) metoden kaldes i samletpris()
	@Test
	public void BeregnPris_rundvisning_tc3() {
		ordre2.setStudierabat(true);
		assertEquals(900.0, ordre2.samletpris(), 0.01);
	}
	//test af samletpris
	@Test
	public void samletpris_uc1() {
		ordrelinje1 = new Ordrelinje(5, produktpris, ordre1);
		assertEquals(200.0, ordre1.samletpris(), 0.01);
	}
	//test af samletpris_med_rabat
	@Test
	public void samletpris_med_rabat_uc1() {
		new Ordrelinje(10, produktpris, ordre3);
		new Ordrelinje(5, produktpris, ordre3);
		ordre3.setStrategy(new Giv_rabat_i_procent());
		ordre3.samletpris(); 
		assertEquals(150.0, ordre3.samletpris_med_rabat(), 0.01);
	}
		
	
}
