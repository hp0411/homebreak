package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.CustomTextField;
import uk.ac.sheffield.com2008.team41.models.Booking;
import uk.ac.sheffield.com2008.team41.models.ChargeBand;
import uk.ac.sheffield.com2008.team41.models.Guest;
import uk.ac.sheffield.com2008.team41.models.Property;
import uk.ac.sheffield.com2008.team41.helper.ValidationHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

/**
 * Booking Page for creating bookings
 * Credit: Team 41 Homebreak submission 2021
 */
public class BookingPage extends DisplayPanel{
    DefaultWindow window;
    Property chosenProp;

    //Designed prior to DateDisplay
    private CustomTextField startDay;
    private CustomTextField startMonth;
    private CustomTextField startYear;

    private CustomTextField endDay;
    private CustomTextField endMonth;
    private CustomTextField endYear;

    private JLabel error;
    private JButton checkPrice;

    private Date startDate;
    private Date endDate;

    private JLabel pricePN;
    private JLabel cleaningC;
    private JLabel serviceC;
    private JLabel totalP;

    private JButton bookBtn = new JButton("Book Now!");

    /**
     * Creates a BookingPage, which allows people to make a booking
     * @param width Width of page
     * @param height Height of page
     * @param window Window
     * @param chosenProp Property Chosen for the booking
     */
    public BookingPage(int width, int height, DefaultWindow window, Property chosenProp){
        super(width,height);
        this.window = window;
        BookingPage self = this;
        this.chosenProp = chosenProp;
        createBorder();
        constraints.insets = new Insets(3,3,3,3);

        constraints.gridx=0;
        constraints.gridy=0;
        addNewLabel("Property ShortName:");
        constraints.gridx=1;
        addNewLabel(chosenProp.getShortName());

        constraints.gridx=0;
        constraints.gridy=1;
        addNewLabel("Start Day");
        constraints.gridx=1;
        this.startDay = new CustomTextField(5,2, "([a-zA-Z])*");
        addNew(startDay);

        constraints.gridx=0;
        constraints.gridy=2;
        addNewLabel("Start Month");
        constraints.gridx=1;
        this.startMonth = new CustomTextField(5,2, "([a-zA-Z])*");
        addNew(startMonth);

        constraints.gridx=0;
        constraints.gridy=3;
        addNewLabel("Start Year");
        constraints.gridx=1;
        this.startYear = new CustomTextField(5, 4, "([a-zA-Z])*");
        addNew(startYear);

        constraints.gridx=0;
        constraints.gridy=4;
        addNewLabel("End Day");
        constraints.gridx=1;
        this.endDay = new CustomTextField(5,2, "([a-zA-Z])*");
        addNew(endDay);

        constraints.gridx=0;
        constraints.gridy=5;
        addNewLabel("End Month");
        constraints.gridx=1;
        this.endMonth = new CustomTextField(5,2, "([a-zA-Z])*");
        addNew(endMonth);

        constraints.gridx=0;
        constraints.gridy=6;
        addNewLabel("End Year");
        constraints.gridx=1;
        this.endYear = new CustomTextField(5, 4, "([a-zA-Z])*");
        addNew(endYear);

        constraints.gridx=0;
        constraints.gridy=7;
        this.error = new JLabel("");
        addNew(error);

        constraints.gridx=0;
        constraints.gridy=8;
        this.checkPrice = new JButton("Check Price");
        addNew(checkPrice);

        constraints.gridx=0;
        constraints.gridy=9;
        this.pricePN = new JLabel("");
        addNew(pricePN);

        constraints.gridx=0;
        constraints.gridy=10;
        this.cleaningC = new JLabel("");
        addNew(cleaningC);

        constraints.gridx=0;
        constraints.gridy=11;
        this.serviceC = new JLabel("");
        addNew(serviceC);

        constraints.gridx=0;
        constraints.gridy=12;
        this.totalP = new JLabel("");
        addNew(totalP);


        checkPrice.addActionListener(new ActionListener() {
            @Override
            /**
             * Performs input sanitization and raises error, if all valid, calculates the price and then gives user option to book
             */
            public void actionPerformed(ActionEvent e) {
            	if ( isEmptyField(startDay.getText(),startDay, error, "Please enter start day") ||
                    isEmptyField(startMonth.getText(),startMonth, error, "Please enter start month") ||
                    isEmptyField(startYear.getText(),startYear, error, "Please enter start year") ||
                    isEmptyField(endDay.getText(),endDay, error,"Please enter end day") ||
                    isEmptyField(endMonth.getText(),endMonth, error, "Please enter end month") ||
                    isEmptyField(endYear.getText(),endYear, error, "Please enter end year")) {
            	
            	}
            	else {
            		String date1 = String.format("%s-%s-%s", startYear.getText(), startMonth.getText(), startDay.getText());
            		String date2 = String.format("%s-%s-%s", endYear.getText(), endMonth.getText(), endDay.getText());
            		if (ValidationHelper.isValid(date1) && ValidationHelper.isValid(date2)) {
		                try{
		                    self.startDate = new Date(Integer.parseInt(startYear.getText()) - 1900, Integer.parseInt(startMonth.getText()) - 1, Integer.parseInt(startDay.getText()));
		                    self.endDate = new Date(Integer.parseInt(endYear.getText()) - 1900, Integer.parseInt(endMonth.getText()) - 1, Integer.parseInt(endDay.getText()));
		                    if (startDate.compareTo(endDate) < 0){
		         
			                    ChargeBand chargeBand = ChargeBand.findCharge(window.getCon(), chosenProp.getPropertyID(), startDate, endDate);
			                    if (chargeBand!= null) {
			                        error.setText("");
			                        pricePN.setText("The price per night is £" + chargeBand.getPricePerNight());
			                        cleaningC.setText("The cleaning charge is £" + chargeBand.getCleaningCharge());
			                        serviceC.setText("The service charge is £" + chargeBand.getServiceCharge());
			
			                        long diff = endDate.getTime() - startDate.getTime();
			                        float days = (diff / (1000*60*60*24));
			                        double totalPrice = chargeBand.getPricePerNight() * (double) days + chargeBand.getCleaningCharge() + chargeBand.getServiceCharge();
			                        totalP.setText("The total price is is £" + totalPrice );
			
			                        constraints.gridx=0;
			                        constraints.gridy=13;
			                        if (window.user != null){
			                            addNew(bookBtn);
			                        }
			                        else {
			                            addNewLabel("You are only Enquirer. Please Login / Register as Guest to book!");
			                        }
			
			                        revalidate();
			                        repaint();
			
			                    }
			                    else {
			                        error.setText("No valid dates found. Choose other dates!");
			
			                    }
		                    }
		                    else {
		                    	error.setText("Start Date cannot be after End Date!");
		                    }
		                }
		                catch (Exception ex){
		                    ex.printStackTrace();
		                }
		               
            		}
            		
            		else {
            			 error.setText("Invalid date!");
            		}
            	}
            }
            	
        });
	        //Creates booking
	        bookBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                bookBtn.setEnabled(false);
	                Guest guest = (Guest) window.user;
	                Booking booking = new Booking(window.getCon(), guest.getGuestID(), chosenProp.getPropertyID(), self.startDate, self.endDate, true, false);
	                constraints.gridx=0;
	                constraints.gridy=14;
	                addNewLabel("Your booking was booked. Please wait for the host to agree");
	                revalidate();
	                repaint();
	            }
	        });

	        

    }
}

