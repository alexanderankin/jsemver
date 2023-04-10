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

/**
 * The {@code NormalVersion} class represents the version core.
 * <p>
 * This class is immutable and hence thread-safe.
 *
 * @author Zafar Khaja {@literal <zafarkhaja@gmail.com>}
 * @since 0.2.0
 */
class NormalVersion implements Comparable<NormalVersion> {

    /**
     * The major version number.
     */
    private final long major;

    /**
     * The minor version number.
     */
    private final long minor;

    /**
     * The patch version number.
     */
    private final long patch;

    /**
     * Constructs a {@code NormalVersion} with the
     * major, minor and patch version numbers.
     *
     * @param major the major version number
     * @param minor the minor version number
     * @param patch the patch version number
     * @throws IllegalArgumentException if any of the arguments is a negative number
     */
    NormalVersion(long major, long minor, long patch) {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException(
                "Major, minor and patch versions MUST be non-negative integers."
            );
        }
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Returns the major version number.
     *
     * @return the major version number
     */
    long getMajor() {
        return major;
    }

    /**
     * Returns the minor version number.
     *
     * @return the minor version number
     */
    long getMinor() {
        return minor;
    }

    /**
     * Returns the patch version number.
     *
     * @return the patch version number
     */
    long getPatch() {
        return patch;
    }

    /**
     * Increments the major version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    NormalVersion incrementMajor() {
        return new NormalVersion(tryIncrement(major), 0, 0);
    }

    /**
     * Increments the minor version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    NormalVersion incrementMinor() {
        return new NormalVersion(major, tryIncrement(minor), 0);
    }

    /**
     * Increments the patch version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    NormalVersion incrementPatch() {
        return new NormalVersion(major, minor, tryIncrement(patch));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(NormalVersion other) {
        long result = major - other.major;
        if (result == 0) {
            result = minor - other.minor;
            if (result == 0) {
                result = patch - other.patch;
                if (result == 0) {
                    return 0;
                }
            }
        }
        return result < 0 ? -1 : 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NormalVersion)) {
            return false;
        }
        return compareTo((NormalVersion) other) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Long.hashCode(major);
        hash = 31 * hash + Long.hashCode(minor);
        hash = 31 * hash + Long.hashCode(patch);
        return hash;
    }

    /**
     * Returns the string representation of this normal version.
     * <p>
     * A normal version number MUST take the form X.Y.Z where X, Y, and Z are
     * non-negative integers. X is the major version, Y is the minor version,
     * and Z is the patch version. (SemVer p.2)
     *
     * @return the string representation of this normal version
     */
    @Override
    public String toString() {
        return String.format("%d.%d.%d", major, minor, patch);
    }

    private long tryIncrement(long l) {
        return Math.incrementExact(l);
    }
}
