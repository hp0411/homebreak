package uk.ac.sheffield.com2008.team41.gui.panels;

import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Creates DisplayPanel to be used by all future panels
 * Credit: Team 41 Homebreak submission 2021
 */
public class DisplayPanel extends JPanel {
    protected int width;
    protected int height;
    public GridBagConstraints constraints;

    /**
     * Creates a grid panel with base settings and specified height and width
     * @param width
     * @param height
     */
    public DisplayPanel(int width, int height){
        this.width = width;
        this.height = height;
//        System.out.println("width: " + width + ", height: " + height);
        setUp();
    }

    /**
     * Performs basics window setup, setting layout, constraints and background.
     */
    private void setUp(){
        this.setLayout(new GridBagLayout());
        this.constraints = new GridBagConstraints();
        createBorder();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        setSize(width,height);
        setBackground(Color.lightGray);
    }

    /**
     * Creates a 25px border around the panel
     */
    protected void createBorder(){
        this.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
    }

    /**
     * Constructor for default settings that does not set size;
     */
    public DisplayPanel(){
        setUp();
    }

    /**
     * Adds a new element to the current position in the constraints
     * @param element
     */
    public void addNew(Component element){
        add(element, constraints);
    }

    /**
     * Adds a new element to a pre-specified position
     * @param element
     * @param x
     * @param y
     */
    protected void addNew(Component element,int x, int y){
        constraints.gridx = x;
        constraints.gridy = y;
        add(element, constraints);
    }

    /**
     * Adds a new element to a prespecified position
     * @param element
     * @param point
     */
    protected void addNew(Component element,Point point){
        addNew(element,point.x,point.y);
    }

    /**
     * Adds a new JLabel at the current position in constraints
     * @param text String for new JLabel
     * @return new JLabel
     */
    public JLabel addNewLabel(String text){
        JLabel returnLabel = new JLabel(text);
        add(returnLabel, constraints);
        return returnLabel;
    }

    /**
     * Adds a new JLabel at the pre-specified position in constraints
     * @param text String for new JLabel
     * @param x
     * @param y
     * @return new JLabel
     */
    protected JLabel addNewLabel(String text, int x, int y){
        constraints.gridx = x;
        constraints.gridy = y;
        return addNewLabel(text);
    }

    /**
     * Adds a new JLabel at the pre-specified position in constraints
     * @param text String for new JLabel
     * @param point
     * @return new JLabel
     */
    protected JLabel addNewLabel(String text, Point point){
        constraints.gridx = point.x;
        constraints.gridy = point.y;
        return addNewLabel(text);
    }

    /**
     * Adds a new element to a pre-specified position with a set width
     * @param element
     * @param x
     * @param y
     * @param width
     */
    protected void addNew(Component element,int x, int y, int width){
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = 1;
        add(element, constraints);
        constraints.weightx = 0;
        constraints.gridwidth = 1;
    }

    /**
     * Adds a new element to a pre-specified position with a specified width
     * @param element
     * @param point
     * @param width
     */
    protected void addNew(Component element,Point point, int width){
        addNew(element,point.x,point.y,width);
    }

    /**
     * Returns a list of checkboxes and last location when adding to a large amount of positions
     * Creates Checkboxes from a list string in a 2d format of pre-specified width
     * @param checkboxNames
     * @param position starting position
     * @param maxItemWidth 2d with
     * @return [checkboxHashMap, Position]
     */
    public ArrayList<Object> addCheckboxFromList(List<String> checkboxNames, Point position, int maxItemWidth){
        HashMap<String,JCheckBox> checkBoxHashMap = new HashMap<String,JCheckBox>();
        int rowItems = maxItemWidth;
        for(int x = 0; x < checkboxNames.size(); x++){
            position.x = x%rowItems;
            if (x%rowItems == 0) {
                position.y += 1;
            }
            String text = checkboxNames.get(x);
            JCheckBox button = new JCheckBox(text,false);
            button.setBackground(Color.gray);
            checkBoxHashMap.put(text,button);
            addNew(button,position);
        }
        return new ArrayList<Object>(Arrays.asList(checkBoxHashMap,position));
    }

    /**
     * Shows an error in a field and set the border to red
     * @param field Field parameter to be modified
     * @param error Label to change
     * @param message Message to show in label
     */
    protected void showErrorField(JComponent field, JLabel error, String message){
    	field.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
    	error.setText(message);
    }

    /**
     * If field is empty show error else reset error and don't show
     * @param input
     * @param field
     * @param error
     * @param message
     * @return isEmpty
     */
    protected boolean isEmptyField(String input, JComponent field, JLabel error, String message) {
    	 error.setForeground(Color.RED);
    	 if (input.equals("")) {
    		 showErrorField(field, error, message);
    		 return true;
    	 }
    	 else {
    		 field.setBorder(null);
    		 error.setText(""); 
    	 }
    	 return false;
    }
}
