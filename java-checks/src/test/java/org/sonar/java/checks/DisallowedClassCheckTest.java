/*
 * SonarQube Java
 * Copyright (C) 2012-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.AnalysisException;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DisallowedClassCheckTest {

  @Test
  public void check() {
    DisallowedClassCheck visitor = new DisallowedClassCheck();
    visitor.disallowedClass = "java.lang.String";
    JavaCheckVerifier.newVerifier()
      .onFile("src/test/files/checks/DisallowedClassCheck.java")
      .withCheck(visitor)
      .verifyIssues();
    JavaCheckVerifier.newVerifier()
      .onFile("src/test/files/checks/DisallowedClassCheck.java")
      .withCheck(visitor)
      .withoutSemantic()
      .verifyNoIssues();
  }

  @Test
  public void check_annotation() {
    DisallowedClassCheck visitor = new DisallowedClassCheck();
    visitor.disallowedClass = "org.foo.MyAnnotation";
    JavaCheckVerifier.newVerifier()
      .onFile("src/test/files/checks/DisallowedClassCheckAnnotation.java")
      .withCheck(visitor)
      .verifyIssues();
    JavaCheckVerifier.newVerifier()
      .onFile("src/test/files/checks/DisallowedClassCheckAnnotation.java")
      .withCheck(visitor)
      .withoutSemantic()
      .verifyNoIssues();
  }

  @Test
  public void checkRegex() {
    DisallowedClassCheck visitor = new DisallowedClassCheck();
    visitor.disallowedClass = "java.lang\\..*";
    JavaCheckVerifier.newVerifier()
      .onFile("src/test/files/checks/DisallowedClassCheckRegex.java")
      .withCheck(visitor)
      .verifyIssues();
  }

  @Test
  public void checkBadRegex() {
    DisallowedClassCheck visitor = new DisallowedClassCheck();
    // bad regex
    visitor.disallowedClass = "java.lang(";

    AnalysisException e = assertThrows(AnalysisException.class,
      () -> JavaCheckVerifier.newVerifier().onFile("src/test/files/checks/DisallowedClassCheckRegex.java").withCheck(visitor).verifyIssues());
    assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
  }
}
