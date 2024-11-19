package server.network;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesManager {

    public static int totalRoundsSet() {
        // Skapar ett properties object
        Properties properties = new Properties();

        //Läser fil och laddar filens rundor i properties
        try (FileInputStream file = new FileInputStream("src/config.properties")) {
            properties.load(file);

            // Hämta "rounds" från filen
            String totalRounds = properties.getProperty("rounds");

            // Kollar så att det finns något i filen
            if (totalRounds != null) {
                return Integer.parseInt(totalRounds); // Tar inhållet och parsar till Integer
            } else {
                throw new RuntimeException("Property 'rounds' not found in config");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading config", e);
        }
    }
}
