# Change Log

## Release Notes

### 1.0.0

This is the initial release of the JSON Validator, Formatter and Java Model Class Generator plugin for IntelliJ IDEA.

The following features are included in the initial release:

- **JSON Validation**: Automatically validates the opened file JSON input and validates.
- **Formatted JSON Output**: Upon successful validation, formats the JSON input for better readability.
- **Java Model Class Generation**: Generates Java model classes from the given JSON structure.
- **Java Model Class Generation - Lombok**: Generates Lombok style Java model classes from the given JSON structure.

The generated classes include:
  - Fields corresponding to the JSON properties.
  - Getters and setters for each field.
  - Nested class support for complex JSON structures (objects inside objects).
  - Arrays handled as List&lt;type&gt; in Java classes.
  - Default Class Name: A default class name is pre-filled to simplify generation.

### 1.2.0 (Future Version)

Planned improvements to include:  
    Support for custom constructors in generated Java classes.  
    More configurable options for formatting, annotations, or additional language support.
