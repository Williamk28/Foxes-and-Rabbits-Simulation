import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.Timer;


//This class makes a GUI interface to control the simulation
public class GUI implements ActionListener{ //This receives action events whenever a button is pressed
	
	private JButton Run; //Creates a button called Run
	private JButton Reset; //Creates a button called Reset
	private JButton Stop; //Creates a button called Stop
	private JButton Step; //Creates a button called Step
	private JButton Quit; //Creates a button called Step
	private JRadioButton One; //Creates a radio button called One
	private JRadioButton Fifty; //Creates a radio button called Fifty
	private JRadioButton TwoF; //Creates a radio button called TwoF
	private JRadioButton Five; //Creates a radio button called Five
	private JRadioButton Forever; //Creates a radio button called Forever
	private JRadioButton Speed1; //Creates a radio button called Speed1
	private JRadioButton Speed2; //Creates a radio button called Speed2
	
	private JFrame frame; //This creates a java frame
	private Simulator sim; //Declares sim with type Simulator class
	
	Timer simTimer = new Timer(50, this); //Declares simTimer as a Timer object and initialises it
	Timer FastTimer = new Timer(25, this); //Declares FastTimer as a Timer object and initialises it

	//Creates the GUI
	public GUI () {
    	this.frame();
    }
	
	
	//Creates the frame
	public void frame() {
		frame = new JFrame("Predator/Prey Simulation"); //This creates the frame for the simulation and the title is called Predator/Prey Simulation
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //This closes the frame when the user clicks the Exit Button
    	Container content = frame.getContentPane(); //This is a container object where it stores the contents for the frame 	
    	sim = new Simulator(); //This initialise the simulator as sim
    	
    	JTabbedPane tab = new JTabbedPane(); //This declares and initialises the tabs for the frame
    	
    	JPanel panel1 = new JPanel(new BorderLayout()); //This creates a panel
    	JPanel panel2 = new JPanel(new GridLayout(3,3)); //This creates another panel where the column and row is 3
    	JPanel panel3 = new JPanel(new BorderLayout()); //This creates another panel
    	JPanel panel4 = new JPanel(new GridLayout(1,5)); //This creates another panel with a layout 1 by 4
    	JPanel panel5 = new JPanel(new BorderLayout()); //This creates another panel
    	
    	//This is the text for the Welcome tab 
    	JLabel welcome = new JLabel("Welcome!"); 
    	welcome.setFont(new Font(null, Font.BOLD, 44)); //Sets the font size to 44 and makes it bold
    	
    	JTextArea WelcomeText = new JTextArea("This is a simulation for predators and preys." + "\n" +
    	"Here, i'll explain to you how this simulation will work." + "\n" +
    	"Simulation consists of Hunters, Foxes, Rabbits, Fruits and Poisonus Berries." + "\n" +
    	"Hunters can hunt and eat anything but very few will spawn on the field."  + "\n" +
    	"Foxes can only hunt and eat rabbits, fruits or poisonus berries." + "\n" +
    	"Rabbits can only eat fruits and poisonus berries." + "\n" + 
    	"If anyone eats a Poisonus Berry they will die in the next step." + "\n" +
    	"Fruits can give birth by planting seeds and make them sprout to new Fruits." + "\n" +
    	"New Poisonous Berries can be made if they die from old age");
    	
    	WelcomeText.setEditable(false); //Makes the text area non editable
    	WelcomeText.setFont(new Font(null, Font.PLAIN, 14)); //Set the font 14

    	JLabel text1 = new JLabel("To start the simulation click the Simulation Settings Tab.");
    	text1.setFont(new Font(null, Font.BOLD, 14)); //This makes the text bold 
    	
    	//This is the text for the Simulation Settings tab
    	JLabel text2 = new JLabel("Steps:");
    	JLabel text3 = new JLabel("Speed:");
    	
    	//This is the text for the Help tab with the text size as 14
    	JLabel help = new JLabel("Help!"); 
    	help.setFont(new Font(null, Font.BOLD, 44)); //Sets the font size to 44 and makes it bold
    	JTextArea HelpText = new JTextArea("The Step Buttons are used to specify how many steps you want to simulate."+ "\n" +
    	"The Speed Buttons are used to specify the speed of the simulation." + "\n" +
    	"The Run Button is used to simulate the simulation." + "\n" + 
    	"The Step Button is used to simulate one step in the simulation." + "\n" +
    	"Hunter is represented as black on the field." + "\n" +
    	"Fox is represented as blue on the field." + "\n" +
    	"Rabbit is represented as orange on the field." + "\n" +
    	"Fruits is represented as magenta on the field." + "\n" +
    	"Poisonous Berries is represented as red on the field.");
    	
    	HelpText.setEditable(false);
    	HelpText.setFont(new Font(null, Font.PLAIN, 14));
    	
    	Run = new JButton("Run"); //This initialises the Run Button
    	Run.addActionListener(this); //Actions will be performed when the Run button is clicked 
    	
    	Step = new JButton("Step"); //This initialises the Step Button
    	Step.addActionListener(this); //Actions will be performed when the Step button is clicked
    	
    	Reset = new JButton("Reset"); //This initialises the Reset Button
    	Reset.addActionListener(this); //Actions will be performed when the Reset button is clicked
    	
    	Stop = new JButton("Stop"); //This initialises the Stop Button
    	Stop.addActionListener(this); //Actions will be performed when the Stop button is clicked
    	
    	Quit = new JButton("Quit"); //This initialises the Quit Button
    	Quit.addActionListener(this); //Actions will be performed when the Quit button is clicked
    	
    	Fifty = new JRadioButton("50"); //This initialises the Fifty radio button called 50
    	Fifty.setSelected(true); //Sets the default radio button to 50
    	Fifty.addActionListener(this);; //Actions are performed when the 50 is selected
    	
    	One = new JRadioButton("100"); //This initialises the One radio button called 100
    	One.addActionListener(this); //Actions are performed when the 100 is selected
    	
    	TwoF = new JRadioButton("250"); //This initialises the TwoF radio button called 250
    	TwoF.addActionListener(this); //Actions are performed when the 250 is selected
    	
    	Five = new JRadioButton("500"); //THis initialises the Five radio button called 500
    	Five.addActionListener(this); //Actions are performed when the 500 is selected
    	
    	Forever = new JRadioButton("Forever"); //THis initialises the Five radio button called 500
    	Forever.addActionListener(this); //Actions are performed when the 500 is selected
    	
    	
    	Speed1 = new JRadioButton("1");
    	Speed1.setSelected(true);
    	Speed1.addActionListener(this);
    	
    	Speed2 = new JRadioButton("2");
    	Speed2.addActionListener(this);
    	
    	ButtonGroup group = new ButtonGroup(); //THis groups the radio button so only one can be selected
    	//Adds all the radio buttons to group
    	group.add(Fifty); 
    	group.add(One);
    	group.add(TwoF);
    	group.add(Five);
    	group.add(Forever);
    	
    	ButtonGroup group1 = new ButtonGroup();
    	group1.add(Speed1);
    	group1.add(Speed2);
    	
    	//This adds all the labels for the Welcome tab to panel1
    	panel1.add(welcome, BorderLayout.NORTH);
    	panel1.add(WelcomeText, BorderLayout.CENTER);
    	panel1.add(text1, BorderLayout.SOUTH);
    	
    	//This adds labels and radio buttons for the Simulation Settings tab to panel 2
    	panel2.add(text2);
    	panel2.add(Fifty);
    	panel2.add(One);
    	panel2.add(TwoF);
    	panel2.add(Five);
    	panel2.add(Forever);
    	panel2.add(text3);
    	panel2.add(Speed1);
    	panel2.add(Speed2);
    	
    	//This adds the buttons to the panel 3
    	panel4.add(Run);
    	panel4.add(Step);
    	panel4.add(Reset);
    	panel4.add(Stop);
    	panel4.add(Quit);
    	
    	//This adds all the labels for the Help tab to panel 3
    	panel3.add(help, BorderLayout.NORTH);
    	panel3.add(HelpText, BorderLayout.CENTER);
    	
    	//This adds the panels so it can be sorted in the borderlayout
    	panel5.add(panel2, BorderLayout.CENTER);
    	panel5.add(panel4, BorderLayout.SOUTH);
    	
    	//This adds the name of the tab and the panel
    	tab.add("Welcome", panel1);
    	tab.add("Simulation Settings", panel5);
    	tab.add("Help", panel3);
    	
    	content.add(tab, BorderLayout.CENTER); //Adds the tab to the container content for the frame
    	
    	frame.pack(); //This sizes everything in the frame so that they will all fit
    	frame.setVisible(true); //Makes the frame visible
    	frame.setSize(500, 400); //Sets the size of the frame
    	
    	
	}

