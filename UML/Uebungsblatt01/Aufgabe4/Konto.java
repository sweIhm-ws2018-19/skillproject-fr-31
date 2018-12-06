public class Konto {
    private String beizeichung;
    private Kunde[] zeichunungsberechtigter;
    private Geldbetrag geldbetrag;

    public double saldo(){
        return geldbetrag.getBetrag();
    }
    public void einzahlen(Geldbetrag einzahlung){
        geldbetrag.setBetrag(geldbetrag.getBetrag() + einzahlung.getBetrag());
    }
}
