public class Privatkunde extends Kunde {
    private String vorname;
    private String nachname;
    private Adresse postAdresse;

    public Privatkunde(Konto[] konten, String vorname) {
        super(konten);
        this.vorname = vorname;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public Adresse getPostAdresse() {
        return postAdresse;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setPostAdresse(Adresse postAdresse) {
        this.postAdresse = postAdresse;
    }
}
