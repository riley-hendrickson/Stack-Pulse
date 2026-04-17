package stackpulse.scraper;

import okhttp3.OkHttpClient;
import stackpulse.scraper.client.AdzunaClient;
import stackpulse.scraper.config.EnvLoader;

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
        System.out.println(adzunaClient.fetchJobs("junior java developer", 1));
    }
}
