package edu.http_server.server.http.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class HttpRequest {
    // head
    private HttpMethod method;
    private String url;
    private HttpVersion version;

    // headers
    private Map<String, String> headers = new HashMap<>();

    // body
    private String body;

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getParamValue(String name){
        if(!url.contains("?")){
            return null;
        }
        String urlParamsLine = url.split("\\?")[1];
        String[] params = urlParamsLine.split("&");
        for(String param : params){
            String paramName = param.split("=")[0];
            String paramValue = param.split("=")[1];
            if(paramName.equals(name)){
                return paramValue;
            }
        }
        return null;
    }

    public String getPlainUrl(){
        if(url.contains("?")){
            return url.split("\\?")[0];
        }
        return url;
    }
}
