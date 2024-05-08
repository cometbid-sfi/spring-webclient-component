/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.component.webclient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Component
class ReactiveWebClient {

    private final ReactiveClientInterface clientInterface;

    public ReactiveWebClient(ReactiveClientInterface clientInterface) {
        this.clientInterface = clientInterface;
    }

    /**
     *
     * @param webClient
     * @param payload
     * @param pathTemplate
     * @param templateVar
     * @param params
     * @param token
     * @param cookieList
     * @return
     */
    ResponseSpec doPut(WebClient webClient, Object payload, String pathTemplate, Map<String, Object> templateVar,
            MultiValueMap<String, String> params, String token, List<Cookie> cookieList) {

        return clientInterface.doPostOrPutOrPatch(webClient, templateVar, payload, pathTemplate, params,
                prepareHeaders(token), cookieList, HttpMethod.PUT);
    }

    /**
     *
     * @param <T>
     * @param webClient
     * @param payload
     * @param clazzRequest
     * @param pathTemplate
     * @param templateVar
     * @param params
     * @param token
     * @param cookieList
     * @return
     */
    <T> ResponseSpec doPut(WebClient webClient, Mono<T> payload, Class<? extends T> clazzRequest, String pathTemplate,
            Map<String, Object> templateVar, MultiValueMap<String, String> params, String token,
            List<Cookie> cookieList) {

        return clientInterface.doPublisherPostOrPutOrPatch(webClient, templateVar, payload, clazzRequest, pathTemplate,
                params, prepareHeaders(token), cookieList, HttpMethod.PUT);
    }

    /**
     *
     * @param webClient
     * @param payload
     * @param pathTemplate
     * @param templateVar
     * @param params
     * @param token
     * @param cookieList
     * @return
     */
    ResponseSpec doPost(WebClient webClient, Object payload, String pathTemplate, Map<String, Object> templateVar,
            MultiValueMap<String, String> params, String token, List<Cookie> cookieList) {

        return clientInterface.doPostOrPutOrPatch(webClient, templateVar, payload, pathTemplate, params,
                prepareHeaders(token), cookieList, HttpMethod.POST);
    }

    /**
     *
     * @param <T>
     * @param webClient
     * @param payload
     * @param clazzRequest
     * @param pathTemplate
     * @param templateVar
     * @param params
     * @param token
     * @param cookieList
     * @return
     */
    <T> ResponseSpec doPost(WebClient webClient, Mono<T> payload, Class<? extends T> clazzRequest, String pathTemplate,
            Map<String, Object> templateVar, MultiValueMap<String, String> params, String token,
            List<Cookie> cookieList) {

        return clientInterface.doPublisherPostOrPutOrPatch(webClient, templateVar, payload, clazzRequest, pathTemplate,
                params, prepareHeaders(token), cookieList, HttpMethod.POST);
    }

    /**
     *
     * @param webClient
     * @param payload
     * @param pathTemplate
     * @param templateVar
     * @param params
     * @param token
     * @param cookieList
     * @return
     */
    ResponseSpec doPatch(WebClient webClient, Object payload, String pathTemplate, Map<String, Object> templateVar,
            MultiValueMap<String, String> params, String token, List<Cookie> cookieList) {

        return clientInterface.doPostOrPutOrPatch(webClient, templateVar, payload, pathTemplate, params,
                prepareHeaders(token), cookieList, HttpMethod.PATCH);
    }

    /**
     *
     * @param <T>
     * @param webClient
     * @param payload
     * @param clazzRequest
     * @param pathTemplate
     * @param templateVar
     * @param params
     * @param token
     * @param cookieList
     * @return
     */
    <T> ResponseSpec doPatch(WebClient webClient, Mono<T> payload, Class<? extends T> clazzRequest, String pathTemplate,
            Map<String, Object> templateVar, MultiValueMap<String, String> params, String token,
            List<Cookie> cookieList) {

        return clientInterface.doPublisherPostOrPutOrPatch(webClient, templateVar, payload, clazzRequest, pathTemplate,
                params, prepareHeaders(token), cookieList, HttpMethod.PATCH);
    }

    /**
     *
     * @param path
     * @param uri
     * @param templateVar
     * @param params
     * @param token
     * @param cookieList
     * @return
     */
    ResponseSpec doGetMethod(WebClient webClient, String pathTemplate, Map<String, Object> templateVar,
            MultiValueMap<String, String> params, String token, List<Cookie> cookieList) {

        log.info("Beginning GET REST Service call...");

        return clientInterface.doGet(webClient, templateVar, params, pathTemplate,
                prepareHeaders(token), cookieList);
    }

    /**
     *
     * @param formData
     * @param path
     * @param uri
     * @param templateVar
     * @param authorizationHeader
     * @param cookieList
     * @param httpMethod
     * @return
     */
    ResponseSpec doFormDataPost(WebClient webClient, MultiValueMap<String, String> formData, String path, String uri,
            Map<String, Object> templateVar, String token, List<Cookie> cookieList) {

        log.info("Beginning Form Post REST Service call...");

        Map<String, List<String>> headerMap = prepareHeaders(token);
        headerMap.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        return clientInterface.doFormDataPostOrPut(webClient, templateVar, formData, path,
                headerMap, cookieList,
                HttpMethod.POST);
    }

    /**
     *
     * @param parts
     * @param path
     * @param uri
     * @param templateVar
     * @param authorizationHeader
     * @param cookieList
     * @param httpMethod
     * @return
     */
    ResponseSpec doMultipartDataPost(WebClient webClient, MultiValueMap<String, HttpEntity<?>> multiParts, String path,
            Map<String, Object> templateVar, String token, List<Cookie> cookieList) {

        log.info("Beginning Multipart REST Service call...");

        Map<String, List<String>> headerMap = prepareHeaders(token);
        headerMap.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(MediaType.MULTIPART_FORM_DATA_VALUE));

        return clientInterface.doMultipartPostOrPut(webClient, templateVar,
                multiParts, path, headerMap, cookieList,
                HttpMethod.POST);
    }

    /**
     *
     * @param path
     * @param uri
     * @param templateVar
     * @param authorizationHeader
     * @param cookieList
     * @return
     */
    ResponseSpec doDelete(WebClient webClient, String path, Map<String, Object> templateVar, String token,
            MultiValueMap<String, String> params, List<Cookie> cookieList) {

        log.info("Beginning Delete REST Service call...");

        return clientInterface.doDelete(webClient, templateVar, path, params,
                prepareHeaders(token), cookieList);
    }

    private Map<String, List<String>> prepareHeaders(String authHeader) {

        Map<String, List<String>> myHeaders = new HashMap<>();
        if (StringUtils.isNotBlank(authHeader)) {

            myHeaders.put(HttpHeaders.AUTHORIZATION, Arrays.asList("Bearer " + authHeader));
        }

        myHeaders.put(HttpHeaders.ACCEPT, Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        return myHeaders;
    }

}
