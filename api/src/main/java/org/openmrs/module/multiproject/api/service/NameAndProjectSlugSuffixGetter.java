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

import java.util.function.Function;

/**
 * Utility class to extract project name as a name-like identifier's suffix.
 *
 * @param <O> the object to get a name-like identifier
 */
public final class NameAndProjectSlugSuffixGetter<O> implements Function<O, NameAndProjectSlug> {
  private final Function<O, String> fullNameGetter;

  public NameAndProjectSlugSuffixGetter(Function<O, String> fullNameGetter) {
    this.fullNameGetter = fullNameGetter;
  }

  @Override
  public NameAndProjectSlug apply(O object) {
    final String fullName = fullNameGetter.apply(object);
    final int lastDotIdx = fullName.lastIndexOf('.');

    if (lastDotIdx != -1 && fullName.length() > lastDotIdx + 1) {
      return new NameAndProjectSlug(
          fullName, fullName.substring(0, lastDotIdx), fullName.substring(lastDotIdx + 1));
    }

    return new NameAndProjectSlug(fullName, fullName, null);
  }
}
