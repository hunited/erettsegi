package emelt20maj;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.*;

public class Main {

    private final Temperatures t = new Temperatures();
    private final Scanner scanner = new Scanner(in);
    private List<TemperatureData> temperatureData = new ArrayList<>();

    public static void main(String[] args) {
        Main m = new Main();
        out.println("1. felatat");
        m.first();
        out.println("2. felatat");
        m.second();
        out.println("3. felatat");
        m.third();
        out.println("4. felatat");
        m.fourth();
        out.println("5. felatat");
        m.fifth();
        out.println("6. felatat");
        m.sixth();
    }

    private void first() {
        try {
            t.addTemperatures();
            temperatureData = t.getTemperatures();
            out.println("Sikeresen beolvasva " + temperatureData.size() + " sor.");
        } catch (IllegalStateException ise) {
            throw new IllegalStateException("Sikertelen beolvasás!");
        }
    }

    private void second() {
        out.print("Adja meg egy település kódját! Település: ");
        List<TemperatureData> city = getTempDataByCity(scanner.nextLine());
        if (!city.isEmpty()) {
            city.sort(Comparator.comparing(TemperatureData::getTime).reversed());
            out.println("Az utolsó mérési adat a megadott településről " + city.get(0).getTime() + "-kor érkezett.");
        } else {
            out.println("Nincs ilyen kódszámú város az adatbázisban!");
        }
    }

    private void third() {
        temperatureData.sort(Comparator.comparing(TemperatureData::getTemperature));
        TemperatureData min = temperatureData.get(0);
        TemperatureData max = temperatureData.get(temperatureData.size() - 1);
        out.println("A legalacsonyabb hőmérséklet: " + min.getCity() + " " + min.getTime() + " " + min.getTemperature() + " fok");
        out.println("A legmagasabb hőmérséklet: " + max.getCity() + " " + max.getTime() + " " + max.getTemperature() + " fok");
    }

    private void fourth() {
        temperatureData.sort(Comparator.comparing(TemperatureData::getTime));
        StringBuilder sb = new StringBuilder();
        for (TemperatureData tData : temperatureData) {
            if (tData.getWindStrength() == 0) {
                sb.append(tData.getCity()).append(" ").append(tData.getTime()).append("\n");
            }
        }
        if (sb.isEmpty()) {
            sb.append("Nem volt szélcsend a mérések idején.");
        } else {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        out.println(sb.toString());
    }

    private void fifth() {
        for (String city : getCities()) {
            List<TemperatureData> mediums = getTempDataByCityForMediumTemp(city);
            List<TemperatureData> fluctuations = getTempDataByCity(city);
            fluctuations.sort(Comparator.comparing(TemperatureData::getTemperature));
            int fluctuation = fluctuations.get(fluctuations.size() - 1).getTemperature() - fluctuations.get(0).getTemperature();
            if (mediums.isEmpty()) {
                out.printf("%s NA; Hőmérséklet-ingadozás: %d%n", city, fluctuation);
            } else {
                out.printf("%s Középhőmérséklet: %.0f°C; Hőmérséklet-ingadozás: %d%n", city, getAverageTemp(mediums), fluctuation);
            }
        }
    }

    private void sixth() {
        for (String city : getCities()) {
            createFiles(city);
        }
        out.println("A fájlok elkészültek.");
    }

    private List<TemperatureData> getTempDataByCity(String city) {
        List<TemperatureData> result = new ArrayList<>();
        for (TemperatureData tData : temperatureData) {
            if (tData.getCity().equalsIgnoreCase(city)) {
                result.add(tData);
            }
        }
        return result;
    }

    private List<String> getCities() {
        List<String> cities = new ArrayList<>();
        for (TemperatureData tData : temperatureData) {
            String city = tData.getCity();
            if (!cities.contains(tData.getCity())) {
                cities.add(city);
            }
        }
        return cities;
    }

    private List<TemperatureData> getTempDataByCityForMediumTemp(String city) {
        List<TemperatureData> result = new ArrayList<>();
        for (TemperatureData tData : getTempDataByCity(city)) {
            int hour = tData.getTime().getHour();
            if (checkHours(city) && (hour == 1 || hour == 7 || hour == 13 || hour == 19)) {
                result.add(tData);
            }
        }
        return result;
    }

    private double getAverageTemp(List<TemperatureData> mediums) {
        int averageTempSum = 0;
        for (TemperatureData tData : mediums) {
            averageTempSum += tData.getTemperature();
        }
        return 1.0 * averageTempSum / mediums.size();
    }

    private void createFiles(String city) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("src/main/resources/emelt20maj/" + city + ".txt"))) {
            writer.write(city + "\n");
            List<TemperatureData> listByCity = getTempDataByCity(city);
            listByCity.sort(Comparator.comparing(TemperatureData::getTime));
            for (TemperatureData data : listByCity) {
                writer.write(data.getTime().toString() + " ");
                writer.write("#".repeat(data.getWindStrength()) + "\n");
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("Hiba történt: ", ioe);
        }
    }

    private boolean checkHours(String city) {
        boolean has1 = checkHour(city, 1);
        boolean has7 = checkHour(city, 7);
        boolean has13 = checkHour(city, 13);
        boolean has19 = checkHour(city, 19);
        return has1 && has7 && has13 && has19;
    }

    private boolean checkHour(String city, int number) {
        for (TemperatureData tData : getTempDataByCity(city)) {
            if (tData.getTime().getHour() == number) {
                return true;
            }
        }
        return false;
    }

}
