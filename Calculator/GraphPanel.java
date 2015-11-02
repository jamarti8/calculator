/*
ECE 309 Lab 11 by Team 18
Kevin Keller
Francesco Palermo
Justin Martin
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
//import com.sun.xml.internal.bind.v2.model.util.ArrayInfoUtil;

/**
 * Created by kevin on 29.10.15.
 */

public class GraphPanel extends JPanel implements MouseListener
{
	double[] xValuesArray;
	double[] yValuesArray;
	double[] yScaleValuesArray;
	Calculator calcProgram;
	String expressionString;
	double[] yAxisValues;
	double xPixelsToValueConversionFactor = 1;
	double yPixelsToValueConversionFactor = 1;
	double yRange;
	double xRange;
	int padding = 75;
	double yMin;
    double yMax;
	
	
	JFrame miniXYdisplayWindow = new JFrame();
	JLabel   xTextField        = new JLabel();
	JLabel   yTextField	       = new JLabel();
	
	
	
    public GraphPanel (String     expression, // CONSTRUCTOR
                       double[]   xValues,
                       double[]   yValues,
                       Calculator calculatorProgram)
            throws IllegalArgumentException
    {
        // To-dos for this constructor method:
        // 1 Verify arrays are same size
        System.out.println("X values to plot: " + Arrays.toString(xValues));
        // this should be impossible to have, but...
        if (xValues.length != yValues.length) return;

        // 2 Verify x increment is positive
        if ((xValues[1] - xValues[0]) < 0) {
            System.out.println("received x array has negative x increment value");
            return;
        }
        // 3 Save Calculator address for call back
        calcProgram = calculatorProgram;
        // 4 Save expression for call back
        expressionString = expression;
        System.out.println("expression string in constructor is: "+expressionString);
        // 5 Register with the panel as MouseListener
        this.addMouseListener(this);
        // 6 Calculate Y scale values (and save them)
        	//Get min and max Y values
        yValuesArray = yValues;
        xValuesArray = xValues;
        yMin = yValues[0];
        yMax = yValues[0];
        for(int i=1; i<yValues.length; i++){
        	if(yValues[i] < yMin){
        		yMin = yValues[i];
        	}
        	if(yValues[i] > yMax){
        		yMax = yValues[i];
        	}
        }
        double xMin = xValues[0];
        double xMax = xValues[0];
        for(int i=1; i<xValues.length; i++){
        	if(xValues[i] < yMin){
        		xMin = xValues[i];
        	}
        	if(xValues[i] > xMax){
        		xMax = xValues[i];
        	}
        }
        yRange =  (yMax - yMin);
        xRange =  (xMax - yMin);
        double yValuePerDiv = yRange/10;
        double yAxisMin = (yMin - (yValuePerDiv/2));
        double yAxisMax = (Math.ceil(yMax) + (yValuePerDiv/2));
        yAxisValues = new double[12]; //Allocate for 12 values (12 y axis tic marks)
        yAxisValues[0] = yAxisMax;
        yAxisValues[11] = yAxisMin;
        for(int i=1; i<yValues.length; i++){
        	yAxisValues[i] = yValuePerDiv*(i+1);
        }
        //build miniXYdisplayWindow
        miniXYdisplayWindow.getContentPane().add(xTextField, "North");
        miniXYdisplayWindow.getContentPane().add(yTextField, "South");
        miniXYdisplayWindow.setSize(100, 100);
       
        
        
    }

    @Override
    public void paint(Graphics g) // overrides paint() in JPanel!
    {
        System.out.println("expression String in paint method: "+expressionString);
        
        // 1 Calculate x and y pixels-to-value conversion factors
        int windowWidth  = getWidth(); // call methods
        int windowHeight = getHeight();// in JPanel!
        //g.clearRect(0, 0, windowWidth, windowHeight);
        int originX = padding;
        int originY = windowHeight - padding;
        double minimumTickRange = yRange / 10;
        double magnitude = Math.pow(10, Math.floor(Math.log10(minimumTickRange)));
        double residual = minimumTickRange / magnitude;
        double tickRange;
        if(residual > 5){
        	tickRange = 10 * magnitude;
        }
        else if(residual > 2){
        	tickRange = 5 * magnitude;
        }
        else if(residual > 1){
        	tickRange = 2 * magnitude;
        }
        else{
        	tickRange = magnitude;
        }
        
    	yPixelsToValueConversionFactor = yRange / originY ;
    	xPixelsToValueConversionFactor = xRange / originX ;
        // 2 Do ALL drawing here in paint()
        
        
        g.drawLine(originX,		0,		originX,originY);		//Draw Y axis
        g.drawLine(originX, originY, windowWidth, originY);		//Draw X axis
        	// Draw tic marks on axes
        for(int i=1; i<12; i++){
        	g.drawLine(originX-2, originY-(originY*i/12), originX+2, originY-(originY*i/12));
        	g.drawString(Double.toString(tickRange*(i+yMin)), originX-60,originY-(originY*i/12) );
        	g.drawLine(originX+((windowWidth-originX)*i/12), originY-2, originX+((windowWidth-originX)*i/12), originY+2);
        	
        }
        //g.drawString(Double.toString(tickRange), 5,10 );
        
        //Convert Y values to pixel values for drawing
        	//Y axis max will equal 0
        	//Y axis min will equal height of window
        int[] yAxisPixelValues = new int[12]; //Allocate for 12 values (12 y axis tic marks)
        yAxisPixelValues[0] = 0;
        yAxisPixelValues[11] = windowHeight;
        
        g.fillOval(originX-2,originY-2,4,4);		// This draws a circle and fills it in
        											//   Subtract half of the diameter from the X and
        											//   Y of the point in order to center the circle
        											//   over the point that we want.
        
        for(int i=1; i<xValuesArray.length; i++){	// Use for loop to plot points
        	
        }
        
    }

    public void mousePressed(MouseEvent me) // show tiny x,y values window
    {
    	//xPixelsToValueConversionFactor = 1;
        // xTextField and yTextField are in the miniXYdisplayWindow
        int xInPixels = me.getX();
        int yInPixels = me.getY();
        if(xInPixels < padding || yInPixels > getHeight()-padding){
        	return;
        }
        double xValue = xInPixels * xPixelsToValueConversionFactor;
        String xValueString = String.valueOf(xValue);
        xTextField.setText("X = " + xValueString);

        // can use this to calculate a specific y value
        String yValueString = null;
        System.out.println("expression right before y value is found is: "+expressionString);
        //expressionString = calcProgram.readExpression();
		try {
			yValueString = calcProgram.valueFromExpression(expressionString,xValue);
            System.out.println("returned y value is: " + yValueString);
		} catch (Exception e) {
			e.printStackTrace();
		}
        yTextField.setText("Y = " + yValueString);

        // show mini x,y display window
        miniXYdisplayWindow.setLocation(me.getX(), me.getY());
        miniXYdisplayWindow.setVisible(true);
    }

    public void mouseReleased(MouseEvent me) // hide tiny window
    {
        // "erase" mini x,y display window
        miniXYdisplayWindow.setVisible(false);
        System.out.println("expression in mouse released: "+ expressionString);
    } 

    public void mouseClicked(MouseEvent me){} // take no action
    public void mouseEntered(MouseEvent me){} // on these
    public void mouseExited(MouseEvent  me){} // window events
}
