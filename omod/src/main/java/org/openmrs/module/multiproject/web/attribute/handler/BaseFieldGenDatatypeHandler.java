/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.web.attribute.handler;

import org.apache.commons.lang.StringUtils;
import org.openmrs.OpenmrsObject;
import org.openmrs.customdatatype.CustomDatatype;
import org.openmrs.customdatatype.InvalidCustomValueException;
import org.openmrs.customdatatype.SerializingCustomDatatype;
import org.openmrs.web.attribute.handler.FieldGenDatatypeHandler;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditor;
import java.util.Map;

abstract class BaseFieldGenDatatypeHandler<
        D extends SerializingCustomDatatype<T>, T extends OpenmrsObject>
    implements FieldGenDatatypeHandler<D, T> {

  private final PropertyEditor typeEditor;

  BaseFieldGenDatatypeHandler(final PropertyEditor typeEditor) {
    this.typeEditor = typeEditor;
  }

  @Override
  public Map<String, Object> getWidgetConfiguration() {
    // not used
    return null;
  }

  @Override
  public T getValue(D datatype, HttpServletRequest request, String formFieldName)
      throws InvalidCustomValueException {
    String formFieldValue = request.getParameter(formFieldName);

    if (StringUtils.isBlank(formFieldValue)) {
      return null;
    }

    if (StringUtils.isNumeric(formFieldValue)) {
      typeEditor.setAsText(formFieldValue);

      final T entity = (T) typeEditor.getValue();

      if (entity != null) {
        formFieldValue = entity.getUuid();
      }
    }

    return datatype.deserialize(formFieldValue);
  }

  @Override
  public CustomDatatype.Summary toHtmlSummary(CustomDatatype<T> datatype, String valueReference) {
    return datatype.getTextSummary(valueReference);
  }

  @Override
  public String toHtml(CustomDatatype<T> datatype, String valueReference) {
    return toHtmlSummary(datatype, valueReference).getSummary();
  }

  @Override
  public void setHandlerConfiguration(String s) {
    // not used
  }
}
