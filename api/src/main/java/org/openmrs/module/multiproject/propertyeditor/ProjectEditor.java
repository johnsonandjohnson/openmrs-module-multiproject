/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.propertyeditor;

import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.ProjectService;

public class ProjectEditor extends BasePropertyEditorSupport<Project> {

  public ProjectEditor() {
    super(Project.class.getSimpleName());
  }

  @Override
  Integer getEntityId(Project license) {
    return license.getId();
  }

  @Override
  Project getEntityById(Integer id) {
    return Context.getService(ProjectService.class).getProject(id);
  }

  @Override
  Project getEntityByName(String name) {
    return Context.getService(ProjectService.class).getProject(name);
  }
}
