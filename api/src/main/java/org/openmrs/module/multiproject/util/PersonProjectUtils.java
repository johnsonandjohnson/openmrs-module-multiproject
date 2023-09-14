/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.util;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.exception.MultiProjectRuntimeException;
import org.openmrs.module.multiproject.api.service.ProjectService;

import java.util.Optional;

public class PersonProjectUtils {
  private PersonProjectUtils() {}

  public static Optional<Project> getPersonProject(Person person) {
    final Location personLocation = getPersonLocation(person);

    final ProjectService projectService = Context.getService(ProjectService.class);
    return getProjectAttribute(personLocation)
        .map(LocationAttribute::getValueReference)
        .map(projectService::getProjectByUuid);
  }

  private static Location getPersonLocation(Person person) {
    final Optional<Location> personLocation = getLocationFromAttribute(person);
    if (!personLocation.isPresent()) {
      throw new MultiProjectRuntimeException(
          String.format(
              "Missing location for person with uuid %s! Not able to determine location project.",
              person.getUuid()));
    }

    return personLocation.get();
  }

  private static Optional<Location> getLocationFromAttribute(Person person) {
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

  private static Optional<LocationAttribute> getProjectAttribute(Location location) {
    return location.getActiveAttributes().stream()
        .filter(
            attribute ->
                StringUtils.equalsIgnoreCase(attribute.getAttributeType().getName(), "Project"))
        .findFirst();
  }
}
