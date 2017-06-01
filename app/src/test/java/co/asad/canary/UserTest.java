package co.asad.canary;

import org.junit.Test;
import static org.junit.Assert.*;


public class UserTest {

    @Test
    public void jsonConversionTest() throws Exception {
        User u = new User("John Doe","0","Jane Doe","090078601","jane@doe.com");
        assertEquals(u.toJson(),"{\"name\":\"John Doe\",\"id\":\"0\",\"emergencyContact\":{\"name\":\"Jane Doe\",\"number\":\"090078601\",\"email\":\"jane@doe.com\"}}");
    }

}