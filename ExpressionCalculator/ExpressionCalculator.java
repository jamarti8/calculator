/*
ECE 309 Lab 9 by Team 18
Kevin Keller
Francesco Palermo
Justin Martin
 */

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import javax.swing.*;

import static java.lang.Math.*;

public class ExpressionCalculator implements ActionListener {
	
	public static void main(String[] args) {
		System.out.println("Team 18");
		System.out.println("Lab 10");

		new ExpressionCalculator();
        //if (args.length > 0) debug = true; // fix

	}
	
	public boolean debug = false; //Use this to turn command line messages on/off
	String calcMode = "accumulator"; // Default mode is accumulator
    String newLine  = System.lineSeparator();

	// GUI Objects
	JPanel      mainPanel       = new JPanel();
	JPanel      topPanel        = new JPanel();
	JFrame      calcWindow      = new JFrame(); // the graphics window.
	JTextArea   logTextArea     = new JTextArea();
	JScrollPane inScrollPane    = new JScrollPane(logTextArea);
	JTextField  amountTF 		= new JTextField(12);
	JTextField  totalTF   		= new JTextField(12);
	JTextField  errorTF      	= new JTextField(12);
	JTextField  xInputTF			= new JTextField(12);
	JLabel 		xInputLabel		= new JLabel("x = ");
	JLabel 		totalLabel 		= new JLabel("Total:");
	JLabel 		errorLabel 		= new JLabel("Error:");
    JLabel      enterLabel      = new JLabel("<html><b>Accumulator Mode</b><br>Enter value to be added to sum: " +
            "<br> Enter only 2 decimal places." +
            "<br> You can enter a $ at the start if desired.</html>");
	JLabel 		emptyLabel 		= new JLabel("");
	JLabel 		emptyLabel2 	= new JLabel("");
	JButton 	clearButton    	= new JButton("Clear");
	JPanel 		upperTopPanel 	= new JPanel();
	JPanel 		lowerTopPanel 	= new JPanel();
	JPanel 		labelPanel 		= new JPanel();
	JPanel 		totalPanel 		= new JPanel();
	JPanel 		errorPanel 		= new JPanel();
	JScrollPane logTextScroll 	= new JScrollPane(logTextArea);
	GridLayout 	oneByThree 		= new GridLayout(1,3); // rows, cols
	GridLayout 	oneByTwo 		= new GridLayout(1,2);
	GridLayout 	twoByOne 		= new GridLayout(2,1);
	GroupLayout gLayoutTotal 	= new GroupLayout(upperTopPanel);
	static double value = 0;
	
	JMenuBar    menuBar         = new JMenuBar();
	JMenu       pullDownMenu    = new JMenu("Mode");
	JMenuItem   itemAccumulator = new JMenuItem("Accumulator");
	JMenuItem   itemExpression = new JMenuItem("Expression");
	JMenuItem   itemTest = new JMenuItem("Test");
	JMenuItem   itemGraph = new JMenuItem("Graph");	
	
