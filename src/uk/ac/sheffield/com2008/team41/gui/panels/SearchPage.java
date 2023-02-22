package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.CustomTextField;
import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static uk.ac.sheffield.com2008.team41.models.Property.searchProperty;

/**
 * SearchPage which allows a user to search through properties
 * Credit: Team 41 Homebreak submission 2021
 */
public class SearchPage extends DisplayPanel{

    DefaultWindow window;
    CustomTextField shortNameField;
    CustomTextField generalLocation;
    JCheckBox hasBreakfastCB;
    ArrayList<JCheckBox> sleepingFCB;
    ArrayList<JCheckBox> bathingFCB;
    ArrayList<JCheckBox> kitchenFCB;
    ArrayList<JCheckBox> livingFCB;
    ArrayList<JCheckBox> utilityFCB;
    ArrayList<JCheckBox> outdoorFCB;
    JButton submit;
    ArrayList<Object> amen;
    ArrayList<Property> propertyArrayList;
    JLabel output = new JLabel("");
    JLabel error = new JLabel("");
    JComboBox<String> jComboBox;
    JButton viewProperty;
    JButton bookProperty;
    Boolean addCB = false;

    /**
     * Creates a search page with a set width, height and parent window
     * @param width
     * @param height
     * @param window
     */
    public SearchPage(int width, int height, DefaultWindow window){
        super(width,height);
        this.window = window;
        DisplayPanel self = this;
        createBorder();
        constraints.insets = new Insets(3,3,3,3);
        constraints.gridx=0;
        constraints.gridy=0;
        addNewLabel("Search Page Time");

        // Property Details
        constraints.gridx=0;
        constraints.gridy=1;
        addNewLabel("Short Name");
        constraints.gridx=1;
        constraints.gridwidth = 6;
        shortNameField = new CustomTextField(50,null);
        addNew(shortNameField);

        constraints.gridwidth = 1;
        constraints.gridx=0;
        constraints.gridy=2;
        addNewLabel("General Location");
        constraints.gridx=1;
        generalLocation = new CustomTextField(100,null);
        constraints.gridwidth = 6;
        addNew(generalLocation);
//
        constraints.gridx=0;
        constraints.gridy=3;
        constraints.gridwidth = 1;
        hasBreakfastCB = new JCheckBox("Has BreakFast");
        hasBreakfastCB.setBackground(Color.lightGray);
        addNew(hasBreakfastCB);

        constraints.gridx=0;
        constraints.gridy=5;

        sleepingFCB = new ArrayList<>();
        addNewLabel("Sleeping Facilities ");
        int counter = 6;
        for (int i = 0; i < SleepingFacilities.allAmenities.size(); i++){
            constraints.gridx=0;
            constraints.gridy=counter + i;
            JCheckBox temp = new JCheckBox(SleepingFacilities.allAmenities.get(i));
            temp.setBackground(Color.lightGray);
            sleepingFCB.add(temp);
            addNew(temp);
        }
        constraints.gridx=1;
        constraints.gridy=5;
        bathingFCB = new ArrayList<>();
        addNewLabel("Bathing Facilities ");
        for (int i = 0; i < BathingFacilities.allAmenities.size(); i++){
            constraints.gridx=1;
            constraints.gridy=counter + i;
            JCheckBox temp = new JCheckBox(BathingFacilities.allAmenities.get(i));
            temp.setBackground(Color.lightGray);
            bathingFCB.add(temp);
            addNew(temp);
        }

        constraints.gridx=2;
        constraints.gridy=5;
        kitchenFCB = new ArrayList<>();
        addNewLabel("Kitchen Facilities ");
        for (int i = 0; i < KitchenFacility.allAmenities.size(); i++){
            constraints.gridx=2;
            constraints.gridy=counter + i;
            JCheckBox temp = new JCheckBox(KitchenFacility.allAmenities.get(i));
            temp.setBackground(Color.lightGray);
            kitchenFCB.add(temp);
            addNew(temp);
        }

        constraints.gridx=3;
        constraints.gridy=5;
        livingFCB = new ArrayList<>();
        addNewLabel("Living Facilities ");
        for (int i = 0; i < LivingFacilities.allAmenities.size(); i++){
            constraints.gridx=3;
            constraints.gridy=counter + i;
            JCheckBox temp = new JCheckBox(LivingFacilities.allAmenities.get(i));
            temp.setBackground(Color.lightGray);
            livingFCB.add(temp);
            addNew(temp);

        }

        constraints.gridx=4;
        constraints.gridy=5;
        utilityFCB = new ArrayList<>();
        addNewLabel("Utility Facilities ");
        for (int i = 0; i < UtilityFacilities.allAmenities.size(); i++){
            constraints.gridx=4;
            constraints.gridy=counter + i;
            JCheckBox temp = new JCheckBox(UtilityFacilities.allAmenities.get(i));
            temp.setBackground(Color.lightGray);
            utilityFCB.add(temp);
            addNew(temp);

        }

        constraints.gridx=5;
        constraints.gridy=5;
        outdoorFCB = new ArrayList<>();
        addNewLabel("Outdoor Facilities ");
        for (int i = 0; i < OutdoorFacilities.allAmenities.size(); i++){
            constraints.gridx=5;
            constraints.gridy=counter + i;
            JCheckBox temp = new JCheckBox(OutdoorFacilities.allAmenities.get(i));
            temp.setBackground(Color.lightGray);
            outdoorFCB.add(temp);
            addNew(temp);
        }

        submit = new JButton("Submit");
        constraints.gridwidth = 6;
        constraints.gridx=0;
        constraints.gridy=15;
        addNew(submit);

        constraints.gridx=0;
        constraints.gridy=16;
        addNew(output);
        constraints.gridwidth = 1;

        //Adds search method
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DriverHelper driverHelper = new DriverHelper(window.getCon());
                ArrayList<Object> prop = getProp();
                ArrayList<Object> sleep = getFacility(sleepingFCB, SleepingFacilities.allAmenities);
                ArrayList<Object> bath = getFacility(bathingFCB,  BathingFacilities.allAmenities);
                ArrayList<Object> kit = getFacility(kitchenFCB, KitchenFacility.allAmenities);
                ArrayList<Object> liv = getFacility(livingFCB, LivingFacilities.allAmenities);
                ArrayList<Object> util = getFacility(utilityFCB, UtilityFacilities.allAmenities);
                ArrayList<Object> out = getFacility(outdoorFCB, OutdoorFacilities.allAmenities);
                propertyArrayList = new ArrayList<>();

                //Performs Query
                output.setForeground(Color.BLACK);
                try {
                    PreparedStatement preparedStatement = searchProperty(window.getCon(), prop , sleep, bath, kit, liv, util, out);
                    ResultSet rs = driverHelper.execSafeQuery(preparedStatement);
                    while (rs.next()){
                        propertyArrayList.add(new Property(window.getCon(), rs.getInt("propertyID")));
                    }
                    preparedStatement.close();
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                //Checks if results found
                if (propertyArrayList.isEmpty()){
                    output.setText("Sorry no properties found. Search Again with different parameters");
                    if (addCB){
                        remove(jComboBox);
                        remove(viewProperty);
                        remove(bookProperty);
                        revalidate();
                        repaint();
                    }

                }
                else {
                    output.setText("Properties found. Select Property");
                    HashMap<String, Integer> propNumList = new HashMap<String, Integer>();
                    jComboBox = new JComboBox<>();
                    for (int i = 0; i < propertyArrayList.size(); i++) {
                        propNumList.put("Property Number " + (i + 1), i);
                        jComboBox.addItem("Property Number " + (i + 1));
                    }
                    constraints.gridx = 0;
                    constraints.gridy = 17;
                    addNew(jComboBox);
                    addCB = true;

                    //Sets view property if result found to visible
                    constraints.gridx = 0;
                    constraints.gridy = 18;
                    viewProperty = new JButton("View Property");
                    addNew(viewProperty);
                    viewProperty.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String chosenPropNum = (String) jComboBox.getSelectedItem();
                            Property chosenProp = propertyArrayList.get(propNumList.get(chosenPropNum));
                            window.changeCurrentWindow(new ViewProperty(width, height, window, chosenProp, self));
                        }
                    });

