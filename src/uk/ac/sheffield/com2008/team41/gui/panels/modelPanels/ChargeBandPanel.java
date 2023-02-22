package uk.ac.sheffield.com2008.team41.gui.panels.modelPanels;

import javafx.scene.control.DatePicker;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.CustomTextField;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.DateDisplay;
import uk.ac.sheffield.com2008.team41.models.Bedroom;
import uk.ac.sheffield.com2008.team41.models.ChargeBand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * ChargeBandPanel class made for easy creation, and display of chargebands in a property.
 * Credit: Team 41 Homebreak submission 2021
 */
public class ChargeBandPanel extends ModelPanel{

    private CustomTextField pricePerNight;
    private CustomTextField cleaningCharge;
    private CustomTextField serviceCharge;
    private DateDisplay startDate;
    private DateDisplay endDate;

    //For testing if valid decimals
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Creation of ChargeBandPanel if a ChargeBand already exists in database
     * @param band //Pre-existing chargeband
     * @param number //Incremental charge band value
     */
    public ChargeBandPanel(ChargeBand band, int number) {
        super(band,number,"Chargeband");
        chargeBandSetup();
        //Sets all charge band values and disables all input
        pricePerNight.setText(df.format(band.getPricePerNight()));
        pricePerNight.setEnabled(false);
        cleaningCharge.setText(df.format(band.getCleaningCharge()));
        cleaningCharge.setEnabled(false);
        serviceCharge.setText(df.format(band.getServiceCharge()));
        serviceCharge.setEnabled(false);
        startDate.setDate(band.getStartDate().toLocalDate());
        startDate.setEnabled(false);
        endDate.setDate(band.getEndDate().toLocalDate());
        endDate.setEnabled(false);
        isNew = false;
    }

    /**
     * Sets up the chargeband panel to display information in a nice manner.
     */
    private void chargeBandSetup(){
        addNew(new JLabel("Price per Night (£?.??): "), current);
        current.x = 1;
        pricePerNight = new CustomTextField(6,"([a-zA-Z])*");
        addNew(pricePerNight,current);
        current.x = 0;
        current.y += 1;
        addNew(new JLabel("Cleaning Charge (£?.??): "), current);
        current.x = 1;
        cleaningCharge = new CustomTextField(6,"([a-zA-Z])*");
        addNew(cleaningCharge,current);
        current.x = 0;
        current.y += 1;
        addNew(new JLabel("Service Charge (£?.??): "), current);
        current.x = 1;
        serviceCharge = new CustomTextField(6,"([a-zA-Z])*");
        addNew(serviceCharge,current);
        current.x = 0;
        current.y += 1;
        addNew(new JLabel("Start Date: "), current);
        current.x = 1;
        startDate = new DateDisplay(width,height);
        addNew(startDate,current);
        current.x = 0;
        current.y += 1;
        addNew(new JLabel("End Date: "), current);
        current.x = 1;
        endDate = new DateDisplay(width,height);
        addNew(endDate,current);
        //Goes through all components in this object and adds action listener
        for(Component component : this.getComponents()){
            if (component instanceof DateDisplay){
                ((DateDisplay)(component)).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        needsUpdate = true;
                    }
                });
            }
            else if (component instanceof CustomTextField){
                ((CustomTextField)(component)).addActionListener(
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                needsUpdate = true;
                            }
                        }
                );
            }
        }
    }

    /**
     * Creation of ChargeBandPanel if a ChargeBand doesn't already exist in database
     * @param number //Incremental charge band value
     */
    public ChargeBandPanel(int number) {
        super(number,"Chargeband");
        chargeBandSetup();
        isNew = true;
    }

    /**
     * Returns the start and end dates of this ChargeBand
     * @return Start and End Date in array
     */
    public LocalDate[] dates(){
        return new LocalDate[]{startDate.getSelectedDate(), endDate.getSelectedDate()};
    }

    /**
     * Creates a ChargeBand in the database and returns the converted object
     * @param con Connection to the MySQL Server
     * @param propertyID //ID of Property of which this chargeband applies
     * @return Chargeband Object
     */
    public ChargeBand createChargeband(Connection con, int propertyID){
        ChargeBand result = new ChargeBand(
                con,
                propertyID,
                Double.parseDouble(pricePerNight.getText()),
                Double.parseDouble(cleaningCharge.getText()),
                Double.parseDouble(serviceCharge.getText()),
                java.sql.Date.valueOf(startDate.getSelectedDate()),
                java.sql.Date.valueOf(endDate.getSelectedDate())
                );
        return result;
    }

    @Override
    /**
     * Checks if the chargeband is valid and sets appropriate error code if not valid
     */
    public boolean isValidPanel() {
        try{
            //Ensures all charges and prices are in double compatible format.
            Double val = Double.parseDouble(pricePerNight.getText());
            val = Double.parseDouble(cleaningCharge.getText());
            val = Double.parseDouble(serviceCharge.getText());
            //Gets the dates so comparison can be made later
            LocalDate date = startDate.getSelectedDate();
            LocalDate date2 = endDate.getSelectedDate();
            //Returns error if End Date before StartDate
            if (date.compareTo(date2) >= 0){
                errorCode = "End Date cannot be equal or less than start date";
                return false;
            }
            //Returns error if Start Date in Past
            if (isNew && date.compareTo(LocalDate.now()) < 0){
                errorCode = "Start date for a new Chargeband cannot be in the past";
                return false;
            }
            return true;
        }
        catch(Exception e){
            errorCode = "Invalid Information";
            return false;
        }
    }

}
