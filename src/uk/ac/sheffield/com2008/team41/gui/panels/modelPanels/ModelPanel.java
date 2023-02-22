package uk.ac.sheffield.com2008.team41.gui.panels.modelPanels;

import uk.ac.sheffield.com2008.team41.gui.panels.DisplayPanel;

import javax.swing.*;
import java.awt.*;

/**
 * ModelPanel Abstract class to make other panels
 * Credit: Team 41 Homebreak submission 2021
 */
public abstract class ModelPanel extends DisplayPanel {
    protected Object obj;
    protected Point current;
    protected boolean isNew;
    protected boolean needsUpdate;
    //Error Code encase of validation error
    public String errorCode;

    /**
     * @param obj Object already exists, so is set in database
     * @param number Increment number to create display
     * @param name Name of Object to help create display
     */
    public ModelPanel(Object obj, int number, String name){
        this.obj = obj;
        panelSetup(number,name);
        isNew = true;
    }

    /**
     * Creates the base panel structure, including a title from number and name
     * @param number number for title
     * @param name name for title
     */
    private void panelSetup(int number, String name){
        constraints.insets = new Insets(3,3,3,3);
        setSize(new Dimension(PropertyDisplay.width,(int)(PropertyDisplay.height)));
        setBackground(Color.gray);
        JLabel textLabel = new JLabel(name + " " + number);
        addNew(textLabel,0,0);
        current = new Point(0,1);
    }
    /**
     * @param number number for title
     * @param name name for title
     */
    public ModelPanel(int number, String name){
        panelSetup(number,name);
    }

    /**
     * Determines if object needs an update and is possibly different to contents in panel
     * @return
     */
    public boolean isNeedsUpdate(){
        return needsUpdate;
    }

    /**
     * @return is the object in this panel new
     */
    public boolean isNew(){
        return isNew;
    }

    /**
     * @return Are the inputs to this panel valid
     */
    public abstract boolean isValidPanel();

}
