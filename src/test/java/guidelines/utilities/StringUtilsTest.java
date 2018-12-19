package guidelines.utilities;

import guidelines.models.Coordinate;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsTest {
    @Test
    public void createFromatedString(){
        List<String> list = new ArrayList<>(Arrays.asList("Berbling", "Muenchen", "Bad Aibling", "Rosenheim"));
        String want = "1<break time=\"0.3s\" />. Berbling, <break time=\"0.5s\" />2<break time=\"0.3s\" />. Muenchen, <break time=\"0.5s\" />3<break time=\"0.3s\" />. Bad Aibling oder 4<break time=\"0.3s\" />. Rosenheim";
        Assert.assertEquals(want, StringUtils.prepStringForChoiceIntent(list));
    }
}