package guidelines;

public class SpeechStrings {
    private SpeechStrings() {
    }

    // complete Strings
    public static final String WELCOME_NO_CONFIG = "Herzlich willkommen bei guidelines, der Skill wird jetzt eingerichtet. Bitte sag mir deinen Namen";
    public static final String REPROMPT = "Auf gehts!";
    public static final String HELP = "Der Einrichtungsassistent besteht aus drei grundlegenden Schritten. " +
            "Du startest mit der Einrichtung deiner Heimadresse. Anschliessend kannst du bis zu 3 Ziele definieren und " +
            "diese benennen. GuideLines kann dir so schnell helfen, deine schnellste und puenktlichste Verbindung " +
            "zu finden. Wenn du mehr Informationen zur Einrichtung wissen moechtest, sage Zieladresse, Heimadresse oder " +
            "zurueck zum Start";
    public static final String USE_GPS_OR_NOT = " . Als naechstes brauche ich deine Heimatadresse. Waere es fuer dich"
            + " in Ordnung deine aktuelle Position zu verwenden?";

    public static final String HELP_HOME_ADDRESS = "Deine Heimadresse benoetigt GuideLines, um einen Startpunkt im " +
            "System festzulegen. Neben deiner Adresse, deiner Haltestelle und einem bevorzugten Verkehrsmittel kannst " +
            "du deinen Startpunkt selbst benennen. Vorschlagen wuerde ich dir hier die Benennung Zuhause. " +
            "Willst du weitere Infos zu den Zieladressen? Sage einfach Zieladresse oder zurueck zum Start";

    public static final String HELP_DESTINATION_ADDRESS = "Deine Zieladressen stellen haeufig von dir im Alltag " +
            "verwendete Ziele dar. Viele Leute haben hier Ziele wie ihren Arbeitsplatz, Universitaetscampus oder " +
            "ihre Schule hinterlegt. <break time=\"0.05s\" /> Auch hier brauche ich die Adresse, die Haltestelle und " +
            "einen Namen für dein Ziel. Ich kann dir noch weitere Informationen zur Heimadresse geben. Sage einfach " +
            "Heimadresse oder zurueck zum start";
    public static final String DEFAULT = "Ich weiß nicht was los ist?";
    public static final String NAMEUNKNOWN = "Ich weiß deinen Namen noch nicht. Kannst du ihn mir veraten. Sage zum Beispiel: ich heiße Anita.";
    public static final String REPROMPT_DESTINATION_ADDRESS = "Bitte wiederhole nochmal was du gesagt hast? " +
            "Möchtest du mit den Infos zur Heimadresse weiterfahren oder die Hilfefunktion beenden?";
    public static final String SPEECH_ERROR_DESTINATION_ADDRESS = "Leider hat etwas nicht geklappt, " +
            "bitte sage mir nochmal ob du Infos zur Heimadresse oder die Hilfefunktion beenden willst";
    public static final String NO_PERMISSION_DEVICE_ADDRESS = "Du hast dem Alexa Skill keine Berechtigung erteilt auf deine" +
            " Adresse zuzugreifen. Bitte gebe dem Skill die notwendigen Berechtigungen";
    public static final String START_CONFIG_DEST_ADDRESS = ". Nun beginnen wir mit der Einrichtung der Zieladressen. " +
            "Du hast nun die Moeglichkeit bis zu drei Zieladressen einzurichten. " +
            "Wie moechtest du deine erste Adresse benennen? Bitte sage hier: \"Mein Ziel heisst\" plus den Namen";

    // format Strings
    public static final String WELCOME_TRANSIT = "Herzlich willkommen bei guidelines, %s wo willst du hin?";

    // Short answers to combine
    public static final String THANKS = "Vielen Dank ";
    public static final String WELCOME_USER = "Hallo ";
    public static final String PLS = "Bitte ";
    public static final String STREET = "jetzt die Straße angeben.";
    public static final String INAUDIBLE = "Das habe ich nicht verstande.";
    public static final String STATE = "State";

    // Info
    public static final String SKILL_NAME = "Guidelines";
}
