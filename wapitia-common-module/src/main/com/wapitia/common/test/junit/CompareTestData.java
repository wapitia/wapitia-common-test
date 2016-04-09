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

package com.wapitia.common.test.junit;

import static java.lang.String.format;
import static org.junit.Assert.fail;

import com.wapitia.common.test.TextSource;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Utility as part of the wapitia JUnit test framework to compare
 * actual results with expected results.
 * This extends the common JUnit functionality to compare text scripts
 * as would be stored in test files.
 *
 * @author Corey Morgan
 *
 */
public class CompareTestData {

    private static final String TOO_LONG_STR =
        "Expected end of actual data at line %d, but got \"%s\"";
    private static final String CAME_UP_SHORT_STR =
        "Came up short at line %d, expected \"%s\"";
    private static final String ITEM_MISMATCH_STR =
        "Item mismatch at line %d, expected \"%s\", but got \"%s\"";

    /**
     * Regular Expression matching a hash-code comment line.
     * A hash-code comment line is one that starts with a hash character '#'
     * preceded by optional whitespace, and continuing to the end of the
     * string.
     * @see Pattern#compile(String)
     */
    public static final String HASH_COMMENT_REGEX = "^\\s*#.*$";

    /**
     * Predicate tests {@code true} when the String is
     * not a {@link #HASH_COMMENT_REGEX comment}. This is used to
     * filter out comment strings.
     *
     * @return a new Predicate that will filter out hash comment lines.
     */
    public static Predicate<String> noHashComments() {
        final Pattern commentPattern = Pattern.compile(HASH_COMMENT_REGEX);
        final Predicate<String> noComments =
            ts -> ! commentPattern.matcher(ts).matches();
        return noComments;
    }

    /**
     * Predicate always tests {@code true} to be used as a pass-through
     * filter.
     */
    public static Predicate<String> noFilter = ts -> true;

    /**
     * Compare expected and actual lines of text.
     * Comment lines (those that begin with '#') are ignored.
     *
     * @param expectedsResourceName Expected lines of test data,
     *                              as a resource to load.
     * @param actuals Actual lines of test data.
     *
     * @see #assertEqual(Stream, Stream,
     *          Comparator, Predicate, boolean)
     */
    public static void assertEqual(
        final String expectedsResourceName,
        final Stream<String> actuals)
    {
        final TextSource ts =
            TextSource.fromResource(expectedsResourceName);
        assertEqual(ts.toStream(), actuals, noHashComments());
    }

    /**
     * Compare expected and actual lines of text.
     * If the stream of strings mismatch then this will
     * {@link org.junit.Assert#fail fail}.
     * The {@code lineFilter} predicate is used to ignore certain lines in
     * both streams such as comment lines. This must not be null:
     * If all lines are to be compared (no filtering),
     * then {@link #noFilter} should be used here.
     *
     * @param expected Expected lines of test data.
     * @param actuals Actual lines of test data.
     * @param lineFilter filter applied to both streams to ignore
     *                   particular line types, such as comments.
     */
    public static void assertEqual(
        final Stream<String> expected,
        final Stream<String> actuals,
        final Predicate<String> lineFilter)
    {
        assertEqual(expected, actuals, String::compareTo,
            lineFilter, false);
    }

    /**
     * Compare expected Stream of Strings to some actual stream.
     * The actual
     * stream must be at least as long as the expected stream.
     * If the two streams of strings don't match then this will
     * {@link org.junit.Assert#fail fail}.
     *
     * <p>None of these parameters may be null.
     *
     * @param <T> any comparable type.
     *
     * @param expectedStr  Expected strings, marks the lower
     *                     baseline of results
     * @param actualStr Actual Strings, must be at least as long as expected.
     * @param comp Comparator for equality.
     * @param lineFilter filter applied to both streams to ignore
     *                   particular line types, such as comments.
     * @param mustBeSameLength {@code true} if the expected stream must have
     *                         no extra lines beyond what the actuals provide.
     */
    public static <T> void assertEqual(
        final Stream<T> expectedStr,
        final Stream<T> actualStr,
        final Comparator<T> comp,
        final Predicate<String> lineFilter,
        final boolean mustBeSameLength)
    {
        final Iterator<T> it1 = expectedStr.iterator();
        final Iterator<T> it2 = actualStr.iterator();
        long line = 0L;
        while (it1.hasNext()) {
            ++line;
            final T s1 = it1.next();

            if (it2.hasNext()) {
                final T s2 = it2.next();
                if (comp.compare(s1, s2) != 0) {
                    fail(format(ITEM_MISMATCH_STR, line, s1, s2));
                }
            } else {
                // lines2 came up short
                fail(format(CAME_UP_SHORT_STR, line, s1));
                break;
            }
        }
        if (it2.hasNext() && mustBeSameLength) {
            final T s2 = it2.next();
            fail(format(TOO_LONG_STR, line, s2));
        }
    }

    /**
     * Constructor is private as this is a utility class.
     */
    private CompareTestData() {
    }

}
