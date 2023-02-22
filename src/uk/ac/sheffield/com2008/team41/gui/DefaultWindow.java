package uk.ac.sheffield.com2008.team41.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

//import com.mysql.cj.api.log.Log;
import uk.ac.sheffield.com2008.team41.gui.panels.*;
import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.models.Guest;
import uk.ac.sheffield.com2008.team41.models.*;

/**
 * DefaultWindow to contain a toolbar and layered panel with whatever panel required inside.
 * Credit: Team 41 Homebreak submission 2021
 */
public class DefaultWindow extends JFrame {

    protected Connection con;
    public DriverHelper helper;
    private static final String title = "Homebreak PLC - AirBNB but better";
    public int width = 0;
    public int height = 0;
    public int winheight;
    private static ImageIcon logo;
    private HomebreakToolbar toolbar;
    private DisplayPanel currentWindow;
    private JLayeredPane layeredPane = new JLayeredPane();
    public Person user;

    /**
     * Creates the default window which will be used throughout the program possessing a set width, height and a database connection
     * @param w
     * @param h
     * @param con Connection to MySQL Database
     */
    public DefaultWindow(int w, int h, Connection con){
        DefaultWindow self = this;
        width = w;
        height = h;
        winheight = (int)(height*0.9);
        this.con = con;
        this.helper = new DriverHelper(con);
        //Gets resource and sets icon
        logo = new ImageIcon(getClass().getResource("img/logo.png"));
        this.setIconImage(logo.getImage());
        currentWindow = new Login(width,height,self,logo);
        setTitle(title);
        setSize(width,height);
        setVisible(true);
        setExtendedState(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        resetToolbar(true);
        setLocationRelativeTo(null);
        layeredPane.add(currentWindow);
        createGUI(true,false,false);
        revalidate();
        repaint();
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                finally {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Changes the current panel to a new panel and is used pretty much all the way throughout the program
     * @param panel
     */
    public void changeCurrentWindow(DisplayPanel panel){
        layeredPane.remove(currentWindow);
        currentWindow = panel;
        layeredPane.add(currentWindow);
        if (currentWindow instanceof Login | currentWindow instanceof Register){
            layeredPane.setSize(width,winheight);
        }
        else{
            layeredPane.setSize(width,height);
        }
        revalidate();
        repaint();
    }

    /**
     * Refreshes the gui
     * @param addPane
     * @param removePane
     * @param withToolbar
     */
    public void createGUI(Boolean addPane, Boolean removePane , Boolean withToolbar){
        if (withToolbar){
            resetToolbar(true);
        }
        if (removePane){
            remove(layeredPane);
        }
        if (withToolbar){
            add(toolbar, BorderLayout.NORTH);
        }
        else{
            remove(toolbar);
        }
        if (addPane){
            add(layeredPane);
        }
        revalidate();
        repaint();
    }


    /**
     * Recreates the toolbar for the gui and repaints
     * @param withOptions
     */
    private void resetToolbar(Boolean withOptions){
        toolbar = new HomebreakToolbar(width,(int)(height*0.1),logo);
        if (withOptions){
            addAllToolbarOptions();
        }
        revalidate();
        repaint();
    }

    /**
     * Adds all of the base user options ot the toolbar
     */
    private void addAllToolbarOptions(){
        DefaultWindow self = this;
        String logoutLabel = "Return to Login";
        if (user == null){
        }
        else {
            boolean person = (user instanceof Person);
            logoutLabel = "Logout";
            if (person){
                toolbar.addButton("Home Page", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        self.changeCurrentWindow(new HomePage(width,winheight,self));
                    }
                });
            }
            if (user instanceof Host){
                toolbar.addButton("My Bookings", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        self.changeCurrentWindow(new ViewBookingsHost(width, winheight, self));
                    }
                });
                toolbar.addButton("My Properties", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        self.changeCurrentWindow(new HostProperties(width,winheight,self));
                    }
                });
            }
            else if (user instanceof Guest){
                toolbar.addButton("My Bookings", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        self.changeCurrentWindow(new ViewBookingsGuest(width, winheight, self));
                    }
                });
                toolbar.addButton("Search Properties", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        self.changeCurrentWindow(new SearchPage(width,winheight, self));
                    }
                });

            }
        }

        toolbar.addButton(logoutLabel, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user = null;
                createGUI(true,true,false);
                self.changeCurrentWindow(new Login(width,height,self,logo));
            }
        });
    }

    /**
     * Returns the Connection to the Database
     * @return
     */
    public Connection getCon (){
        return this.con;
    }

}
