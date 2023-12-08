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

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.Converter;
import org.openmrs.module.webservices.rest.web.resource.api.CrudResource;
import org.openmrs.module.webservices.rest.web.resource.api.Listable;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.Resource;
import org.openmrs.module.webservices.rest.web.resource.api.Searchable;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingPropertyAccessor;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceHandler;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubclassHandler;
import org.openmrs.module.webservices.rest.web.response.ConversionException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * The MultiProjectFilteringDelegatingCrudResource class helps in overriding REST resource
 * controllers.
 *
 * <p>Following example shows a class which overrides default OpenMRS REST resource for VisitType
 * (implementation details omitted for clarity):
 *
 * <pre>
 * {@code @Resource(}
 *   order = VisitTypeMultiProjectResource.RESOURCE_ORDER,
 *   name = RestConstants.VERSION_1 + "/visittype",
 *   supportedClass = VisitType.class,
 *   supportedOpenmrsVersions = {
 *     "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*"
 *   })
 * public class VisitTypeMultiProjectResource
 *     extends MultiProjectFilteringDelegatingCrudResource<VisitTypeResource1_9, VisitType> {
 *  public static final int RESOURCE_ORDER = 10;
 *
 *  public VisitTypeMultiProjectResource() {
 *    super(new VisitTypeResource1_9());
 *    this.projectBasedFilter =
 *        new ProjectAssignmentProjectBasedFilter<>(VisitType.class, VisitType::getUuid);
 *  }
 *
 * {@code @Override}
 *  protected PageableResult doGetAll(RequestContext context) throws ResponseException {
 *    // ...
 *    projectBasedFilter.doFilter(allVisitTypes);
 *    return new NeedsPaging<>(allVisitTypes, context);
 *  }
 *
 * {@code @Override}
 *  protected PageableResult doSearch(RequestContext context) {
 *    // ...
 *    projectBasedFilter.doFilter(filteredVisitTypes);
 *    return new NeedsPaging<>(filteredVisitTypes, context);
 *  }
 * }
 * </pre>
 *
 * @param <R> the original REST resource type
 * @param <T> the object handled by the REST resource controller
 * @see org.openmrs.module.multiproject.filter.ProjectBasedFilter
 */
