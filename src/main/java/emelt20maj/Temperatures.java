package emelt20maj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Temperatures {

    private final List<TemperatureData> tempDatas = new ArrayList<>();

    public void addTemperatures() {
        try (InputStream is = Temperatures.class.getResourceAsStream("tavirathu13.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(" ");
                tempDatas.add(new TemperatureData(
                        row[0],
                        LocalTime.of(
                                Integer.parseInt(row[1].substring(0, 2)),
                                Integer.parseInt(row[1].substring(2))
                        ),
                        row[2].substring(0, 3),
                        Integer.parseInt(row[2].substring(3)),
                        Integer.parseInt(row[3])
                ));
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("No file", ioe);
        }
    }

    public List<TemperatureData> getTemperatures() {
        return new ArrayList<>(tempDatas);
    }

}
