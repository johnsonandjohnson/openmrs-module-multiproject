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
import org.openmrs.module.multiproject.api.constant.PrivilegeConstants;
import org.openmrs.module.multiproject.api.dao.ProjectDAO;
import org.openmrs.module.multiproject.filter.CurrentProjectProvider;

import java.util.List;

/**
 * The ProjectService service is a default OpenMRS-style service for managing Project entity.
 *
 * <p>The Project entity is used to configure available projects in the system.
 */
public interface ProjectService extends OpenmrsService, CurrentProjectProvider {
  void setProjectDAO(ProjectDAO projectDAO);

  Project getProject(Integer projectId) throws APIException;

  Project getProject(String name) throws APIException;

  Project getProjectByUuid(String uuid) throws APIException;

  List<Project> getAllProjects() throws APIException;

  List<Project> getAllProjects(boolean includeRetired) throws APIException;

  long getProjectCount(boolean includeRetired) throws APIException;

  @Authorized(PrivilegeConstants.MANAGE_PROJECTS)
  Project saveProject(Project project) throws APIException;

  @Authorized(PrivilegeConstants.MANAGE_PROJECTS)
  Project retireProject(Project project, String reason) throws APIException;

  @Authorized(PrivilegeConstants.MANAGE_PROJECTS)
  Project unretireProject(Project project) throws APIException;

  @Authorized(PrivilegeConstants.PURGE_PROJECTS)
  void purgeProject(Project project) throws APIException;
}
