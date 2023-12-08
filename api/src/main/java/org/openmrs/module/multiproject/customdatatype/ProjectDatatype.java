/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.customdatatype;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.customdatatype.CustomDatatype;
import org.openmrs.customdatatype.SerializingCustomDatatype;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.ProjectService;
import org.springframework.stereotype.Component;

@Component
public class ProjectDatatype extends SerializingCustomDatatype<Project> {

  @Override
  public Summary doGetTextSummary(Project project) {
    return new CustomDatatype.Summary(project.getName(), true);
  }

  @Override
  public String serialize(Project project) {
    if (project == null || project.getUuid() == null) {
      return null;
    }
    return project.getUuid();
  }

  @Override
  public Project deserialize(String serializedValue) {
    if (StringUtils.isEmpty(serializedValue)) {
      return null;
    }
    return Context.getService(ProjectService.class).getProjectByUuid(serializedValue);
  }
}
