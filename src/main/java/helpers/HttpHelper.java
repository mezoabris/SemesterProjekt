package helpers;

import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HttpHelper {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String readRequestBody(HttpExchange httpExchange) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()))) {
            String line;
            StringBuilder body = new StringBuilder();
            while ((line = br.readLine()) != null) {
                body.append(line);
            }
            return body.toString();
        }
    }
    public static <T>T parseRequestBody(HttpExchange exchange,  Class<T> template ) throws IOException {
        String body = readRequestBody(exchange);
        return mapper.readValue(body, template);
    }
    public static void sendJSONResponse(HttpExchange httpExchange, int status, Object responseObject) throws IOException {
        String jsonResponse = mapper.writeValueAsString(responseObject);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(bytes);
        }
    }
    public static void sendTextResponse(HttpExchange httpExchange, int status, String text) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "text/plain");
        httpExchange.sendResponseHeaders(status, text.getBytes().length);
        try(OutputStream os = httpExchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

}
