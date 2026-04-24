package stackpulse.scraper.client;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AdzunaClient
{
    private final String appId;
    private final String appKey;
    private final OkHttpClient client;

    public AdzunaClient(@Value("${adzuna.app-id}") String appId,
                        @Value("${adzuna.app-key}") String appKey)
    {
        this.appId = appId;
        this.appKey = appKey;
        this.client = new OkHttpClient();
    }

    public String fetchJobs(String query, int page) throws IOException
    {
        HttpUrl baseUrl = HttpUrl.parse("https://api.adzuna.com/v1/api/jobs/us/search/" + page);
        if(baseUrl == null) throw new IOException("Invalid URL for page: " + page);

        HttpUrl url = baseUrl.newBuilder()
                .addQueryParameter("app_id", appId)
                .addQueryParameter("app_key", appKey)
                .addQueryParameter("what", query)
                .addQueryParameter("results_per_page", "50")
                .addQueryParameter("content-type", "application/json")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try(Response response = client.newCall(request).execute())
        {
            if(response.body() == null) throw new IOException("Empty response from Adzuna API");
            return response.body().string();
        }
    }
}
