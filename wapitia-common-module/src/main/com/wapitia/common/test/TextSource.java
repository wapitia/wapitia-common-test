/*
 * Copyright 2016 wapitia.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * Neither the name of wapitia.com or the names of contributors may be used to
 * endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 * WAPITIA.COM ("WAPITIA") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL WAPITIA OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * WAPITIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */

package com.wapitia.common.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 * A source of text lines, plus some helpful factory methods to provide them.
 *
 * <p>The {@link #toStream()} supplies the {@link FunctionalInterface} to this
 * class, and once it is called, the stream of data is usually exhausted,
 * and calling this method again will produce unpredictable results.
 * @author Corey Morgan
 *
 */
@FunctionalInterface
public interface TextSource {

    /**
     * Produce a stream of test data strings.
     * Once this method is called, the stream of data is usually exhausted,
     * and calling this method again will produce unpredictable results.
     *
     * @return a non-null {@code Stream} of {@code String}s.
     */
    Stream<String> toStream();

    /**
     * Builds and returns a {@code TextSource} instance given some
     * text stream resource name.
     *
     * <p>The resource is located using the
     * {@link java.lang.ClassLoader ClassLoader} of this {@code TextSource}
     * object.
     *
     * @param resourceName the name of a resource, resolved to an i/o
     *                     stream from a call to
     *                     {@link Class#getResourceAsStream(String)}
     * @return A newly build TextSource structure,
     *         built from the resource stream.
     *
     * @see #fromResource(String, Class)
     */
    public static TextSource fromResource(String resourceName) {
        TextSource result = fromResource(resourceName, TextSource.class);
        return result;
    }

    /**
     * Builds a {@code TextSource} instance given some text stream resource,
     * and some {@link java.lang.Class Class} reference specifying which
     * {@link java.lang.ClassLoader ClassLoader} to use to load the
     * resource.
     *
     * @param resourceName the name of a resource, suitable for passing
     *                     to {@link Class#getResourceAsStream(String)}
     * @param clss {@code Class} to use to load the text resource.
     *
     * @return A newly build TextSource structure,
     *         built from the resource stream.
     */
    public static TextSource fromResource(
          String resourceName, Class<?> clss)
    {
        InputStream istream = clss.getResourceAsStream(resourceName);
        BufferedReader rdr = new BufferedReader(new InputStreamReader(istream));
        TextSource result = new ReaderTextSource(rdr);
        return result;
    }

}
