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
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import io.netty.handler.codec.http.cookie.Cookie;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author samueladebowale
 */
public interface ReactiveClientInterface {

    /**
     *
     * @param <T>
     * @param webClient
     * @param uri
     * @param params
     * @param headerFields
     * @param clazzResponse
     * @return
     */
    <T> Mono<T> performGetToMono(WebClient webClient, URI uri, Class<? extends T> clazzResponse,
            @NonNull Map<String, List<String>> headerFields, MultiValueMap<String, String> params);

    /**
     *
     * @param <T>
     * @param webClient
     * @param uri
     * @param params
     * @param headerFields
     * @param clazzResponse
     * @return
     */
    <T> Flux<T> performGetToFlux(WebClient webClient, URI uri, Class<? extends T> clazzResponse,
            @NonNull Map<String, List<String>> headerFields, MultiValueMap<String, String> params);

    /**
     *
     * @param <T>
     * @param webClient
     * @param uri
     * @param formData
     * @param clazzResponse
     * @param headerFields
     * @param params
     * @return
     */
    <T> Mono<T> performPostFormToMono(WebClient webClient, URI uri, MultiValueMap<String, String> formData,
            Class<? extends T> clazzResponse, @NonNull Map<String, List<String>> headerFields,
            MultiValueMap<String, String> params);

    /**
     *
     * @param webClient
     * @param templateVar
     * @param params
     * @param path
     * @param headerFields
     * @param listOfCookies
     * @return
     */
    ResponseSpec doGet(WebClient webClient, Map<String, Object> templateVar, MultiValueMap<String, String> params,
            String path, Map<String, List<String>> headerFields, List<Cookie> listOfCookies);

    /**
     *
     * @param webClient
     * @param templateVar
     * @param formData
     * @param path
     * @param headerFields
     * @param listOfCookies
     * @param methodType
     * @return
     */
    ResponseSpec doFormDataPostOrPut(WebClient webClient, Map<String, Object> templateVar,
            MultiValueMap<String, String> formData, String path, Map<String, List<String>> headerFields,
            List<Cookie> listOfCookies, HttpMethod methodType);

    /**
     *
     * @param webClient
     * @param templateVar
     * @param parts
     * @param path
     * @param headerFields
     * @param listOfCookies
     * @param methodType
     * @return
     */
    ResponseSpec doMultipartPostOrPut(WebClient webClient, Map<String, Object> templateVar,
            MultiValueMap<String, HttpEntity<?>> parts, String path, Map<String, List<String>> headerFields,
            List<Cookie> listOfCookies, HttpMethod methodType);

    /**
     *
     * @param webClient
     * @param templateVar
     * @param path
     * @param params
     * @param headerFields
     * @param listOfCookies
     * @return
     */
    ResponseSpec doDelete(WebClient webClient, Map<String, Object> templateVar, String path,
            MultiValueMap<String, String> params, Map<String, List<String>> headerFields, List<Cookie> listOfCookies);

    /**
     *
     * @param <T>
     * @param webClient
     * @param uri
     * @param requestBody
     * @param clazzResponse
     * @param headerFields
     * @param params
     * @return
     */
    <T> Mono<T> performPostToMono(WebClient webClient, URI uri, Object requestBody, Class<? extends T> clazzResponse,
            @NonNull Map<String, List<String>> headerFields, MultiValueMap<String, String> params);

    /**
     *
     * @param <T>
     * @param webClient
     * @param uri
     * @param requestBody
     * @param clazzResponse
     * @param headerFields
     * @param params
     * @return
     */
    <T> Mono<T> performPutToMono(WebClient webClient, URI uri, Object requestBody, Class<? extends T> clazzResponse,
            @NonNull Map<String, List<String>> headerFields, MultiValueMap<String, String> params);

    /**
     *
     * @param <T>
     * @param webClient
     * @param uri
     * @param params
     * @param headerFields
     * @param clazzResponse
     * @return
     */
    <T> Mono<T> performDeleteToMono(WebClient webClient, URI uri, Class<? extends T> clazzResponse,
            @NonNull Map<String, List<String>> headerFields, MultiValueMap<String, String> params);

    /**
     *
     * @param <T>
     * @param <R>
     * @param webClient
     * @param uri
     * @param requestBody
     * @param clazzRequest
     * @param clazzResponse
     * @param headerFields
     * @param params
     * @return
     */
    <T, R> Mono<R> performPublisherPost(WebClient webClient, URI uri, Mono<T> requestBody,
            Class<? extends T> clazzRequest, Class<? extends R> clazzResponse,
            @NonNull Map<String, List<String>> headerFields, MultiValueMap<String, String> params);

    /**
     *
     * @param <T>
     * @param <R>
     * @param webClient
     * @param uri
     * @param requestBody
     * @param clazzRequest
     * @param clazzResponse
     * @param headerFields
     * @param params
     * @return
     */
    <T, R> Mono<R> performPublisherPut(WebClient webClient, URI uri, Mono<T> requestBody,
            Class<? extends T> clazzRequest, Class<? extends R> clazzResponse,
            @NonNull Map<String, List<String>> headerFields, MultiValueMap<String, String> params);

    /**
     *
     * @param webClient
     * @param templateVar
     * @param data
     * @param path
     * @param headerFields
     * @param params
     * @param listOfCookies
     * @param methodType
     * @return
     */
    ResponseSpec doPostOrPutOrPatch(WebClient webClient, @NonNull Map<String, Object> templateVar, @NonNull Object data,
            @NonNull String path, @NonNull MultiValueMap<String, String> params,
            @NonNull Map<String, List<String>> headerFields, List<Cookie> listOfCookies,
            @NonNull HttpMethod methodType);

    /**
     * @param <T>
     * @param webClient
     * @param templateVar
     * @param path
     * @param monoData
     * @param clazzRequest
     * @param headerFields
     * @param params
     * @param listOfCookies
     * @param methodType
     * @return
     */
    <T> ResponseSpec doPublisherPostOrPutOrPatch(WebClient webClient, @NonNull Map<String, Object> templateVar,
            @NonNull Mono<T> monoData, Class<? extends T> clazzRequest, @NonNull String path,
            @NonNull MultiValueMap<String, String> params, @NonNull Map<String, List<String>> headerFields,
            List<Cookie> listOfCookies, @NonNull HttpMethod methodType);

    /**
     *
     * @param <T>
     * @param webClient
     * @param uri
     * @param params
     * @param clazzResponse
     * @param uriVariables
     * @param headerFields
     * @return
     */
    <T> Flux<T> getWithUriTemplateToFlux(WebClient webClient, String uri, Class<? extends T> clazzResponse,
            Map<String, List<String>> headerFields, @NonNull Map<String, Object> uriVariables,
            Map<String, String> params);

    /**
     *
     * @param <T>
     * @param webClient
     * @param uri
     * @param params
     * @param clazzResponse
     * @param headerFields
     * @param uriVariables
     * @return
     */
    <T> Mono<T> postWithUriTemplateToMono(WebClient webClient, String uri, Class<? extends T> clazzResponse,
            Map<String, List<String>> headerFields, @NonNull Map<String, Object> uriVariables,
            Map<String, String> params);

}
