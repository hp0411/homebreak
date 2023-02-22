package uk.ac.sheffield.com2008.team41.gui.panels;

import uk.ac.sheffield.com2008.team41.gui.DefaultWindow;
import uk.ac.sheffield.com2008.team41.gui.panelFeatures.*;
import uk.ac.sheffield.com2008.team41.helper.ValidationHelper;
import uk.ac.sheffield.com2008.team41.models.Address;
import uk.ac.sheffield.com2008.team41.models.Guest;
import uk.ac.sheffield.com2008.team41.models.Host;
import uk.ac.sheffield.com2008.team41.models.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Creates a panel where users can register accounts
 * Credit: Team 41 Homebreak submission 2021
 */
public class Register extends DisplayPanel {
    private DefaultWindow window;
    private static String[] comboOptions = {"Host and Guest", "Host", "Guest"};

    /**
     * Creates a Register panel to allow users to register and make new accounts
     * @param width
     * @param height
     * @param window
     * @param logo
     */
    public Register(int width, int height, DefaultWindow window,ImageIcon logo)
    {
        super(width,height);
        this.window = window;
        constraints.insets = new Insets(3,3,3,3);
        createBorder();

        //Creates all fields and buttons
        CustomTextField titleField = new CustomTextField(5,"([0-9])*");
        CustomTextField forenameField = new CustomTextField(30,"([0-9])*");
        CustomTextField surnameField = new CustomTextField(30,"([0-9])*");
        CustomTextField phoneNumberField = new CustomTextField(13,"([a-zA-Z])*");
        CustomTextField emailField = new CustomTextField(30,null);
        JPasswordField passwordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        CustomTextField houseNumber = new CustomTextField(20,null);
        CustomTextField streetName = new CustomTextField(20,null);
        CustomTextField placeName = new CustomTextField(20,null);
        CustomTextField postCode = new CustomTextField(7,null);
        JComboBox<String> personChoose = new JComboBox<>(comboOptions);
        CustomTextField hostName = new CustomTextField(20,null);
        CustomTextField guestName = new CustomTextField(20,null);
        JButton register = new JButton("Register");
        JButton returnToLogin = new JButton("Return to Login");
        JLabel error = new JLabel("");
        JLabel succ = new JLabel("");

        constraints.gridx=1;
        constraints.gridy=2;
        addNewLabel("Login Information");

        //Adds all display layouts
        constraints.gridx=0;
        constraints.gridy=3;
        addNewLabel("Title:");
        constraints.gridx=1;
        addNew(titleField);
        constraints.gridx=0;
        constraints.gridy=4;
        addNewLabel("Forename: ");
        constraints.gridx=1;
        addNew(forenameField);
        constraints.gridx=0;
        constraints.gridy=5;
        addNewLabel("Surname:");
        constraints.gridx = 1;
        addNew(surnameField);
        constraints.gridx=0;
        constraints.gridy=6;
        addNewLabel("Phone Number:");
        constraints.gridx = 1;
        addNew(phoneNumberField);
        constraints.gridx=0;
        constraints.gridy=7;
        addNewLabel("Email: ");
        constraints.gridx=1;
        addNew(emailField);
        constraints.gridx=0;
        constraints.gridy=8;
        addNewLabel("Password: ");
        constraints.gridx=1;
        addNew(passwordField);
        constraints.gridx=0;
        constraints.gridy=9;
        addNewLabel("Confirm Password: ");
        constraints.gridx=1;
        addNew(confirmPasswordField);

        constraints.gridx=2;
        addNewLabel("       "); //Padding

        constraints.gridx = 4;
        constraints.gridy = 2;
        addNewLabel("Address Information");
        constraints.gridx=3;
        constraints.gridy = 3;
        addNewLabel("House Number/Name: ");
        constraints.gridx = 4;
        addNew(houseNumber);
        constraints.gridx =3;
        constraints.gridy = 4;
        addNewLabel("Street Name: ");
        constraints.gridx = 4;
        addNew(streetName);
        constraints.gridx = 3;
        constraints.gridy = 5;
        addNewLabel("Place Name: ");
        constraints.gridx = 4;
        addNew(placeName);
        constraints.gridx = 3;
        constraints.gridy = 6;
        addNewLabel("Postcode: ");
        constraints.gridx = 4;
        addNew(postCode);
        constraints.gridx = 3;
        constraints.gridy = 7;
        addNewLabel("Choose if you want to be Host and or Guest:");
        constraints.gridx = 4;
        addNew(personChoose);
        constraints.gridx = 3;
        constraints.gridy = 8;
        addNewLabel("Host Name:");
        constraints.gridx = 4;
        addNew(hostName);
        constraints.gridx = 3;
        constraints.gridy = 9;
        addNewLabel("Guest Name:");
        constraints.gridx = 4;
        addNew(guestName);


        constraints.gridwidth = 5;
        constraints.gridx = 0;
        constraints.gridy = 7;
        addNewLabel("   ");
        constraints.gridy = 1;
        addNewLabel("   ");
        constraints.gridy = 0;
        addNew(new JLabel(logo));
        constraints.gridy = 10;
        addNew(error);
        constraints.gridy = 11;
        addNew(register);
        constraints.gridy = 12;
        addNew(returnToLogin);
        constraints.gridy = 13;
        addNew(succ);

        //Registers a new user
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String houseNumberVal = houseNumber.getText();
                String streetNameVal = streetName.getText();
                String placeNameVal = placeName.getText();
                String postCodeVal = postCode.getText();

                String title = titleField.getText();
                String forename = forenameField.getText();
                String surname = surnameField.getText();
                String email = emailField.getText();
                String phoneNumber = phoneNumberField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
                //Checks if information is missing
                String errorMessage = "Please fill in the missing information";
                if (isEmptyField(title,titleField, error,errorMessage) || isEmptyField(forename,forenameField, error,errorMessage)
					|| isEmptyField(surname,surnameField, error,errorMessage) || isEmptyField(phoneNumber,phoneNumberField, error,errorMessage)
					|| isEmptyField(email,emailField, error,errorMessage) || isEmptyField(password,passwordField, error,errorMessage)
					|| isEmptyField(confirmPassword,confirmPasswordField, error, errorMessage) || isEmptyField(houseNumberVal,houseNumber, error, errorMessage)
					|| isEmptyField(streetNameVal,streetName, error, errorMessage) || isEmptyField(placeNameVal,placeName, error, errorMessage)
			        || isEmptyField(postCodeVal,postCode, error,errorMessage)
					) {
	
                }
                //Checks other validation
                else {
	                boolean emailValid = ValidationHelper.patternMatches(email, ValidationHelper.emailRegex);
	                boolean passwordSame = password.equals(confirmPassword);
	                boolean passwordValid = ValidationHelper.patternMatches(password, ValidationHelper.passwordRegex);
	                String errorString = "";
	                if (!emailValid){
	                    errorString = "Your email does not comply!";
	                }else if (Person.findUser(window.getCon(), email)){
                        errorString = "Your email is already being used!";
                    }
                    else if (!passwordValid){
	                    errorString = "Your password should have 8+ characters; At least one lowercase and uppercase character; At least one special character";
	                }else if (!passwordSame){
	                    errorString = "Your passwords are not the same!";
	                }

	
	                error.setText(errorString);
	              
	                if (error.getText().equals("")){
	                    try {
                            //Adds new User to database
                            window.getCon().setAutoCommit(false);
	                        // First make address object/ record
	                        Address personAddress = new Address(window.getCon(), houseNumberVal, streetNameVal, placeNameVal, postCodeVal);
	                        // Second make person object
	                        Person person = new Person(window.getCon(), title, email, forename, surname, password, phoneNumber, personAddress);
	
	                        // Third make the host and or guest objects
	                        String hostNameVal = hostName.getText();
	                        String guestNameVal = guestName.getText();
	                        if (hostNameVal.equals("")){
	                        	hostNameVal = forename;
	                        }
	                        if (guestNameVal.equals("")){
	                        	guestNameVal = forename;
	                        }
	                        Host host;
	                        Guest guest;
	                        switch((String) personChoose.getSelectedItem()){
	                            case ("Host and Guest"):
	                                host = new Host(window.getCon(), person.getEmail(), hostNameVal);
	                                guest = new Guest(window.getCon(), person.getEmail(), guestNameVal);
	                                break;
	                            case ("Host"):
	                                host = new Host(window.getCon(), person.getEmail(), hostNameVal);
	                                break;
	
	                            case ("Guest"):
	                                guest = new Guest(window.getCon(), person.getEmail(), guestNameVal);
	                                break;
	                        }

                            window.getCon().commit();
                            window.getCon().setAutoCommit(true);
	                        succ.setText("You have completed registration! Please press \"Return to Login\" to go to the login page");
                        

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

                }

            }
        });
        //Returns the user to the login page
        returnToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.changeCurrentWindow(new Login(width,height,window,logo));
            }
        });
        //Sets fields depending on if Guest, Host or Both
        personChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch((String) personChoose.getSelectedItem()){
                    case ("Host and Guest"):
                        hostName.setText("");
                        hostName.setEditable(true);

                        guestName.setText("");
                        guestName.setEditable(true);
                        break;
                    case ("Host"):
                        hostName.setText("");
                        hostName.setEditable(true);

                        guestName.setText("");
                        guestName.setEditable(false);
                        break;

                    case ("Guest"):
                        hostName.setText("");
                        hostName.setEditable(false);

                        guestName.setText("");
                        guestName.setEditable(true);
                        break;
                }
            }
        });
    }
    

}
