/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.filter;

import org.openmrs.api.APIException;

import java.util.Collection;

/**
 * The ProjectBasedFilter implementations are objects which filter a collection of objects based on
 * the object's assigned project. The original collection is changed, so it contains items which are
 * assigned to the current user's Project or or are not assigned to any other Project.
 *
 * <p>If a current user's project can't be determined, either because user has no Project assigned
 * or there is no user, no filtering is performed.
 *
 * <p>The exact detail of how it's determined that an object is assigned to Project depends on the
 * implementation.
 *
 * @param <R> the type of object to filter
 */
public interface ProjectBasedFilter<R> {
  void doFilter(Collection<R> itemsToFilter) throws APIException;
}
