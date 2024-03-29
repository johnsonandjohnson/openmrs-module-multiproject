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

import org.hibernate.criterion.Restrictions;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.ProjectAssignment;

import java.util.List;

public class ProjectAssignmentDAO extends BaseMultiProjectOpenmrsObjectDAO<ProjectAssignment> {

  ProjectAssignmentDAO() {
    super(ProjectAssignment.class, "voided");
  }

  public ProjectAssignment getProjectAssignment(Integer projectAssignmentId) {
    return internalRead(projectAssignmentId);
  }

  public ProjectAssignment getProjectAssignmentByUuid(String uuid) {
    return internalReadByUuid(uuid);
  }

  public List<ProjectAssignment> getAllProjectAssignments(boolean includeRetired) {
    return internalReadAll(includeRetired);
  }

  public List<ProjectAssignment> getProjectAssignmentsForObjectClass(String objectClass) {
    return getSession()
        .createCriteria(ProjectAssignment.class)
        .add(Restrictions.eq("voided", Boolean.FALSE))
        .add(Restrictions.eq("objectClass", objectClass))
        .list();
  }

  public List<ProjectAssignment> getProjectAssignmentsForObjectAndProject(String objectClass, Project project) {
    return getSession()
        .createCriteria(ProjectAssignment.class)
        .add(Restrictions.eq("voided", Boolean.FALSE))
        .add(Restrictions.eq("objectClass", objectClass))
        .add(Restrictions.eq("project", project))
        .list();
  }

  public long getProjectAssignmentCount(boolean includeRetired) {
    return internalCountAll(includeRetired);
  }

  public ProjectAssignment saveProjectAssignment(ProjectAssignment projectAssignment) {
    return internalSave(projectAssignment);
  }

  public void deleteProjectAssignment(ProjectAssignment projectAssignment) {
    internalDelete(projectAssignment);
  }
}
