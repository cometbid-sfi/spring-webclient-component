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

import java.io.IOException;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.http.HttpStatus.*;
import lombok.extern.log4j.Log4j2;
import org.cometbid.component.api.auth.exceptions.AuthenticationError;
import org.cometbid.component.api.auth.exceptions.TooManyRequestException;
import org.cometbid.component.api.exceptions.handler.ErrorPublisher;
import org.cometbid.component.api.generic.exceptions.ApiResponseException;
import org.cometbid.component.api.generic.exceptions.BadRequestException;
import org.cometbid.component.api.generic.exceptions.ResourceNotFoundException;
import org.cometbid.component.api.generic.exceptions.ServerTimeoutRequestException;
import org.cometbid.component.api.generic.exceptions.ServiceUnavailableException;
import org.cometbid.component.api.response.model.AppResponse;
import reactor.core.publisher.Mono;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class WebClientExceptionHandler {

    /**
     *
     * @param <T>
     * @param <R>
     * @param handlerFunction
     * @return
     */
    public static <T, R> Function<T, Mono<R>> handleCheckedExceptionFunction(
            LambdaCheckedExceptionFunction<T, R> handlerFunction) {

        return obj -> {
            R r = null;
            try {
                r = handlerFunction.apply(obj);
            } catch (RuntimeException ex) {
                log.error("Exception occured: ", ex);

                return Mono.error(ex);
            }
            return Mono.just(r);
        };
    }

    /**
     *
     * @param <T>
     * @param <R>
     */
    @FunctionalInterface
    public interface LambdaCheckedExceptionFunction<T, R> {

        public R apply(T t) throws RuntimeException;
    }

    /**
     *
     * @param <R>
     * @param clientResponse
     * @param clazzResponse
     * @return
     */
    public static <R> Mono<R> processResponse(ClientResponse clientResponse, Class<? extends R> clazzResponse) {
        HttpStatusCode status = clientResponse.statusCode();

        Mono<R> respObj = Mono.empty();

        if (status.is2xxSuccessful()) {
            respObj = clientResponse.bodyToMono(clazzResponse);

        } else if (status.isError()) {
            if (status.is4xxClientError()) {
                log.error("Client Error occurred while processing request");

                return clientResponse.createException().flatMap(ex -> Mono.error(handle4xxException(ex)));

            } else if (status.is5xxServerError()) {
                log.error("Server Error occurred while processing request");
                return clientResponse.createException().flatMap(ex -> Mono.error(handle5xxException(ex)));
            } else {

                return clientResponse.createException().flatMap(ex -> {

                    return ErrorPublisher.raiseApiServiceError(ex.getMessage(), clientResponse.statusCode().value(), ex);
                });
            }
        }

        return respObj;
    }

    public static Throwable handle5xxException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException)) {
            log.warn("Got a unexpected error: ", ex);

            return new ApiResponseException("Server api error occured", HttpStatus.SERVICE_UNAVAILABLE, ex);
        }

        WebClientResponseException wcre = (WebClientResponseException) ex;
        HttpStatus httpStatus = HttpStatus.valueOf(wcre.getStatusCode().value());

        switch (httpStatus) {

            case SERVICE_UNAVAILABLE -> {
                return new ServiceUnavailableException(new Object[]{getErrorMessage(wcre)});
            }
            case HttpStatus.TOO_MANY_REQUESTS -> {
                return new TooManyRequestException(new Object[]{getErrorMessage(wcre)});
            }
            case HttpStatus.GATEWAY_TIMEOUT -> {
                return new ServerTimeoutRequestException(new Object[]{getErrorMessage(wcre)});
            }
            case INTERNAL_SERVER_ERROR -> {
                return new ServiceUnavailableException(new Object[]{getErrorMessage(wcre)});
            }
            default -> {
                log.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                log.warn("Error body: {}", wcre.getResponseBodyAsString());

                return new ApiResponseException(wcre.getMessage(), wcre.getStatusCode().value(), wcre);
            }
        }
    }

    public static Throwable handle4xxException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException)) {
            log.warn("Got a unexpected error: ", ex);

            return new ApiResponseException("Client api error occured", HttpStatus.BAD_REQUEST, ex);
        }

        WebClientResponseException wcre = (WebClientResponseException) ex;
        HttpStatus httpStatus = HttpStatus.valueOf(wcre.getStatusCode().value());

        switch (httpStatus) {

            case NOT_FOUND -> {
                return new ResourceNotFoundException(new Object[]{getErrorMessage(wcre)});
            }
            case UNPROCESSABLE_ENTITY -> {
                return new BadRequestException(new Object[]{getErrorMessage(wcre)});
            }
            case UNAUTHORIZED, FORBIDDEN -> {
                return new AuthenticationError(getErrorMessage(wcre));
            }
            default -> {
                log.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                log.warn("Error body: {}", wcre.getResponseBodyAsString());

                return new ApiResponseException(wcre.getMessage(), wcre.getStatusCode().value(), wcre);
            }
        }
    }

    static String getErrorMessage(WebClientResponseException ex) {
        ObjectMapper mapper = new ObjectMapper();
        // try {
        //AppResponse appResponse = mapper.readValue(ex.getResponseBodyAsString(), AppResponse.class);

        return ex.getMessage();
        /*
        } catch (IOException ioex) {
            return ex.getMessage();
        }
         */
    }
}
