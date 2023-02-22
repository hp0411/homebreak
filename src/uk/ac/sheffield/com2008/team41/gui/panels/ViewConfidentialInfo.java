package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.models.Person;
import uk.ac.sheffield.com2008.team41.models.Property;

import java.awt.*;

/**
 * A panel to show confidential information
 * Credit: Team 41 Homebreak submission 2021
 */
public class ViewConfidentialInfo extends DisplayPanel{
    private final DefaultWindow window;
    Person person;
    Property property;

    /**
     * Gets width, height, window, person and property where it will create a window which shows the confidential info only when required
     * @param width
     * @param height
     * @param window
     * @param person
     * @param property
     */
    public ViewConfidentialInfo(int width, int height, DefaultWindow window, Person person, Property property) {
        super(width, height);
        this.window = window;
        this.person = person;
        this.property = property;

        constraints.insets = new Insets(3,3,3,3);
        createBorder();

        constraints.gridx=0;
        constraints.gridy=0;
        addNewLabel(person.getFullName() + "'s details below");

        constraints.gridx=0;
        constraints.gridy=1;
        addNewLabel("Email: " + person.getEmail());

        constraints.gridx=0;
        constraints.gridy=2;
        addNewLabel("Phone Number: " + person.getEmail());

        constraints.gridx=0;
        constraints.gridy=3;
        addNewLabel(property.getShortName() + "'s details below");

        constraints.gridx=0;
        constraints.gridy=4;
        addNewLabel("House Number: " + property.getAddress().getHouseNumber());

        constraints.gridx=0;
        constraints.gridy=5;
        addNewLabel("Street Name: " + property.getAddress().getStreetName());

        constraints.gridx=0;
        constraints.gridy=6;
        addNewLabel("Place Name: " + property.getAddress().getPlaceName());

        constraints.gridx=0;
        constraints.gridy=7;
        addNewLabel("Post Code: " + property.getAddress().getPostCode());

        constraints.gridx=0;
        constraints.gridy=8;
        addNewLabel("Please contact them!");
    }

}
