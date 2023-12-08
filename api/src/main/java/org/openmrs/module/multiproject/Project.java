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

import org.apache.commons.lang3.StringUtils;
import org.openmrs.BaseOpenmrsMetadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/** The Project entity is used to configure projects in the system. */
@Entity(name = "multiproject.Project")
@Table(name = "multiproject_project")
public class Project extends BaseOpenmrsMetadata {
  private static final long serialVersionUID = -8644940243883631180L;

  @Id
  @GeneratedValue
  @Column(name = "project_id")
  private Integer projectId;

  @Column(name = "slug", nullable = false)
  private String slug;

  @Override
  public Integer getId() {
    return projectId;
  }

  @Override
  public void setId(Integer id) {
    this.projectId = id;
  }

  public Integer getProjectId() {
    return projectId;
  }

  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getDisplayString() {
    if (!StringUtils.isEmpty(getName())) {
      return getName();
    } else {
      return getUuid();
    }
  }

  @Override
  public String toString() {
    // We need it here, because user's context serialization in REST interfaces
    // User's context is used by FE to get the project's slug, so we want it visible when OpenMRS
    // serializes a value of user's LocationAttribute with the Project
    return getSlug();
  }
}
