/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.api.service;

import java.util.Objects;

public class NameAndProjectSlug {
  private final String fullName;
  private final String name;
  private final String projectSlug;

  public NameAndProjectSlug(String fullName, String name, String projectSlug) {
    this.fullName = fullName;
    this.name = name;
    this.projectSlug = projectSlug;
  }

  public String getFullName() {
    return fullName;
  }

  public String getName() {
    return name;
  }

  public String getProjectSlug() {
    return projectSlug;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NameAndProjectSlug that = (NameAndProjectSlug) o;
    return Objects.equals(getName(), that.getName())
        && Objects.equals(getProjectSlug(), that.getProjectSlug());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getProjectSlug());
  }
}
