package gui;

import java.time.LocalDate;

import container.Container;
import controller.*;
import model.*;

public class MainApp {

    public static void main(String[] args) {
        // Controller instans
        Controller controller = Controller.getInstance();

        // Container instans
        Container container = Container.getInstance();

        controller.createSomeObjects();

        // for (Produktgruppe p : container.getProduktgrupper()) {
        // System.out.println(p.getNavn() + ": " + p.getProdukter());
        // }

        for (Ordre o : container.getOrdre()) {
            // kalder beregnpris på alle ordrer for at salget registreres på alle ordrer
            controller.beregnPris(o);
        }

        // oversigt over dagens salg
        // controller.getDagenssalg(LocalDate.of(2018, 10, 8));

        for (Salg s : container.getSalg()) {
            System.out.println(s);
        }

        // System.out.println(controller.getAntal_brugte_klip(LocalDate.of(2018, 10, 8),
        // LocalDate.of(2018, 10, 10)));

        System.out.println(controller.getAntal_solgte_klippekort());

    }

}
