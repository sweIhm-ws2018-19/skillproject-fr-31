package guidelines.utilities;

import guidelines.models.Coordinate;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    public void createFromatedString(){
        List<String> list = new ArrayList<>(Arrays.asList("Berbling", "Muenchen", "Bad Aibling", "Rosenheim"));
        String want = "1. Berbling 2. Muenchen 3. Bad Aibling 4. Rosenheim";
        Assert.assertEquals(want, StringUtils.prepStringForChoiceIntent(list));
    }
}