package uk.ac.sheffield.com2008.team41.gui.panels.modelPanels;

import uk.ac.sheffield.com2008.team41.models.Bedroom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * Creates a BedroomPanel that allows for easy display, modification and creation of multiple bedrooms.
 * Credit: Team 41 Homebreak submission 2021
 */
public class BedroomPanel extends ModelPanel {
    // Combo Boxes stating what beds are set for bedroom.
    private JComboBox<String> bedOne;
    private JComboBox<String> bedTwo;

    /**
     * Creates a bedroom panel pre-loaded with information from an existing bedroom
     * @param room //Existing Bedroom
     * @param number //Number for incrementation display.
     */
    public BedroomPanel(Bedroom room, int number) {
        super(room,number,"Bedroom");
        bedroomSetup();
        bedOne.setSelectedIndex(Bedroom.bed1Types.valueOf(room.getBed1()).ordinal());
        if (room.getBed2() == null){
            bedTwo.setSelectedIndex(0);
        }
        else{
            bedTwo.setSelectedIndex(Bedroom.bed2Types.valueOf(room.getBed2()).ordinal());
        }
        isNew = false;
    }

    /**
     * Sets up the bedroom Combo boxes and adds event listeners to help check for updates.
     */
    private void bedroomSetup(){
        bedOne = new JComboBox<String>(Bedroom.bed1Type);
        bedTwo = new JComboBox<String>(Bedroom.bed2Type);
        addNew(new JLabel("Bed One: "),current);
        current.x += 1;
        addNew(bedOne,current);
        current.x += 1;
        addNew(new JLabel("Bed Two: "),current);
        current.x += 1;
        addNew(bedTwo,current);
        bedOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                needsUpdate = true;
            }
        });
        bedTwo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                needsUpdate = true;
            }
        });
    }

    /**
     * Creates a brand new bedroom and will then create a new bedroom in db later if required
     * @param number Incremental Display Value
     */
    public BedroomPanel(int number) {
        super(number,"Bedroom");
        isNew = true;
        bedroomSetup();
        bedOne.setSelectedIndex(0);
        bedTwo.setSelectedIndex(0);
    }

    /**
     * Updates the bedroom information on the database and returns updated objects
     * @param con Connection to MySQL Server
     * @return Updated Bedroom Object
     */
    public Bedroom updateBedroom(Connection con){
        Bedroom room = (Bedroom)(this.obj);
        room.setBed1((String)bedOne.getSelectedItem());
        room.setBed2((String)bedTwo.getSelectedItem());
        room.updateBedDB(con);
        return room;
    }

    @Override
    /**
     * BedroomPanel always valid due to no text input or miscellaneous input.
     * Created due to inheritance from Abstract Class
     */
    public boolean isValidPanel() {
        return true;
    }

    /**
     * Creates a brand new bedroom in the database from the pre-existing information in this
     * @param con Connection to the MySQL Server
     * @param propertyID PropertyID to assign new Bedroom
     * @return New Bedroom Returned as an object
     */
    public Bedroom createBedroom(Connection con,int propertyID){
        Bedroom room;
        if (bedTwo.getSelectedIndex() == 0){
            room = new Bedroom(con,propertyID,(String)bedOne.getSelectedItem());
        }
        else{
            room = new Bedroom(con,propertyID,(String)bedOne.getSelectedItem(),(String)bedTwo.getSelectedItem());
        }
        return room;
    }
}
