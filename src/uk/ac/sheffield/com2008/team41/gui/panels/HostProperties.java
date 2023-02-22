package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.ItemListPane;
import uk.ac.sheffield.com2008.team41.gui.panels.modelPanels.PropertyDisplay;
import uk.ac.sheffield.com2008.team41.models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * HostProperties panel to display all of the hosts properties and allow editing
 * Credit: Team 41 Homebreak submission 2021
 */
public class HostProperties extends DisplayPanel{
    // A Scrolling Pane, literally does as it says, means we can scroll through infinite container height.
    private ItemListPane itemList;
    //Contains all panel information to display
    private DefaultWindow window;
    private Host host;
    private ArrayList<PropertyDisplay> displayedProperties;

    /**
     * Creates a HostProperties panel and uses window for get con and to get user information.
     * @param width
     * @param height
     * @param window
     */
    public HostProperties(int width, int height, DefaultWindow window)
    {
        super((int)(width),height);
        this.window = window;
        host = (Host)window.user;
        this.setBorder(new EmptyBorder(0,0,0,0));
        this.setLayout(new BorderLayout(0,0));
        displayedProperties = new ArrayList<PropertyDisplay>();
        ArrayList<Property> propertiesList = host.getProperties();
        DisplayPanel options = new DisplayPanel(width, (int)(height*0.1));
        //options.setLayout(new BoxLayout(options,BoxLayout.X_AXIS));
        DisplayPanel scrollContainer = new DisplayPanel(width,(int)(height*0.9));
        itemList = new ItemListPane(new DisplayPanel(width,(int)(height*0.9)));
        scrollContainer.setBorder(new EmptyBorder((int)(height*0.1),0,30,15));
        scrollContainer.setLayout(new BoxLayout(scrollContainer, BoxLayout.X_AXIS));
        scrollContainer.addNew(itemList);
        PropertyDisplay.width = (int)(width*0.8);
        PropertyDisplay.height = (int)(height*0.4);
        add(options);
        add(scrollContainer);
        //Create method for adding properties
        addButton("Add Property", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyDisplay property = new PropertyDisplay(window, displayedProperties.size()+1);
                itemList.addElement(property);
                displayedProperties.add(property);
                revalidate();
                repaint();
            }
        },options);

        //Create method for saving changes
        addButton("Save Changes", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Property> properties = new ArrayList<Property>();
                String code = "";
                try{
                    window.getCon().setAutoCommit(false);
                    for (int x = 0; x < displayedProperties.size(); x++){
                        PropertyDisplay property =  displayedProperties.get(x);
                        if (property.isValidPanel()){

                            if (property.getIsNew()){
                                property.createProperty(window.getCon());
                            }
                            else if (property.getNeedsUpdate()){
                                property.updateProperty(window.getCon());
                            }
                        }
                        else{
                            code = property.errorCode;
                            window.getCon().rollback();
                            throw new Exception("Invalid Property Info");
                        }
                    }
                    window.getCon().commit();
                    window.getCon().setAutoCommit(true);
                    host.properties = host.getProperties(window.getCon());
                    JOptionPane.showMessageDialog(window,"Update Successful");
                    window.changeCurrentWindow(new HostProperties(width,height,window));
                }
                catch (Exception exception){
//                    System.out.println(exception.getStackTrace()[0]);
                    try {
                        window.getCon().rollback();
                        window.getCon().setAutoCommit(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(window,code);
                }

            }
        },options);
        itemList.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        //Creates all property display panels for all properties the host owns in db
        for (Property property: propertiesList) {
            PropertyDisplay display = new PropertyDisplay(property, window,displayedProperties.size()+1);
            displayedProperties.add(display);
            itemList.addElement(display);
        }
    }

    /**
     * Adds a button to the host properties panel with default settings
     * @param label
     * @param action
     * @param panel
     */
    public void addButton(String label, ActionListener action, DisplayPanel panel){
        panel.add(Box.createHorizontalStrut(10));
        JButton button = new JButton();
        button.setText(label);
        button.addActionListener(action);
        button.setEnabled(true);
        button.setPreferredSize(new Dimension(100, (int)(height)));
        panel.add(button);
    }
}
