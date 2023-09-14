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

import org.openmrs.GlobalProperty;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.util.PersonProjectUtils;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GlobalPropertyWithoutUserContextAdvice implements MethodBeforeAdvice {

  private static final String GET_GLOBAL_PROPERTY_METHOD_NAME = "getGlobalProperty";

  private List<String> globalPropertiesSupportingMultiProject;

  public GlobalPropertyWithoutUserContextAdvice(
      List<String> globalPropertiesSupportingMultiProject) {
    this.globalPropertiesSupportingMultiProject = globalPropertiesSupportingMultiProject;
  }

  public GlobalPropertyWithoutUserContextAdvice() {}

  @Override
  public void before(Method method, Object[] objects, Object o) throws Throwable {
    if (GET_GLOBAL_PROPERTY_METHOD_NAME.equals(method.getName())) {
      String gpKey = (String) objects[0];
      if (globalPropertiesSupportingMultiProject != null
          && globalPropertiesSupportingMultiProject.contains(gpKey)) {
        overrideGPWithProjectSuffixIfNeeded(objects);
      }
    }
  }

  private void overrideGPWithProjectSuffixIfNeeded(Object[] methodParams) {
    final Optional<Project> patientProject =
        PersonProjectUtils.getPersonProject((Patient) methodParams[1]);
    patientProject.ifPresent(
        project ->
            methodParams[0] =
                findProjectOrDefaultGPName((String) methodParams[0], project.getSlug()));
  }

  private String findProjectOrDefaultGPName(String basicGPName, String projectSlug) {
    String projectGPName = String.join(".", basicGPName, projectSlug);
    List<String> allPropertiesByPrefix =
        Context.getAdministrationService().getGlobalPropertiesByPrefix(basicGPName).stream()
            .map(GlobalProperty::getProperty)
            .collect(Collectors.toList());
    if (allPropertiesByPrefix.contains(projectGPName)) {
      return projectGPName;
    }

    return basicGPName;
  }
}
