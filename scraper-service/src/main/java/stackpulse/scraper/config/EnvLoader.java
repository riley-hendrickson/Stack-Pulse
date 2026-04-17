package stackpulse.scraper.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader
{
    public static Map<String, String> load(String filePath)
    {
        Map<String, String> env = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("=", 2);
                if (parts.length == 2) env.put(parts[0].trim(), parts[1].trim());

            }
        } catch (IOException e)
        {
            throw new RuntimeException("Failed to load env file from: " + filePath, e);
        }

        return env;
    }
}