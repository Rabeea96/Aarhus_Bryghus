package model;

public class Fustage extends Produkt {

    private int liter;
    private int pant = 0;

	public Fustage(String navn, Produktgruppe produktgruppe, int liter) {
        super(navn, produktgruppe);
        super.setNavn(navn + " (Pant: 200kr.)");
        this.liter = liter;
    }

    public int getLiter() {
        return liter;
    }

    public void setLiter(int liter) {
        this.liter = liter;
    }
    
    @Override
    public int getPant() {
  		return pant;
  	} 
    

    @Override
    public void beregnPris() {

    }

}
