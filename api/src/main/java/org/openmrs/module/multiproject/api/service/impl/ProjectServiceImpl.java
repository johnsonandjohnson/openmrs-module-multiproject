/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.LocationAttribute;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.ProjectService;
import org.openmrs.module.multiproject.api.constant.LocationAttributeConstants;
import org.openmrs.module.multiproject.api.dao.ProjectDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ProjectServiceImpl extends BaseOpenmrsService implements ProjectService {
  private ProjectDAO projectDAO;

  @Override
  public void setProjectDAO(ProjectDAO projectDAO) {
    this.projectDAO = projectDAO;
  }

  @Transactional(readOnly = true)
  @Override
  public boolean isCurrentUserProjectSet() {
    return !Daemon.isDaemonThread() && getCurrentUserProject().isPresent();
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<Project> getCurrentUserProject() throws APIException {
    if (Context.getUserContext().getLocation() == null) {
      return Optional.empty();
    }

    final Optional<LocationAttribute> locationProjectAttribute =
        Context.getUserContext().getLocation().getActiveAttributes().stream()
            .filter(
                attribute ->
                    StringUtils.equalsIgnoreCase(
                        attribute.getAttributeType().getName(),
                        LocationAttributeConstants.PROJECT_LOCATION_ATTRIBUTE_TYPE_NAME))
            .findFirst();

    return locationProjectAttribute.map(
        attribute -> {
          final Object value = attribute.getValue();

          if (value instanceof String) {
            return getProjectByUuid((String) value);
          } else if (value instanceof Project) {
            return (Project) value;
          } else {
            return null;
          }
        });
  }

  @Transactional(readOnly = true)
  @Override
  public Project getProject(Integer projectId) throws APIException {
    return projectDAO.getProject(projectId);
  }

  @Transactional(readOnly = true)
  @Override
  public Project getProject(String name) throws APIException {
    return projectDAO.getProject(name);
  }

  @Transactional(readOnly = true)
  @Override
  public Project getProjectByUuid(String uuid) throws APIException {
    return projectDAO.getProjectByUuid(uuid);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Project> getAllProjects() throws APIException {
    return projectDAO.getAllProjects(false);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Project> getAllProjects(boolean includeRetired) throws APIException {
    return projectDAO.getAllProjects(includeRetired);
  }

  @Transactional(readOnly = true)
  @Override
  public long getProjectCount(boolean includeRetired) throws APIException {
    return projectDAO.getProjectCount(includeRetired);
  }

  @Transactional
  @Override
  public Project saveProject(Project project) throws APIException {
    return projectDAO.saveProject(project);
  }

  @Transactional
  @Override
  public Project retireProject(Project project, String reason) throws APIException {
    // Fields are set by org.openmrs.aop.RequiredDataAdvice
    return projectDAO.saveProject(project);
  }

  @Transactional
  @Override
  public Project unretireProject(Project project) throws APIException {
    // Fields are set by org.openmrs.aop.RequiredDataAdvice
    return projectDAO.saveProject(project);
  }

  @Transactional
  @Override
  public void purgeProject(Project project) throws APIException {
    projectDAO.deleteProject(project);
  }
}
