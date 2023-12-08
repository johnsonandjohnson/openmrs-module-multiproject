/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.ProjectAssignment;
import org.openmrs.module.multiproject.api.service.ProjectAssignmentService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.RepHandler;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Date;

@Resource(
    name = RestConstants.VERSION_1 + "/projectAssignment",
    supportedClass = ProjectAssignment.class,
    supportedOpenmrsVersions = "2.*")
public class ProjectAssignmentResource extends DataDelegatingCrudResource<ProjectAssignment> {

  @Override
  public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
    if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
      DelegatingResourceDescription description = new DelegatingResourceDescription();
      description.addProperty("id");
      description.addProperty("uuid");
      description.addProperty("voided");
      description.addProperty("project", Representation.FULL);
      description.addProperty("objectClass");
      description.addProperty("objectId");
      description.addSelfLink();
      return description;
    }
    return null;
  }

  @Override
  public DelegatingResourceDescription getCreatableProperties() {
    DelegatingResourceDescription description = new DelegatingResourceDescription();
    description.addRequiredProperty("project");
    description.addRequiredProperty("objectClass");
    description.addRequiredProperty("objectId");
    return description;
  }

  @Override
  public DelegatingResourceDescription getUpdatableProperties() {
    return getCreatableProperties();
  }

  @Override
  public ProjectAssignment getByUniqueId(String uuid) {
    return Context.getService(ProjectAssignmentService.class).getProjectAssignmentByUuid(uuid);
  }

  @Override
  protected void delete(
      ProjectAssignment projectAssignment, String reason, RequestContext requestContext)
      throws ResponseException {
    if (Boolean.FALSE.equals(projectAssignment.getVoided())) {
      projectAssignment.setVoided(true);
      projectAssignment.setVoidedBy(Context.getAuthenticatedUser());
      projectAssignment.setDateVoided(new Date());
      projectAssignment.setVoidReason(reason);
      this.save(projectAssignment);
    }
  }

  @Override
  public ProjectAssignment newDelegate() {
    return new ProjectAssignment();
  }

  @Override
  public ProjectAssignment save(ProjectAssignment projectAssignment) {
    return Context.getService(ProjectAssignmentService.class)
        .saveProjectAssignment(projectAssignment);
  }

  @Override
  public void purge(ProjectAssignment projectAssignment, RequestContext requestContext)
      throws ResponseException {
    if (projectAssignment != null) {
      Context.getService(ProjectAssignmentService.class).purgeProjectAssignment(projectAssignment);
    }
  }

  @Override
  protected PageableResult doGetAll(RequestContext context) throws ResponseException {
    return new NeedsPaging<>(
        Context.getService(ProjectAssignmentService.class)
            .getAllProjectAssignments(context.getIncludeAll()),
        context);
  }

  @RepHandler(DefaultRepresentation.class)
  public SimpleObject asDefaultRep(ProjectAssignment delegate) {
    return this.convertDelegateToRepresentation(
        delegate, getRepresentationDescription(Representation.DEFAULT));
  }

  @RepHandler(FullRepresentation.class)
  public SimpleObject asFullRep(ProjectAssignment delegate) {
    return this.convertDelegateToRepresentation(
        delegate, getRepresentationDescription(Representation.FULL));
  }
}
