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

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.NameAndProjectSlug;
import org.openmrs.module.multiproject.api.service.ProjectService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The NameSuffixProjectBasedFilter class is an implantation of {@link ProjectBasedFilter} which
 * determines the object's Project based on it's name-like property suffix (or any other part of the
 * String-like property), which contains a Project's slug. The property getter is configurable.
 *
 * <p>Following example shows an execution of this filter on a collection of Extension objects. The
 * {@link org.openmrs.module.multiproject.api.service.NameAndProjectSlugSuffixGetter} utility is
 * used to configure that the suffix of an Extension's id property must be used to determine
 * Extension's project:
 *
 * <pre>
 * new NameSuffixProjectBasedFilter<>(
 *   new NameAndProjectSlugSuffixGetter<>(Extension::getId)
 * ).doFilter(myCollectionOfExtension);
 * </pre>
 *
 * @param <R> the type of object to filter
 */
public class NameSuffixProjectBasedFilter<R> implements ProjectBasedFilter<R> {
  private final Function<R, NameAndProjectSlug> nameAndSlugGetter;

  public NameSuffixProjectBasedFilter(Function<R, NameAndProjectSlug> nameAndSlugGetter) {
    this.nameAndSlugGetter = nameAndSlugGetter;
  }

  @Override
  public void doFilter(Collection<R> itemsToFilter) throws APIException {
    final ProjectService projectService = Context.getService(ProjectService.class);

    if (projectService.isCurrentUserProjectSet()) {
      final Set<String> selectedProjectBasedItems = filterProjectBased(itemsToFilter);
      removeDefaultValues(itemsToFilter, selectedProjectBasedItems);
    }
  }

  private Set<String> filterProjectBased(Collection<R> results) {
    final ProjectService projectService = Context.getService(ProjectService.class);
    final Set<String> allProjectSlugs =
        projectService.getAllProjects().stream().map(Project::getSlug).collect(Collectors.toSet());
    final Optional<Project> currentProject = projectService.getCurrentUserProject();

    final Set<String> selectedProjectBasedItems = new HashSet<>();

    final Iterator<R> resultIterator = results.iterator();
    while (resultIterator.hasNext()) {
      final R resultItem = resultIterator.next();
      final NameAndProjectSlug nameAndProjectSlug = nameAndSlugGetter.apply(resultItem);

      if (StringUtils.isBlank(nameAndProjectSlug.getProjectSlug())
          || !allProjectSlugs.contains(nameAndProjectSlug.getProjectSlug())) {
        continue;
      }

      if (currentProject.isPresent()
          && currentProject.get().getSlug().equals(nameAndProjectSlug.getProjectSlug())) {
        selectedProjectBasedItems.add(nameAndProjectSlug.getName());
      } else {
        resultIterator.remove();
      }
    }

    return selectedProjectBasedItems;
  }

  private void removeDefaultValues(Collection<R> results, Set<String> selectedProjectBasedItems) {
    final Iterator<R> resultIterator = results.iterator();
    while (resultIterator.hasNext()) {
      final R resultItem = resultIterator.next();
      final NameAndProjectSlug nameAndProjectSlug = nameAndSlugGetter.apply(resultItem);

      if (selectedProjectBasedItems.contains(nameAndProjectSlug.getFullName())) {
        resultIterator.remove();
      }
    }
  }
}
