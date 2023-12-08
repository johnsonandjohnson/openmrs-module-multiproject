/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.ui;

import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.web.taglib.fieldgen.ProjectHandler;
import org.openmrs.ui.framework.StandardModuleUiConfiguration;
import org.openmrs.ui.framework.fragment.FragmentFactory;
import org.openmrs.ui.framework.page.PageFactory;
import org.openmrs.ui.framework.resource.ResourceFactory;
import org.openmrs.web.taglib.fieldgen.FieldGenHandlerFactory;

/**
 * The MultiProjectModuleUiConfiguration Class.
 *
 * <p>Responsible for adding {@link ProjectHandler} to OpenMRS framework, to make the dynamic
 * editors (e.g.: attribute value) support these types.
 */
public class MultiProjectModuleUiConfiguration extends StandardModuleUiConfiguration {

  @Override
  public void afterContextRefreshed(
      PageFactory pageFactory, FragmentFactory fragmentFactory, ResourceFactory resourceFactory) {
    super.afterContextRefreshed(pageFactory, fragmentFactory, resourceFactory);

    FieldGenHandlerFactory.getSingletonInstance()
        .getHandlers()
        .put(Project.class.getName(), ProjectHandler.class.getName());
  }
}
