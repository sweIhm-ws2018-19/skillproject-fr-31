package main.java.guidelines;

public class SpeechStrings {

    // complete Strings
    public static final String WELCOME_NO_CONFIG = "Herzlich willkommen bei guidelines, der Skill wird jetzt eingerichtet. Bitte sag mir deinen Namen";
    public static final String REPROMPT = "Auf gehts!";
    public static final String HELP = "Der Einrichtungsassistent besteht aus drei grundlegenden Schritten. " +
            "Du startest mit der Einrichtung deiner Heimadresse. Anschliessend kannst du bis zu 3 Ziele definieren und " +
            "diese benennen. GuideLines kann dir so schnell helfen, deine schnellste und puenktlichste Verbindung " +
            "zu finden. Wenn du mehr Informationen zur Einrichtung wissen moechtest, sage Zieladresse, Heimadresse oder " +
            "zurück zum Start";

    public static final String HELP_HOME_ADDRESS = "Deine Heimadresse benoetigt GuideLines, um einen Startpunkt im " +
            "System festzulegen. Neben deiner Adresse, deiner Haltestelle und einem bevorzugten Verkehrsmittel kannst " +
            "du deinen Startpunkt selbst benennen. Vorschlagen wuerde ich dir hier die Benennung Zuhause. " +
            "Willst du weitere Infos zu den Zieladressen? Sage einfach Zieladresse oder zurück zum Start";

    public static final String HELP_DESTINATION_ADDRESS = "Deine Zieladressen stellen haeufig von dir im Alltag " +
            "verwendete Ziele dar. Viele Leute haben hier Ziele wie ihren Arbeitsplatz, Universitaetscampus oder " +
            "ihre Schule hinterlegt. <break time=\"0.05s\" /> Auch hier brauche ich die Adresse, die Haltestelle und " +
            "einen Namen für dein Ziel. Ich kann dir noch weitere Informationen zur Heimadresse geben. Sage einfach " +
            "Heimadresse oder zurück zum start";
    public static final String DEFAULT = "Ich weiß nicht was los ist?";
    public static final String NAMEUNKNOWN = "Ich weiß deinen Namen noch nicht. Kannst du ihn mir veraten. Sage zum Beispiel: ich heiße Anita.";

    // formatt Strings
    public static final String WELCOME_TRANSIT = "Herzlich willkommen bei guidelines, %s wo willst du hin?";

    // Short answers to combine
    public static final String THANKS = "Vielen Dank ";
    public static final String WELCOME_USER = "Hallo ";
    public static final String PLS = "Bitte ";
    public static final String STREET = "jetzt die Straße angeben.";
    public static final String INAUDIBLE = "Das habe ich nicht verstande.";

    // Infos
    public static final String SKILL_NAME = "Guidelines";
}
