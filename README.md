# JSON Utility - Validator, Formatter and Java Model Generator

This plugin provides an efficient way to validate, format, and generate Java model classes from JSON input. It simplifies the workflow for developers dealing with JSON data and Java projects by ensuring the JSON structure is valid and converting it into clean, well-structured Java classes.

**Authors**: Suresh Nettur, and Akhil Dusi.

## Features

- **JSON Validator**: Automatically checks the given JSON input for syntax errors.
- **JSON Formatter**: Once the JSON is validated, the plugin provides a neatly formatted version of the JSON for better readability and usability.
- **Java Model Generator**: If the JSON validation passes, the plugin generates corresponding Java model classes based on the structure of the JSON. This includes fields, getters, setters, and support for nested objects and arrays.
- **Java Model Generator - Lombok**: Generates Lombok style Java model classes from the given JSON structure.

## Requirements

## Installation

  1. Open IntelliJ.
  2. File -> Settings -> Plugins.
  3. Search for "JSON Validator, Formatter and Java Model Generator".
  4. Click Install to add the plugin to your workspace.
  5. Once installed, open project, json file.
  6. From Menu, select Code -> "Validate Format JSON And Generate Java Model" or "Validate Format JSON And Generate Java Lombok Model" to run the commands for validation, formatting, and Java class generation.


**Opened JSON file Person.json**:  
{  
&emsp;"name": "John Doe",  
&emsp;"age": 30,  
&emsp;"address": {  
&emsp;&emsp;&emsp;"city": "New York",  
&emsp;&emsp;&emsp;"zipcode": "10001"  
&emsp;},  
&emsp;"skills": ["Java", "TypeScript"]  
&#9;}  

**Generated Java Class**:  
import java.util.List;  

public class Person {  
&emsp;private String name;  
&emsp;private int age;  
&emsp;private Address address;  
&emsp;private List<String> skills;  

    // Getters and Setters  
}

public class Address {  
&emsp;private String city;  
&emsp;private String zipcode;  

    // Getters and Setters  
}

**with Lombok**:  

import lombok.Data;  
import lombok.Getter;  
import lombok.Setter;  
import lombok.NoArgsConstructor;  
import lombok.AllArgsConstructor;  

/*  
	Make sure to include lombok library in classpath or in pom.xml for maven projects,  

	Example:  
		<dependency\>  
      		<groupId\>org.projectlombok\</groupId\>  
   			<artifactId\>lombok\</artifactId\>  
		</dependency\>
*/

@Data  
@AllArgsConstructor  
@NoArgsConstructor  

public class Address {  
&emsp;private String city;  
&emsp;private String zipcode;  
}

import java.util.List;

import lombok.Data;  
import lombok.AllArgsConstructor;  
import lombok.NoArgsConstructor;

@Data  
@AllArgsConstructor  
@NoArgsConstructor  

public class Person {  
&emsp;private String name;  
&emsp;private int age;  
&emsp;private Address address;  
&emsp;private List&lt;String&gt; skills;  
}

## Known Issues

Handling of highly complex nested JSON structures may result in deeply nested Java classes, which might require further optimization in future versions.  
Currently, only basic Java class structure (fields, getters, and setters) is supported.   
Advanced Java features (such as constructors or annotations) will be considered in future updates.


## License

This plugin is licensed under the [MIT License](LICENSE).
See the LICENSE file for details.

## Disclaimer

- **Ethical Usage**: This tool is designed for ethical development and testing purposes only. Do not use it for any unethical or inappropriate activities.
- **PII/PHI Handling**: Avoid including personally identifiable information (PII) or protected health information (PHI) in the input spec. The developers are not responsible for any misuse of the plugin.

### Support

For issues or questions, visit the GitHub repository or contact us via the IntelliJ IDEA Community.