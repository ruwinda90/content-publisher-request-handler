package com.example.contentpub.reqhandler.external.util.rest;

import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity2;
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
import static com.example.contentpub.reqhandler.external.constant.RestConstants.PARSE_FAILURE_DESCRIPTION;
import static com.example.contentpub.reqhandler.external.constant.RestConstants.TIMEOUT_FAILURE;

/**
 * The RestTemplateUtil class invokes REST APIs to communicate with other microservices.
 */
@Component
public class RestTemplateUtil {

    @Autowired
    private RestTemplate restTemplate;

    public <T> CommonResponseEntity2<JSONObject> getResponse2(String url, HttpMethod method, T requestBody) throws DomainException {

        CommonResponseEntity2<JSONObject> domainResponse;

        try {
            HttpEntity<?> httpEntity = (requestBody == null) ? httpEntityGenerator() : httpEntityGenerator(requestBody);

            ResponseEntity<JSONObject> response = restTemplate.exchange(url, method, httpEntity, JSONObject.class);

            domainResponse = new CommonResponseEntity2<>();
            domainResponse.setHttpStatusCode(response.getStatusCodeValue());
            domainResponse.setCode(response.getBody().getAsString("code"));
            domainResponse.setDescription(response.getBody().getAsString("description"));
            domainResponse.setData(new JSONObject((LinkedHashMap) response.getBody().getOrDefault("data", new LinkedHashMap<>())));

        } catch (HttpStatusCodeException ex) {
            domainResponse = handleInvalidStatusCodes2(ex);
        } catch (ResourceAccessException ex) {
            throw new DomainException(BACKEND_TIMEOUT);
        }

        return domainResponse;
    }

    /**
     * Send an API to backend microservice(s) and process the response.
     * @param url the API URL.
     * @param method the REST method.
     * @param requestBody the request body.
     * @return the processed domain response.
     * @param <T> the type of the request body.
     */
    public <T> CommonResponseEntity getResponse(String url, HttpMethod method, T requestBody) {

        CommonResponseEntity domainResponse;

        try {
            HttpEntity<?> httpEntity = (requestBody == null) ? httpEntityGenerator() : httpEntityGenerator(requestBody);

            ResponseEntity<JSONObject> response = restTemplate.exchange(url, method, httpEntity, JSONObject.class);

            domainResponse = new CommonResponseEntity();
            domainResponse.setStatusCode(response.getStatusCodeValue());
            domainResponse.setResponseBody(response.getBody());

        } catch (HttpStatusCodeException ex) {
            domainResponse = handleInvalidStatusCodes(ex);
        } catch (ResourceAccessException ex) {
            domainResponse = new CommonResponseEntity();
            domainResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            domainResponse.setDescription(TIMEOUT_FAILURE);
        }

        return domainResponse;
    }

    /**
     * Handle 4xx and 5xx invalid status codes given by backend microservices.
     * @param exception the exception thrown by the restTemplate.
     * @return the processed domain response.
     */
    private CommonResponseEntity2<JSONObject> handleInvalidStatusCodes2(HttpStatusCodeException exception) throws DomainException {

        CommonResponseEntity2<JSONObject> domainResponse = new CommonResponseEntity2<>();
        domainResponse.setHttpStatusCode(exception.getRawStatusCode());

        try {
            String responseAsString = exception.getResponseBodyAsString();
            JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
            JSONObject responseBody = (JSONObject) parser.parse(responseAsString);
            domainResponse.setCode(responseBody.getAsString("code"));
            domainResponse.setDescription(responseBody.getAsString("description"));
            domainResponse.setData(new JSONObject((LinkedHashMap) responseBody.getOrDefault("data", new LinkedHashMap<>())));

        } catch (ParseException parseException) {
            throw new DomainException(BACKEND_RESP_PARSE_FAILURE);
        }

        return domainResponse;
    }

    private CommonResponseEntity handleInvalidStatusCodes(HttpStatusCodeException exception) {

        CommonResponseEntity domainResponse = new CommonResponseEntity();
        domainResponse.setStatusCode(exception.getRawStatusCode());

        try {
            String responseAsString = exception.getResponseBodyAsString();
            JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
            JSONObject responseBody = (JSONObject) parser.parse(responseAsString);
            domainResponse.setResponseBody(responseBody);

        } catch (ParseException parseException) {
            domainResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            domainResponse.setDescription(PARSE_FAILURE_DESCRIPTION);
        }

        return domainResponse;
    }

    /**
     * Generate HTTP entity object for the request.
     * @param object the request body of the request.
     * @return the HTTP entity to be sent.
     * @param <T> the type of the request body.
     */
    private  <T> HttpEntity<T> httpEntityGenerator(T object) {
        return new HttpEntity<>(object, httpHeaderGenerator());
    }

    /**
     * Generate HTTP entity object for the request when there is no request body.
     * @return the HTTP entity to be sent.
     */
    private HttpEntity<String> httpEntityGenerator() {
        return new HttpEntity<>(httpHeaderGenerator());
    }

    /**
     * Generate HTTP headers for the API request.
     * @return the generated headers.
     */
    private HttpHeaders httpHeaderGenerator() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());
        return headers;
    }

}
