/*
ECE 309 Lab 9 by Team 18
Kevin Keller
Francesco Palermo
Justin Martin
 */

import com.sun.xml.internal.ws.util.StringUtils;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import javax.swing.*;

import static java.lang.Math.*;

public class ExpressionCalculator implements ActionListener {
	
	public static void main(String[] args) {
		System.out.println("Team 18");
		System.out.println("Lab 10");

		new ExpressionCalculator();
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
	JTextField  resultTF   		= new JTextField(12);
	JTextField  errorTF      	= new JTextField(12);
	JTextField  xInputTF		= new JTextField(12);
	JTextField  totalCorrectTF	= new JTextField(12);
	JTextField  totalWrongTF= new JTextField(12);
	JLabel 		xInputLabel		= new JLabel("x = ");
	JLabel 		resultLabel		= new JLabel("Result:");
	JLabel 		errorLabel 		= new JLabel("Error:");
    JLabel      enterLabel      = new JLabel("<html><b>Accumulator Mode</b><br>Enter value to be added to sum: " +
            "<br> Enter only 2 decimal places." +
            "<br> You can enter a $ at the start if desired.</html>");
	JLabel 		emptyLabel 		= new JLabel("");
	JLabel 		emptyLabel2 	= new JLabel("");
	JLabel 		totalCorrectLabel = new JLabel("Total Correct:");
	JLabel 		totalWrongLabel = new JLabel("Total Wrong:");
	JButton 	clearButton    	= new JButton("Clear");
	JPanel 		upperTopPanel 	= new JPanel();
	JPanel 		lowerTopPanel 	= new JPanel();
	JPanel 		labelPanel 		= new JPanel();
	JPanel 		totalPanel 		= new JPanel();
	JPanel 		errorPanel 		= new JPanel();
	JPanel 		enterPanel 		= new JPanel();
	JPanel 		middleLeftPanel	= new JPanel();
	JPanel 		correctPanel 	= new JPanel();
	JPanel 		xInputPanel		= new JPanel();
	JScrollPane logTextScroll 	= new JScrollPane(logTextArea);
	GridLayout 	oneByThree 		= new GridLayout(1,3); // rows, cols
	GridLayout 	oneByTwo 		= new GridLayout(1,2);
	GridLayout 	twoByOne 		= new GridLayout(2,1);
	GridLayout oneByOne			= new GridLayout(1,1);
	GroupLayout gLayoutTotal 	= new GroupLayout(upperTopPanel);
	GroupLayout gLayoutEnter	= new GroupLayout(enterPanel);
	GroupLayout gLayoutCorrect  = new GroupLayout(correctPanel);
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
	    totalPanel.add(resultLabel);  
	    totalPanel.add(resultTF);
	    errorPanel.setLayout(new FlowLayout());
	    errorPanel.add(errorLabel);
	    errorPanel.add(errorTF);

		
		middleLeftPanel.setLayout(oneByTwo);
		middleLeftPanel.add(enterPanel);
		middleLeftPanel.add(correctPanel);
		enterPanel.add(enterLabel);
		gLayoutEnter.setVerticalGroup(
				   gLayoutEnter.createSequentialGroup()
				      .addGroup(gLayoutEnter.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(enterLabel)
				           .addComponent(xInputPanel)
				      )
				);
		gLayoutCorrect.setVerticalGroup(
				   gLayoutCorrect.createParallelGroup(GroupLayout.Alignment.CENTER)
				      .addGroup(gLayoutCorrect.createSequentialGroup()
				           .addComponent(totalCorrectLabel)
				           .addComponent(totalCorrectTF)
				      )
				      .addGroup(gLayoutCorrect.createSequentialGroup()
					           .addComponent(totalWrongLabel)
					           .addComponent(totalWrongTF)
					      )
				);
		xInputPanel.setLayout(new FlowLayout());
		xInputPanel.add(xInputLabel);
		xInputPanel.add(xInputTF);
		xInputPanel.setVisible(false);
		correctPanel.setVisible(false);
		
		lowerTopPanel.setLayout(oneByTwo); //Add items to lowerTopPanel
		lowerTopPanel.add(middleLeftPanel);
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
		resultLabel.setFont(new Font("default", Font.BOLD, 20));
		resultTF.setFont(new Font("default", Font.PLAIN, 25));
		resultTF.setEditable(false);
		resultTF.setColumns(17);
		resultTF.setText("00.00");
		errorTF.setFont(new Font("default", Font.PLAIN, 25));
		errorTF.setEditable(false);
		errorTF.setColumns(17);
		totalCorrectTF.setText("0");
		totalCorrectTF.setHorizontalAlignment(JTextField.CENTER);
		totalWrongTF.setText("0");
		totalWrongTF.setHorizontalAlignment(JTextField.CENTER);
		errorLabel.setFont(new Font("default", Font.BOLD, 20));
		enterLabel.setFont(new Font("default", Font.PLAIN, 12));
		
		amountTF.setFont(new Font("default", Font.BOLD, 25));
	    logTextArea.setLineWrap(true);
	    logTextArea.setWrapStyleWord(true);
	    logTextArea.setText("Log text will go here.");
	    logTextArea.setFont(new Font("default", Font.PLAIN, 12));
	    totalCorrectTF.setEditable(false);
	    totalWrongTF.setEditable(false);

		// action listeners
	    clearButton.addActionListener(this);
        amountTF.addActionListener(this);
        itemAccumulator.addActionListener(this);
        itemExpression.addActionListener(this);
        itemTest.addActionListener(this);
        itemGraph.addActionListener(this);

		// Show the window
	    calcWindow.setSize(990, 550);
	    calcWindow.setMinimumSize(new Dimension(990,550));
	    calcWindow.setVisible(true); // show the graphics window
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == clearButton){
			resultTF.setText("00.00");
			errorTF.setText("");
			amountTF.setText("");
		}
		if (ae.getSource() == amountTF) {
			if(calcMode == "accumulator") parseAccumulatorInput();
			else if(calcMode == "expression") parseExpressionInput();
			else if(calcMode == "test") parseTestInput();
			else if(calcMode == "graph") parseGraphInput();
		}
		if (ae.getSource() == itemAccumulator) {
			calcMode = "accumulator";
			xInputPanel.setVisible(false);
			correctPanel.setVisible(false);
			calcWindow.setTitle("Calculator: Accumulator Mode");
			enterLabel.setText("<html><b>Accumulator Mode</b><br>Enter value to be added to sum: " +
								"<br> Enter only 2 decimal places." +
								"<br> You can enter a $ at the start if desired.</html>");
		}
		if (ae.getSource() == itemExpression) {
			calcMode = "expression";
			xInputPanel.setVisible(true);
			correctPanel.setVisible(false);
			calcWindow.setTitle("Calculator: Expression Mode");
			enterLabel.setText("<html><b>Expression Mode</b><br>ENTER INSTRUCTIONS HERE " +
								"<br> MORE INSTRUCTIONS" +
								"<br> MORE INSTRUCTIONS </html>");
		}
		if (ae.getSource() == itemTest){
			calcMode = "test";
			xInputPanel.setVisible(true);
			correctPanel.setVisible(true);
			calcWindow.setTitle("Calculator: Test Mode");
			enterLabel.setText("<html><b>Test Mode</b><br>ENTER INSTRUCTIONS HERE " +
								"<br> MORE INSTRUCTIONS" +
								"<br> MORE INSTRUCTIONS </html>");
		}
		if (ae.getSource() == itemGraph){
			calcMode = "graph";
			xInputPanel.setVisible(true);
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
    This method parses the input and does:
    1. calls function to set x value
    2. calls function to read in expression
    	in this mode everything after = is cut
    3. checks function for illegal characters like a or z
    4. checks function for negative unary operator and replaces with u
    5.
     */
	private void parseExpressionInput() {
		errorTF.setText(" "); // clear error each time calculate is pressed
		// clear text area if first time pressed
		if (logTextArea.getText().contains("Log text will go here"))
		{
			logTextArea.setText("");
		}

		// read input x value
		Double xValue = readXValue();
		if (xValue.equals(null)) return;
		System.out.println("X value is: " + xValue);

		// read input expression
		String expression = readExpression();
		String origExpression = expression; // keep input version for log printing purposes

		// NEEDS TO BE WRITTEN
		if (stringContainsIllegalCharacters(expression))
			errorTF.setText("Expression contains an illegal character");

		// replace unary operator (-) with (+u)
		// NEEDS TO BE WRITTEN
		expression = replaceUnary(expression);
		if (expression.equals(null)) return;

		// Add () to outside if they don't exist. This is required for the recursive call
		// the later functions parse everything based on the ()
		expression = addParentheses(expression);
		if (expression == null) return;

		// replace expression inside innermost (), call recursively
		// this will return the answer as a string
		expression = recursiveReduce(expression,xValue);
		if (expression.equals(null)) return;

		// print expression + answer
		resultTF.setText(expression.toString());
		logTextArea.append(origExpression + " = " + expression + " at x= " + xValue + newLine);
	}
	
	private void printAnswer(String expression, String origExpression, Double xValue){
		// print expression + answer
		resultTF.setText(expression.toString());
		logTextArea.append(origExpression + " = " + expression + " at x= " + xValue + newLine);
	}
	/*
	 * Test mode:
    This method parses works exactly the same as the expression input BUT it checks the input expression for
    an equals sign and runs everything twice for both sides
     */
	private void parseTestInput() {
		errorTF.setText(" "); // clear error each time calculate is pressed
		// clear text area if first time pressed
		if (logTextArea.getText().contains("Log text will go here"))
		{
			logTextArea.setText("");
		}

		// read input x value
		Double xValue = readXValue();
		if (xValue.equals(null)) return;
		System.out.println("X value is: " + xValue);

		// read input expression
		String[] expression = readTestExpression();
		String leftExpression = expression[0];
		String rightExpression = expression[1];
		String origExpression = leftExpression+" = "+rightExpression; // keep input version for log printing purposes
		
		// NEEDS TO BE WRITTEN
		if (stringContainsIllegalCharacters(leftExpression))
			errorTF.setText("Left expression contains an illegal character");
		if (stringContainsIllegalCharacters(rightExpression))
			errorTF.setText("Right expression contains an illegal character");
		
		// replace unary operator (-) with (+u)
		// NEEDS TO BE WRITTEN
		leftExpression = replaceUnary(leftExpression);
		if (leftExpression.equals(null)) return;
		rightExpression = replaceUnary(rightExpression);
		if (rightExpression.equals(null)) return;

		// Add () to outside if they don't exist. This is required for the recursive call
		// the later functions parse everything based on the ()
		leftExpression = addParentheses(leftExpression);
		if (leftExpression == null) return;
		rightExpression = addParentheses(rightExpression);
		if (rightExpression == null) return;

		// replace expression inside innermost (), call recursively
		// this will return the answer as a string
		leftExpression = recursiveReduce(leftExpression,xValue);
		if (leftExpression.equals(null)) return;
		rightExpression = recursiveReduce(rightExpression,xValue);
		if (rightExpression.equals(null)) return;

		if (Double.parseDouble(leftExpression) == Double.parseDouble(rightExpression)){
			// print expression + answer
			resultTF.setText("Correct!");
			logTextArea.append(origExpression + " is correct at x= " + xValue + newLine);
		}
		else{
			// print expression + answer
			resultTF.setText("Oops!");
			logTextArea.append(leftExpression + "     " + rightExpression + newLine);
			logTextArea.append(origExpression + " is incorrect at x= " + xValue + newLine);
		}
		
		
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
        resultTF.setText(totalString);
        logTextArea.append(previousTotalString + " + " + inputString + " = " + totalString + newLine);
        amountTF.setText(" ");
    }

	private Double readXValue()
	{
		String x = xInputTF.getText().trim();
		Double xValue;
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
			} catch (NumberFormatException nfe) {
				errorTF.setText("Please set x to be a number");
				return null;
			}
		}
		return xValue;
	}

	/*
	This function reads in the input expression from field and parses it
	 */
	private String readExpression()
	{
		String expression = amountTF.getText();
		if (expression.length() == 0) errorTF.setText("Input an expression");

		// set expression to known values
		if (expression.contains("=")) {
			// delete everything after =
			expression = expression.split("=")[0];
			errorTF.setText("everything after = removed");
		}
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

		return expression;
	}
	
	/*
	This function reads in the input expression from field and parses it
	 */
	private String[] readTestExpression()
	{
		String expression = amountTF.getText();
		String[] expressionArray = {null,null};

		if (expression.length() == 0) {
			errorTF.setText("Input an expression");
			return expressionArray;
		}

		// set expression to known values
		if (expression.contains("=")) {
			// get left and right side of expression
			//leftExpression = expression.split("=")[0];
			//rightExpression = expression.split("=")[1];
			
			// set expression to known values
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
			expressionArray = expression.split("=");
		}
		else{
			errorTF.setText("Improper format: Does not contain \"=\".");
		}
		

		return expressionArray;
	}

	/*
	This function can take two operands and an operator and return their value
	 */
	private Double evaluateSimpleExpression(String leftString, String operator, String rightString, Double xValue)
	{
		Double left;
		Double right;

		if(leftString.contains("ux")) left = -xValue;
		else if(leftString.contains("x")) left = xValue;
		else if(leftString.contains("e")) left = E;
		else if(leftString.contains("pi")) left = PI;
		else if(leftString.contains("u")) {
			leftString = leftString.replace("u","-"); // unary indicator for negative number
			left = Double.parseDouble(leftString);
		}
		else left = Double.parseDouble(leftString);

		if(rightString.contains("ux")) right = -xValue;
		else if(rightString.contains("x")) right = xValue;
		else if(rightString.contains("e")) right = E;
		else if(rightString.contains("pi")) right = PI;
		else if(rightString.contains("u")) {
			rightString = rightString.replace("u","-"); // unary indicator for negative number
			right = Double.parseDouble(rightString);
		}
		else right = Double.parseDouble(rightString);

		switch (operator)
		{
			case "+": return left + right;
			case "-": return left - right;
			case "*": return left*right;
			case "/":
				if (right.equals(0.0))
					return null;
				return left/right;
			case "^": return pow(left,right);
			case "r": return pow(left, (1.0/right)); // evaluates roots
			default: {
				errorTF.setText("please only include: + - * / ^ r");
				return 0.0;
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
		if(expression.startsWith(")")) {
			errorTF.setText("error in parentheses");
			return null;
		}

		if(expression.endsWith("(")) {
			errorTF.setText("error in parentheses");
			return null;
		}

		// CHECK IF THERE IS IMPLICIT MULITIPLICATION


		expression = "("+expression;
		expression = expression+")";
		return expression;
	}

	private String recursiveReduce(String expression, Double xValue)
	{
		System.out.println("Expression is: " + expression);
		int openCount = expression.length() - expression.replace("(","").length();
		int closeCount = expression.length() - expression.replace(")","").length();

		if (openCount != closeCount) {
			System.out.println("Please match parentheses");
			errorTF.setText("Please match parentheses");
			return expression;
		}

		// finds last () set in group
		int lastOpen = expression.lastIndexOf("(");
		//System.out.println(lastOpen);
		int matchingClose = expression.indexOf(")", lastOpen);
		//System.out.println(matchingClose);

		// substring of last () set
		String subString = expression.substring(lastOpen, matchingClose + 1);
		System.out.println("Program will evaluate this substring: " + subString);
		String newString = evalExpression(subString, xValue);
		if(newString.equals(null)) return null;
		System.out.println("returned string after evaluation" + newString);

		expression = expression.replace(subString,newString);
		expression = replaceUnary(expression); // need to fix new string if unary characters are present
		System.out.println("Expression after replacement " + expression);

		// while there are still ( present, keep calling the recursive reduce
		if (openCount > 1)
		{
			expression = recursiveReduce(expression,xValue);
		}

		return expression;

	}

	// Input an expression between parentheses and an x value,
	// solve expression using order of operations and return a string value
	private String evalExpression(String expression, Double xValue)
	{
		if (expression.startsWith("("))
			expression = expression.replace("(","");
		if (expression.endsWith(")"))
			expression = expression.replace(")","");
		System.out.println("eval expression: " + expression + " with give x value = " + xValue);

		// split input expression inside () into an array of operators and operands
		// this will look like xr2 = [x,2] and [r]
		java.util.List<String> operatorList = new ArrayList<String>();
		java.util.List<String> operandList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(expression, "+-*/^r", true);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if ("+-/*^r".contains(token)) {
				operatorList.add(token);
			} else {
				operandList.add(token);
			}
		}

		System.out.println("Operators:" + operatorList); // something like [r,*,^]
		System.out.println("Operands:" + operandList); // something like [x,1,5,e]

		// checks that the number of operators is correct
		if (operatorList.size() >= operandList.size())
		{
			errorTF.setText("Too many operators"); // this will be messed up if the unary operation is not correct
			return null;
		}

		// this does the order of operations using the two lists
		while (operatorList.size() > 0) {
			for (int i = 0; i < operatorList.size(); i++) {
				if ((operatorList.get(i).equals("r")) || (operatorList.get(i).equals("^"))) {
					System.out.println("found: " + operatorList.get(i));
					operandList.set(i, Double.toString(evaluateSimpleExpression(operandList.get(i), operatorList.get(i), operandList.get(i + 1), xValue)));
					operatorList.remove(i);
					operandList.remove(i + 1); // shorten list by 1
					i--; // decrement
					System.out.println("operators list after r is found: " + operatorList);
					System.out.println("Operand list after r is found: " + operandList);
				}
			}
			if ((!operatorList.contains("r")) && !operatorList.contains("^")) {
				for (int i = 0; i < operatorList.size(); i++) {
					if ((operatorList.get(i).equals("*")) || (operatorList.get(i).equals("/"))) {
						System.out.println("found * or /");
						// this does not work
						if (Double.toString(evaluateSimpleExpression(operandList.get(i), operatorList.get(i), operandList.get(i + 1), xValue)).equals(null))
							System.out.println("divide by 0");
						// does not properly check for null return
						operandList.set(i, Double.toString(evaluateSimpleExpression(operandList.get(i), operatorList.get(i), operandList.get(i + 1), xValue)));
						operatorList.remove(i);
						operandList.remove(i + 1); // shorten list by 1
						i--;
						System.out.println("operators list after r is found: " + operatorList);
						System.out.println("Operand list after r is found: " + operandList);
					}
				}
			}
			if ((!operatorList.contains("*")) && !operatorList.contains("/")) {
				for (int i = 0; i < operatorList.size(); i++) {
					if ((operatorList.get(i).equals("+")) || (operatorList.get(i).equals("-"))) {
						operandList.set(i, Double.toString(evaluateSimpleExpression(operandList.get(i), operatorList.get(i), operandList.get(i + 1), xValue)));
						operatorList.remove(i);
						operandList.remove(i + 1); // shorten list by 1
						i--;
						System.out.println("operators list after r is found: " + operatorList);
						System.out.println("Operand list after r is found: " + operandList);
					}
				}
			}
		}
		return operandList.get(0); // returns final value equivalent to what was inside the ()
	}

}
