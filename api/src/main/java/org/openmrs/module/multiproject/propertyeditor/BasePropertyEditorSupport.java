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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

public abstract class BasePropertyEditorSupport<E> extends PropertyEditorSupport {
  private final Log log = LogFactory.getLog(this.getClass());
  private final String entityName;

  BasePropertyEditorSupport(final String entityName) {
    this.entityName = entityName;
  }

  @Override
  public String getAsText() {
    final E entity = (E) this.getValue();
    if (entity == null && Context.isAuthenticated()) {
      return null;
    } else {
      return ObjectUtils.toString(getEntityId(entity), null);
    }
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    E value = null;

    if (StringUtils.hasText(text)) {
      try {
        value = getEntityById(Integer.valueOf(text));
      } catch (Exception ex) {
        value = getEntityByName(text);

        if (value == null) {
          this.log.error("Error setting text: " + text, ex);
          throw new IllegalArgumentException(entityName + " not found.", ex);
        }
      }
    }

    setValue(value);
  }

  abstract Integer getEntityId(E entity);

  abstract E getEntityById(Integer id);

  abstract E getEntityByName(String text);
}
