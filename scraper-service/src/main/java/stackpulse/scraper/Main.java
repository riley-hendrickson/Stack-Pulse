package stackpulse.scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import stackpulse.scraper.client.AdzunaClient;
import stackpulse.scraper.config.EnvLoader;
import stackpulse.scraper.models.AdzunaJobResult;
import stackpulse.scraper.models.AdzunaResponse;

import java.io.IOException;
import java.util.Map;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Map<String, String> envContents = EnvLoader.load(".env");
        String appId = envContents.get("ADZUNA_APP_ID");
        String appKey = envContents.get("ADZUNA_APP_KEY");

        AdzunaClient adzunaClient = new AdzunaClient(appId, appKey, new OkHttpClient());
        String jsonResponse = adzunaClient.fetchJobs("backend developer", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        AdzunaResponse response = objectMapper.readValue(jsonResponse, AdzunaResponse.class);

        System.out.println("Found " + response.getCount() + " relevant jobs:");
        for(AdzunaJobResult listing : response.getJobResults())
        {
            System.out.println("Company name: " + listing.getCompany().getCompanyName());
            System.out.println("Listing title: " + listing.getTitle());
            System.out.println("Listing description: " + listing.getDescription().substring(0, 100));
        }
    }
}
