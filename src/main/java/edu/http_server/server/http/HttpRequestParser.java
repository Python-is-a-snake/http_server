package edu.http_server.server.http;

import edu.http_server.server.http.model.HttpMethod;
import edu.http_server.server.http.model.HttpRequest;
import edu.http_server.server.http.model.HttpVersion;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.reflections.Reflections.log;

@Slf4j
public class HttpRequestParser {

    @SneakyThrows
    public HttpRequest parseRequest(InputStream inputStream) {
        int bytes = inputStream.available();
        int linesRead = 0;
        String requestString = new String(inputStream.readNBytes(bytes));
        log.debug("Request str : {}", requestString);

        String[] requestLines = requestString.split("\r\n");

        // 1 - parse first line
        String[] firstLine = requestLines[linesRead++].split(" ");
        HttpMethod method = HttpMethod.valueOf(firstLine[0]);
        String url = firstLine[1];
        HttpVersion version = HttpVersion.getVersion(firstLine[2]);

        // 2 - parse headers
        Map<String, String> headers = new HashMap<>();
        String line;

        while (linesRead != requestLines.length && !(line = requestLines[linesRead++]).isEmpty()){
            String[] headerLine = line.split(":");
            headers.put(headerLine[0], headerLine[1]);
        }

        // 3 - parse body if present
        String body = "";

        while (linesRead != requestLines.length){
            body += requestLines[linesRead++];
        }

        return HttpRequest.builder()
                .method(method)
                .url(url)
                .version(version)
                .headers(headers)
                .body(body)
                .build();
    }
}
