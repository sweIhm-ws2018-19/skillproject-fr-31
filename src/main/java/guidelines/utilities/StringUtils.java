package guidelines.utilities;

import java.util.List;

public class StringUtils {
    public static String prepStringForChoiceIntent(List<String> stations){
        int lastIndex = stations.size() - 1;
        int count =1;
        for (int i = 0; i < stations.size(); i++) {
            stations.set(i,(i+1)+"<break time=\"0.3s\" />. "+stations.get(i));
        }
        String result = String.join(" oder ", String.join(", <break time=\"0.5s\" />", stations.subList(0, lastIndex)), stations.get(lastIndex));
        result += "<break time=\"0.5s\" />.";
        if(stations.size() == 1){
            return stations.get(0);
        }
        return result;
    }
}
