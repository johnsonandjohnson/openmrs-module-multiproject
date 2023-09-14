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
import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.ProjectAssignment;
import org.openmrs.module.multiproject.api.service.ProjectAssignmentService;
import org.openmrs.module.multiproject.api.service.ProjectService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The ProjectAssignmentProjectBasedFilter class is an implantation of {@link ProjectBasedFilter}
 * which determines the object's Project based on {@link ProjectAssignment} entity. An object is
 * considered as assigned to Project only if there is ProjectAssignment instance with the object's
 * type and it's unique id. An object which is not assigned to any Project, is always included. The
 * property with ID is configurable, but for most OpenMRS entities it should usually be an UUID.
 *
 * <p>This implementation allows to have different type of items to filter and different type for
 * assignment determination. It's useful, when you make assignment on entity level (ex.: assign
 * VisitType to Project) but want to filter a collection of DAO objects (ex.: filter a list of
 * VisitTypeDAO).
 *
 * <p>Following example shows an execution of this filter on a collection of VisitTypeDAO objects,
 * but the assignment is determined by VisitType assignment:
 *
 * <pre>
 * new ProjectAssignmentProjectBasedFilter<>(VisitType.class, VisitTypeDAO::getUuid).doFilter(myVisitTypeDAOs);
 * </pre>
 *
 * @param <R> the type of object to filter
 */
public class ProjectAssignmentProjectBasedFilter<R> implements ProjectBasedFilter<R> {
  private final Class<?> assignmentClass;
  private final Function<R, String> idGetter;
  private final CurrentProjectProvider currentProjectProvider;

  public ProjectAssignmentProjectBasedFilter(
      Class<?> assignmentClass, Function<R, String> idGetter) {
    this(Context.getService(ProjectService.class), assignmentClass, idGetter);
  }

  public ProjectAssignmentProjectBasedFilter(
      CurrentProjectProvider currentProjectProvider,
      Class<?> assignmentClass,
      Function<R, String> idGetter) {
    this.currentProjectProvider = currentProjectProvider;
    this.assignmentClass = assignmentClass;
    this.idGetter = idGetter;
  }

  @Override
  public void doFilter(Collection<R> itemsToFilter) throws APIException {
    if (currentProjectProvider.isCurrentUserProjectSet()) {
      filterProjectBased(itemsToFilter);
    }
  }

  private void filterProjectBased(Collection<R> results) {
    final Optional<Project> currentProject = currentProjectProvider.getCurrentUserProject();

    final ProjectAssignmentService projectAssignmentService =
        Context.getService(ProjectAssignmentService.class);

    List<ProjectAssignment> assignmentsByObject =
        projectAssignmentService.getProjectAssignmentsForObject(assignmentClass);

    List<ProjectAssignment> assignmentsByClassAndProject = new ArrayList<>();
    if (currentProject.isPresent()) {
      assignmentsByClassAndProject =
          assignmentsByObject.stream()
              .filter(assignment -> assignment.getProject().equals(currentProject.get()))
              .collect(Collectors.toList());
    }

    List<String> assignmentsByClassAndProjectObjectIDs =
        assignmentsByClassAndProject.stream()
            .map(ProjectAssignment::getObjectId)
            .collect(Collectors.toList());

    final Iterator<R> resultIterator = results.iterator();
    while (resultIterator.hasNext()) {
      final R resultItem = resultIterator.next();
      final String itemId = idGetter.apply(resultItem);

      if (!assignmentsByClassAndProjectObjectIDs.contains(itemId)) {
        List<String> assignmentsByObjectIDs =
            assignmentsByObject.stream()
                .map(ProjectAssignment::getObjectId)
                .collect(Collectors.toList());
        if (assignmentsByObjectIDs.contains(itemId)) {
          resultIterator.remove();
        }
      }
    }
  }
}
