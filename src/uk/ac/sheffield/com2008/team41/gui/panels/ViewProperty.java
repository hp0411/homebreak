package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.ItemListPane;
import uk.ac.sheffield.com2008.team41.gui.panels.modelPanels.PropertyInfo;
import uk.ac.sheffield.com2008.team41.gui.panels.modelPanels.ReviewPanel;
import uk.ac.sheffield.com2008.team41.models.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Creates a panel which allows a user to view a property
 * Credit: Team 41 Homebreak submission 2021
 */
public class ViewProperty extends DisplayPanel{
    // A Scrolling Pane, literally does as it says, means we can scroll through infinite container height.
    private ItemListPane itemList;
    //Contains all panel information to display

    /**
     * Creates a ViewProperty panel where it contains all property information to display and has a backpanel to return to the search page
     * @param width
     * @param height
     * @param window
     * @param chosenProp
     * @param backPanel
     */
    public ViewProperty (int width, int height, DefaultWindow window, Property chosenProp, DisplayPanel backPanel){
        super(width,height);
        this.setBorder(new EmptyBorder(0,0,0,0));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        itemList = new ItemListPane(new DisplayPanel(width,height));
        createBorder();
        itemList.setBorder(new EmptyBorder(0,0,0,0));
        itemList.view.setBorder(new EmptyBorder(0,0,10,0));
        add(itemList);
        itemList.addElement(new PropertyInfo(width,(int)(height*0.8),chosenProp,window));
        JButton backButton = new JButton("Go back to search");
        itemList.addElement(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.changeCurrentWindow(backPanel);
            }
        });
        ArrayList<Review> reviewArrayList = chosenProp.getAllReviews(window.getCon());
        revalidate();
        repaint();
        itemList.addElement(new JLabel("Reviews: "));
        if (reviewArrayList.isEmpty()){
            itemList.addElement(new JLabel("No Reviews Currently!"));
        }
        else {
            for(int i = 0; i < reviewArrayList.size(); i++){
                itemList.addElement( new ReviewPanel(reviewArrayList.get(i), i + 1));
            }
        }
    }
}

