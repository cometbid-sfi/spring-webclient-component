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

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.util.MultiValueMap;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.extern.log4j.Log4j2;
import org.cometbid.component.api.exceptions.handler.ErrorPublisher;
import static org.cometbid.component.webclient.WebClientExceptionHandler.processResponse;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Component
public class ReactiveWebClientImpl implements ReactiveClientInterface {

    /**
     *
     * @param <T>
     * @param uri
     * @param params
     * @param clazzResponse
     * @return
     */
    @Override
    public <T> Flux<T> getWithUriTemplateToFlux(WebClient webClient, String uri, Class<? extends T> clazzResponse,
            Map<String, List<String>> headerFields, @NonNull Map<String, Object> uriVariables,
            Map<String, String> params) {

        return webClient.get()
                //
                .uri(uri, uriVariables)
                //
                .attributes(attr -> {
                    if (params != null) {
                        attr.putAll(params);
                    }
                })
                //
                .headers(headers -> {
                    if (MapUtils.isNotEmpty(headerFields)) {
                        headers.putAll(headerFields);
                    }
                }).exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param params
     * @param clazzResponse
     * @return
     */
    @Override
    public <T> Mono<T> postWithUriTemplateToMono(WebClient webClient, String uri, Class<? extends T> clazzResponse,
            Map<String, List<String>> headerFields, @NonNull Map<String, Object> uriVariables,
            Map<String, String> params) {

        return webClient.post()
                //
                .uri(uri, uriVariables)
                //
                .attributes(attr -> {
                    if (params != null) {
                        attr.putAll(params);
                    }
                })
                //
                .headers(headers -> {
                    if (MapUtils.isNotEmpty(headerFields)) {
                        headers.putAll(headerFields);
                    }
                }).exchangeToMono(clientResponse -> processResponse(clientResponse, clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param params
     * @param clazzResponse
     * @return
     */
    @Override
    public <T> Mono<T> performGetToMono(WebClient webClient, URI uri, Class<? extends T> clazzResponse,
            Map<String, List<String>> headerFields, MultiValueMap<String, String> params) {

        return webClient.get().uri(uriBuilder -> uriBuilder.scheme(uri.getScheme())
                //
                .host(uri.getHost())
                //
                .port(uri.getPort())
                //
                .path(uri.getPath())
                //
                .queryParams(params).build())
                //
                .headers(headers -> {
                    if (MapUtils.isNotEmpty(headerFields)) {
                        headers.putAll(headerFields);
                    }
                }).exchangeToMono(clientResponse -> processResponse(clientResponse, clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param params
     * @param clazzResponse
     * @return
     */
    @Override
    public <T> Flux<T> performGetToFlux(WebClient webClient, URI uri, Class<? extends T> clazzResponse,
            Map<String, List<String>> headerFields, MultiValueMap<String, String> params) {

        return webClient.get().uri(uriBuilder -> uriBuilder.scheme(uri.getScheme()).host(uri.getHost())
                .port(uri.getPort()).path(uri.getPath()).queryParams(params).build()).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param requestBody
     * @param clazzResponse
     * @return
     */
    @Override
    public <T> Mono<T> performPostToMono(WebClient webClient, URI uri, Object requestBody,
            Class<? extends T> clazzResponse, Map<String, List<String>> headerFields,
            MultiValueMap<String, String> params) {

        return webClient.post().uri(uriBuilder -> uriBuilder.scheme(uri.getScheme()).host(uri.getHost())
                .port(uri.getPort()).path(uri.getPath()).queryParams(params).build()).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).body(BodyInserters.fromValue(requestBody))
                .exchangeToMono(clientResponse -> processResponse(clientResponse, clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param requestBody
     * @param clazzResponse
     * @return
     */
    @Override
    public <T> Mono<T> performPutToMono(WebClient webClient, URI uri, Object requestBody,
            Class<? extends T> clazzResponse, Map<String, List<String>> headerFields,
            MultiValueMap<String, String> params) {

        return webClient.put()
                .uri(uriBuilder -> uriBuilder.scheme(uri.getScheme()).host(uri.getHost()).port(uri.getPort())
                .path(uri.getPath()).queryParams(params).build())
                .body(BodyInserters.fromValue(requestBody)).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).exchangeToMono(clientResponse -> processResponse(clientResponse, clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param formData
     * @param clazzResponse
     * @return
     */
    @Override
    public <T> Mono<T> performPostFormToMono(WebClient webClient, URI uri, MultiValueMap<String, String> formData,
            Class<? extends T> clazzResponse, Map<String, List<String>> headerFields,
            MultiValueMap<String, String> params) {

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.scheme(uri.getScheme()).host(uri.getHost()).port(uri.getPort())
                .path(uri.getPath()).queryParams(params).build())
                .body(BodyInserters.fromFormData(formData)).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).accept(MediaType.APPLICATION_JSON).acceptCharset(StandardCharsets.UTF_8)
                .exchangeToMono(clientResponse -> processResponse(clientResponse, clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param requestBody
     * @param clazzResponse
     * @return
     */
    @Override
    public <T, R> Mono<R> performPublisherPost(WebClient webClient, URI uri, Mono<T> requestBody,
            Class<? extends T> clazzRequest, Class<? extends R> clazzResponse, Map<String, List<String>> headerFields,
            MultiValueMap<String, String> params) {

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.scheme(uri.getScheme()).host(uri.getHost()).port(uri.getPort())
                .path(uri.getPath()).queryParams(params).build())
                .body(requestBody, clazzRequest).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).exchangeToMono(clientResponse -> processResponse(clientResponse, clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param requestBody
     * @param clazzResponse
     * @return
     */
    @Override
    public <T, R> Mono<R> performPublisherPut(WebClient webClient, URI uri, Mono<T> requestBody,
            Class<? extends T> clazzRequest, Class<? extends R> clazzResponse, Map<String, List<String>> headerFields,
            MultiValueMap<String, String> params) {

        return webClient.put()
                .uri(uriBuilder -> uriBuilder.scheme(uri.getScheme()).host(uri.getHost()).port(uri.getPort())
                .path(uri.getPath()).queryParams(params).build())
                .body(requestBody, clazzRequest).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).exchangeToMono(clientResponse -> processResponse(clientResponse, clazzResponse));
    }

    /**
     *
     * @param <T>
     * @param uri
     * @param params
     * @param clazzResponse
     * @return
     */
    @Override
    public <T> Mono<T> performDeleteToMono(WebClient webClient, URI uri, Class<? extends T> clazzResponse,
            Map<String, List<String>> headerFields, MultiValueMap<String, String> params) {

        return webClient.delete().uri(uriBuilder -> uriBuilder.scheme(uri.getScheme()).host(uri.getHost())
                .port(uri.getPort()).path(uri.getPath()).queryParams(params).build()).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).exchangeToMono(clientResponse -> processResponse(clientResponse, clazzResponse));
    }

    /**
     *
     * @param templateVar
     * @param params
     * @param path
     * @param headerFields
     * @param listOfCookies
     * @return
     */
    @Override
    public ResponseSpec doGet(WebClient webClient, @NonNull Map<String, Object> templateVar,
            @NonNull MultiValueMap<String, String> params, @NonNull String path, Map<String, List<String>> headerFields,
            List<Cookie> listOfCookies) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).queryParams(params).build(templateVar))
                // .header("X-B3-TRACEID", ThreadContext.get("X-B3-TRACEID"))
                .headers(headers -> {
                    if (MapUtils.isNotEmpty(headerFields)) {
                        headers.putAll(headerFields);
                    }
                }).cookies(cookie -> {
            if (CollectionUtils.isNotEmpty(listOfCookies)) {
                listOfCookies.forEach(p -> cookie.add(p.name(), p.value()));
            }
        }).retrieve();

    }

    /**
     *
     * @param templateVar
     * @param requestBody
     * @param path
     * @param headerFields
     * @param listOfCookies
     * @param methodType
     * @return
     */
    @Override
    public ResponseSpec doPostOrPutOrPatch(WebClient webClient, @NonNull Map<String, Object> templateVar,
            @NonNull Object requestBody, @NonNull String path, @NonNull MultiValueMap<String, String> params,
            Map<String, List<String>> headerFields, List<Cookie> listOfCookies, @NonNull HttpMethod methodType) {

        validateMethodType(methodType);

        return webClient.method(methodType)
                .uri(uriBuilder -> uriBuilder.path(path).queryParams(params).build(templateVar))
                .body(BodyInserters.fromValue(requestBody)).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).cookies(cookie -> {
            if (CollectionUtils.isNotEmpty(listOfCookies)) {
                listOfCookies.forEach(p -> cookie.add(p.name(), p.value()));
            }
        }).retrieve();

    }

    /**
     *
     * @param templateVar
     * @param path
     * @param headerFields
     * @param listOfCookies
     * @param methodType
     * @return
     */
    @Override
    public <T> ResponseSpec doPublisherPostOrPutOrPatch(WebClient webClient, @NonNull Map<String, Object> templateVar,
            @NonNull Mono<T> monoData, Class<? extends T> clazzRequest, @NonNull String path,
            @NonNull MultiValueMap<String, String> params, Map<String, List<String>> headerFields,
            List<Cookie> listOfCookies, @NonNull HttpMethod methodType) {

        validateMethodType(methodType);

        return webClient.method(methodType)
                .uri(uriBuilder -> uriBuilder.path(path).queryParams(params).build(templateVar))
                .body(monoData, clazzRequest).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).cookies(cookie -> {
            if (CollectionUtils.isNotEmpty(listOfCookies)) {
                listOfCookies.forEach(p -> cookie.add(p.name(), p.value()));
            }
        }).retrieve();

    }

    /**
     *
     * @param templateVar
     * @param formData
     * @param path
     * @param headerFields
     * @param listOfCookies
     * @param methodType
     * @return
     */
    @Override
    public ResponseSpec doFormDataPostOrPut(WebClient webClient, @NonNull Map<String, Object> templateVar,
            @NonNull MultiValueMap<String, String> formData, @NonNull String path,
            Map<String, List<String>> headerFields, List<Cookie> listOfCookies, @NonNull HttpMethod methodType) {

        validateMethodType(methodType);

        return webClient.method(methodType)
                .uri(uriBuilder -> uriBuilder.path(path).build(templateVar)).body(BodyInserters.fromFormData(formData))
                .headers(headers -> {
                    if (MapUtils.isNotEmpty(headerFields)) {
                        headers.putAll(headerFields);
                    }
                }).cookies(cookie -> {
            if (CollectionUtils.isNotEmpty(listOfCookies)) {
                listOfCookies.forEach(p -> cookie.add(p.name(), p.value()));
            }
        }).retrieve();
    }

    /**
     *
     * @param templateVar
     * @param path
     * @param multipart
     * @param headerFields
     * @param listOfCookies
     * @param methodType
     * @return
     */
    @Override
    public ResponseSpec doMultipartPostOrPut(WebClient webClient, @NonNull Map<String, Object> templateVar,
            @NonNull MultiValueMap<String, HttpEntity<?>> multipart, @NonNull String path,
            Map<String, List<String>> headerFields, @NonNull List<Cookie> listOfCookies,
            @NonNull HttpMethod methodType) {

        validateMethodType(methodType);

        BodyInserter<?, ? super ClientHttpRequest> multipartData = BodyInserters.fromMultipartData(multipart);

        return webClient.method(methodType)
                .uri(uriBuilder -> uriBuilder.path(path).build(templateVar)).body(multipartData).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).cookies(cookie -> {
            if (CollectionUtils.isNotEmpty(listOfCookies)) {
                listOfCookies.forEach(p -> cookie.add(p.name(), p.value()));
            }
        }).retrieve();
    }

    /**
     *
     * @param templateVar
     * @param path
     * @param headerFields
     * @param listOfCookies
     * @return
     */
    @Override
    public ResponseSpec doDelete(WebClient webClient, Map<String, Object> templateVar, String path,
            MultiValueMap<String, String> params, Map<String, List<String>> headerFields, List<Cookie> listOfCookies) {

        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path(path).queryParams(params).build(templateVar)).headers(headers -> {
            if (MapUtils.isNotEmpty(headerFields)) {
                headers.putAll(headerFields);
            }
        }).cookies(cookie -> {
            if (CollectionUtils.isNotEmpty(listOfCookies)) {
                listOfCookies.forEach(p -> cookie.add(p.name(), p.value()));
            }
        }).retrieve();

    }

    private void validateMethodType(HttpMethod methodType) {

        if (!(methodType.matches("POST") || methodType.matches("PUT") || methodType.matches("PATCH"))) {
            ErrorPublisher.raiseBadRequestException("invalid.methodType", new Object[]{methodType});
        }
    }

}
