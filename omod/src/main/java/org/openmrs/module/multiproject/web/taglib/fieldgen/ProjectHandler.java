/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.web.taglib.fieldgen;

import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.ProjectService;

import java.util.List;

/**
 * The ProjectHandler Class.
 *
 * <p>This is handler used to initialize model for 'OpenMRS field gen' fields. These inputs are
 * dynamically created depending on the type of model data, e.g.: for inputs for attribute values.
 *
 * @see fieldGen/project.jsp
 */
public class ProjectHandler extends BaseFieldGenHandler {
  /**
   * This path is a workaround!
   *
   * <p>OpenMRS doesn't copy the license.field in the correct place in WEB-INF. Instead in
   * ./fieldGen/module/multiproject/*, it's in ./module/multiproject/fieldGen/*.
   */
  private static final String DEFAULT_URL = "../module/multiproject/fieldGen/project.field";

  public ProjectHandler() {
    super(DEFAULT_URL);
  }

  @Override
  public void loadAdditionalData() {
    final ProjectService projectService = Context.getService(ProjectService.class);
    final List<Project> projects = projectService.getAllProjects();
    setParameter("projects", projects);
  }
}
