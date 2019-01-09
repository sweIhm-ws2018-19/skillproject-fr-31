package guidelines;

public class SpeechStrings {
    private SpeechStrings() {
    }

    // complete Strings
    public static final String WELCOME_NO_CONFIG = "Willkommen beim GuideLines Einrichtungsassistenten! Die Einrichtung dauert circa fünf Minuten. Bitte sage mir deinen Namen";
    public static final String REPROMPT = "Auf gehts!";
    public static final String HELP = "Der Einrichtungsassistent besteht aus drei grundlegenden Schritten. " +
            "Du startest mit der Einrichtung deiner Heimadresse. Anschliessend kannst du bis zu 3 Ziele definieren und " +
            "diese benennen. GuideLines kann dir so schnell helfen, deine schnellste und puenktlichste Verbindung " +
            "zu finden. Wenn du mehr Informationen zur Einrichtung wissen moechtest, sage Zieladresse, Heimadresse oder " +
            "zurueck zum Start";

    public static final String HELP_HOME_ADDRESS = "Deine Heimadresse benoetigt GuideLines, um einen Startpunkt im " +
            "System festzulegen. Neben deiner Adresse, deiner Haltestelle und einem bevorzugten Verkehrsmittel kannst " +
            "du deinen Startpunkt selbst benennen. Vorschlagen wuerde ich dir hier die Benennung Zuhause. " +
            "Willst du weitere Infos zu den Zieladressen? Sage einfach Zieladresse oder zurueck zum Start";

    public static final String HELP_DESTINATION_ADDRESS = "Deine Zieladressen stellen haeufig von dir im Alltag " +
            "verwendete Ziele dar. Viele Leute haben hier Ziele wie ihren Arbeitsplatz, Universitaetscampus oder " +
            "ihre Schule hinterlegt. <break time=\"0.05s\" /> Auch hier brauche ich die Adresse, die Haltestelle und " +
            "einen Namen für dein Ziel. Ich kann dir noch weitere Informationen zur Heimadresse geben. Sage einfach " +
            "Heimadresse oder zurueck zum start";

    public static final String NAMEUNKNOWN = "Ich weiß deinen Namen noch nicht. Kannst du ihn mir veraten. Sage zum Beispiel: ich heiße Anita.";
    public static final String REPROMPT_DESTINATION_ADDRESS = "Bitte wiederhole nochmal was du gesagt hast? " +
            "Möchtest du mit den Infos zur Heimadresse weiterfahren oder die Hilfefunktion beenden?";
    public static final String SPEECH_ERROR_DESTINATION_ADDRESS = "Leider hat etwas nicht geklappt, " +
            "bitte sage mir nochmal ob du Infos zur Heimadresse oder die Hilfefunktion beenden willst";

    public static final String SAY_ADDRESS = "Bitte sage mir die Strasse, Hausnummer und die Stadt\"";

    public static final String SAY_HOME_ADDRESS = " .Bitte sage mir die Strasse, Hausnummer und die Stadt deiner Heimadresse";

    public static final String FOLLOWING_ADDRESSES = "Okay du kannst jetzt die nächste Adresse einrichten.";

    public static final String START_CONFIG_DEST_ADDRESS = "Nun beginnen wir mit der Einrichtung der Zieladressen. " +
            "Du hast nun die Moeglichkeit bis zu drei Zieladressen einzurichten. " +
            "Bitte nenne mir deine erste Zieladresse. Sage mir die Strasse, Hausnummer und die Stadt";

    public static final String NEW_STREET = "Du befindest dich nun im Einrichtungsassistenten. Willst du eine neue Straße hinzufügen?";
    public static final String NEW_CONFIG = "Möchtest du dein Profil neu anlegen?";


    // format Strings
    public static final String WELCOME_TRANSIT = " Hallo %s. Du kannst eine Route erfragen " +
            "oder den Einrichtungsassistenten starten. Was möchtest du tun?";

    // Short answers to combine
    public static final String THANKS = "Vielen Dank ";
    public static final String INAUDIBLE = "Das habe ich nicht verstande.";

    // Info
    public static final String SKILL_NAME = "Utilities";
}
