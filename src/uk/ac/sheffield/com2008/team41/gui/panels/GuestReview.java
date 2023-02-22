package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.ItemListPane;
import uk.ac.sheffield.com2008.team41.models.Review;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GuestReview panel for creating a review.
 * Credit: Team 41 Homebreak submission 2021
 */
public class GuestReview extends DisplayPanel{
    private final DefaultWindow window;
    private int bookingID;
    private JTextArea description = new JTextArea(8, 50);
    private String[] score = {"1", "2", "3", "4", "5"};
    private JComboBox<String> cleanliness = new JComboBox(score);
    private JComboBox<Integer> communication = new JComboBox(score);
    private JComboBox<Integer> checkIn = new JComboBox(score);
    private JComboBox<Integer> accuracy = new JComboBox(score);
    private JComboBox<Integer> location = new JComboBox(score);
    private JComboBox<Integer> value = new JComboBox(score);
    private JLabel succ = new JLabel("");

    public GuestReview(int width, int height, DefaultWindow window, int bookingID) {
        super(width, height);
        this.window = window;
        this.bookingID = bookingID;

        this.setBorder(new EmptyBorder(0,0,30,15));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        ItemListPane itemListPane = new ItemListPane(new DisplayPanel(width, height));

        constraints.gridx=0;
        constraints.gridy=0;

        itemListPane.addElement(new JLabel("Type your review below"));

        //Sets description to text area input

        constraints.gridx=0;
        constraints.gridy=1;
        itemListPane.addElement(new JLabel("Description:"));

        constraints.gridx=0;
        constraints.gridy=2;

        final int constant = 5;

        constraints.gridwidth = constant;
        constraints.gridheight = constant;

        description.setSize(new Dimension((int)(width*0.8),((int)(height*0.4))));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);

        itemListPane.addElement(description);

        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        //Sets all review labels and creates relative comboboxes
        constraints.gridx=0;
        constraints.gridy=3 + constant;
        itemListPane.addElement(new JLabel("Cleanliness Score:"));

        constraints.gridx=0;
        constraints.gridy=4 + constant;
        cleanliness.setSelectedIndex(score.length - 1);
        itemListPane.addElement(cleanliness);

        constraints.gridx=0;
        constraints.gridy=5 + constant;
        itemListPane.addElement(new JLabel("Communication Score:"));

        constraints.gridx=0;
        constraints.gridy=6 + constant;
        communication.setSelectedIndex(score.length - 1);
        itemListPane.addElement(communication);

        constraints.gridx=0;
        constraints.gridy=7 + constant;
        itemListPane.addElement(new JLabel("CheckIn Score:"));

        constraints.gridx=0;
        constraints.gridy=8 + constant;
        checkIn.setSelectedIndex(score.length - 1);
        itemListPane.addElement(checkIn);

        constraints.gridx=0;
        constraints.gridy=9 + constant;
        itemListPane.addElement(new JLabel("Accuracy Score:"));

        constraints.gridx=0;
        constraints.gridy=10 + constant;
        accuracy.setSelectedIndex(score.length - 1);
        itemListPane.addElement(accuracy);

        constraints.gridx=0;
        constraints.gridy=11 + constant;
        itemListPane.addElement(new JLabel("Location Score:"));

        constraints.gridx=0;
        constraints.gridy=12 + constant;
        location.setSelectedIndex(score.length - 1);
        itemListPane.addElement(location);

        constraints.gridx=0;
        constraints.gridy=13 + constant;
        communication.setSelectedIndex(score.length - 1);
        itemListPane.addElement(new JLabel("Value Score:"));

        constraints.gridx=0;
        constraints.gridy=14 + constant;
        value.setSelectedIndex(score.length - 1);
        itemListPane.addElement(value);

        constraints.gridx=0;
        constraints.gridy=16 + constant;
        JButton submitReview = new JButton("Submit review");
        itemListPane.addElement(submitReview);

        constraints.gridx=0;
        constraints.gridy=17 + constant;
        itemListPane.addElement(succ);

        //Submits a review to the database
        submitReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String desc = description.getText();
                int cScore = Integer.parseInt(cleanliness.getSelectedItem().toString());
                int comScore = Integer.parseInt(communication.getSelectedItem().toString());
                int cIScore = Integer.parseInt(checkIn.getSelectedItem().toString());
                int aScore = Integer.parseInt(accuracy.getSelectedItem().toString());
                int lScore = Integer.parseInt(location.getSelectedItem().toString());
                int vScore = Integer.parseInt(value.getSelectedItem().toString());


                Review review = new Review(window.getCon(), bookingID, desc, cScore, comScore, cIScore, aScore, lScore, vScore);

                submitReview.setEnabled(false);
                succ.setText("You have created a review!");
            }
        });

        constraints.gridx=0;
        constraints.gridy=0;

        addNew(itemListPane);






    }

}
