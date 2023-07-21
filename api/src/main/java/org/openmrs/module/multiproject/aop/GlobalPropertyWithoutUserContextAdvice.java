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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.GlobalProperty;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.ProjectService;
import org.openmrs.module.multiproject.api.exception.MultiProjectRuntimeException;
import org.springframework.aop.MethodBeforeAdvice;

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
    Optional<Location> patientLocation = getPatientLocation((Patient) methodParams[1]);
    Optional<LocationAttribute> locationProjectAttribute =
        getProjectAttribute(patientLocation.get());
    if (locationProjectAttribute.isPresent()) {
      Project project =
          Context.getService(ProjectService.class)
              .getProjectByUuid(locationProjectAttribute.get().getValueReference());
      methodParams[0] = findProjectOrDefaultGPName((String) methodParams[0], project.getSlug());
    }
  }

  private Optional<Location> getPatientLocation(Patient patient) {
    Optional<Location> patientLocation = getLocationFromAttribute(patient);
    if (!patientLocation.isPresent()) {
      throw new MultiProjectRuntimeException(
          String.format(
              "Missing location for patient with uuid %s! Not able to determine location project.",
              patient.getUuid()));
    }

    return patientLocation;
  }

  private Optional<LocationAttribute> getProjectAttribute(Location location) {
    return location.getActiveAttributes().stream()
        .filter(
            attribute ->
                StringUtils.equalsIgnoreCase(attribute.getAttributeType().getName(), "Project"))
        .findFirst();
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

  public static Optional<Location> getLocationFromAttribute(Person person) {
    final String locationAttributeName =
        Context.getAdministrationService().getGlobalProperty("cfl.person.attribute.location");

    if (locationAttributeName == null) {
      return Optional.empty();
    }

    final LocationService locationService = Context.getLocationService();
    return Optional.ofNullable(person.getAttribute(locationAttributeName))
        .map(PersonAttribute::getValue)
        .map(locationService::getLocationByUuid);
  }
}
