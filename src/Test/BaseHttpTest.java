package Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

public abstract class BaseHttpTest {
    URI createUri(String url) {
        return URI.create(url);
    }

    HttpRequest buildGetRequest(URI url) {
        return HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json;charset=utf-8")
                .build();
    }

    HttpRequest buildPostRequest(URI url, String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json;charset=utf-8")
                .build();
    }

    private HttpRequest buildPutRequest(URI url, String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json;charset=utf-8")
                .build();
    }
    HttpRequest buildDeleteRequest(URI url) {
        return HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .timeout(Duration.ofSeconds(10))
                .build();
    }
}
