package stackpulse.queryAPI.interceptors;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor
{
    private final ProxyManager<String> proxyManager;
    private final BucketConfiguration bucketConfiguration;

    public RateLimitInterceptor(ProxyManager<String> proxyManager, BucketConfiguration bucketConfiguration)
    {
        this.proxyManager = proxyManager;
        this.bucketConfiguration = bucketConfiguration;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
        else ip = ip.split(",")[0].trim();

        Bucket bucket = proxyManager.builder().build(ip, () -> bucketConfiguration);

        boolean requestsRemaining = bucket.tryConsume(1);
        if (!requestsRemaining)
        {
            response.setStatus(429);
            response.setContentType("application/json");
            response.addHeader("Retry-After", "60");
            try
            {
                response.getWriter().write("{\"status\": 429, \"error\": \"Too Many Requests\", \"message\": \"Rate limit exceeded. You may send up to 10 requests per minute. Please wait 60 seconds before retrying.\"}");
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to write rate limit response", e);
            }
            return false;
        } else return true;
    }
}
