package uk.ac.sheffield.com2008.team41.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;

/**
 * Homebreak toolbar to switch between panels
 * Credit: Team 41 Homebreak submission 2021
 */
public class HomebreakToolbar extends JPanel {
    private int width;
    private int height;
    private ImageIcon logo;
    private ArrayList<JButton> buttons = new ArrayList<JButton>();

    /**
     * Creates a Toolbar with a pre-specified width, height and logo
     * @param width
     * @param height
     * @param logo
     */
    public HomebreakToolbar(int width, int height, ImageIcon logo){
        this.width = width;
        this.height = height;
        this.logo = FunctionHelper.rescaleImage(logo,(int)(height*0.9),true);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setAlignmentX(0.2f);
        setSize(width,height);
        JLabel logoLabel = new JLabel(this.logo);
        add(logoLabel);
        setBackground(Color.gray);
    }

    /**
     * Adds a button to the toolbar with a preset size and spacing
     * @param label Label Name
     * @param action //Action upon Click
     */
    public void addButton(String label, ActionListener action){
        add(Box.createHorizontalStrut(10));
        JButton button = new JButton();
        button.setText(label);
        button.addActionListener(action);
        button.setEnabled(true);
        button.setPreferredSize(new Dimension(100, (int)(height)));
        buttons.add(button);
        add(button);
        this.revalidate();
        this.repaint();
    }

    /**
     * Sets all Buttons to enabled or disabled
     * @param state
     */
    public void setButtonsState(boolean state){
        for (JButton button: buttons) {
            button.setEnabled(state);
        }
    }

}
