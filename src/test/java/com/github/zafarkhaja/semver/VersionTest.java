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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static com.github.zafarkhaja.semver.expr.CompositeExpression.Helper.gte;
import static com.github.zafarkhaja.semver.expr.CompositeExpression.Helper.lt;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Zafar Khaja {@literal <zafarkhaja@gmail.com>}
 */
public class VersionTest {

    @Nested
    class CoreFunctionalityTest {

        @Test
        public void mayHavePreReleaseFollowingPatchAppendedWithHyphen() {
            Version v = Version.parse("1.2.3-alpha");
            assertEquals("alpha", v.getPreReleaseVersion());
        }

        @Test
        public void preReleaseShouldHaveLowerPrecedenceThanAssociatedNormal() {
            Version v1 = Version.of(1, 3, 7);
            Version v2 = Version.of(1, 3, 7, "alpha");
            assertTrue(0 < v1.compareTo(v2));
            assertTrue(0 > v2.compareTo(v1));
        }

        @Test
        public void mayHaveBuildFollowingPatchOrPreReleaseAppendedWithPlus() {
            Version v = Version.parse("1.2.3+build");
            assertEquals("build", v.getBuildMetadata());
        }

        @Test
        public void shouldIgnoreBuildMetadataWhenDeterminingVersionPrecedence() {
            Version v1 = Version.of(1, 3, 7, "beta");
            Version v2 = Version.of(1, 3, 7, "beta", "build.1");
            Version v3 = Version.of(1, 3, 7, "beta", "build.2");
            assertTrue(0 == v1.compareTo(v2));
            assertTrue(0 == v1.compareTo(v3));
            assertTrue(0 == v2.compareTo(v3));
        }

        @Test
        public void shouldHaveGreaterThanMethodReturningBoolean() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = Version.of(1, 3, 7);
            assertTrue(v1.greaterThan(v2));
            assertFalse(v2.greaterThan(v1));
            assertFalse(v1.greaterThan(v1));
        }

