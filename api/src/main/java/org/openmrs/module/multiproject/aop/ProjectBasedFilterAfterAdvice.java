/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.aop;

import org.openmrs.module.multiproject.filter.ProjectBasedFilter;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Filters result of a service call based on current's user project. <br>
 * Example:
 *
 * <pre>
 *   Context.addAdvice(
 *     FlagService.class,
 *       new ProjectBasedFilterAfterAdvice<>(
 *         new ProjectAssignmentProjectBasedFilter<>(Flag.class, Flag::getUuid),
 *         FlagService.class.getMethod("generateFlagsForPatient", Patient.class)));
 * </pre>
 *
 * <pre>
 *   Context.addAdvice(
 *     AppFrameworkService.class,
 *       new ProjectBasedFilterAfterAdvice<>(
 *         new NameSuffixProjectBasedFilter<>(
 *           new NameAndProjectSlugSuffixGetter<>(Extension::getId)),
 *         AppFrameworkService.class.getMethod("getExtensionsForCurrentUser"),
 *         AppFrameworkService.class.getMethod("getExtensionsForCurrentUser", String.class),
 *         AppFrameworkService.class.getMethod("getExtensionsForCurrentUser", String.class, AppContextModel.class)));
 * </pre>
 *
 * @param <R> the result item to handle, service method must return Collection or R
 */
public class ProjectBasedFilterAfterAdvice<R> implements AfterReturningAdvice {

  private final ProjectBasedFilter<R> projectBasedFilter;
  private final Set<Method> wrappedMethods;

  public ProjectBasedFilterAfterAdvice(
      ProjectBasedFilter<R> projectBasedFilter, Method... methods) {
    this.projectBasedFilter = projectBasedFilter;
    this.wrappedMethods = new HashSet<>(Arrays.asList(methods));
  }

  @Override
  public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
    if (!wrappedMethods.contains(method)) {
      return;
    }

    if (!(returnValue instanceof Collection)) {
      throw new IllegalStateException(
          "ProjectBasedSlugFilterAfterAdvice used with method which doesn't return a "
              + "Collection-type result. Method: "
              + method.getName());
    }

    projectBasedFilter.doFilter((Collection<R>) returnValue);
  }
}
