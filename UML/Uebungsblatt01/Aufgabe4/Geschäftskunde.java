public class Geschäftskunde extends Kunde{
    private String firmenname;
    private Adresse domizilAdresse;

    public Geschäftskunde(Konto[] konten, String firmenname) {
        super(konten);
        this.firmenname = firmenname;
    }

    public String getFirmenname() {
        return firmenname;
    }

    public Adresse getDomizilAdresse() {
        return domizilAdresse;
    }

    public void setFirmenname(String firmenname) {
        this.firmenname = firmenname;
    }

    public void setDomizilAdresse(Adresse domizilAdresse) {
        this.domizilAdresse = domizilAdresse;
    }
}
