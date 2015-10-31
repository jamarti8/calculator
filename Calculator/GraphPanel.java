import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by kevin on 29.10.15.
 */

public class GraphPanel extends JPanel implements MouseListener
{
	JFrame      graphWindow      = new JFrame();
	
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
            System.out.println("recieved x array has negative x increment value");
            return;
        }
        // 3 Save Calculator address for call back

        // 4 Save expression for call back

        // 5 Register with the panel as MouseListener
        // 6 Calculate Y scale values (and save them)
        // 7 Build miniXYdisplayWindow (reuse for each mouse click!)
        
        
    }

    @Override
    public void paint(Graphics g) // overrides paint() in JPanel!
    {
        // 1 Calculate x and y pixels-to-value conversion factors
        // 2 Do ALL drawing here in paint()
        int windowWidth  = getWidth(); // call methods
        int windowHeight = getHeight();// in JPanel!

    }

    /*
    public void mousePressed(MouseEvent me) // show tiny x,y values window
    {
        // xTextField and yTextField are in the miniXYdisplayWindow
        int xInPixels = me.getX();
        double xValue = xInPixels * xPixelsToValueConversionFactor;
        String xValueString = String.valueOf(xValue);
        xTextField.setText("X = " + xValueString);

        // can use this to calculate a specific y value
        // NEED TO SAVE INSTANCE VARIABLE WITH CALCULATOR IN CONSTRUCTOR
        try {
            calculatorProgram.valueFromExpression(expression, xValues[0]);
        } catch (NullPointerException npe) {
            return;
        }

        String yValueString = calculator.(expression,xValueString);
        yTextField.setText("Y = " + yValueString);

        // show mini x,y display window
        miniXYdisplayWindow.setLocation(me.getX(), me.getY());
        miniXYdisplayWindow.setVisible(true);
    }

    public void mouseReleased(MouseEvent me) // hide tiny window
    {
        // "erase" mini x,y display window
        miniXYdisplayWindow.setVisible(false);
    } */

    public void mouseClicked(MouseEvent me){} // take no action
    public void mouseEntered(MouseEvent me){} // on these
    public void mouseExited(MouseEvent  me){} // window events
    public void mousePressed(MouseEvent me){}
    public void mouseReleased(MouseEvent me){}
}
