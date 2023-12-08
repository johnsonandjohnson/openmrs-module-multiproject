# Multi-project

The OpenMRS Multi-Project module tries to deliver simplified multi-tenancy solution for an OpenMRS platform.
This module alone does not provide any functions out-of-the-box, it contains utilities to apply multi-project idea to any
 OpenMRS distribution.

## Idea

The Project that user is assigned is determined by their Locations.
The Project a Patient is assigned is determined by their Identifier's Location.
Single Project can be assigned to multiple Locations via LocationAttribute.
Project allows to group exclusive configuration which is valid for one Project but invalid for the other Project, or
configuration which should be different for different Projects.
This is a simplified multi-tenancy solution where parts of application are different depending on the user's Project.
A Daemon user have access to all data.
For the runtime data, like Patient information, a Location based access module is used -
https://github.com/openmrs/openmrs-module-locationbasedaccess

## Technical Details

### Project entity

The Project entity stores all projects configured in the system. The OpenMRS-style REST is included. This entity can be
referenced is compatible with OpenMRS Attributes.

The ProjectService provides utilities to read current user's project, patient's project etc.

### Project-based Filters

There are two filters which can filter a collection to contain data object which belong to the current user's Project or are
not assigned to any other project.

#### Name based filter - NameSuffixProjectBasedFilter

The NameSuffixProjectBasedFilter filters objects based on the suffix in their ID-like property.
Data object is assigned to Project when it's ID-like property ends with Project's slug - any property can be used.
If Data object ID-like property doesn't contain any valid Project slug, it is considered as not assigned, meaning it should
be included in all Projects.

#### Assignment based filter - ProjectAssignmentProjectBasedFilter

For the data object where there is no ID-like property, or a Project's slug can't be encoded into the property, the
Multi-project offers ProjectAssignment entity.
The ProjectAssignment allows to assign any data object to Project - data objects are identified by class name and a value of
ID-like property.
The ProjectAssignmentProjectBasedFilter filters objects based on their assignment made with ProjectAssignment.
Data objects which are not assigned to any Project, are included in all Projects.

### Filter utilities

#### Advice to filter method results

When a method returns data objects which needs to be filtered based on their Project, use advice:
ProjectBasedFilterAfterAdvice.

#### OpenMRS REST resource override

When a REST resource returns a data which needs to be filtered based on their Project, use a helper:
MultiProjectFilteringDelegatingCrudResource
