/*
 * The MIT License
 *
 * Copyright 2012-2023 Zafar Khaja <zafarkhaja@gmail.com>.
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
import com.github.zafarkhaja.semver.util.UnexpectedElementException;
import java.util.Arrays;

/**
 * Thrown when attempting to consume a character of unexpected types.
 * <p>
 * This exception is a wrapper exception extending {@code ParseException}.
 *
 * @author Zafar Khaja {@literal <zafarkhaja@gmail.com>}
 * @since 0.8.0
 */
public class UnexpectedCharacterException extends ParseException {

    private static final long serialVersionUID = 0L;

    /**
     * The unexpected character.
     */
    private final Character unexpected;

    /**
     * The position of the unexpected character.
     */
    private final int position;

    /**
     * The array of expected character types.
     */
    private final CharType[] expected;

    /**
     * Constructs a {@code UnexpectedCharacterException} instance with
     * the wrapped {@code UnexpectedElementException} exception.
     *
     * @param cause the wrapped exception
     */
    UnexpectedCharacterException(UnexpectedElementException cause) {
        this(
            (Character) cause.getUnexpectedElement(),
            cause.getPosition(),
            (CharType[]) cause.getExpectedElementTypes()
        );
    }

    /**
     * Constructs a {@code UnexpectedCharacterException} instance
     * with the unexpected character, its position and the expected types.
     *
     * @param unexpected the unexpected character
     * @param position the position of the unexpected character
     * @param expected an array of the expected character types
     */
    UnexpectedCharacterException(Character unexpected, int position, CharType... expected) {
        super(createMessage(unexpected, position, expected));

        this.unexpected = unexpected;
        this.position   = position;
        this.expected   = expected;
    }

    /**
     * Gets the unexpected character.
     *
     * @return the unexpected character
     */
    Character getUnexpectedCharacter() {
        return unexpected;
    }

    /**
     * Gets the position of the unexpected character.
     *
     * @return the position of the unexpected character
     */
    int getPosition() {
        return position;
    }

    /**
     * Gets the expected character types.
     *
     * @return an array of expected character types
     */
    CharType[] getExpectedCharTypes() {
        return expected;
    }

    /**
     * Returns the string representation of this exception
     * containing the information about the unexpected
     * element and, if available, about the expected types.
     *
     * @return the string representation of this exception
     */
    @Override
    public String toString() {
        return getMessage();
    }

    private static String createMessage(Character unexpected, int position, CharType... expected) {
        String msg = String.format(
            "Unexpected character %s(%s) at position %d",
            CharType.forCharacter(unexpected),
            unexpected,
            position
        );
        if (expected.length > 0) {
            msg += String.format(", expecting %s", Arrays.toString(expected));
        }
        return msg;
    }
}
