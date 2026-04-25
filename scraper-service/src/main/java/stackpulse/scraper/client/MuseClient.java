package stackpulse.scraper.client;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MuseClient
{
    private final String apiKey;
    private final OkHttpClient client;

    public MuseClient(@Value("${muse.api-key}") String apiKey)
    {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    public String fetchJobs(int page) throws IOException
    {
        HttpUrl baseUrl = HttpUrl.parse("https://www.themuse.com/api/public/jobs");
        if (baseUrl == null) throw new IOException("Invalid Muse API URL");

        HttpUrl url = baseUrl.newBuilder()
                .addQueryParameter("category", "Software Engineering")
                .addQueryParameter("page", String.valueOf(page))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("X-Muse-Api-Key", apiKey)
                .build();

        try(Response response = client.newCall(request).execute())
        {
            if(response.body() == null) throw new IOException("Empty response from Muse API");
            return response.body().string();
        }
    }
}
