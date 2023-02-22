package uk.ac.sheffield.com2008.team41.gui.panels.modelPanels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panels.DisplayPanel;
import uk.ac.sheffield.com2008.team41.models.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static uk.ac.sheffield.com2008.team41.helper.FunctionHelper.readOnlyCheckbox;

/**
 * PropertyInfo, Not really needed, but it helps distinguish all property information and makes code cleaner
 * Used for displaying properties after searching
 * Credit: Team 41 Homebreak submission 2021
 */
public class PropertyInfo extends DisplayPanel {
    private SleepingFacilities sleepingFacilities;
    private BathingFacilities bathingFactilites;
    private KitchenFacility kitchenFacility;
    private LivingFacilities livingFacilities;
    private UtilityFacilities utilityFacilities;
    private OutdoorFacilities outdoorFacilities;
    Property chosenProp;

    /**
     * Creates a Panel to display property information
     * @param width Width of panel
     * @param height Height of panel
     * @param chosenProp The panel of which information will be shown
     * @param window //Window so getCon can be used
     */
    public PropertyInfo(int width, int height, Property chosenProp, DefaultWindow window){
        super(width,height);
        //Removes the border for decent spacing
        this.setBorder(new EmptyBorder(0,0,0,15));
        this.chosenProp = chosenProp;

        createBorder();
        constraints.insets = new Insets(3,3,3,3);

        this.sleepingFacilities = chosenProp.getSleepingFacilities();
        this.bathingFactilites = chosenProp.getBathingFacilities();
        this.kitchenFacility = chosenProp.getKitchenFacility();
        this.livingFacilities = chosenProp.getLivingFacilities();
        this.utilityFacilities = chosenProp.getUtilityFacilities();
        this.outdoorFacilities = chosenProp.getOutdoorFacilities();

        Host host = chosenProp.getHost();

        //Sets all labels and checkboxes and fields, and creates the gui
        Point current = new Point(0,0);
        constraints.gridy = 0;
        addNewLabel("Property Name: " + chosenProp.getShortName());
        constraints.gridy = 1;
        addNewLabel("Property General Location: " + chosenProp.getGeneralLocation());
        constraints.gridy = 2;
        addNewLabel("Property Description: ");

        constraints.gridy = 3;
        constraints.gridwidth = 7;
        JTextArea desc = new JTextArea(chosenProp.getDescription());
        Border border = BorderFactory.createLineBorder(Color.black);
        desc.setBorder(border);
        desc.setEditable(false);
        addNew(desc);
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        addNew(readOnlyCheckbox(new JCheckBox("Has Breakfast",chosenProp.getHasBreakfast())));

        constraints.gridx = 2;
        constraints.gridy = 0;
        addNewLabel("Number Of Bedrooms: " + sleepingFacilities.calcBedrooms());

        constraints.gridx = 2;
        constraints.gridy = 1;
        addNewLabel("Number Of Beds: " + sleepingFacilities.calcBeds());

        constraints.gridx = 2;
        constraints.gridy = 2;
        addNewLabel("Number Of Sleepers: " + sleepingFacilities.calcSleeper());


        constraints.gridx = 5;
        constraints.gridy = 0;
        addNewLabel("Host Name: " + host.getHostName());

        constraints.gridx = 5;
        constraints.gridy = 1;
        addNewLabel("Is SuperHost: " + host.isSuperhost(window.getCon()));

        constraints.gridx = 5;
        constraints.gridy = 2;
        addNewLabel("Property AVG Score: " + Review.calcPropRating(window.getCon(), chosenProp.getPropertyID()));

        int facilityY = 5;
        constraints.gridx = 0;
        constraints.gridy = facilityY;
        addNewLabel("Sleeping Facilities ");
        for (String amenity : SleepingFacilities.allAmenities){
            constraints.gridy += 1;
            addNew(readOnlyCheckbox(new JCheckBox("Has "+amenity,sleepingFacilities.getAmenity(amenity))));
        }

        constraints.gridx = 1;
        constraints.gridy = facilityY;
        addNewLabel("Bathing Facilities ");
        for (String amenity : BathingFacilities.allAmenities){
            constraints.gridy += 1;
            addNew(readOnlyCheckbox(new JCheckBox("Has "+amenity,bathingFactilites.getAmenity(amenity))));
        }

        constraints.gridx = 2;
        constraints.gridy = facilityY;
        addNewLabel("Kitchen Facilities ");
        for (String amenity : KitchenFacility.allAmenities){
            constraints.gridy += 1;
            addNew(readOnlyCheckbox(new JCheckBox("Has "+amenity,kitchenFacility.getAmenity(amenity))));
        }

        constraints.gridx = 3;
        constraints.gridy = facilityY;
        addNewLabel("Living Facilities ");
        for (String amenity : LivingFacilities.allAmenities){
            constraints.gridy += 1;
            addNew(readOnlyCheckbox(new JCheckBox("Has "+amenity,livingFacilities.getAmenity(amenity))));
        }

        constraints.gridx = 4;
        constraints.gridy = facilityY;
        addNewLabel("Utility Facilities ");
        for (String amenity : UtilityFacilities.allAmenities){
            constraints.gridy += 1;
            addNew(readOnlyCheckbox(new JCheckBox("Has "+amenity,utilityFacilities.getAmenity(amenity))));
        }

        constraints.gridx = 5;
        constraints.gridy = facilityY;
        addNewLabel("Outdoor Facilities ");
        for (String amenity : OutdoorFacilities.allAmenities){
            constraints.gridy += 1;
            addNew(readOnlyCheckbox(new JCheckBox("Has "+amenity,outdoorFacilities.getAmenity(amenity))));
        }
    }
}
