package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.ItemListPane;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;
import uk.ac.sheffield.com2008.team41.models.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

import static uk.ac.sheffield.com2008.team41.helper.FunctionHelper.*;

/**
 * Homepage which is displayed upon login
 * Credit: Team 41 Homebreak submission 2021
 */
public class HomePage extends DisplayPanel {
    // A Scrolling Pane, literally does as it says, means we can scroll through infinite container height.
    private ItemListPane itemList;
    //Contains all panel information to display
    private DefaultWindow window;

    /**
     * Creates a new Homepage with a set width and height, and uses window for con.
     * @param width
     * @param height
     * @param window
     */
    public HomePage(int width, int height, DefaultWindow window)
    {
        super(width,height);
        this.window = window;
        this.setBorder(new EmptyBorder(0,0,0,15));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        itemList = new ItemListPane(new DisplayPanel(width,height));
        add(itemList);
        displayUserInfo(window.user, window.getCon());
    }

    /**
     * Displays user information showing upcoming bookings
     * @param person
     * @param con
     */
    private void displayUserInfo(Person person, Connection con){
        boolean isHost = (person instanceof Host);

        if (person != null){
            itemList.addElement(new JLabel("Upcoming Bookings",SwingConstants.CENTER));
            ArrayList<Booking> bookings = person.getBookings();
            int i = 0;
            for (i = 0; i < bookings.size(); i++){
                Booking booking = bookings.get(i);
                if (booking.getEndDate().toLocalDate().compareTo(LocalDate.now()) > 0){
                    itemList.addElement((bookingInfoPanel(booking,con,isHost)));
                }
            }
            if (i == 0){
                itemList.addElement(new JLabel("None Available",SwingConstants.CENTER));
            }
        }
    }

    /**
     * Creates a booking panel for home page in an easily displayable format.
     * @param booking
     * @param con
     * @param isHost
     * @return
     */
    private DisplayPanel bookingInfoPanel(Booking booking, Connection con, boolean isHost){
        DisplayPanel resultPanel = FunctionHelper.generatePanel(1,1);
        resultPanel.setSize(new Dimension((int)(width*0.6),(int)(height*0.1)));
        resultPanel.setBackground(Color.gray);
        resultPanel.addNew(new JLabel(booking.getStartDate().toString()  + " - " + booking.getEndDate()));
        resultPanel.constraints.gridx = 1;
        Property property = new Property(con,booking.getPropertyID());
        resultPanel.addNew(new JLabel("Property Name: " + property.getShortName()));
        resultPanel.constraints.gridy = 1;
        resultPanel.constraints.gridx = 0;
        if (isHost){
            Guest guest = new Guest(con,booking.getGuestID());
            JLabel guestLabel = new JLabel("Guest Name: " + guest.getGuestName());
            resultPanel.addNew(guestLabel);
            if (booking.hostAgree()) {
                //If host agreed and you are host, let them know agreed profit.
                resultPanel.constraints.gridy += 1;
                resultPanel.addNewLabel("You have agreed to this booking for the price of: £" + booking.getPrice(con));
            }
            else if (booking.pending()){
                //If host not yet agreed and you are host, let them know agreed profit and ask for decision.
                resultPanel.constraints.gridy += 1;
                resultPanel.addNewLabel("A booking is requested for this property, the profit for this transaction would be £" + booking.getPrice(con));
                resultPanel.constraints.gridy += 1;
                resultPanel.addNewLabel("Do you accept?");
                resultPanel.constraints.gridy += 1;
                resultPanel.constraints.gridx = 0;
                JButton accept = new JButton("Yes, I accept this booking");
                JButton decline = new JButton("No, I decline this booking");
                //Accept and decline requests
                accept.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        booking.setHostAgree(con,true);
                        ((Host)(window.user)).bookings = Booking.getBookingsHost(con,((Host)(window.user)).getHostID());
                        window.changeCurrentWindow(new HomePage(width,height,window));
                    }
                });
                decline.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        booking.setHostAgree(con,false);
                        ((Host)(window.user)).bookings = Booking.getBookingsHost(con,((Host)(window.user)).getHostID());
                        window.changeCurrentWindow(new HomePage(width,height,window));
                    }
                });
                resultPanel.addNew(accept);
                resultPanel.constraints.gridx = 1;
                resultPanel.addNew(decline);
                resultPanel.constraints.gridx = 0;
            }

        }
        else{
            //If guest show host name and only show email if host agreed and only show property address after start date
            Host host = property.getHost();
            JLabel hostLabel = new JLabel("Host Name: " + host.getHostName());
            resultPanel.addNew(hostLabel);
            if (booking.hostAgree()){
                //Show Contact Info
                JLabel hostEmail = new JLabel("Host Email: " + host.getEmail());
                resultPanel.constraints.gridy += 1;
                resultPanel.addNew(hostEmail);
                if (booking.getStartDate().toLocalDate().compareTo(LocalDate.now()) >= 0){
                    resultPanel.constraints.gridy += 1;
                    resultPanel.addNewLabel("Property Address: ");
                    JTextArea address = new JTextArea();
                    Address propertyAddress = property.getAddress();
                    address.setEnabled(false);
                    address.setBackground(Color.gray);
                    address.setForeground(Color.BLACK);
                    address.setText("House Number: " + propertyAddress.getHouseNumber() + "\nStreet Name: " + propertyAddress.getStreetName()
                            + "\nPlace Name: " + propertyAddress.getPlaceName() + "\nPostcode: " + propertyAddress.getPostCode());
                    resultPanel.constraints.gridy += 1;
                    resultPanel.addNew(address);
                }
            }
            else if (booking.pending()){
                //Lets guest know booking is still being requested
                resultPanel.constraints.gridy += 1;
                resultPanel.addNewLabel("You have requested a booking for this property, the cost for this transaction will be £" + booking.getPrice(con));
            }
        }
        return resultPanel;
    }
}
