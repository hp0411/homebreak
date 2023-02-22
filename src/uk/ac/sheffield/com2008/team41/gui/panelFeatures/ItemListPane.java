package uk.ac.sheffield.com2008.team41.gui.panelFeatures;

import uk.ac.sheffield.com2008.team41.gui.panels.DisplayPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Extends ScrollPane to set default information as well as make it easily usable by other team members wanting to make infinite height panels via scrolling.
 * Credit: Team 41 Homebreak submission 2021
 */
public class ItemListPane extends JScrollPane {
    public DisplayPanel view;
    public Dimension size;

    //Current index, so if replacing elements, the current position isn't lost
    private int yIndex = 0;
    ArrayList<Component> elements = new ArrayList<Component>();

    /**
     * Sets default information for the ScrollPane
     * Takes DisplayPanel as parameter as it cannot create any components before super is initiated.
     * @param showPanel
     */
    public ItemListPane(DisplayPanel showPanel){
        super(showPanel,VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        setSize(showPanel.getSize());
        showPanel.setLayout(new GridBagLayout());
        view = showPanel;
        view.constraints.insets = new Insets(3,3,3,3);
        view.constraints.gridx = 0;
        view.constraints.gridy = 0;
        view.constraints.fill = GridBagConstraints.HORIZONTAL;

    }

    /**
     * Adds an element to the ItemListPane, to next index below
     * @param element // Element to be added
     */
    public void addElement(Component element) {
        view.constraints.gridy = yIndex;
        yIndex += 1;
        elements.add(element);
        view.addNew(element);
    }

    /**
     * Replaces the component at the specified index
     * @param index //Index to be replaced
     * @param element //Element to replace old Element
     */
    public void replaceElement(int index, Component element){
        if (index < yIndex){
            Component old = elements.get(index);

            elements.set(index,element);
            view.constraints.gridy = index;
            view.remove(old);
            view.add(element);
        }
    }

    /**
     * Deletes all elements from the itemList
     */
    public void deleteAll(){
        view.removeAll();
        elements = new ArrayList<Component>();
        yIndex = 0;
    }
}
