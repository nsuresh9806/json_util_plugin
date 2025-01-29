package com.utility.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtility {

    private static final Logger log = LoggerFactory.getLogger(JsonUtility.class);

    public boolean isValidJson(String json) {
        log.info("isValidJson:: {}", json);
        try {
            new ObjectMapper().readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String formatJson(String json) {
        log.info("formatJson:: {}", json);
        try {
            return new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(
                            new ObjectMapper().readTree(json));
        } catch (Exception ex) {
            return "Invalid JSON.";
        }
    }

    // Main method to generate the Java class from the JSON string
    public String generateJavaClassFromJson(String jsonContent, String className) throws Exception {
        // Parse the JSON content
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        // Begin creating the Java class
        StringBuilder javaClass = new StringBuilder();
        javaClass.append("public class ").append(className).append(" {\n\n");

        // Iterate through fields of the JSON and generate corresponding Java fields
        Map<String, String> fields = new LinkedHashMap<>();
        generateFields(rootNode, javaClass, "", fields);
        generateGettersAndSetters(javaClass, fields);

        javaClass.append("\n}"); // End the class

        return javaClass.toString();
    }

    private void generateGettersAndSetters(StringBuilder javaClass, Map<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            String fieldType = entry.getValue();
            String capitalizedField = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

            // Generate getter
            javaClass.append("\n    public ").append(fieldType).append(" get").append(capitalizedField).append("() {\n")
                    .append("        return ").append(fieldName).append(";\n")
                    .append("    }\n");

            // Generate setter
            javaClass.append("\n    public void set").append(capitalizedField).append("(").append(fieldType)
                    .append(" ").append(fieldName).append(") {\n")
                    .append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n")
                    .append("    }\n");
        }
    }


    // Method to generate Lombok-style Java class from JSON
    public String generateLombokJavaClassFromJson(String jsonContent, String className) throws Exception {
        // Parse the JSON content
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        // Begin creating the Lombok Java class
        StringBuilder javaClass = new StringBuilder();
        javaClass.append("import lombok.Data;\n");
        javaClass.append("import lombok.Getter;\n");
        javaClass.append("import lombok.Setter;\n");
        javaClass.append("import lombok.NoArgsConstructor;\n");
        javaClass.append("import lombok.AllArgsConstructor;\n\n");

        javaClass.append("/*\n");
        javaClass.append("Make sure to include lombok library in classpath or in pom.xml for maven projects, Example:\n");
        javaClass.append("<dependency>\n");
        javaClass.append("<groupId>org.projectlombok</groupId>\n");
        javaClass.append("<artifactId>lombok</artifactId>\n");
        javaClass.append("</dependency>\n");
        javaClass.append("*/\n");

        javaClass.append("@Data\n");
        javaClass.append("@Getter\n");
        javaClass.append("@Setter\n");
        javaClass.append("@NoArgsConstructor\n");
        javaClass.append("@AllArgsConstructor\n");
        javaClass.append("public class ").append(className).append(" {\n");

        // Iterate through fields of the JSON and generate corresponding Java fields
        Map<String, String> fields = new LinkedHashMap<>();
        generateFields(rootNode, javaClass, "", fields);

        javaClass.append("\n}"); // End the class

        return javaClass.toString();
    }

    // Capitalize the first letter of a string (for class names)
    public String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Method to create a new Java file and write processed content to it
    public void createJavaFile(VirtualFile currentFile, String content, String fileName) {
        // Get the directory of the currently opened file
        VirtualFile parentDir = currentFile.getParent();

        if (parentDir != null) {
            // Define the file path for the new Java file by using the same name as the current file but with .java extension
            String newFilePath = parentDir.getPath() + "/" + fileName + ".java";

            try {
                // Write the processed content to the new Java file
                Files.write(Paths.get(newFilePath), content.getBytes());

                // Inform the user that the file was created successfully
                Messages.showInfoMessage("Java file created: " + newFilePath, "Success");

            } catch (IOException ex) {
                // Handle errors in file creation
                Messages.showErrorDialog("Failed to create Java file: " + ex.getMessage(), "Error");
            }
        } else {
            Messages.showErrorDialog("Failed to get the parent directory of the current file", "Error");
        }
    }

    // Recursive method to generate fields based on JSON nodes
    private void generateFields(JsonNode node, StringBuilder javaClass, String parentFieldName, Map<String, String> fields) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fieldIterator = node.fields();
            while (fieldIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldIterator.next();
                String fieldName = field.getKey();
                JsonNode fieldNode = field.getValue();

                if (fieldNode.isObject()) {
                    String newClassName = capitalizeFirstLetter(fieldName);
                    javaClass.append("\n    private ").append(newClassName).append(" ").append(fieldName).append(";");

                    // Collect field for getter/setter generation
                    fields.put(fieldName, newClassName);

                    // Recurse for nested object
                    generateFields(fieldNode, javaClass, newClassName, fields);
                } else {
                    String fieldType = determineFieldType(fieldNode);
                    javaClass.append("\n    private ").append(fieldType).append(" ").append(fieldName).append(";");

                    // Store field name and type for getter/setter generation
                    fields.put(fieldName, fieldType);
                }
            }
        }
    }

    // Determine the field type based on the JSON node
    private String determineFieldType(JsonNode node) {
        if (node.isTextual()) {
            return "String";
        } else if (node.isInt()) {
            return "int";
        } else if (node.isDouble()) {
            return "double";
        } else if (node.isBoolean()) {
            return "boolean";
        } else if (node.isArray()) {
            // Handling arrays; assuming the array items are of the same type
            JsonNode firstElement = node.get(0);
            String arrayType = determineFieldType(firstElement);
            return "List<" + arrayType + ">";
        }
        return "Object"; // Default to Object if type is unrecognized
    }

}

