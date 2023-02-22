package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.CustomTextField;
import uk.ac.sheffield.com2008.team41.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

/**
 * Panel for logging into a user account
 */
public class Login extends DisplayPanel {
    private DefaultWindow window;
    private JLabel email = new JLabel("Email: ");
    private JLabel password = new JLabel("Password: ");
    private CustomTextField emailField = new CustomTextField(30,null);
    private JPasswordField  passwordField = new JPasswordField(15);
    private JLabel error = new JLabel();

    /**
     * Creates a login panel with a set width and height, using window for con, setting user and changing page as well as logo for displaying the logo
     * @param width
     * @param height
     * @param window
     * @param logo
     */
    public Login(int width, int height, DefaultWindow window, ImageIcon logo)
    {
        super(width, height);
        this.window = window;
        createBorder();
        constraints.insets = new Insets(3,3,3,3);
        error.setForeground(Color.RED);

        JLabel labelLogo = new JLabel(logo);
        JButton guestLogin = new JButton("Login as Guest");
        JButton hostLogin = new JButton("Login as Host");
        JButton register = new JButton("Don't have an account - Register");
        JButton enquirer = new JButton("Just browsing - Login later");
        DisplayPanel loginOptions = new DisplayPanel();
        constraints.gridx=1;
        constraints.gridy=0;
        addNew(labelLogo);

        constraints.gridy=1;
        addNew(error);

        constraints.gridx=0;
        constraints.gridy=2;
        addNew(email);
        constraints.gridx=1;
        addNew(emailField);

        constraints.gridx=0;
        constraints.gridy=3;
        addNew(password);
        constraints.gridx=1;
        addNew(passwordField);

        loginOptions.constraints.insets = new Insets(0,3,0,3);
        loginOptions.constraints.gridx = 0;
        loginOptions.addNew(guestLogin);
        loginOptions.constraints.gridx = 1;
        loginOptions.addNew(hostLogin);

        constraints.gridx=1;
        constraints.gridy=5;
        add(loginOptions, constraints);
        constraints.gridx=1;
        constraints.gridy=6;
        add(register, constraints);
        constraints.gridx=1;
        constraints.gridy=7;
        add(enquirer, constraints);

        /**
         * Handles Host Login, and reports error on empty field.
         */
        hostLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailText = emailField.getText().toLowerCase(Locale.ROOT);
                String passwordText = new String(passwordField.getPassword());
                
                if (isEmptyField(emailText,emailField, error, "Please enter your email address to login as host") ||
                		isEmptyField(passwordText,passwordField, error, "Please enter the password to login as host")) {
                	
                }
                else {
	                Host user = Host.userWithCredentials(emailText,passwordText,window.getCon());
	                
	                if (user != null){
	                    window.user = (Person)user;
	                    window.createGUI(true,true,true);
	                    window.changeCurrentWindow(new HomePage(window.width+10,window.winheight,window));
	                }
	                else{
	                    error.setText("There is no host with your specified credentials");
	                } 
                }
            }
        });

        /**
         * Handles Guest Login, and reports error on empty field.
         */
        guestLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailText = emailField.getText().toLowerCase(Locale.ROOT);
                String passwordText = new String(passwordField.getPassword());
                if (isEmptyField(emailText,emailField, error, "Please enter your email address to login as guest") ||
                		isEmptyField(passwordText,passwordField, error, "Please enter the password to login as guest")) {

                }
                else {
	                Guest user = (Guest) Guest.userWithCredentials(emailText,passwordText,window.getCon());
	                if (user != null){
	                    window.user = (Person)user;
	                    window.createGUI(true,true,true);
	                    window.changeCurrentWindow(new HomePage(window.width,window.winheight,window));
	                }
	                else{
	                    error.setText("There is no guest with your specified credentials");
	                }  
                }
            }
        });

        //Changes the window to enquirer making sure user is null.
        enquirer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.user = null;
                window.createGUI(true,true,true);
                window.changeCurrentWindow(new SearchPage(window.width,window.winheight,window));
            }
        });

        //Redirects the user to the register page upon click
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.changeCurrentWindow(new Register(window.width,window.height,window,logo));
            }
        });
    }
}
