/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.multiproject.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openmrs.api.context.Context;
import org.openmrs.module.multiproject.Project;
import org.openmrs.module.multiproject.api.service.ProjectService;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Advisor which overrides method parameter with project-specific name. Method must contain only one
 * method parameter!
 */
public class MultiProjectAroundAdvisor extends StaticMethodMatcherPointcutAdvisor
    implements Advisor {

  private static final long serialVersionUID = -472867290226052828L;

  private final Set<Method> wrappedMethods;

  public MultiProjectAroundAdvisor(Method... methods) {
    this.wrappedMethods = new HashSet<>(Arrays.asList(methods));
  }

  @Override
  public boolean matches(Method method, Class<?> aClass) {
    return wrappedMethods.contains(method);
  }

  @Override
  public Advice getAdvice() {
    return new MultiProjectAroundAdvice();
  }

  private class MultiProjectAroundAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
      Object[] args = methodInvocation.getArguments();
      if (args.length != 1) {
        throw new IllegalArgumentException("This advisor accepts only one method parameter");
      }

      String projectOrDefaultParamName = getProjectSpecificOrDefaultParamName((String) args[0]);

      Object result =
          methodInvocation
              .getMethod()
              .invoke(methodInvocation.getThis(), projectOrDefaultParamName);
      if (result != null) {
        return result;
      }

      return methodInvocation.proceed();
    }
  }

  private String getProjectSpecificOrDefaultParamName(String paramName) {
    Optional<Project> project = Context.getService(ProjectService.class).getCurrentUserProject();
    return project.map(value -> String.join(".", paramName, value.getSlug())).orElse(paramName);
  }
}
