/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.api.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.OpenmrsMetadata;

import java.util.List;

abstract class BaseMultiProjectOpenmrsMetadataDAO<E extends OpenmrsMetadata>
    extends BaseMultiProjectOpenmrsObjectDAO<E> {

  /** @param openmrsObjectClass the Class of the main type handled by the Dao, not null */
  BaseMultiProjectOpenmrsMetadataDAO(final Class<E> openmrsObjectClass) {
    super(openmrsObjectClass, "retired");
  }

  E internalReadByName(final String name) {
    final Criteria criteria =
        getSession().createCriteria(getOpenmrsObjectClass()).add(Restrictions.eq("name", name));
    return firstResult(criteria, getOpenmrsObjectClass());
  }

  List<E> internalReadByNameFragment(final String nameFragment) {
    Criteria criteria =
        getSession()
            .createCriteria(getOpenmrsObjectClass())
            .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
            .add(Restrictions.ilike("name", nameFragment, MatchMode.START));
    return criteria.list();
  }
}
