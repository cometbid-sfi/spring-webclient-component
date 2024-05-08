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
package org.cometbid.component.test;

import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;

/**
 *
 * @author samueladebowale
 */
public class ReactiveWebClientUsage {
    
    
    public MultiValueMap<String, HttpEntity<?>> buildMultiparts(Map<String, Object> formParts) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        formParts.forEach((k, v) -> builder.part(k, v));

        // Build and use
        return builder.build();
    }

    public MultiValueMap<String, HttpEntity<?>> sampleBuilder() throws IOException {

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("profileImage", new ClassPathResource("test-image.jpg").getFile())
                .header("Content-Disposition", "form-data; name=profileImage; filename=profile-image.jpg");

        Resource logo = new ClassPathResource("/org/springframework/http/converter/logo.jpg");
        Flux<DataBuffer> buffers = DataBufferUtils.read(logo, new DefaultDataBufferFactory(), 1024);
        long contentLength = logo.contentLength();

        bodyBuilder.asyncPart("buffers", buffers, DataBuffer.class).headers(h -> {
            h.setContentDispositionFormData("buffers", "buffers.jpg");
            h.setContentType(MediaType.IMAGE_JPEG);
            h.setContentLength(contentLength);
        });

        bodyBuilder.part("resource", new UrlResource("file:/tmp/test-document.pdf")).headers(h -> {
            h.setContentDispositionFormData("userDocument", "my-thesis.pdf");
            h.setContentType(MediaType.APPLICATION_PDF);
        });

        bodyBuilder.part("username", "shiveenpandita", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=username")
                .header("Content-type", "text/plain");

        bodyBuilder.part("email", "shiveenpandita@gmail.com", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=email")
                .header("Content-type", "text/plain");

        return bodyBuilder.build();
    }
}
