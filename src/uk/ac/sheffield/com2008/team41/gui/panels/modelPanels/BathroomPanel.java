package uk.ac.sheffield.com2008.team41.gui.panels.modelPanels;

import uk.ac.sheffield.com2008.team41.models.Bathroom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * BathroomPanel, Created for the dynamic viewing of multiple bathrooms
 * Credit: Team 41 Homebreak submission 2021
 */
public class BathroomPanel extends ModelPanel {
    /**
     * HashMap relating Checkboxes with their original names
     */
    HashMap<String, JCheckBox> ammenities;

    /**
     * Constructor for BathroomPanel if a bathroom is pre-existing
     * @param room Bathroom
     * @param number Number for incrementation on the gui.
     */
    public BathroomPanel(Bathroom room, int number) {
        super(room,number,"Bathroom");
        bathroomSetup();
        ammenities.get("Toilet").setSelected(((Bathroom)room).getToilet());
        ammenities.get("Bath").setSelected(((Bathroom)room).getBath());
        ammenities.get("Shower").setSelected(((Bathroom)room).getShower());
        ammenities.get("Shared").setSelected(((Bathroom)room).getIsShared());
        setEnabled(false);
        isNew = false;
    }
    private void bathroomSetup(){
        //Sets up all checkboxes
        ArrayList temp = addCheckboxFromList(Bathroom.allAmenities,current,4);
        current = (Point)temp.get(1);
        ammenities = (HashMap<String, JCheckBox>) temp.get(0);
        ammenities.forEach((k,v) -> v.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                needsUpdate = true;
            }
        }));
    }

    /**
     * Constructor for BathroomPanel if a bathroom is not pre-existing
     * @param number Number for incrementation on the gui.
     */
    public BathroomPanel(int number) {
        super(number,"Bathroom");
        bathroomSetup();
        setEnabled(true);
        isNew = true;
    }

    /**
     * Sets all Checkboxes to enabled or disabled depending on value
     * @param enabled Checkboxes Disabled
     */
    public void setEnabled(boolean enabled){
        ammenities.forEach((k,v) -> v.setEnabled(enabled));
    }

    /**
     * Updates current room stored with information now set by user in the database and returns the new Bathroom object.
     * @param con Connection to the MySQL Server
     * @return Bathroom
     */
    public Bathroom updateBathroom(Connection con){
        Bathroom room = (Bathroom)(this.obj);
        room.setBath(ammenities.get("Bath").isSelected());
        room.setIsShared(ammenities.get("Shared").isSelected());
        room.setToilet(ammenities.get("Toilet").isSelected());
        room.setShower(ammenities.get("Shower").isSelected());
        room.updateBathroomDB(con);
        return room;
    }

    @Override
    /**
     * All Bathroom Panels are valid, this is created via an abstract method and needed overriding.
     */
    public boolean isValidPanel() {
        return true;
    }

    /**
     * Creates a new Bathroom object and stores the information in the database
     * @param con Connection to the MySQL Server
     * @param propertyID PropertyID for creating bathroom
     * @return Bathroom
     */
    public Bathroom createBathroom(Connection con, int propertyID){
        ArrayList<String> ammenites = new ArrayList<String>();
        //Goes through all items in hashmap and they adds each value to the new Bathroom which will be added in db.
        for (HashMap.Entry<String, JCheckBox> entry : this.ammenities.entrySet()) {
            if (entry.getValue().isSelected()){
                ammenites.add(entry.getKey());
            }
        }
        Bathroom bathroom = new Bathroom(con,ammenites,propertyID);
        return bathroom;
    }
}