        @Test
        public void shouldHaveGreaterThanOrEqualToMethodReturningBoolean() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = Version.of(1, 3, 7);
            assertTrue(v1.greaterThanOrEqualTo(v2));
            assertFalse(v2.greaterThanOrEqualTo(v1));
            assertTrue(v1.greaterThanOrEqualTo(v1));
        }

        @Test
        public void shouldHaveLessThanMethodReturningBoolean() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = Version.of(1, 3, 7);
            assertFalse(v1.lessThan(v2));
            assertTrue(v2.lessThan(v1));
            assertFalse(v1.lessThan(v1));
        }

        @Test
        public void shouldHaveLessThanOrEqualToMethodReturningBoolean() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = Version.of(1, 3, 7);
            assertFalse(v1.lessThanOrEqualTo(v2));
            assertTrue(v2.lessThanOrEqualTo(v1));
            assertTrue(v1.lessThanOrEqualTo(v1));
        }

        @Test
        public void shouldOverrideEqualsMethod() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = Version.of(2, 3, 7);
            Version v3 = Version.of(1, 3, 7);
            assertTrue(v1.equals(v1));
            assertTrue(v1.equals(v2));
            assertFalse(v1.equals(v3));
        }

        @Test
        public void shouldCorrectlyCompareAllVersionsFromSpecification() {
            String[] versions = {
                "1.0.0-alpha",
                "1.0.0-alpha.1",
                "1.0.0-alpha.beta",
                "1.0.0-beta",
                "1.0.0-beta.2",
                "1.0.0-beta.11",
                "1.0.0-rc.1",
                "1.0.0",
                "2.0.0",
                "2.1.0",
                "2.1.1"
            };
            for (int i = 1; i < versions.length; i++) {
                Version v1 = Version.parse(versions[i-1]);
                Version v2 = Version.parse(versions[i]);
                assertTrue(v1.lessThan(v2));
            }
        }

        @Test
        public void shouldHaveGetters() {
            Version v = Version.of(1, 0, 0, "rc.1", "build.1");
            assertEquals(1, v.getMajorVersion());
            assertEquals(0, v.getMinorVersion());
            assertEquals(0, v.getPatchVersion());
            assertEquals("1.0.0", v.getNormalVersion());
            assertEquals("rc.1", v.getPreReleaseVersion());
            assertEquals("build.1", v.getBuildMetadata());
        }

        @Test
        public void shouldProvideIncrementMajorVersionMethod() {
            Version v = Version.of(1, 2, 3);
            Version incrementedMajor = v.incrementMajorVersion();
            assertEquals("2.0.0", incrementedMajor.toString());
        }

        @Test
        public void shouldIncrementMajorVersionWithPreReleaseIfProvided() {
            Version v = Version.of(1, 2, 3);
            Version incrementedMajor = v.incrementMajorVersion("beta");
            assertEquals("2.0.0-beta", incrementedMajor.toString());
        }

        @Test
        public void shouldProvideIncrementMinorVersionMethod() {
            Version v = Version.of(1, 2, 3);
            Version incrementedMinor = v.incrementMinorVersion();
            assertEquals("1.3.0", incrementedMinor.toString());
        }

        @Test
        public void shouldIncrementMinorVersionWithPreReleaseIfProvided() {
            Version v = Version.of(1, 2, 3);
            Version incrementedMinor = v.incrementMinorVersion("alpha");
            assertEquals("1.3.0-alpha", incrementedMinor.toString());
        }

        @Test
        public void shouldProvideIncrementPatchVersionMethod() {
            Version v = Version.of(1, 2, 3);
            Version incrementedPatch = v.incrementPatchVersion();
            assertEquals("1.2.4", incrementedPatch.toString());
        }

        @Test
        public void shouldIncrementPatchVersionWithPreReleaseIfProvided() {
            Version v = Version.of(1, 2, 3);
            Version incrementedPatch = v.incrementPatchVersion("rc");
            assertEquals("1.2.4-rc", incrementedPatch.toString());
        }

        @Test
        public void shouldDropBuildMetadataWhenIncrementing() {
            Version v = Version.of(1, 2, 3, "alpha", "build");

            Version major1 = v.incrementMajorVersion();
            assertEquals("2.0.0", major1.toString());
            Version major2 = v.incrementMajorVersion("beta");
            assertEquals("2.0.0-beta", major2.toString());

            Version minor1 = v.incrementMinorVersion();
            assertEquals("1.3.0", minor1.toString());
            Version minor2 = v.incrementMinorVersion("beta");
            assertEquals("1.3.0-beta", minor2.toString());

            Version patch1 = v.incrementPatchVersion();
            assertEquals("1.2.4", patch1.toString());
            Version patch2 = v.incrementPatchVersion("beta");
            assertEquals("1.2.4-beta", patch2.toString());
        }

        @Test
        public void shouldProvideSetPreReleaseVersionMethod() {
            Version v1 = Version.of(1, 0, 0);
            Version v2 = v1.setPreReleaseVersion("alpha");
            assertEquals("1.0.0-alpha", v2.toString());
        }

        @Test
        public void shouldDropBuildMetadataWhenSettingPreReleaseVersion() {
            Version v1 = Version.of(1, 0, 0, "alpha", "build");
            Version v2 = v1.setPreReleaseVersion("beta");
            assertEquals("1.0.0-beta", v2.toString());
        }

        @Test
        public void shouldProvideSetBuildMetadataMethod() {
            Version v1 = Version.of(1, 0, 0);
            Version v2 = v1.setBuildMetadata("build");
            assertEquals("1.0.0+build", v2.toString());
        }

        @Test
        public void shouldProvideIncrementPreReleaseVersionMethod() {
            Version v1 = Version.of(1, 0, 0, "beta.1");
            Version v2 = v1.incrementPreReleaseVersion();
            assertEquals("1.0.0-beta.2", v2.toString());
        }

        @Test
        public void shouldThrowExceptionWhenIncrementingPreReleaseIfItsNull() {
            Version v1 = Version.of(1, 0, 0);
            try {
                v1.incrementPreReleaseVersion();
            } catch (NullPointerException e) {
                return;
            }
            fail("Method was expected to throw NullPointerException");
        }

        @Test
        public void shouldDropBuildMetadataWhenIncrementingPreReleaseVersion() {
            Version v1 = Version.of(1, 0, 0, "beta.1", "build");
            Version v2 = v1.incrementPreReleaseVersion();
            assertEquals("1.0.0-beta.2", v2.toString());
        }

        @Test
        public void shouldProvideIncrementBuildMetadataMethod() {
            Version v1 = Version.of(1, 0, 0, null, "build.1");
            Version v2 = v1.incrementBuildMetadata();
            assertEquals("1.0.0+build.2", v2.toString());
        }

        @Test
        public void shouldThrowExceptionWhenIncrementingBuildIfItsNull() {
            Version v1 = Version.of(1, 0, 0);
            try {
                v1.incrementBuildMetadata();
            } catch (NullPointerException e) {
                return;
            }
            fail("Method was expected to throw NullPointerException");
        }

        @Test
        public void shouldBeImmutable() {
            Version v = Version.of(1, 2, 3, "alpha.1", "build.1");

            assertNotEquals(v, v.incrementMajorVersion());
            assertNotEquals(v, v.incrementMinorVersion());
            assertNotEquals(v, v.incrementPatchVersion());
            assertNotEquals(v, v.incrementPreReleaseVersion());
            assertNotEquals(v, v.setPreReleaseVersion("alpha.2"));
            assertNotEquals(v.toString(), v.incrementBuildMetadata().toString());
            assertNotEquals(v.toString(), v.setBuildMetadata("build.2").toString());
        }

        @Test
        public void shouldBeAbleToCompareWithoutIgnoringBuildMetadata() {
            Version v1 = Version.of(1, 3, 7, "beta", "build.1");
            Version v2 = Version.of(1, 3, 7, "beta", "build.2");
            assertTrue(0 == v1.compareTo(v2));
            assertTrue(0 > v1.compareWithBuildsTo(v2));
        }

        @Test
        public void shouldCheckIfVersionSatisfiesExpression() {
            Version v = Version.of(2, 0, 0, "beta");
            assertTrue(v.satisfies(gte("1.0.0").and(lt("2.0.0"))));
            assertFalse(v.satisfies(gte("2.0.0").and(lt("3.0.0"))));
        }

        @Test
        public void shouldCheckIfMajorVersionCompatible() {
            Version v1 = Version.of(1, 0, 0);
            Version v2 = Version.of(1, 2, 3);
            Version v3 = Version.of(2, 0, 0);
            assertTrue(v1.isMajorVersionCompatible(v2));
            assertFalse(v1.isMajorVersionCompatible(v3));
        }

        @Test
        public void shouldCheckIfMinorVersionCompatible() {
            Version v1 = Version.of(1, 1, 1);
            Version v2 = Version.of(1, 1, 2);
            Version v3 = Version.of(1, 2, 3);
            assertTrue(v1.isMinorVersionCompatible(v2));
            assertFalse(v1.isMinorVersionCompatible(v3));
        }

        @Test
        public void shouldCheckVersionValidity() {
            assertTrue(Version.isValid("1.2.3-rc.1+abcdefg"));
            assertFalse(Version.isValid("1.2.3+rc.1+abcdefg"));
        }

        @Test
        public void shouldParseVersionsOptionallyIfValid() {
            assertTrue(Version.tryParse("1.2.3-rc.1+abcdefg").isPresent());
            assertFalse(Version.tryParse("1.2.3+rc.1+abcdefg").isPresent());
        }
    }

    @Nested
    class EqualsMethodTest {

        @Test
        public void shouldBeReflexive() {
            Version v1 = Version.of(2, 3, 7);
            assertTrue(v1.equals(v1));
        }

        @Test
        public void shouldBeSymmetric() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = Version.of(2, 3, 7);
            assertTrue(v1.equals(v2));
            assertTrue(v2.equals(v1));
        }

        @Test
        public void shouldBeTransitive() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = Version.of(2, 3, 7);
            Version v3 = Version.of(2, 3, 7);
            assertTrue(v1.equals(v2));
            assertTrue(v2.equals(v3));
            assertTrue(v1.equals(v3));
        }

        @Test
        public void shouldBeConsistent() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = Version.of(2, 3, 7);
            assertTrue(v1.equals(v2));
            assertTrue(v1.equals(v2));
            assertTrue(v1.equals(v2));
        }

        @Test
        public void shouldReturnFalseIfOtherVersionIsOfDifferentType() {
            Version v1 = Version.of(2, 3, 7);
            assertFalse(v1.equals(new String("2.3.7")));
        }

        @Test
        public void shouldReturnFalseIfOtherVersionIsNull() {
            Version v1 = Version.of(2, 3, 7);
            Version v2 = null;
            assertFalse(v1.equals(v2));
        }

        @Test
        public void shouldIgnoreBuildMetadataWhenCheckingForEquality() {
            Version v1 = Version.of(2, 3, 7, "beta", "build");
            Version v2 = Version.of(2, 3, 7, "beta");
            assertTrue(v1.equals(v2));
        }
    }

    @Nested
    class HashCodeMethodTest {

        @Test
        public void shouldReturnSameHashCodeIfVersionsAreEqual() {
            Version v1 = Version.of(2, 3, 7, "beta", "build");
            Version v2 = Version.of(2, 3, 7, "beta");
            assertTrue(v1.equals(v2));
            assertEquals(v1.hashCode(), v2.hashCode());
        }
    }

    @Nested
    class ToStringMethodTest {

        @Test
        public void shouldReturnStringRepresentation() {
            String value = "1.2.3-beta+build";
            Version v = Version.parse(value);
            assertEquals(value, v.toString());
        }
    }

    @Nested
    class BuilderTest {

        @Test
        public void shouldBuildVersionInSteps() {
            Version.Builder builder = new Version.Builder();
            builder.setNormalVersion("1.0.0");
            builder.setPreReleaseVersion("alpha");
            builder.setBuildMetadata("build");
            assertEquals(Version.of(1, 0, 0, "alpha", "build"), builder.build());
        }

        @Test
        public void shouldBuildVersionFromNormalVersion() {
            Version.Builder builder = new Version.Builder("1.0.0");
            assertEquals(Version.of(1, 0, 0), builder.build());
        }

        @Test
        public void shouldBuildVersionWithPreReleaseVersion() {
            Version.Builder builder = new Version.Builder("1.0.0");
            builder.setPreReleaseVersion("alpha");
            assertEquals(Version.of(1, 0, 0, "alpha"), builder.build());
        }

        @Test
        public void shouldBuildVersionWithBuildMetadata() {
            Version.Builder builder = new Version.Builder("1.0.0");
            builder.setBuildMetadata("build");
            assertEquals(Version.of(1, 0, 0, null, "build"), builder.build());
        }

        @Test
        public void shouldBuildVersionWithPreReleaseVersionAndBuildMetadata() {
            Version.Builder builder = new Version.Builder("1.0.0");
            builder.setPreReleaseVersion("alpha");
            builder.setBuildMetadata("build");
            assertEquals(Version.of(1, 0, 0, "alpha", "build"), builder.build());
        }

        @Test
        public void shouldImplementFluentInterface() {
            Version.Builder builder = new Version.Builder();
            Version version = builder
                .setNormalVersion("1.0.0")
                .setPreReleaseVersion("alpha")
                .setBuildMetadata("build")
                .build();
            assertEquals(Version.of(1, 0, 0, "alpha", "build"), version);
        }
    }

    @Nested
    class BuildAwareOrderTest {

        @Test
        public void shouldCorrectlyCompareAllVersionsWithBuildMetadata() {
            String[] versions = {
                "1.0.0-alpha",
                "1.0.0-alpha.1",
                "1.0.0-beta.2",
                "1.0.0-beta.11",
                "1.0.0-rc.1",
                "1.0.0-rc.1+build.1",
                "1.0.0",
                "1.0.0+0.3.7",
                "1.3.7+build",
                "1.3.7+build.2.b8f12d7",
                "1.3.7+build.11.e0f985a"
            };
            for (int i = 1; i < versions.length; i++) {
                Version v1 = Version.parse(versions[i-1]);
                Version v2 = Version.parse(versions[i]);
                assertTrue(0 > Version.BUILD_AWARE_ORDER.compare(v1, v2));
            }
        }
    }

    @Nested
    class SerializationTest {

        @Test
        public void shouldBeSerializable() throws Exception {
            Version v1 = Version.of(1, 2, 3, "alpha.1", "build.1");
            Version v2 = deserialize(serialize(v1));
            assertEquals(v1, v2);
        }

        private byte[] serialize(Version v) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(v);
            return baos.toByteArray();
        }

        private Version deserialize(byte[] bytes) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Version) ois.readObject();
        }
    }
}
