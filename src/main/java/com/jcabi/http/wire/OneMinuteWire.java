/**
 * Copyright (c) 2011-2015, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.http.wire;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Timeable;
import com.jcabi.http.Request;
import com.jcabi.http.Response;
import com.jcabi.http.Wire;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Wire that throws an {@link IOException} if a request takes longer
 * than a minute.
 *
 * <p>It's recommended to use this decorator in production, in order
 * to avoid stuck requests:
 *
 * <pre> String html = new JdkRequest("http://goggle.com")
 *   .through(OneMinuteWire.class)
 *   .header(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN)
 *   .fetch()
 *   .body();</pre>
 *
 * <p>The class is immutable and thread-safe.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.10
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "origin")
public final class OneMinuteWire implements Wire {

    /**
     * Original wire.
     */
    private final transient Wire origin;

    /**
     * Public ctor.
     * @param wire Original wire
     */
    public OneMinuteWire(@NotNull(message = "wire can't be NULL")
        final Wire wire) {
        this.origin = wire;
    }

    // @checkstyle ParameterNumber (5 lines)
    @Override
    @Timeable(limit = 1, unit = TimeUnit.MINUTES)
    public Response send(final Request req, final String home,
						 final String method,
						 final Collection<Map.Entry<String, String>> headers,
						 final InputStream content,
						 final int connectTimeout, final int readTimeout) throws IOException {
        return this.origin.send(req, home, method, headers, content,
				connectTimeout, readTimeout);
    }
}
