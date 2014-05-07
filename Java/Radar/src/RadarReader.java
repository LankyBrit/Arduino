import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.BorderLayout;
import java.io.InputStream;

import javax.swing.JFrame;

public class RadarReader
{
	public static final int MIN_DISTANCE = 20;
	public static final int MAX_DISTANCE = 12000;
	
	protected int currentDistances[] = new int[180];
	protected int previousDistances[] = new int[180];
	protected int firstDerivativeDistances[] = new int[180];
	protected int lastGoodDistance = 0;
	protected int lastAngle = 0;
	
    public RadarReader()
    {
        super();
    }
    
    protected void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(19200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                
                new Thread(new SerialReader(in)).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    private class SerialReader implements Runnable 
    {
    	private StringBuffer buffer = new StringBuffer();
        private InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
        	int value = 0;
        	
        	do
        	{
        		try
        		{
        			value = in.read();
        		}
        		catch (Exception e)
        		{
        			value = -1;
        		}
        		
        		if (value>0)
        		{
        			buffer.append((char) value);
        		}
        		else if (value==0)
        		{
        			parseString(buffer.toString());
        			buffer.setLength(0);
        		}
        	} while(true);
        }
        	
        private synchronized void parseString(String s)
        {
        	String parse = s.trim();
        	
        	String values[] = parse.split(",");
        	if (values.length==2)
        	{
        		int angle;
        		int distance;
        		
        		try
        		{
        			angle = Integer.parseInt(values[0]);
        			RadarReader.this.lastAngle = angle;
        		}
        		catch (Exception e)
        		{
        			angle = -1;
        		}
        		
        		try
        		{
        			distance = Integer.parseInt(values[1]);
        		}
        		catch (Exception e)
        		{
        			distance = -1;
        		}
        		
        		if (angle>=0 && distance>=0)
        		{
        			
	        		if (distance>MAX_DISTANCE)
	        		{
	        			System.out.printf("Distance is out of bounds, at %dmm\n", distance);
	        			distance = RadarReader.this.lastGoodDistance;
	        		}
	
	        		if (angle<RadarReader.this.currentDistances.length)
	        		{
	        			RadarReader.this.previousDistances[angle] = RadarReader.this.currentDistances[angle];
	            		RadarReader.this.currentDistances[angle] = distance;
	            		RadarReader.this.calculateFirstDerivatives(angle);
	            		
	            		RadarReader.this.lastGoodDistance = distance;
	        		}
	        	}
        	}
        }
    }

    public int[] getDistanceData()
    {
    	return RadarReader.this.currentDistances;
    }
    
    public int[] getDerivativeData()
    {
    	return RadarReader.this.firstDerivativeDistances;
    }
    
    private void calculateFirstDerivatives(int angle)
    {
		RadarReader.this.firstDerivativeDistances[angle] = Math.abs(RadarReader.this.currentDistances[angle] - RadarReader.this.previousDistances[angle]);
    }
    
    public int getLastAngle()
    {
    	return this.lastAngle;
    }
    
    public static void main ( String[] args )
    {
        try
        {
            RadarReader reader = new RadarReader();
            
            JFrame frame = new JFrame("Radar Reader");
            RadarDisplay display = new RadarDisplay(reader);
            RadarToggle  toggle = new RadarToggle(display);
            RadarScale   scale = new RadarScale(display);
            display.bindScale(scale);
            
            frame.setSize(800,600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            frame.setLayout(new BorderLayout());
            frame.add(display,BorderLayout.CENTER);
            frame.add(toggle, BorderLayout.SOUTH);
            frame.add(scale, BorderLayout.WEST);
            frame.setVisible(true);
            
            reader.connect("COM7");
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}