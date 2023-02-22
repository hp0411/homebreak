package uk.ac.sheffield.com2008.team41.gui.panels.modelPanels;

import uk.ac.sheffield.com2008.team41.gui.panelFeatures.CustomTextField;
import uk.ac.sheffield.com2008.team41.gui.panels.DisplayPanel;
import uk.ac.sheffield.com2008.team41.models.Address;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * AddressPanel, Not really needed, but it helps distinguish all property information and makes code cleaner
 * Used for the creation of new Addresses for Properties.
 * Credit: Team 41 Homebreak submission 2021
 */
public class AddressPanel extends DisplayPanel {
    // All TextField containing information
    CustomTextField houseNumber = new CustomTextField(20,null);
    CustomTextField streetName = new CustomTextField(20,null);
    CustomTextField placeName = new CustomTextField(20,null);
    CustomTextField postCode = new CustomTextField(8,null);
    private Address address;
    //Error Code encase of Validation Error when attempting to build
    String errorCode = "";

    /**
     * Creates an Address panel that can't be modified as it already contains an existing address
     * @param address //Existing address
     * @param width //Panel Width
     * @param height //Panel Height
     */
    public AddressPanel(Address address, int width, int height){
        super(width,height);
        addressBuilder();
        setEnabled(false);
        houseNumber.setText(address.getHouseNumber());
        streetName.setText(address.getStreetName());
        placeName.setText(address.getPlaceName());
        postCode.setText(address.getPostCode());
    }

    /**
     * Builds the gui, Setting all Components in this panel and their positions.
     */
    public void addressBuilder(){
        constraints.gridx = 0;
        addNewLabel("House Number:");
        constraints.gridx =1;
        addNew(houseNumber);
        constraints.gridx = 0;
        constraints.gridy = 1;
        addNewLabel("Street Name:");
        constraints.gridx =  1;
        addNew(streetName);
        constraints.gridx = 0;
        constraints.gridy = 2;
        addNewLabel("Place Name:");
        constraints.gridx =  1;
        addNew(placeName);
        constraints.gridx = 0;
        constraints.gridy = 3;
        addNewLabel("Post Code:");
        constraints.gridx =  1;
        addNew(postCode);
    }

    /**
     * Checks if the panel inputs are valid and if not sets the appropriate error code.
     * @return panel validity
     */
    public boolean isValidPanel(){
        if (postCode.getText().length() < 6 && !postCode.getText().contains(" ")){
            errorCode = "Invalid PostCode";
            return false;
        }
        if (streetName.getText().length() < 3){
            errorCode = "Invalid Street Name";
            return false;
        }
        if (houseNumber.getText().length() < 1){
            errorCode = "Invalid House Number";
            return false;
        }
        if (placeName.getText().length() < 1){
            errorCode = "Place Name";
            return false;
        }
        return true;
    }

    /**
     * Creates a new current address in an object and in the database
     * @param con //Connection to the MySQL Server
     * @return //Address Object which can be used later
     * @throws SQLException //Couldn't add new address to database
     */
    public Address createNewAddress(Connection con) throws SQLException {
        address = new Address(con,houseNumber.getText(),streetName.getText(),placeName.getText(),postCode.getText());
        return address;
    }

    /**
     * Sets all parameter textboxes as enabled or disabled depending on parameter
     * @param enabled If textboxes are enabled
     */
    public void setEnabled(boolean enabled){
        houseNumber.setEnabled(enabled);
        streetName.setEnabled(enabled);
        placeName.setEnabled(enabled);
        postCode.setEnabled(enabled);
    }

    /**
     * Creates an Address panel that can be modified as it does not already contain an existing address
     * @param width //Panel Width
     * @param height //Panel Height
     */
    public AddressPanel(int width, int height){
        super(width,height);
        addressBuilder();
        setEnabled(true);
    }

}
