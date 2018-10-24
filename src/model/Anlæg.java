package model;

public class Anlæg extends Produkt {

    private int antalHaner;
    private int pant = 0;

	public Anlæg(String navn, Produktgruppe produktgruppe, int antalHaner) {
        super(navn, produktgruppe);
        this.antalHaner = antalHaner;
    }

    public int getAntalHaner() {
        return antalHaner;
    }

    public void setAntalHaner(int antalHaner) {
        this.antalHaner = antalHaner;
    }
    
    @Override
    public int getPant() {
  		return pant;
  	}

    @Override
    public void beregnPris() {
        // TODO Auto-generated method stub

    }

}
