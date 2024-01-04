/*
 * The MIT License
 *
 * Copyright 2012-2024 Zafar Khaja <zafarkhaja@gmail.com>.
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
package com.github.zafarkhaja.semver;

import com.github.zafarkhaja.semver.VersionParser.CharType;
import org.junit.jupiter.api.Test;
import static com.github.zafarkhaja.semver.VersionParser.CharType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Zafar Khaja {@literal <zafarkhaja@gmail.com>}
 */
class VersionParserCharTypeTest {

    @Test
    void shouldBeMatchedByDigit() {
        assertTrue(DIGIT.isMatchedBy('0'));
        assertTrue(DIGIT.isMatchedBy('9'));
        assertFalse(DIGIT.isMatchedBy('a'));
        assertFalse(DIGIT.isMatchedBy('A'));
    }

    @Test
    void shouldBeMatchedByLetter() {
        assertTrue(LETTER.isMatchedBy('a'));
        assertTrue(LETTER.isMatchedBy('A'));
        assertFalse(LETTER.isMatchedBy('0'));
        assertFalse(LETTER.isMatchedBy('9'));
    }

    @Test
    void shouldBeMatchedByDot() {
        assertTrue(DOT.isMatchedBy('.'));
        assertFalse(DOT.isMatchedBy('-'));
        assertFalse(DOT.isMatchedBy('0'));
        assertFalse(DOT.isMatchedBy('9'));
    }

    @Test
    void shouldBeMatchedByHyphen() {
        assertTrue(HYPHEN.isMatchedBy('-'));
        assertFalse(HYPHEN.isMatchedBy('+'));
        assertFalse(HYPHEN.isMatchedBy('a'));
        assertFalse(HYPHEN.isMatchedBy('0'));
    }

    @Test
    void shouldBeMatchedByPlus() {
        assertTrue(PLUS.isMatchedBy('+'));
        assertFalse(PLUS.isMatchedBy('-'));
        assertFalse(PLUS.isMatchedBy('a'));
        assertFalse(PLUS.isMatchedBy('0'));
    }

    @Test
    void shouldBeMatchedByEol() {
        assertTrue(EOI.isMatchedBy(null));
        assertFalse(EOI.isMatchedBy('-'));
        assertFalse(EOI.isMatchedBy('a'));
        assertFalse(EOI.isMatchedBy('0'));
    }

    @Test
    void shouldBeMatchedByIllegal() {
        assertTrue(ILLEGAL.isMatchedBy('!'));
        assertFalse(ILLEGAL.isMatchedBy('-'));
        assertFalse(ILLEGAL.isMatchedBy('a'));
        assertFalse(ILLEGAL.isMatchedBy('0'));
    }

    @Test
    void shouldReturnCharTypeForCharacter() {
        assertEquals(DIGIT,   CharType.forCharacter('1'));
        assertEquals(LETTER,  CharType.forCharacter('a'));
        assertEquals(DOT,     CharType.forCharacter('.'));
        assertEquals(HYPHEN,  CharType.forCharacter('-'));
        assertEquals(PLUS,    CharType.forCharacter('+'));
        assertEquals(EOI,     CharType.forCharacter(null));
        assertEquals(ILLEGAL, CharType.forCharacter('!'));
    }
}
