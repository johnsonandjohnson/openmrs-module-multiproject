<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ attribute name="formFieldName" required="true" %>
<%@ attribute name="initialValue" required="false" %>
<%@ attribute name="optionHeader" required="false" %>
<%@ attribute name="projects" required="true" type="java.util.List" %>

<%--
  ~ This Source Code Form is subject to the terms of the Mozilla Public License,
  ~  v. 2.0. If a copy of the MPL was not distributed with this file, You can
  ~  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
  ~  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
  ~
  ~ Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
  ~ graphic logo is a trademark of OpenMRS Inc.
  --%>

<c:if test="${empty projects}">
    <openmrs:message code="multiproject.project.list.empty"/>
</c:if>
<c:if test="${not empty projects}">
    <select name="${formFieldName}">
        <c:if test="${optionHeader != ''}">
            <c:if test="${optionHeader == '[blank]'}">
                <option value=""></option>
            </c:if>
            <c:if test="${optionHeader != '[blank]'}">
                <option value="">${optionHeader}</option>
            </c:if>
        </c:if>
        <c:forEach var="project" items="${projects}">
            <option value="${project.projectId}"
                    <c:if test="${project.projectId == initialValue}">selected</c:if>>
                <c:if test="${project.retired}">(RETIRED) </c:if>${project.displayString}
            </option>
        </c:forEach>
    </select>
</c:if>
