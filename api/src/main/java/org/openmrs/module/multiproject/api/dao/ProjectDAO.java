/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.api.dao;

import org.openmrs.module.multiproject.Project;

import java.util.List;

public class ProjectDAO extends BaseMultiProjectOpenmrsMetadataDAO<Project> {
  ProjectDAO() {
    super(Project.class);
  }

  public Project getProject(Integer projectId) {
    return internalRead(projectId);
  }

  public Project getProject(String name) {
    return internalReadByName(name);
  }

  public Project getProjectByUuid(String uuid) {
    return internalReadByUuid(uuid);
  }

  public List<Project> getAllProjects(boolean includeRetired) {
    return internalReadAll(includeRetired);
  }

  public long getProjectCount(boolean includeRetired) {
    return internalCountAll(includeRetired);
  }

  public List<Project> getProjectByNameFragment(String nameFragment) {
    return internalReadByNameFragment(nameFragment);
  }

  public Project saveProject(Project project) {
    return internalSave(project);
  }

  public void deleteProject(Project project) {
    internalDelete(project);
  }
}
