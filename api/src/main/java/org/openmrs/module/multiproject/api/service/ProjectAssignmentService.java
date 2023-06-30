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

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.ProjectAssignment;
import org.openmrs.module.multiproject.api.constant.PrivilegeConstants;
import org.openmrs.module.multiproject.api.dao.ProjectAssignmentDAO;

import java.util.List;

/**
 * The ProjectAssignment service is a default OpenMRS-style service for managing ProjectAssignment
 * entity.
 *
 * <p>The ProjectAssignment entity is used to assign any identifiable data object to a Project.
 */
public interface ProjectAssignmentService extends OpenmrsService {

  void setProjectAssignmentDAO(ProjectAssignmentDAO projectAssignmentDAO);

  ProjectAssignment getProjectAssignment(Integer projectAssignmentId) throws APIException;

  ProjectAssignment getProjectAssignmentByUuid(String uuid) throws APIException;

  List<ProjectAssignment> getAllProjectAssignments() throws APIException;

  List<ProjectAssignment> getAllProjectAssignments(boolean includeRetired) throws APIException;

  List<ProjectAssignment> getProjectAssignmentsForObject(Class<?> objectClass);

  List<ProjectAssignment> getProjectAssignmentsForObjectAndProject(Class<?> objectClass, Project project);

  long getProjectAssignmentCount(boolean includeRetired) throws APIException;

  @Authorized(PrivilegeConstants.MANAGE_PROJECT_ASSIGNMENTS)
  ProjectAssignment saveProjectAssignment(ProjectAssignment projectAssignment) throws APIException;

  @Authorized(PrivilegeConstants.MANAGE_PROJECT_ASSIGNMENTS)
  ProjectAssignment voidProjectAssignment(ProjectAssignment projectAssignment, String reason)
      throws APIException;

  @Authorized(PrivilegeConstants.MANAGE_PROJECT_ASSIGNMENTS)
  ProjectAssignment unvoidProjectAssignment(ProjectAssignment projectAssignment)
      throws APIException;

  @Authorized(PrivilegeConstants.PURGE_PROJECT_ASSIGNMENTS)
  void purgeProjectAssignment(ProjectAssignment projectAssignment) throws APIException;
}
