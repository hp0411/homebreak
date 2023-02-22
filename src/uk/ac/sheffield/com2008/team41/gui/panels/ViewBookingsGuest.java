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
 * A panel for viewing bookings from a guest perspective1
 * Credit: Team 41 Homebreak submission 2021
 */
public class ViewBookingsGuest extends DisplayPanel{
    private final DefaultWindow window;
    private ItemListPane itemList;

    /**
     * Creates a View Booking Guest Pane with a set width, height and window
     * @param width
     * @param height
     * @param window
     */
    public ViewBookingsGuest(int width, int height, DefaultWindow window) {
        super(width, height);
        this.window = window;
        //Sets the layout so it can fit a itemListPane
        this.setBorder(new EmptyBorder(0,0,0,25));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        itemList = new ItemListPane(new DisplayPanel(width,height));
        add(itemList);

        itemList.addElement(new JLabel("View Bookings Below",SwingConstants.CENTER));
        displayBookings(window.user);
    }

    public void displayBookings(Person user){
        //Adds all bookings below
        Guest guest = (Guest) user;
        ArrayList<Booking> bookings = Booking.getBookingsGuest(window.getCon(), guest.getGuestID());

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
     * Creates a panel for a booking and fills it with information so it appears in the right format for th guest
     * @param booking
     * @return
     */
    private DisplayPanel bookingInfoPanel(Booking booking){
        //
        DisplayPanel resultPanel = FunctionHelper.generatePanel(1,1);
        resultPanel.setSize(new Dimension((int)(width*0.6),(int)(height*0.1)));
        resultPanel.setBackground(Color.gray);
        resultPanel.addNew(new JLabel(booking.getStartDate().toString() + " - " + booking.getEndDate().toString()));
        resultPanel.constraints.gridx = 1;

        Property property = new Property(window.getCon(), booking.getPropertyID());
        resultPanel.addNew(new JLabel("Property Name: " + property.getShortName()));
        resultPanel.constraints.gridx = 0;
        resultPanel.constraints.gridy = 1;
        Host host = property.getHost();
        resultPanel.addNew(new JLabel("Host Name Here: " + host.getHostName()));

        if (booking.pending()){
            resultPanel.constraints.gridx = 0;
            resultPanel.constraints.gridy = 2;
            if (!booking.hostAgree()){
                resultPanel.addNewLabel("Waiting For Host Response");
            }
            else {
                Date curentDate = new Date();
                java.util.Date  utilDate = new java.util.Date(booking.getEndDate().getTime());

                long diff = utilDate.getTime() - curentDate.getTime();
                float days = (diff / (1000*60*60*24));
                if (days > 0) {
                    JButton viewDetails = new JButton("View Details Here");
                    resultPanel.addNew(viewDetails);

                    viewDetails.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Person person = new Person(window.getCon(), host.getEmail());
                            window.changeCurrentWindow(new ViewConfidentialInfo(width, height, window, person, property));
                        }
                    });
                }
                else {
                    resultPanel.addNewLabel("Booking Finished");
                }
                resultPanel.constraints.gridx = 0;
                resultPanel.constraints.gridy = 3;

                //Asks user if they want to make a review
                if (!Review.findReview(window.getCon(), booking.getBookingID())) {
                    JButton makeReview = new JButton("Make a Review");
                    resultPanel.addNew(makeReview);

                    makeReview.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            window.changeCurrentWindow(new GuestReview(width, height, window, booking.getBookingID()));
                        }
                    });
                }
                else{
                    resultPanel.addNewLabel("Review Already Made");
                }
            }
        }
        else {
            resultPanel.constraints.gridx = 0;
            resultPanel.constraints.gridy = 2;
            resultPanel.addNewLabel("Booking Declined");

        }

        return resultPanel;
    }
}
