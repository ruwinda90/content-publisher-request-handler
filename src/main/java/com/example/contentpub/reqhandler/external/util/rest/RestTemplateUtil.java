package com.example.contentpub.reqhandler.external.util.rest;

import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

import static com.example.contentpub.reqhandler.domain.constants.StatusCode.BACKEND_RESP_PARSE_FAILURE;
import static com.example.contentpub.reqhandler.domain.constants.StatusCode.BACKEND_TIMEOUT;

/**
 * The RestTemplateUtil class invokes REST APIs to communicate with other microservices.
 */
@Component
public class RestTemplateUtil {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Send an API to backend microservice(s) and process the response.
     *
     * @param url         the API URL.
     * @param method      the REST method.
     * @param requestBody the request body.
     * @param <T>         the type of the request body.
     * @return the processed domain response.
     */
    public <T> CommonResponseEntity<JSONObject> getResponse(String url, HttpMethod method, T requestBody) throws DomainException {

        CommonResponseEntity<JSONObject> domainResponse;

        try {
            HttpEntity<?> httpEntity = (requestBody == null) ? httpEntityGenerator() : httpEntityGenerator(requestBody);

            ResponseEntity<JSONObject> response = restTemplate.exchange(url, method, httpEntity, JSONObject.class);

            domainResponse = new CommonResponseEntity<>();
            domainResponse.setHttpStatusCode(response.getStatusCodeValue());
            domainResponse.setCode(response.getBody().getAsString("code"));
            domainResponse.setDescription(response.getBody().getAsString("description"));
            domainResponse.setData(response.getBody().get("data") == null ?
                    null :
                    new JSONObject((LinkedHashMap) response.getBody().get("data")));

        } catch (HttpStatusCodeException ex) {
            domainResponse = handleInvalidStatusCodes(ex);
        } catch (ResourceAccessException ex) {
            throw new DomainException(BACKEND_TIMEOUT);
        }

        return domainResponse;
    }

    /**
     * Handle 4xx and 5xx invalid status codes given by backend microservices.
     *
     * @param exception the exception thrown by the restTemplate.
     * @return the processed domain response.
     */
    private CommonResponseEntity<JSONObject> handleInvalidStatusCodes(HttpStatusCodeException exception) throws DomainException {

        CommonResponseEntity<JSONObject> domainResponse = new CommonResponseEntity<>();
        domainResponse.setHttpStatusCode(exception.getRawStatusCode());

        try {
            String responseAsString = exception.getResponseBodyAsString();
            JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
            JSONObject responseBody = (JSONObject) parser.parse(responseAsString);
            domainResponse.setCode(responseBody.getAsString("code"));
            domainResponse.setDescription(responseBody.getAsString("description"));
            domainResponse.setData(responseBody.get("data") == null ?
                    null :
                    new JSONObject((LinkedHashMap) responseBody.get("data")));

        } catch (ParseException parseException) {
            throw new DomainException(BACKEND_RESP_PARSE_FAILURE);
        }

        return domainResponse;
    }

    /**
     * Generate HTTP entity object for the request.
     *
     * @param object the request body of the request.
     * @param <T>    the type of the request body.
     * @return the HTTP entity to be sent.
     */
    private <T> HttpEntity<T> httpEntityGenerator(T object) {
        return new HttpEntity<>(object, httpHeaderGenerator());
    }

    /**
     * Generate HTTP entity object for the request when there is no request body.
     *
     * @return the HTTP entity to be sent.
     */
    private HttpEntity<String> httpEntityGenerator() {
        return new HttpEntity<>(httpHeaderGenerator());
    }

    /**
     * Generate HTTP headers for the API request.
     *
     * @return the generated headers.
     */
    private HttpHeaders httpHeaderGenerator() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());
        return headers;
    }

}