	public ExpressionCalculator() {
		// Add GUI-build code here.
		pullDownMenu.add(itemAccumulator); //Add menu items and add menu bar to window
	    pullDownMenu.add(itemExpression);
	    pullDownMenu.add(itemTest);
	    pullDownMenu.add(itemGraph);
		menuBar.add(pullDownMenu);
		calcWindow.setJMenuBar(menuBar);
		calcWindow.getContentPane().add(mainPanel, "Center"); // Add mainPanel and labelPanel to window
	    calcWindow.getContentPane().add(labelPanel, "North");
		labelPanel.setLayout(oneByThree); // Add items to labelPanel
		labelPanel.add(emptyLabel2);
		labelPanel.add(emptyLabel);
	    labelPanel.add(clearButton);
		mainPanel.setLayout(twoByOne); // Add topPanel to mainPanel
		mainPanel.add(topPanel);
		topPanel.setLayout(twoByOne); // Add upperTopPanel and lowerTopPanel to topPanel
		topPanel.add(upperTopPanel); 
	    topPanel.add(lowerTopPanel); 
	    upperTopPanel.setLayout(twoByOne); // Add items to upperTopPanel
	    upperTopPanel.add(totalPanel);
	    upperTopPanel.add(errorPanel);
	    totalPanel.setLayout(new FlowLayout());
	    totalPanel.add(totalLabel);  
	    totalPanel.add(totalTF);
	    errorPanel.setLayout(new FlowLayout());
	    errorPanel.add(errorLabel);
	    errorPanel.add(errorTF);

		errorPanel.add(xInputLabel);
		errorPanel.add(xInputTF);
		xInputLabel.show(false);
		xInputTF.show(false);

		lowerTopPanel.setLayout(oneByTwo); //Add items to lowerTopPanel
        lowerTopPanel.add(enterLabel);
	    lowerTopPanel.add(amountTF);  
	    mainPanel.add(logTextScroll); // Add logTextScroll to bottom of mainPanel

		// Set attributes of the GUI objects
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    oneByThree.setHgap(30);
	    oneByTwo.setHgap(30);
	    twoByOne.setVgap(10);
		logTextArea.setEditable(false);
		calcWindow.setTitle("Calculator: Accumulator Mode");
		calcWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		totalLabel.setFont(new Font("default", Font.BOLD, 20));
		totalTF.setFont(new Font("default", Font.PLAIN, 25));
		totalTF.setEditable(false);
		totalTF.setColumns(17);
		errorTF.setFont(new Font("default", Font.PLAIN, 25));
		errorTF.setEditable(false);
		errorTF.setColumns(17);
		errorLabel.setFont(new Font("default", Font.BOLD, 20));
		enterLabel.setFont(new Font("default", Font.PLAIN, 12));
		totalTF.setText("00.00");
		amountTF.setFont(new Font("default", Font.BOLD, 25));
	    logTextArea.setLineWrap(true);
	    logTextArea.setWrapStyleWord(true);
	    logTextArea.setText("Log text will go here.");
	    logTextArea.setFont(new Font("default", Font.PLAIN, 12));

		// action listeners
	    clearButton.addActionListener(this);
        amountTF.addActionListener(this);
        itemAccumulator.addActionListener(this);
        itemExpression.addActionListener(this);
        itemTest.addActionListener(this);
        itemGraph.addActionListener(this);

		// Show the window
	    calcWindow.setSize(700, 550);
	    calcWindow.setVisible(true); // show the graphics window
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == clearButton){
			totalTF.setText("00.00");
			errorTF.setText("");
			amountTF.setText("");
		}
		if (ae.getSource() == amountTF) {
			if(calcMode == "accumulator") parseAccumulatorInput();
			else if(calcMode == "expression") parseExpressionInput();
			else if(calcMode == "test") parseTestInput();
			else if(calcMode == "graph") parseGraphInput();
			//System.out.println("enter pressed");
		}
		if (ae.getSource() == itemAccumulator) {
			calcMode = "accumulator";
			xInputLabel.show(false);
			xInputTF.show(false);
			calcWindow.setTitle("Calculator: Accumulator Mode");
			enterLabel.setText("<html><b>Accumulator Mode</b><br>Enter value to be added to sum: " +
								"<br> Enter only 2 decimal places." +
								"<br> You can enter a $ at the start if desired.</html>");
		}
		if (ae.getSource() == itemExpression) {
			calcMode = "expression";
			// show x= box
			xInputLabel.show(true);
			xInputTF.show(true);
			calcWindow.setTitle("Calculator: Expression Mode");
			enterLabel.setText("<html><b>Expression Mode</b><br>ENTER INSTRUCTIONS HERE " +
								"<br> MORE INSTRUCTIONS" +
								"<br> MORE INSTRUCTIONS </html>");
		}
		if (ae.getSource() == itemTest){
			calcMode = "test";
			xInputLabel.show(true);
			xInputTF.show(true);
			calcWindow.setTitle("Calculator: Test Mode");
			enterLabel.setText("<html><b>Test Mode</b><br>ENTER INSTRUCTIONS HERE " +
								"<br> MORE INSTRUCTIONS" +
								"<br> MORE INSTRUCTIONS </html>");
		}
		if (ae.getSource() == itemGraph){
			calcMode = "graph";
			calcWindow.setTitle("Calculator: Graph Mode");
			enterLabel.setText("<html><b>Graph Mode</b><br>ENTER INSTRUCTIONS HERE " +
								"<br> MORE INSTRUCTIONS" +
								"<br> MORE INSTRUCTIONS </html>");
		}
	}

	
	/*
	 * Accumulator mode:
    This method parses the input and sends the clean properly formatted
    decimal to the accumulate and print method.
     */
	private void parseAccumulatorInput() {

        errorTF.setText(" "); // clear error each time calculate is pressed
        // clear text area if first time pressed
        if (logTextArea.getText().contains("Log text will go here"))
        {
            logTextArea.setText("");
        }

        try {
            String entry = amountTF.getText().trim();

            // removes leading $ if it's there
            entry = entry.startsWith("$") ? entry.substring(1) : entry;

            // check that only 2 decimal places are used
            if (entry.contains("."))
            {
                //System.out.println(". detected");
                int idx = entry.indexOf(".");
                int lst_idx = entry.length();
                if( (lst_idx - idx) != 3) {
                    errorTF.setText("Enter 2 decimal places.");
                    return;
                }
            }
            //System.out.println("Entered: " + entry);
            double enteredValue = Double.parseDouble(entry);
            accumulateAndPrint(enteredValue);

        } catch (NumberFormatException nfe)
        {
            System.out.println("Non-numeric value entered.");
            errorTF.setText("Please enter a numeric value.");
        }
	}
	
	/*
	 * Expression mode:
    This method parses the input and sends the clean properly formatted
    decimal to the accumulate and print method.
     */
	private void parseExpressionInput() {
		errorTF.setText(" "); // clear error each time calculate is pressed
		// clear text area if first time pressed
		if (logTextArea.getText().contains("Log text will go here"))
		{
			logTextArea.setText("");
		}

		// read input x value
		String x = xInputTF.getText().trim();
		Double xValue = 0.0;
		if (x.equals("")) {
			xValue = 0.0;
		}
		else if (x.equalsIgnoreCase("e")) xValue = E;
		else if (x.equalsIgnoreCase("-e")) xValue = -E;
		else if (x.equalsIgnoreCase("pi")) xValue = PI;
		else if (x.equalsIgnoreCase("-pi")) xValue = -PI;
		else {
			try {
				xValue = Double.parseDouble(x);
				//System.out.println("double parse");
			} catch (NumberFormatException nfe) {
				errorTF.setText("Please set x to be a number");
				//System.out.println("error in parse");
				return;
			}
		}
		//System.out.println("X value is: " + xValue);

		// read input expression
		String expression = amountTF.getText();
		if (expression.length() == 0) errorTF.setText("Input an expression");

		// set expression to known values
		if (expression.endsWith("=")) // allow it but drop it
			expression = expression.replace("=","");
		if (expression.contains("R")) // replace R with r
			expression = expression.replace("R", "r");
		if (expression.contains("X"))
			expression = expression.replace("X", "x");
		if (expression.contains("E"))
			expression = expression.replace("E", "e");
		if (expression.contains("PI"))
			expression = expression.replace("PI", "pi");
		if (expression.contains("Pi"))
			expression = expression.replace("Pi","pi");

		// needs to be written
		if (stringContainsIllegalCharacters(expression))
			errorTF.setText("Expression contains an illegal character");

		// replace unary operator (-) with (+u)
		// needs to be written
		expression = replaceUnary(expression);

		// if parentheses don't exist, add them outside
		// this makes it easier to just call one function to deal with everything
		expression = addParentheses(expression);

		// if parentheses exist, replace expression inside innermost one, call recursivly


		System.out.println(expression);
		// replace e, pi, and x with value



		// if string contains


	}
	
	/*
	 * Test mode:
    This method parses the input and sends the clean properly formatted
    decimal to the accumulate and print method.
     */
	private void parseTestInput() {

	}
	
	/*
	 * Graph mode:
    This method parses the input and sends the clean properly formatted
    decimal to the accumulate and print method.
     */
	private void parseGraphInput() {

	}

    /*
        This method takes the clean input value, adds the new input
        and prints it the gui
     */
    private void accumulateAndPrint(double input)
    {
        double prevVal = value;
        //System.out.println(prevVal);
        value = value + input; // add new input
        System.out.println(value);

        // convert values
        BigDecimal inputBD = new BigDecimal(input,MathContext.DECIMAL64);//set precision to 16 digits
        inputBD = inputBD.setScale(2,BigDecimal.ROUND_UP);//scale (2) is # of digits to right of decimal point.

        BigDecimal  totalBD = new BigDecimal(value,MathContext.DECIMAL64);//set precision to 16 digits
        totalBD = totalBD.setScale(2,BigDecimal.ROUND_UP);//scale (2) is # of digits to right of decimal point.

        BigDecimal previousTotalBD = new BigDecimal(prevVal,MathContext.DECIMAL64);//set precision to 16 digits
        previousTotalBD = previousTotalBD.setScale(2,BigDecimal.ROUND_UP);//scale (2) is # of digits to right of decimal point.

        String totalString = totalBD.toPlainString();// no exponents
        String previousTotalString = previousTotalBD.toPlainString();
        String inputString = inputBD.toPlainString();

        // set values to GUI
        totalTF.setText(totalString);
        logTextArea.append(previousTotalString + " + " + inputString + " = " + totalString + newLine);
        amountTF.setText(" ");
    }

	/*
	This function can take two operands and an operator and return their value
	 */
	private double evaluateSimpleExpression(double left, char operator, double right)
	{
		switch (operator)
		{
			case '+': return left + right;
			case '-': return left - right;
			case '*': return left*right;
			case '/': return left/right;
			case '^': return pow(left,right);
			case 'r': return pow(left, (1.0/right)); // evaluates roots
			default: {
				errorTF.setText("please only include: + - * / ^ r");
				return 0;
			}
		}
	}

	private boolean stringContainsIllegalCharacters(String expression)
	{
		// check if expression has things not numbers or allowed values

		return false; // no illegals
	}

	private String replaceUnary(String expression)
	{
		//if (expression.contains())

		return expression;
	}

	private String addParentheses(String expression)
	{
		if(!expression.startsWith("("))
			expression = "("+expression;
		if(!expression.endsWith(")"))
			expression = expression+")";
		return expression;
	}

}
