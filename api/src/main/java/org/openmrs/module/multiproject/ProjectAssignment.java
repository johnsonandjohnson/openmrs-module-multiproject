/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject;

import org.openmrs.BaseOpenmrsData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** The ProjectAssignment entity is used to assign any identifiable data object to Project. */
@Entity(name = "multiproject.ProjectAssignment")
@Table(name = "multiproject_project_assignment")
public class ProjectAssignment extends BaseOpenmrsData {
  private static final long serialVersionUID = -5695777704332709494L;

  @Id
  @GeneratedValue
  @Column(name = "project_assignment_id")
  private Integer projectAssignmentId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @Column(name = "object_class", nullable = false)
  private String objectClass;

  @Column(name = "object_id", nullable = false, length = 512)
  private String objectId;

  public ProjectAssignment() {}

  @Override
  public Integer getId() {
    return projectAssignmentId;
  }

  @Override
  public void setId(Integer id) {
    this.projectAssignmentId = id;
  }

  public Integer getProjectAssignmentId() {
    return projectAssignmentId;
  }

  public void setProjectAssignmentId(Integer projectAssignmentId) {
    this.projectAssignmentId = projectAssignmentId;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public String getObjectClass() {
    return objectClass;
  }

  public void setObjectClass(String objectClass) {
    this.objectClass = objectClass;
  }

  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }
}
