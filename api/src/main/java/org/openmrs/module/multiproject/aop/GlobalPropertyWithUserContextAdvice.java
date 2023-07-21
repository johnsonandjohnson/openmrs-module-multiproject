/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.ProjectService;
import org.springframework.aop.MethodBeforeAdvice;

public class GlobalPropertyWithUserContextAdvice implements MethodBeforeAdvice {

  private static final List<String> GETTING_GLOBAL_PROPERTY_METHOD_NAMES =
      Arrays.asList("getGlobalProperty", "getGlobalPropertiesByPrefix");

  private List<String> globalPropertiesSupportingMultiProject;

  public GlobalPropertyWithUserContextAdvice(List<String> globalPropertiesSupportingMultiProject) {
    this.globalPropertiesSupportingMultiProject = globalPropertiesSupportingMultiProject;
  }

  public GlobalPropertyWithUserContextAdvice() {}

  @Override
  public void before(Method method, Object[] objects, Object o) throws Throwable {
    if (GETTING_GLOBAL_PROPERTY_METHOD_NAMES.contains(method.getName())) {
      String gpName = (String) objects[0];
      if (globalPropertiesSupportingMultiProject != null
          && globalPropertiesSupportingMultiProject.contains(gpName)) {
        objects[0] = findProjectOrDefaultGPName(gpName);
      }
    }
  }

  private String findProjectOrDefaultGPName(String basicGPName) {
    Optional<Project> project = Context.getService(ProjectService.class).getCurrentUserProject();
    if (!project.isPresent()) {
      return basicGPName;
    }

    List<String> allPropertiesByPrefix =
        Context.getAdministrationService()
            .getGlobalPropertiesByPrefix(basicGPName.concat("."))
            .stream()
            .map(GlobalProperty::getProperty)
            .collect(Collectors.toList());

    String projectSlug = project.get().getSlug();
    String projectGPName = String.join(".", basicGPName, projectSlug);
    if (allPropertiesByPrefix.contains(projectGPName)) {
      return projectGPName;
    }

    return basicGPName;
  }
}
