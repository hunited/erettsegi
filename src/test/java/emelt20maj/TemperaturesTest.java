package emelt20maj;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperaturesTest {

    @Test
    void addTemperatures() {
        Temperatures t = new Temperatures();
        t.addTemperatures();
        assertEquals(374, t.getTemperatures().size());
    }

}
