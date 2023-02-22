package uk.ac.sheffield.com2008.team41.gui.panels.modelPanels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.CustomTextField;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.ItemListPane;
import uk.ac.sheffield.com2008.team41.gui.panels.DisplayPanel;
import uk.ac.sheffield.com2008.team41.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static uk.ac.sheffield.com2008.team41.helper.FunctionHelper.BetweenDates;

/**
 * PropertyDisplay class to be used by an ItemListPane to display a large amount of properties and allow for easy editing and modification
 * Credit: Team 41 Homebreak submission 2021
 */
public class PropertyDisplay extends DisplayPanel {
    public static int height = 0;
    public static int width = 0;
    //Max width of columns, changing this value affects presentation
    private static int maxItemWidth = 4;

    public Property property;
    private CustomTextField shortName;
    private CustomTextField description;
    private JCheckBox hasBreakfast;
    private CustomTextField generalLocation;
    //Action listener to setUpdated so we know if the property requires an update.
    private ActionListener updateListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            setUpdated();
        }
    };
    private boolean isNew = false;
    private boolean needsUpdate = false;
    /**
     * HashMaps for easy checkbox access and iteration
     */
    private HashMap<String,JCheckBox> sleepingCheckboxes = new HashMap<String, JCheckBox>();
    private HashMap<String,JCheckBox> bathingCheckboxes = new HashMap<String, JCheckBox>();
    private HashMap<String,JCheckBox> kitchenCheckboxes = new HashMap<String, JCheckBox>();
    private HashMap<String,JCheckBox> livingCheckboxes = new HashMap<String, JCheckBox>();
    private HashMap<String,JCheckBox> outdoorCheckboxes = new HashMap<String, JCheckBox>();
    private HashMap<String,JCheckBox> utilityCheckboxes = new HashMap<String, JCheckBox>();
    private List<BedroomPanel> bedroomPanels = new ArrayList<BedroomPanel>();
    private List<BathroomPanel> bathroomPanels = new ArrayList<BathroomPanel>();
    private List<ChargeBandPanel> chargebandPanels = new ArrayList<ChargeBandPanel>();
    private ItemListPane bedroomPanelsPane;
    private ItemListPane chargebandPanelsPane;
    private ItemListPane bathroomPanelsPane;
    private AddressPanel addressPanel;
    private DefaultWindow window;
    public String errorCode = "";

    /**
     * SetUpdated command to be used by updateListener and other actionListeners
     */
    public void setUpdated(){
        needsUpdate = true;
    }

    /**
     * Returns if the property inputs are valid including all sub panels.
     * @return Is Valid Property
     */
    public boolean isValidPanel(){
        if (shortName.getText().length() <= 0){
            errorCode = "Invalid Short Name";
            return false;
        }
        if (description.getText().length() <= 0){
            errorCode = "Invalid Description";
            return false;
        }
        if (generalLocation.getText().length() <= 0){
            errorCode = "General Location";
            return false;
        }
        if (bedroomPanels.size() < 1){
            errorCode = "Minimum of One Bedroom Required";
            return false;
        }
        if (bathroomPanels.size() < 1){
            errorCode = "Minimum of One Bathroom Required";
            return false;
        }
        if (chargebandPanels.size() < 1){
            errorCode = "Minimum of One Chargeband Required";
            return false;
        }
        if (!panelsValid()){
            return false;
        }
        return true;

    }

    /**
     * Sets up the GUI with all contained panels, labels and inputs
     * @param index Property Index depending How many properties user owns
     */
    public void setUp(int index){
        PropertyDisplay self = this;

        setBackground(Color.gray);

        addNew(new JLabel("Property " + index),0,0);
        addNew(new JLabel("Property Shortname: "),0,1);
        constraints.gridwidth =  maxItemWidth;
        shortName = new CustomTextField(30,null, this::setUpdated);
        addNew(shortName,1,1);
        constraints.gridwidth =  1;
        addNew(new JLabel("Description: "),0,2);
        constraints.gridwidth =  maxItemWidth;
        description = new CustomTextField(150,null, this::setUpdated);
        addNew(description,1,2);
        constraints.gridwidth =  1;
        addNew(new JLabel("General Location: "),0,3);
        constraints.gridwidth =  maxItemWidth;
        generalLocation = new CustomTextField(30,30,null);
        addNew(generalLocation,1,3);
        hasBreakfast = new JCheckBox ("Has Breakfast",false);
        hasBreakfast.setBackground(Color.gray);
        hasBreakfast.addActionListener(updateListener);
        addNew(hasBreakfast,0,4);
        constraints.insets = new Insets(3,3,3,3);
        constraints.gridwidth =  1;

        //Adds all facilities and creates hashmaps for facilities whilst also incrementing display index;
        addNew(new JLabel("Bedroom Facilities: "),0,5);
        Point current = new Point(0,6);
        ArrayList<Object> temp = addCheckboxFromList(SleepingFacilities.allAmenities,current,maxItemWidth);
        current = (Point) temp.get(1);
        sleepingCheckboxes = (HashMap<String, JCheckBox>) temp.get(0);

        current.x = 0;
        current.y += 1;
        addNew(new JLabel("Bathing Facilities: "),current);
        current.y += 1;
        temp = addCheckboxFromList(BathingFacilities.allAmenities,current,maxItemWidth);
        current = (Point) temp.get(1);
        bathingCheckboxes = (HashMap<String, JCheckBox>) temp.get(0);

        current.x = 0;
        current.y += 1;
        addNew(new JLabel("Kitchen Facilities: "),current);
        current.y += 1;
        temp = addCheckboxFromList(KitchenFacility.allAmenities,current,maxItemWidth);
        current = (Point) temp.get(1);
        kitchenCheckboxes = (HashMap<String, JCheckBox>) temp.get(0);

        current.x = 0;
        current.y += 1;
        addNew(new JLabel("Living Facilities: "),current);
        current.y += 1;
        temp = addCheckboxFromList(LivingFacilities.allAmenities,current,maxItemWidth);
        current = (Point) temp.get(1);
        livingCheckboxes = (HashMap<String, JCheckBox>) temp.get(0);

        current.x = 0;
        current.y += 1;
        addNew(new JLabel("Outdoor Facilities: "),current);
        current.y += 1;
        temp = addCheckboxFromList(OutdoorFacilities.allAmenities,current,maxItemWidth);
        current = (Point) temp.get(1);
        outdoorCheckboxes = (HashMap<String, JCheckBox>) temp.get(0);

        current.x = 0;
        current.y += 1;
        addNew(new JLabel("Utility Facilities: "),current);
        current.y += 1;
        temp = addCheckboxFromList(UtilityFacilities.allAmenities,current,maxItemWidth);
        current = (Point) temp.get(1);
        utilityCheckboxes = (HashMap<String, JCheckBox>) temp.get(0);

        //Allows Bedrooms to be easily created inside a maxed out ItemListPane
        current.x = 0;
        current.y += 1;
        constraints.gridwidth =  maxItemWidth;
        addNew(new JLabel("Bedrooms: "),current);
        JButton bedroomButton = new JButton("Add Bedroom");
        current.y += 1;
        addNew(bedroomButton,current);
        current.y += 1;
        bedroomPanelsPane = new ItemListPane(new DisplayPanel(width,(int)(height*0.8)));
        bedroomPanelsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        addNew(bedroomPanelsPane,current);

        //Allows Bathrooms to be easily created inside a maxed out ItemListPane
        current.y += 1;
        addNew(new JLabel("Bathrooms: "),current);
        JButton bathroomButton = new JButton("Add Bathroom");
        current.y += 1;
        addNew(bathroomButton,current);
        current.y += 1;
        bathroomPanelsPane = new ItemListPane(new DisplayPanel(width,(int)(height*0.8)));
        bathroomPanelsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        addNew(bathroomPanelsPane,current);

        //Allows Chargebands to be easily created inside a maxed out ItemListPane
        current.y += 1;
        addNew(new JLabel("Chargebands: "),current);
        JButton chargeBandButton = new JButton("Add Chargeband");
        current.y += 1;
        addNew(chargeBandButton,current);
        current.y += 1;
        chargebandPanelsPane = new ItemListPane(new DisplayPanel(width,(int)(height*0.8)));
        chargebandPanelsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        addNew(chargebandPanelsPane,current);
        current.y += 1;
        addNew(new JLabel("Address: "),current);
        current.y += 1;
        addNew(addressPanel,current);
        constraints.gridwidth =  1;

        //Creates listeners for Create Bedroom/Bathroom and ChargeBand Buttons.
        bedroomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BedroomPanel bedroomPanel = new BedroomPanel(bedroomPanels.size() + 1);
                bedroomPanelsPane.addElement(bedroomPanel);
                bedroomPanels.add(bedroomPanel);
                Component parent = self.getParent();
                needsUpdate = true;
                parent.revalidate();
                parent.repaint();
            }
        });

        bathroomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BathroomPanel bathroomPanel = new BathroomPanel(bathroomPanels.size() + 1);
                bathroomPanelsPane.addElement(bathroomPanel);
                bathroomPanels.add(bathroomPanel);
                Component parent = self.getParent();
                needsUpdate = true;
                parent.revalidate();
                parent.repaint();
            }
        });

        chargeBandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChargeBandPanel chargeBandPanel = new ChargeBandPanel(chargebandPanels.size() + 1);
                chargebandPanelsPane.addElement(chargeBandPanel);
                chargebandPanels.add(chargeBandPanel);
                Component parent = self.getParent();
                needsUpdate = true;
                parent.revalidate();
                parent.repaint();
            }
        });
    }

    /**
     * Creates the property from all inputs to be saved as an object and to be added to the db
     * @param con Connection to the MySQL Database
     * @return Property
     * @throws SQLException Failure in creating property or cannot connect to db
     */
    public Property createProperty(Connection con) throws SQLException {
        Property property = new Property(con,addressPanel.createNewAddress(con),(Host) (window.user),shortName.getText(),description.getText(),generalLocation.getText(),hasBreakfast.isSelected());
        property.chargeBands = new ArrayList<ChargeBand>();
        property.bathrooms = new ArrayList<Bathroom>();
        for (int x = 0; x < chargebandPanels.size(); x++){
            property.chargeBands.add(chargebandPanels.get(x).createChargeband(con,property.getPropertyID()));
        }
        for (int x = 0; x < bathroomPanels.size(); x++){
            property.bathrooms.add(bathroomPanels.get(x).createBathroom(con,property.getPropertyID()));
        }
        //Adds all kitchen facilities
        ArrayList<String> currentFacilities = new ArrayList<String>();
        for (HashMap.Entry<String, JCheckBox> entry : kitchenCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()){
                currentFacilities.add(entry.getKey());
            }
        }
        property.kitchenFacility = new KitchenFacility(con, property.getPropertyID(),currentFacilities);

        //Adds all kitchen facilities
        currentFacilities = new ArrayList<String>();
        for (HashMap.Entry<String, JCheckBox> entry : bathingCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()){
                currentFacilities.add(entry.getKey());
            }
        }
        property.bathingFacilities = new BathingFacilities(con,currentFacilities,property.getPropertyID());

        //Adds all living facilities
        currentFacilities = new ArrayList<String>();
        for (HashMap.Entry<String, JCheckBox> entry : livingCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()){
                currentFacilities.add(entry.getKey());
            }
        }
        property.livingFacilities = new LivingFacilities(con,currentFacilities, property.getPropertyID());

        //Adds all living facilities
        currentFacilities = new ArrayList<String>();
        for (HashMap.Entry<String, JCheckBox> entry : outdoorCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()){
                currentFacilities.add(entry.getKey());
            }
        }
        property.outdoorFacilities = new OutdoorFacilities(con,currentFacilities, property.getPropertyID());

        //Adds all living facilities
        currentFacilities = new ArrayList<String>();
        for (HashMap.Entry<String, JCheckBox> entry : utilityCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()){
                currentFacilities.add(entry.getKey());
            }
        }
        property.utilityFacilities = new UtilityFacilities(con,currentFacilities, property.getPropertyID());

        currentFacilities = new ArrayList<String>();
        for (HashMap.Entry<String, JCheckBox> entry : sleepingCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()){
                currentFacilities.add(entry.getKey());
            }
        }

        ArrayList<Bedroom> tempBedrooms = new ArrayList<Bedroom>();
        for (int x = 0; x < bedroomPanels.size(); x++){
            tempBedrooms.add(bedroomPanels.get(x).createBedroom(con,property.getPropertyID()));
        }
        property.sleepingFacilities = new SleepingFacilities(con,currentFacilities,tempBedrooms, property.getPropertyID());
        return property;
    }

    /**
     * Checks that all of the sub panels used to create properties are also valid to help prevent any later steps failure when sending to the db.
     * @return
     */
    public boolean panelsValid(){
        for (int x = 0; x < bedroomPanels.size(); x++){
            BedroomPanel panel = bedroomPanels.get(x);
            if (!panel.isValid()){
                errorCode = "Bedroom " + (x+1) + ": " + panel.errorCode;
                return false;
            }
        }
        ArrayList<LocalDate[]> dates = new ArrayList<>();
        for (int x = 0; x < chargebandPanels.size(); x++){
            ChargeBandPanel panel = chargebandPanels.get(x);
            LocalDate[] panelDate = panel.dates();
            for (int y = 0; y < dates.size(); y++){
                if ( BetweenDates(dates.get(y)[0],panelDate[0],dates.get(y)[1]) || BetweenDates(dates.get(y)[0],panelDate[1],dates.get(y)[1])){
                    errorCode = "Charge Band: Date Collision Error";
                    return false;
                }
            }
            if (!panel.isValidPanel()){
                errorCode = "Chargeband " + (x+1) + ": " + panel.errorCode;
                return false;
            }
            else{
                dates.add(panelDate);
            }
        }
        if (!addressPanel.isValidPanel()){
            errorCode = "Address: " + addressPanel.errorCode;
            return false;
        }
        return true;
    }

    /**
     * Creates a Property Display and sets all information from a pre-existing property
     * @param property Property to get information from
     * @param window Window to be used and referred to when getting Con
     * @param index Index relating to how many properties a User owns.
     */
    public PropertyDisplay(Property property, DefaultWindow window, int index){
        this.window = window;
        addressPanel = new AddressPanel(property.getAddress(),width,(int)(height*0.3));
        setUp(index);
        isNew = false;
        //Set all property info required and disable some constant inputs
        addressPanel.setEnabled(false);
        shortName.setText(property.getShortName());
        description.setText(property.getDescription());
        generalLocation.setText(property.getGeneralLocation());
        generalLocation.setEnabled(false);
        hasBreakfast.setSelected(property.getHasBreakfast());
        this.property = property;

        //Goes through all facilities/amenities setting checkboxes
        BathingFacilities bathingFacilities = property.getBathingFacilities();
        for (String amenity : BathingFacilities.allAmenities){
            bathingCheckboxes.get(amenity).setSelected(bathingFacilities.getAmenity(amenity));
        }

        KitchenFacility kitchenFacilities = property.getKitchenFacility();
        for (String amenity : KitchenFacility.allAmenities){
            kitchenCheckboxes.get(amenity).setSelected(kitchenFacilities.getAmenity(amenity));
        }

        LivingFacilities livingFacilities = property.getLivingFacilities();
        for (String amenity : LivingFacilities.allAmenities){
            livingCheckboxes.get(amenity).setSelected(livingFacilities.getAmenity(amenity));
        }

        OutdoorFacilities outdoorFacilities = property.getOutdoorFacilities();
        for (String amenity : OutdoorFacilities.allAmenities){
            outdoorCheckboxes.get(amenity).setSelected(outdoorFacilities.getAmenity(amenity));
        }

        SleepingFacilities sleepingFacilities = property.getSleepingFacilities();
        for (String amenity : SleepingFacilities.allAmenities){
            sleepingCheckboxes.get(amenity).setSelected(sleepingFacilities.getAmenity(amenity));
        }

        UtilityFacilities utilityFacilities = property.getUtilityFacilities();
        for (String amenity : UtilityFacilities.allAmenities){
            utilityCheckboxes.get(amenity).setSelected(utilityFacilities.getAmenity(amenity));
        }

        for (int x = 0; x < property.chargeBands.size(); x++){
            ChargeBand tempBand = property.chargeBands.get(x);
            ChargeBandPanel panel = new ChargeBandPanel(tempBand,x+1);
            chargebandPanelsPane.addElement(panel);
            chargebandPanels.add(panel);
        }

        List<Bedroom> bedrooms = property.getSleepingFacilities().bedrooms;
        for (int x = 0; x < bedrooms.size(); x++){
            Bedroom tempBedroom = bedrooms.get(x);
            BedroomPanel panel = new BedroomPanel(tempBedroom,x+1);
            bedroomPanelsPane.addElement(panel);
            bedroomPanels.add(panel);
        }

        for (int x = 0; x < property.bathrooms.size(); x++){
            Bathroom tempBathroom = property.bathrooms.get(x);
            BathroomPanel panel = new BathroomPanel(tempBathroom,x+1);
            bathroomPanelsPane.addElement(panel);
            bathroomPanels.add(panel);
        }
    }

    /**
     * Updates all property information not in a subpanel
     * @param con Connection to the MySQL Database
     */
    public void updateBaseProperty(Connection con){
        property.setShortName(shortName.getText());
        property.setDescription(description.getText());
        property.setHasBreakfast(hasBreakfast.isSelected());
        property.updatePropertyDB(con);
    }

    /**
     * Updates the property in the database via updating base properties and then updating all subpanels if needed, or in some cases creating new items for the database.
     * @param con Connection to the MySQL Database
     */
    public void updateProperty(Connection con){
        updateBaseProperty(con);
        //Add all new chargebands or update required chargebands
        ArrayList<ChargeBand> chargeBands = new ArrayList<>();
        for (int x = 0; x < chargebandPanels.size(); x++){
            ChargeBandPanel panel = chargebandPanels.get(x);
            if (panel.isNew){
                panel.createChargeband(con,this.property.getPropertyID());
            }
        }

        //Add all new bedrooms or update required bedrooms
        for (int x = 0; x < bedroomPanels.size(); x++){
            BedroomPanel panel = bedroomPanels.get(x);
            if (!panel.isNew){
                if (panel.needsUpdate){
                    panel.updateBedroom(con);
                }
            }
            else{
                panel.createBedroom(con,this.property.getPropertyID());
            }
        }

        //Add all new bathrooms or update required bathrooms
        for (int x = 0; x < bathroomPanels.size(); x++){
            BathroomPanel panel = bathroomPanels.get(x);
            if (!panel.isNew){
                if (panel.needsUpdate){
                    panel.updateBathroom(con);
                }
            }
            else{
                panel.createBathroom(con,this.property.getPropertyID());
            }
        }
        //Update all facilities with information from the panel
        BathingFacilities bathingFacilities = property.getBathingFacilities();
        for (String amenity : BathingFacilities.allAmenities){
            bathingFacilities.setAmenity(amenity,bathingCheckboxes.get(amenity).isSelected());
        }
        bathingFacilities.updateBathingFacilitiesDB(con);

        KitchenFacility kitchenFacilities = property.getKitchenFacility();
        for (String amenity : KitchenFacility.allAmenities){
            kitchenFacilities.setAmenity(amenity,kitchenCheckboxes.get(amenity).isSelected());
        }
        kitchenFacilities.updateKitchenFacilityDB(con);

        LivingFacilities livingFacilities = property.getLivingFacilities();
        for (String amenity : LivingFacilities.allAmenities){
            livingFacilities.setAmenity(amenity,livingCheckboxes.get(amenity).isSelected());
        }
        livingFacilities.updateLivingFacilityDB(con);

        OutdoorFacilities outdoorFacilities = property.getOutdoorFacilities();
        for (String amenity : OutdoorFacilities.allAmenities){
            outdoorFacilities.setAmenity(amenity,outdoorCheckboxes.get(amenity).isSelected());
        }
        outdoorFacilities.updateOutdoorFacilitiesDB(con);

        SleepingFacilities sleepingFacilities = property.getSleepingFacilities();
        for (String amenity : SleepingFacilities.allAmenities){
            sleepingFacilities.setAmenity(amenity,sleepingCheckboxes.get(amenity).isSelected());
        }
        sleepingFacilities.updateSleepingFacilitiesDB(con);

        UtilityFacilities utilityFacilities = property.getUtilityFacilities();
        for (String amenity : UtilityFacilities.allAmenities){
            utilityFacilities.setAmenity(amenity,utilityCheckboxes.get(amenity).isSelected());
        }
        utilityFacilities.updateUtilityFacilitiesDB(con);
    }

    /**
     * Returns if the property is New
     * @return isNew
     */
    public boolean getIsNew(){
        return isNew;
    }
    /**
     * Returns if the property is needs an update to the db
     * @return needsUpdate
     */
    public boolean getNeedsUpdate(){
        return needsUpdate;
    }

    /**
     * Creates a Property Display ready to be used to create a new property for the db
     * @param window Window to be used and referred to when getting Con
     * @param index Index relating to how many properties a User owns.
     */
    public PropertyDisplay(DefaultWindow window, int index){
        this.window = window;
        isNew = true;
        addressPanel = new AddressPanel(width,(int)(height*0.3));
        setUp(index);
    }
}