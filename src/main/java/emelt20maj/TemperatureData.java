package emelt20maj;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TemperatureData {

    private final String city;
    private final LocalTime time;
    private final String windDirection;
    private final int windStrength;
    private final int temperature;

}
