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

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.function.Consumer;

/**
 * Common <a href="http://junit.org">JUnit</a> functionality for the
 * Wapitia testing framework.
 *
 * @author Corey Morgan
 */
public final class WapitiaTestMain {

    /**
     * {@value}.
     */
    public static final String FAIL = "fail";

    /**
     * {@value}.
     */
    public static final String SUCCESS = "success";

    /**
     * Runs the JUnit tests packaged in the given test {@code suite} by invoking
     * {@link JUnitCore#runClasses} against it, and then prints the failure
     * results to some {@link Consumer log}. The given {@code suiteClass} is a
     * class annotated with {@link org.junit.runners.Suite.SuiteClasses
     * Suite.SuiteClasses } to define the set of {@link org.junit.Test Test}
     * methods to run.
     *
     * <p><b>Usage example:</b>
     *
     * <pre>
     *   &#64;RunWith(Suite.class)
     *   &#64;Suite.SuiteClasses( { MyTest1.class, MyTest1.class } )
     *   class MyTestSuite {};
     *    ...
     *   WapitiaTestMain.runSuite(args, MyTestSuite.class, System.out::println);
     * </pre>
     *
     * @param <T>
     *            Test Suite instance type
     * @param mainArgs
     *            not used here
     * @param suite
     *            Annotated test {@link org.junit.runners.Suite.SuiteClasses
     *            Suite} type packaging a set of {@link org.junit.Test Test}s
     *            to run.
     * @param log
     *            Consumer of lines of failures and other test results.
     *
     * @see org.junit.runners.Suite.SuiteClasses
     * @see org.junit.runner.RunWith
     * @see org.junit.Test
     */
    public static <T> void runSuite(
            final String[] mainArgs,
            final Class<T> suite,
            final Consumer<String> log)
    {
        final Result result = JUnitCore.runClasses(suite);
        result.getFailures().stream().map(Failure::toString).forEach(log);
        final String succFail = result.wasSuccessful() ? SUCCESS : FAIL;
        log.accept(format("%s %s", suite.getSimpleName(), succFail));
    }

    /**
     * Runs the JUnit tests packaged in the given test {@code suite} by invoking
     * {@link JUnitCore#runClasses} against it, and prints the results to
     * {@link System#out standard out}. The given {@code suiteClass} is a class
     * annotated with
     * {@link org.junit.runners.Suite.SuiteClasses @Suite.SuiteClasses } which
     * defines the set of {@link org.junit.Test @Test} methods to run.
     *
     * <p><b>Usage example:</b>
     *
     * <pre>
     *      &#64;RunWith(Suite.class)
     *      &#64;Suite.SuiteClasses( { MyTest1.class, MyTest1.class } )
     *      class MyTestSuite {};
     *      ...
     *      WapitiaTestMain.runSuite(MyTestSuite.class);
     * </pre>
     *
     * @param <T>
     *            Test Suite instance type
     * @param suite
     *            Annotated test {@link org.junit.runners.Suite.SuiteClasses
     *            Suite} type packaging a set of {@link org.junit.Test Test}s to
     *            run.
     *
     * @see org.junit.runners.Suite.SuiteClasses
     * @see org.junit.runner.RunWith
     * @see org.junit.Test
     * @see #runSuite(String[], Class, Consumer)
     */
    public static <T> void runSuite(final Class<T> suite) {
        final String[] mainArgs = new String[] { suite.getSimpleName() };
        WapitiaTestMain.runSuite(mainArgs, suite, System.out::println);
    }

    /**
     * Constructor is private as this is a utility class.
     */
    private WapitiaTestMain() {
    }

}
