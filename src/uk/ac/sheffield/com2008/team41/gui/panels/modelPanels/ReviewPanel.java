package uk.ac.sheffield.com2008.team41.gui.panels.modelPanels;

import uk.ac.sheffield.com2008.team41.gui.panelFeatures.CustomTextField;
import uk.ac.sheffield.com2008.team41.models.Review;

import javax.swing.*;
import java.awt.*;
/**
 * ReviewPanel, Created for the dynamic viewing of multiple reviews
 * Credit: Team 41 Homebreak submission 2021
 */
public class ReviewPanel extends ModelPanel{

    /**
     * Creates a review panel for a property from an existing review object
     * @param review Review Object
     * @param number Numeric Index of Review
     */
    public ReviewPanel(Review review, int number) {
        super(review, number, "Review");
        setSize(width,height);
        reviewSetup(review);
    }

    /**
     * Sets up the review gui
     * @param review Review Object
     */
    private void reviewSetup(Review review){
        JTextArea desc = new JTextArea(review.getDescription());
        desc.setEditable(false);
        desc.setLineWrap(true);
//        desc.setSize(new Dimension((int)(width*0.8),((int)(height*0.4))));
        addNew(desc, current);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx=1;
        current.y+=1;
        JLabel textLabelCS = new JLabel("Cleanliness Score: " + review.getCleanlinessScore());
        addNew(textLabelCS, current);

        current.y+=1;
        JLabel textLabelComS = new JLabel("Communication Score: " + review.getCommunicationScore());
        addNew(textLabelComS, current);

        current.y+=1;
        JLabel textLabelCIS = new JLabel("CheckIn Score: " + review.getCheckInScore());
        addNew(textLabelCIS, current);

        current.y+=1;
        JLabel textLabelAS = new JLabel("Accuracy Score: " + review.getAccuracyScore());
        addNew(textLabelAS, current);

        current.y+=1;
        JLabel textLabelLS = new JLabel("Location Score: " + review.getLocationScore());
        addNew(textLabelLS, current);

        current.y+=1;
        JLabel textLabelVS = new JLabel("Value Score: " + review.getValueScore());
        addNew(textLabelVS, current);
    }

    @Override
    /**
     * Review Panels are always valid as no user input
     */
    public boolean isValidPanel() {
        return true;
    }
}