public abstract class MultiProjectFilteringDelegatingCrudResource<
        R extends DelegatingCrudResource<T>, T>
    implements Converter<T>,
        DelegatingPropertyAccessor<T>,
        Resource,
        DelegatingResourceHandler<T>,
        CrudResource,
        Searchable,
        Listable {

  private final R delegate;

  public MultiProjectFilteringDelegatingCrudResource(R delegate) {
    this.delegate = delegate;
  }

  protected R getDelegate() {
    return delegate;
  }

  @Override
  public T newInstance(String s) {
    return delegate.newInstance(s);
  }

  @Override
  public T getByUniqueId(String s) {
    return delegate.getByUniqueId(s);
  }

  @Override
  public SimpleObject asRepresentation(T t, Representation representation)
      throws ConversionException {
    return delegate.asRepresentation(t, representation);
  }

  @Override
  public Object getProperty(T t, String s) throws ConversionException {
    return delegate.getProperty(t, s);
  }

  @Override
  public void setProperty(Object o, String s, Object o1) throws ConversionException {
    delegate.setProperty(o, s, o1);
  }

  @Override
  public Object create(SimpleObject simpleObject, RequestContext requestContext)
      throws ResponseException {
    return delegate.create(simpleObject, requestContext);
  }

  @Override
  public void delete(String s, String s1, RequestContext requestContext) throws ResponseException {
    delegate.delete(s, s1, requestContext);
  }

  @Override
  public void purge(String s, RequestContext requestContext) throws ResponseException {
    delegate.purge(s, requestContext);
  }

  @Override
  public Object retrieve(String s, RequestContext requestContext) throws ResponseException {
    return delegate.retrieve(s, requestContext);
  }

  @Override
  public List<Representation> getAvailableRepresentations() {
    return delegate.getAvailableRepresentations();
  }

  @Override
  public Object update(String s, SimpleObject simpleObject, RequestContext requestContext)
      throws ResponseException {
    return delegate.update(s, simpleObject, requestContext);
  }

  @Override
  public String getUri(Object o) {
    return delegate.getUri(o);
  }

  @Override
  public String getResourceVersion() {
    return delegate.getResourceVersion();
  }

  @Override
  public T newDelegate() {
    return delegate.newDelegate();
  }

  @Override
  public T newDelegate(SimpleObject simpleObject) {
    return delegate.newDelegate(simpleObject);
  }

  @Override
  public T save(T t) {
    return delegate.save(t);
  }

  @Override
  public void purge(T t, RequestContext requestContext) throws ResponseException {
    delegate.purge(t, requestContext);
  }

  @Override
  public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
    return delegate.getRepresentationDescription(representation);
  }

  @Override
  public DelegatingResourceDescription getCreatableProperties()
      throws ResourceDoesNotSupportOperationException {
    return delegate.getCreatableProperties();
  }

  @Override
  public DelegatingResourceDescription getUpdatableProperties()
      throws ResourceDoesNotSupportOperationException {
    return delegate.getUpdatableProperties();
  }

  @Override
  public SimpleObject getAll(RequestContext context) throws ResponseException {
    if (context.getType() != null) {
      if (!delegate.hasTypesDefined()) {
        throw new IllegalArgumentException(
            getClass() + " does not support " + RestConstants.REQUEST_PROPERTY_FOR_TYPE);
      } else if (context.getType().equals(getResourceName())) {
        throw new IllegalArgumentException(
            "You may not specify "
                + RestConstants.REQUEST_PROPERTY_FOR_TYPE
                + "="
                + context.getType()
                + " because it is the default behavior for this resource");
      }

      DelegatingSubclassHandler<T, ? extends T> handler =
          getDelegateSubclassHandler(context.getType());

      if (handler == null) {
        throw new IllegalArgumentException(
            "No handler is specified for "
                + RestConstants.REQUEST_PROPERTY_FOR_TYPE
                + "="
                + context.getType());
      }

      PageableResult result = handler.getAllByType(context);
      return result.toSimpleObject(this);
    } else {
      PageableResult result = doGetAll(context);
      return result.toSimpleObject(this);
    }
  }

  protected abstract PageableResult doGetAll(RequestContext context) throws ResponseException;

  @Override
  public SimpleObject search(RequestContext context) throws ResponseException {
    PageableResult result = doSearch(context);
    return result.toSimpleObject(this);
  }

  protected abstract PageableResult doSearch(RequestContext context);

  private String getResourceName() {
    org.openmrs.module.webservices.rest.web.annotation.Resource ann =
        delegate
            .getClass()
            .getAnnotation(org.openmrs.module.webservices.rest.web.annotation.Resource.class);

    if (ann == null) {
      throw new IllegalStateException(
          "There is no " + Resource.class + " annotation on " + delegate.getClass());
    } else if (StringUtils.isEmpty(ann.name())) {
      throw new IllegalStateException(
          Resource.class.getSimpleName()
              + " annotation on "
              + delegate.getClass()
              + " must specify a name");
    }

    return ann.name();
  }

  private DelegatingSubclassHandler<T, ? extends T> getDelegateSubclassHandler(String type) {
    try {
      final Method getSubclassHandlerDelegateMethod =
          delegate.getClass().getMethod("getSubclassHandler", String.class);
      getSubclassHandlerDelegateMethod.setAccessible(true);
      return (DelegatingSubclassHandler<T, ? extends T>)
          getSubclassHandlerDelegateMethod.invoke(delegate, type);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(
          "Could not find getSubclassHandler method on REST resource class: "
              + delegate.getClass().getName(),
          e);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(
          "Failed to access getSubclassHandler method on REST resource class: "
              + delegate.getClass().getName(),
          e);
    } catch (InvocationTargetException e) {
      throw new IllegalStateException(
          "Failed to execute getSubclassHandler method on REST resource class: "
              + delegate.getClass().getName(),
          e);
    }
  }
}
