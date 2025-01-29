package com.utility.json;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDocumentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtilityAction extends AnAction {

    private static final Logger log = LoggerFactory.getLogger(JsonUtilityAction.class);
    JsonUtility jsonUtility = new JsonUtility();

    @Override
    public void actionPerformed(AnActionEvent e) {
        log.info("AnActionEvent:: {}", e);

        Project project = e.getProject();
        if (project == null) {
            return;
        }

        try {
            // Get the editor from the current project context
            Editor editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);

            if (editor != null) {
                // Get the document (entire content of the file in the editor)
                Document document = editor.getDocument();

                // Get the full text/content of the document
                String fileContent = document.getText();
                log.info("File content::\n {}", fileContent);

                if (!fileContent.isBlank() || !fileContent.isEmpty()) {
                    if (jsonUtility.isValidJson(fileContent)) {
                        String formattedJson = jsonUtility.formatJson(fileContent);
                        log.info("formatted json:: {}", formattedJson);

                        formattedJson = formattedJson.replace("\r\n", "\n");

                        // Use WriteCommandAction to modify the document
                        String finalFormattedJson = formattedJson;
                        WriteCommandAction.runWriteCommandAction(project, () -> {
                            // Replace the entire document content
                            document.setText(finalFormattedJson);

                            // Optionally, save the file after the change
                            PsiDocumentManager.getInstance(project).commitDocument(document);
                        });
                        String fileName = editor.getVirtualFile().getNameWithoutExtension();
                        String capitalizedFileName = jsonUtility.capitalizeFirstLetter(fileName);

                        String javaClassContent = jsonUtility.generateJavaClassFromJson(formattedJson, capitalizedFileName);

                        jsonUtility.createJavaFile(editor.getVirtualFile(), javaClassContent, capitalizedFileName);
                    }
                }
                else {
                    Messages.showWarningDialog(e.getProject(), "No content found!", "Error");
                }

            } else {
                // No active editor available
                Messages.showWarningDialog(e.getProject(), "No active open file found!", "Error");
            }
        } catch (Exception ex) {
            // Handle any unexpected exceptions
            Messages.showErrorDialog(e.getProject(), "An error occurred while retrieving file content: " + ex.getMessage(), "Error");
        }

    }

}
