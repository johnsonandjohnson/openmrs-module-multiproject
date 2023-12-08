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

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.multiproject.ProjectAssignment;
import org.openmrs.module.multiproject.api.service.ProjectAssignmentService;
import org.openmrs.module.multiproject.api.dao.ProjectAssignmentDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ProjectAssignmentServiceImpl extends BaseOpenmrsService
    implements ProjectAssignmentService {
  private ProjectAssignmentDAO projectAssignmentDAO;

  @Override
  public void setProjectAssignmentDAO(ProjectAssignmentDAO projectAssignmentDAO) {
    this.projectAssignmentDAO = projectAssignmentDAO;
  }

  @Transactional(readOnly = true)
  @Override
  public ProjectAssignment getProjectAssignment(Integer projectAssignmentId) throws APIException {
    return projectAssignmentDAO.getProjectAssignment(projectAssignmentId);
  }

  @Transactional(readOnly = true)
  @Override
  public ProjectAssignment getProjectAssignmentByUuid(String uuid) throws APIException {
    return projectAssignmentDAO.getProjectAssignmentByUuid(uuid);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProjectAssignment> getAllProjectAssignments() throws APIException {
    return projectAssignmentDAO.getAllProjectAssignments(false);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProjectAssignment> getAllProjectAssignments(boolean includeRetired)
      throws APIException {
    return projectAssignmentDAO.getAllProjectAssignments(includeRetired);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProjectAssignment> getProjectAssignmentsForObject(Class<?> objectClass) {
    return projectAssignmentDAO.getProjectAssignmentsForObjectClass(objectClass.getName());
  }

  @Transactional(readOnly = true)
  @Override
  public long getProjectAssignmentCount(boolean includeRetired) throws APIException {
    return projectAssignmentDAO.getProjectAssignmentCount(includeRetired);
  }

  @Transactional
  @Override
  public ProjectAssignment saveProjectAssignment(ProjectAssignment projectAssignment)
      throws APIException {
    return projectAssignmentDAO.saveProjectAssignment(projectAssignment);
  }

  @Transactional
  @Override
  public ProjectAssignment voidProjectAssignment(ProjectAssignment projectAssignment, String reason)
      throws APIException {
    // Fields are set by org.openmrs.aop.RequiredDataAdvice
    return projectAssignmentDAO.saveProjectAssignment(projectAssignment);
  }

  @Transactional
  @Override
  public ProjectAssignment unvoidProjectAssignment(ProjectAssignment projectAssignment)
      throws APIException {
    // Fields are set by org.openmrs.aop.RequiredDataAdvice
    return projectAssignmentDAO.saveProjectAssignment(projectAssignment);
  }

  @Transactional
  @Override
  public void purgeProjectAssignment(ProjectAssignment projectAssignment) throws APIException {
    projectAssignmentDAO.deleteProjectAssignment(projectAssignment);
  }
}