                    //Sets book property if result found to visible
                    constraints.gridx = 1;
                    constraints.gridy = 18;
                    bookProperty = new JButton("Book Property");
                    addNew(bookProperty);
                    bookProperty.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String chosenPropNum = (String) jComboBox.getSelectedItem();
                            Property chosenProp = propertyArrayList.get(propNumList.get(chosenPropNum));
                            window.changeCurrentWindow(new BookingPage(width, height, window, chosenProp));

                        }
                    });
                }
              
            }
        });
    }

    /**
     * Gets the property array list items for query
     * @return QueryItem List
     */
    private ArrayList<Object> getProp(){
        ArrayList<Object> prop = new ArrayList<>();
        prop.add("%" + shortNameField.getText() + "%");
        prop.add("%" + generalLocation.getText() + "%");
        if (hasBreakfastCB.isSelected()){
            prop.add(true);
        }
        else{
            prop.add(false);
        }

        return prop;
    }

    /**
     * If checkboxes and amenities are of the same height, then it will return any indexes of amenities which are parallel to a selected checkbox.
     * @param checkboxes
     * @param amenities
     * @return
     */
    private ArrayList<Object> getFacility(ArrayList<JCheckBox> checkboxes, List<String> amenities){
        amen = new ArrayList<>();
        for (int i = 0; i < checkboxes.size(); i++){
            if (checkboxes.get(i).isSelected()){
                amen.add(amenities.get(i));
            }
        }
        return amen;
    }
}
