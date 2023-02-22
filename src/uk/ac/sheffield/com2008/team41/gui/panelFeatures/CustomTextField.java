package uk.ac.sheffield.com2008.team41.gui.panelFeatures;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A JTextField with extra capabilities for actions, regex filtering and also character limits.
 * Credit: Team 41 Homebreak submission 2021
 */
public class CustomTextField extends JTextField {
    //Action to be ran during Document Listner Update
    // Specified in Constructor
    Runnable action;

    /**
     * Creates a Custom Text Field with a default column value
     * @param maxlength //Maximum Length allowed in Text Field
     * @param bannedRegex //Any specified regex which matches this query will be immediately deleted.
     */
    public CustomTextField(int maxlength, String bannedRegex){
        this(30,maxlength,bannedRegex);
    }

    /**
     * Creates a Custom Text Field with a default column value
     * @param maxlength //Maximum Length allowed in Text Field
     * @param bannedRegex //Any specified regex which matches this query will be immediately deleted.
     * @param extraAction //Extra action to be performed during document listener update.
     */
    public CustomTextField(int maxlength, String bannedRegex, Runnable extraAction){
        //Runs constructor further down and adds extra item
        this(30,maxlength,bannedRegex);
        this.action = extraAction;
    }

    //Variables to determine features of the CustomTextField;
    private boolean updateDisabled = false;
    private String content = "";
    private int maxLength;
    private DocumentListener listener;
    private String bannedRegex;

    /**
     * Set's the value of the String Content inside the TextBox to a pre-specified value
     * @param content //New Value for String Contents
     */
    private void setContent(String content){
        this.content = content;
    }

    /**
     * Creates a Custom Text Field with a specified column value
     * @param columns //Sets the amount of columns
     * @param maxLength //Maximum Length allowed in Text Field
     * @param bannedRegex //Any specified regex which matches this query will be immediately deleted.
     */
    public CustomTextField(int columns, int maxLength, String bannedRegex){
        super(columns);
        CustomTextField self = this;
        this.bannedRegex = bannedRegex;
        this.maxLength = maxLength;
        this.listener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                update(self);
            }
            public void removeUpdate(DocumentEvent e) {
                update(self);
            }
            public void insertUpdate(DocumentEvent e) {
                update(self);
            }

            /**
             * On Updated Text, The program will perform filters on text and set the text value to new providing the textbox does not call itself.
             * @param self
             */
            public void update(CustomTextField self){
                if (action != null){
                    /**
                     * Runs a Runnable Action if pre-provided, this is usually used to add consequence for changing a value.
                     */
                    action.run();
                }
                if (!updateDisabled){
                    String originalText = self.getText();
                    String text = originalText;
                    text = lengthCheck(text);
                    text = regexFilter(text);
                    if (originalText != text){
                        setContent(text);
                        /**
                         * Can't setText immediately as currently in use, waits until later to change the text value, requiring updateDisabled to prevent self-calling loop.
                         */
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                self.updateDisabled = true;
                                self.setText(content);
                                self.updateDisabled = false;
                            }
                        });
                    }
                }
            }

            /**
             * Trims a String to Pre-specified Length.
             * @param currentText
             * @return
             */
            public String lengthCheck(String currentText) {
                if (currentText.length() > maxLength){
                    currentText = currentText.substring(0, maxLength);
                }
                return currentText;
            }

            /**
             * Replaces all matching regex with empty string in a the Text
             * @param currentText
             * @return
             */
            public String regexFilter(String currentText) {
                if (bannedRegex != null) {
                    currentText = currentText.replaceAll(bannedRegex, "");
                }
                return currentText;
            }
        };
        //Adds the listener to the document
        this.getDocument().addDocumentListener(listener);
    }
}
