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

package com.wapitia.test.common.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.wapitia.common.test.junit.CompareTestData;

import org.junit.Test;

import java.util.regex.Pattern;

/**
 * Test class to test the testing class CompareTestData.
 * @author Corey Morgan
 *
 */
public class TestCompareTestData {

    /**
     * Test the Comment filtering functionality.
     */
    @Test
    public void testCommentRegEx() {
        Pattern commentPattern =
            Pattern.compile(CompareTestData.HASH_COMMENT_REGEX);
        assertTrue(commentPattern.matcher(" \t # Just any old stuff")
            .matches());
        assertTrue(commentPattern.matcher("# More anything").matches());
        assertTrue(commentPattern.matcher("#").matches());
        assertFalse(commentPattern.matcher("Shou#n't work").matches());
        assertFalse(commentPattern.matcher("").matches());
    }
}