	//This performs actions
	public void actionPerformed(ActionEvent event) {
	
		if(event.getSource() == Run && Speed1.isSelected()) { //If the run button is clicked and the speed is 1 then start the timer
			simTimer.start();		
		}
		else if (event.getSource() == simTimer) { //If the timer has started then simulate one step
			sim.simulateOneStep();		
			if(Fifty.isSelected()) { //If the radio button 50 is selected then 
				if (sim.getStep() >= 50) { //Stop the timer if the step is greater than or equal to 50
					simTimer.stop();
				}
			}
			else if(One.isSelected()) { //If the radio button 100 is selected then
				if (sim.getStep() >= 100) { //Stop the timer if the step is greater than or equal to 100
					simTimer.stop();
				}
			}
			
			else if(TwoF.isSelected()) { //If the radio button 250 is selected then
				if (sim.getStep() >= 250) { //Stop the timer if the step is greater than or equal to 250
					simTimer.stop();
				}
			}
			else if (Five.isSelected()) { //If the radio button 500 is selected then
				if(sim.getStep() >= 500) { //Stops the timer if the step is greater than or equal to 500
					simTimer.stop();
				}
			}
			else if (Forever.isSelected()) { //RUNS FOREVER unless its stopped by the user
			}		
		}
		
		if(event.getSource() == Run && Speed2.isSelected()) { //If run is clicked and speed is 2 then start timer
			FastTimer.start();
		}
		else if(event.getSource() == FastTimer) {
			sim.simulateOneStep();
			if(Fifty.isSelected()) { //If the radio button 50 is selected then 
				if (sim.getStep() >= 50) { //If the simulator step is greater than or equal to 50 then stop the timer
					FastTimer.stop();
				}
			}
			
			else if(One.isSelected()) { //If the radio button 100 is selected then
				if (sim.getStep() >= 100) { //Stop the timer if the step is greater than or equal to 100
					FastTimer.stop();
				}
			}
			
			else if(TwoF.isSelected()) { //If the radio button 250 is selected then
				if (sim.getStep() >= 250) { //Stop the timer if the step is greater than or equal to 250
					FastTimer.stop();
				}
			}
			
			else if (Five.isSelected()) { //If the radio button 500 is selected then
				if(sim.getStep() >= 500) { //Stops the timer if the step is greater than or equal to 500
					FastTimer.stop();
				}
			}
			else if(Forever.isSelected()) {	//Runs FOREVER unless stopped by user
			}	
		}
		
		if (event.getSource() == Step) { //If the step button is clicked then simulate one step
			sim.simulateOneStep();
		}
		
		if (event.getSource() == Reset) { //If the reset button is clicked then reset the simulation back to step 0
			sim.reset();
		}
		
		if (event.getSource() == Stop) { //If the stop button is clicked then stop the simulation
			simTimer.stop();
			FastTimer.stop();
		}
		if (event.getSource() == Quit) { //If the stop button is clicked then stop the simulation
			System.exit(0); //This stops the program from running and closes the simulation and the frame
		}
	}
}
