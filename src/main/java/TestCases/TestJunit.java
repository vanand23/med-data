package TestCases;
import FXMLControllers.FullNamer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class TestJunit {

    FullNamer testFullNamer = new FullNamer();

    @Test

    public void testingTestCases(){
        String str = "Junit is working!";
        assertEquals("Junit is working!", str);
    }


}
