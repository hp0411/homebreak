package uk.ac.sheffield.com2008.team41.helper;

import uk.ac.sheffield.com2008.team41.gui.panels.DisplayPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class which contains a lot of prebuilt static functions to be used by the rest of the project later
 * Credit: Team 41 Homebreak submission 2021
 */
public class FunctionHelper {
    /**
     * Scales an image via a certain ratio
     * @param image
     * @param scale
     * @return
     */
    public static ImageIcon rescaleImage(ImageIcon image, double scale){
        return new ImageIcon(image.getImage().getScaledInstance((int)(image.getIconWidth()*scale),(int)(image.getIconHeight()*scale),0));
    }

    /**
     * Scales an image to match a certain height or width
     * @param image
     * @param measurement
     * @param isHeight
     * @return
     */
    public static ImageIcon rescaleImage(ImageIcon image, int measurement, Boolean isHeight){
        double scale;
        if (isHeight){
            scale = (double) measurement/(double)image.getIconHeight();
        }
        else{
            scale = (double) measurement/(double)image.getIconWidth();
        }
        return rescaleImage(image,scale);
    }

    /**
     * Converts LocalDates to Date
     * @param dateToConvert
     * @return
     */
    public static Date localDatetoDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Hashes and salts the input via SHA-256
     * @param input
     * @param salt
     * @return
     */
    public static String ShaHashFunction(String input,String salt){
        try{
            return HashFunction(HashFunction(input,"SHA-256")+HashFunction(salt,"SHA-256"),"SHA-256");
        }
        catch (Exception e){
            return ""; //This should never occur but it handles the error to prevent Java from moaning.
        }
    }

    /**
     * Performs a specified hashFunction
     * @param input
     * @param function
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String HashFunction(String input,String function) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytesOfMessage = input.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance(function);
        byte[] theMD5digest = md.digest(bytesOfMessage);
        String result = "";
        for (int i = 0; i < theMD5digest.length; i++){
            result +=  (String.format("%02x", theMD5digest[i]));
        }
        return result;
    }

    /**
     * Generates a basic display which can be dynamically created
     * @param width
     * @param height
     * @return
     */
    public static DisplayPanel generatePanel(int width, int height){
        DisplayPanel resultPanel = new DisplayPanel();
        resultPanel.constraints.insets = new Insets(3,3,3,3);
        resultPanel.setSize(new Dimension(width,height));
        resultPanel.constraints.gridx = 0;
        resultPanel.constraints.gridy = 0;
        resultPanel.constraints.fill = GridBagConstraints.HORIZONTAL;
        return resultPanel;
    }

    /**
     * Updates an object in the database
     * @param con Connection to MySQL Database
     * @param prefix Prefix to the command
     * @param allObjects fieldNames and objects for the command plus final suffix object
     * @param suffix final suffix for the command
     * @return
     * @throws SQLException
     */
    public static int Updater(Connection con,String prefix, ArrayList<Object> allObjects, String suffix) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        ArrayList<Object> queryObjects = new ArrayList<>();
        String query = prefix;
        for (int x  = 0; x < (allObjects.size()-1)/2; x++){
            query += " " + allObjects.get(2*(x)) + " = ?";
            if (x+1 != (allObjects.size()-1)/2){
                query += ",";
            }
            queryObjects.add(allObjects.get(2*(x)+1));
        }
        queryObjects.add(allObjects.get(allObjects.size()-1));
        query += suffix;
        return dHelper.execSafeUpdate(query, queryObjects);
    }

    /**
     * Makes a checkbox readonly with a background of Color.lightGray
     * @param self
     * @return
     */
    public static JCheckBox readOnlyCheckbox(JCheckBox self){
        // Removes all previously existing mouse listeners from Checkbox.
        MouseListener[] ml = (MouseListener[])self.getListeners(MouseListener.class);
        for (int i = 0; i < ml.length; i++) {
            self.removeMouseListener(ml[i]);
        }
        //Modifies the input map so no changes occur when keys are used on checkbox.
        InputMap im = self.getInputMap();
        im.put(KeyStroke.getKeyStroke("SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
        self.setBackground(Color.lightGray);
        return self;
    }

    /**
     * Returns if a date is in between two dates
     * @param lower Lower Bound
     * @param query Date in Question
     * @param higher Higher Bound
     * @return Predicate
     */
    public static boolean BetweenDates(LocalDate lower, LocalDate query, LocalDate higher){
        Boolean value = query.compareTo(higher)<= 0 && query.compareTo(lower)>= 0;
        return value;
    }
}
