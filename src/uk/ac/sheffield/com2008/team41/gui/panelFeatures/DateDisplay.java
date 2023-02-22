package uk.ac.sheffield.com2008.team41.gui.panelFeatures;

import uk.ac.sheffield.com2008.team41.gui.panels.DisplayPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.util.ArrayList;


/**
 * A custom component designed to make date selection easier to implement
 * Credit: Team 41 Homebreak submission 2021
 */
public class DateDisplay extends DisplayPanel {
    //All ComboBoxes to be used
    private JComboBox<Integer> dayPicker;
    private JComboBox<Integer> monthPicker;
    private JComboBox<Integer> yearPicker;
    //Local Information to help correction and prediction later on.
    private int oldYear = -1;
    private int oldMonth = -1;
    private LocalDate date;

    /**
     * Creates a DateDisplay,
     * @param width //Intended Width
     * @param height //Intended Height
     */
    public DateDisplay(int width, int height){
        super(width,height);
        //Makes webpage only addNewItems horizontally, allowing for easy adding of combo boxes
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //Sets border to reduce used space.
        setBorder(new EmptyBorder(0,0,0,0));
        date = LocalDate.now();
        ArrayList<Integer> years = new ArrayList<Integer>();
        yearPicker = new JComboBox<Integer>();
        monthPicker = new JComboBox<>();
        dayPicker = new JComboBox<>();
        for (int x = 0; x < 100; x++){
            yearPicker.addItem(x+date.getYear()-50);
        }
        ArrayList<Integer> months = new ArrayList<Integer>();
        for (int x = 1; x <= 12; x++){
            monthPicker.addItem(x);
        }
        yearPicker.setSelectedIndex(50);
        monthPicker.setSelectedIndex(date.getMonth().getValue()-1);
        yearPicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeComboBoxes();
            }
        });
        monthPicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeComboBoxes();
            }
        });
        dayPicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeComboBoxes();
            }
        });
        dayPicker.removeAllItems();
        changeComboBoxes();
        dayPicker.setSelectedIndex(date.getDayOfMonth()-1);
        add(dayPicker);
        add(monthPicker);
        add(yearPicker);
    }

    /**
     * Gets the current represented date in the DateDisplay
     * @return //DateDisplay Date
     */
    public LocalDate getSelectedDate(){
        return LocalDate.of(yearPicker.getSelectedIndex() + (date.getYear()-50),monthPicker.getSelectedIndex()+1,dayPicker.getSelectedIndex()+1);
    }

    /**
     * Adds an Action Listener to the ComboBoxes so an action occurs a value is changed.
     * @param listener //Action to initiate upon value change
     */
    public void addActionListener(ActionListener listener){
        dayPicker.addActionListener(listener);
        monthPicker.addActionListener(listener);
        yearPicker.addActionListener(listener);
    }

    /**
     * Sets current date for the Display
     * @param value //New Date for DateDisplay
     */
    public void setDate(LocalDate value){
        int valYear = value.getYear() - date.getYear();
        yearPicker.setSelectedIndex(valYear+50);
        monthPicker.setSelectedIndex(value.getMonthValue()-1);
        dayPicker.setSelectedIndex(value.getDayOfMonth()-1);
    }

    private void changeComboBoxes(){
        int year = (date.getYear()-50) + yearPicker.getSelectedIndex();
        int month = monthPicker.getSelectedIndex()+1;
        /**
         * Adds only the days in the selected month and year to the combobox
         * If days year and month are same, then there is no need to change.
         */
        if (oldYear != year || oldMonth != month){
            dayPicker.removeAllItems();
            int days = YearMonth.of(year, month).lengthOfMonth();
            //Get current index and if not in range of max days, go to the closest valid value
            int selectedIndex = dayPicker.getSelectedIndex();
            if (selectedIndex +1 > days){
                selectedIndex = days -1;
            }
            for (int x = 1; x <= days; x++){
                //Simple solution to counteract, weird jumping bug that shouldn't occur if the program is following computational procedure.
                if (dayPicker.getItemCount() + 1 <= days){
                    dayPicker.addItem(dayPicker.getItemCount()+1);
                }
            }
            if (dayPicker.getItemCount() > days || dayPicker.getSelectedIndex() == -1){
                dayPicker.setSelectedIndex(selectedIndex);
            }
        }
        oldYear = year;
        oldMonth = month;
    }

    @Override
    /**
     * Set's all ComboBoxes to enabled or disabled depending on input
     */
    public void setEnabled(boolean enabled) {
        dayPicker.setEnabled(enabled);
        monthPicker.setEnabled(enabled);
        yearPicker.setEnabled(enabled);
    }
}
