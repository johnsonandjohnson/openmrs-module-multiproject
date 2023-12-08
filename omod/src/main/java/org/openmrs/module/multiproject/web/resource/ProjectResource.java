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
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.ProjectService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.MetadataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(
    name = RestConstants.VERSION_1 + "/project",
    supportedClass = Project.class,
    supportedOpenmrsVersions = "2.*")
public class ProjectResource extends MetadataDelegatingCrudResource<Project> {

  @Override
  public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
    if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
      DelegatingResourceDescription description = new DelegatingResourceDescription();
      description.addProperty("id");
      description.addProperty("uuid");
      description.addProperty("retired");
      description.addProperty("name");
      description.addProperty("description");
      description.addProperty("slug");
      description.addSelfLink();
      return description;
    }
    return null;
  }

  @Override
  public DelegatingResourceDescription getCreatableProperties() {
    DelegatingResourceDescription description = new DelegatingResourceDescription();
    description.addRequiredProperty("name");
    description.addRequiredProperty("slug");
    description.addProperty("description");
    return description;
  }

  @Override
  public DelegatingResourceDescription getUpdatableProperties() {
    return getCreatableProperties();
  }

  @Override
  public Project getByUniqueId(String uuid) {
    Project project = Context.getService(ProjectService.class).getProjectByUuid(uuid);

    if (project == null) {
      project = Context.getService(ProjectService.class).getProject(uuid);
    }

    return project;
  }

  @Override
  public Project newDelegate() {
    return new Project();
  }

  @Override
  public Project save(Project project) {
    return Context.getService(ProjectService.class).saveProject(project);
  }

  @Override
  public void purge(Project project, RequestContext requestContext) throws ResponseException {
    if (project != null) {
      Context.getService(ProjectService.class).purgeProject(project);
    }
  }

  @Override
  protected PageableResult doGetAll(RequestContext context) throws ResponseException {
    return new NeedsPaging<>(
        Context.getService(ProjectService.class).getAllProjects(context.getIncludeAll()), context);
  }
}
