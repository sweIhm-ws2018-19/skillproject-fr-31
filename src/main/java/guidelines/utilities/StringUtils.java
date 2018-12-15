package guidelines.utilities;

import java.util.List;

public class StringUtils {
    public static String prepStringForChoiceIntent(List<String> stations){
        int lastIndex = stations.size() - 1;
        String result = String.join(" oder ", String.join(", ", stations.subList(0, lastIndex)), stations.get(lastIndex));
        return result;
    }
}
