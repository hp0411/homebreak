package uk.ac.sheffield.com2008.team41;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;

import java.awt.*;
import java.sql.*;

class Main {
    final static double ratio = 0.7;

    public static void main(String[] args)  {
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team041", "team041", "8840cc73");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            DefaultWindow window = new DefaultWindow((int) (screenSize.width * ratio ), (int) (screenSize.height * ratio), con);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}