package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.ItemListPane;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;
import uk.ac.sheffield.com2008.team41.models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * View Booking Host to Show Bookings in the right format for a Host
 * Credit: Team 41 Homebreak submission 2021
 */
public class ViewBookingsHost extends DisplayPanel{
    private final DefaultWindow window;
    private ItemListPane itemList;

    public ViewBookingsHost(int width, int height, DefaultWindow window) {
        super(width, height);
        this.window = window;
        this.setBorder(new EmptyBorder(0,0,0,25));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //Creates a list pane to show multiple types of items
        itemList = new ItemListPane(new DisplayPanel(width,height));
        add(itemList);

        itemList.addElement(new JLabel("View Bookings Below",SwingConstants.CENTER));
        displayBookings(window.user);
    }

    /**
     * Displays all the bookings in the right format for the host
     * @param user
     */
    public void displayBookings(Person user){
        Host host = (Host)user;
        ArrayList<Booking> bookings = Booking.getBookingsHost(window.getCon(), host.getHostID());
        if (bookings.isEmpty()){
            itemList.addElement(new JLabel("No bookings currently"));
        }
        else {
            for (int i = 0; i< bookings.size(); i++){
                Booking booking = bookings.get(i);
                itemList.addElement((bookingInfoPanel(booking)));
            }
        }

    }

    /**
     * Creates a booking Info Panel that can be used to create a display dynamically
     * @param booking
     * @return
     */
    private DisplayPanel bookingInfoPanel(Booking booking){
        DisplayPanel resultPanel = FunctionHelper.generatePanel(1,1);
        resultPanel.setSize(new Dimension((int)(width*0.6),(int)(height*0.1)));
        resultPanel.setBackground(Color.gray);
        resultPanel.addNew(new JLabel(booking.getStartDate().toString() + " - " + booking.getEndDate().toString()));
        resultPanel.constraints.gridx = 1;

        Property property = new Property(window.getCon(), booking.getPropertyID());
        resultPanel.addNew(new JLabel("Property Name: " + property.getShortName()));
        resultPanel.constraints.gridx = 0;
        resultPanel.constraints.gridy = 1;
        Guest guest = new Guest(window.getCon(), booking.getGuestID());
        resultPanel.addNew(new JLabel("Guest Name Here: " + guest.getGuestName()));

        if (!booking.pending()){
            //If host hasn't agreed yet, then ask if they want to accept the booking
            resultPanel.constraints.gridx = 0;
            resultPanel.constraints.gridy = 2;
            JButton decline = new JButton("Decline Button");
            resultPanel.addNew(decline);
            resultPanel.constraints.gridx = 1;
            JButton accept = new JButton("Accept Booking");
            resultPanel.addNew(accept);

            if(!booking.validBooking(window.getCon())){
                accept.setEnabled(false);
            }

            decline.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    booking.setPending(window.getCon(), false);
                    window.changeCurrentWindow(new ViewBookingsHost(width, height, window));
                }
            });

            accept.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    booking.setHostAgree(window.getCon(), true);
                    window.changeCurrentWindow(new ViewBookingsHost(width, height, window));
                }
            });
        }
        else if (booking.hostAgree()){
            Date curentDate = new Date();
            java.util.Date  utilDate = new java.util.Date(booking.getEndDate().getTime());

            long diff = utilDate.getTime() - curentDate.getTime();
            float days = (diff / (1000*60*60*24));
            resultPanel.constraints.gridx = 0;
            resultPanel.constraints.gridy = 2;
            if (days > 0){
                JButton viewDetails = new JButton("View Details Here");
                resultPanel.addNew(viewDetails);
                viewDetails.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Person person = new Person(window.getCon(), guest.getEmail());
                        window.changeCurrentWindow(new ViewConfidentialInfo(width, height, window, person, property));
                    }
                });
            }
            else{
                resultPanel.addNewLabel("Booking Finished");
            }
        }
        return resultPanel;
    }
}
