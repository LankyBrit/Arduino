import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;


public class RadarDisplay
	extends JPanel
	implements ActionListener
{
	private static final int SENSOR_CONE_WIDTH = 15;

	
	private RadarReader reader = null;
	private boolean     displayRadarData = true;
	private boolean     displayDerivativeData = true;
	private boolean     displaySensorCone = true;
	private boolean     autoScale = true;
	private int         maxScaleValue = 0;
	
	private RadarScale  scale = null;
	
	public RadarDisplay(RadarReader reader)
	{
		super();
		this.reader = reader;
        
        Timer t = new Timer(1000/60,this);
        t.start();
	}
	
	@Override
	public void paint(Graphics g)
	{		
		super.paint(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		if (autoScale)
		{
			this.maxScaleValue = maxDistance(reader.getDistanceData());
			this.scale.setValue(this.maxScaleValue);
		}
		else
		{
			this.maxScaleValue = this.scale.getValue();
		}
		
		int maxDerivativeDistance = maxDistance(reader.getDerivativeData());

		//
		// Draw the sensor cone
		//
		
		if (this.displaySensorCone)
		{
			g.setColor(Color.LIGHT_GRAY);
			int lastAngle = reader.getLastAngle();
			int halfAngle = SENSOR_CONE_WIDTH / 2;
			Point lastPoint = null;
			
			for (int i=lastAngle-halfAngle;i<lastAngle+halfAngle;i++)
			{
	
				if (lastPoint==null)
				{
					lastPoint = calculatePoint(i,this.maxScaleValue,this.maxScaleValue);
				}
				else
				{
					Point p = calculatePoint(i,this.maxScaleValue,this.maxScaleValue);
					
					Polygon poly = new Polygon();
					poly.addPoint(this.getWidth()/2, 5);
					poly.addPoint(lastPoint.x,lastPoint.y);
					poly.addPoint(p.x,p.y);
					g.fillPolygon(poly);
					lastPoint = p;
				}
			}
		}

		//
		// Draw the raw distance data
		//

		if (this.displayRadarData)
		{
			this.drawScaleArcs(g, this.maxScaleValue);

			g.setColor(Color.BLACK);
	
			int[] data = reader.getDistanceData();
			
			Point lastPoint = null;
			int dataValue = 0;
			
			for (int i=0;i<data.length;i++)
			{
				dataValue = data[i];
				
				if (lastPoint==null)
				{
					lastPoint = calculatePoint(i,dataValue,this.maxScaleValue);
				}
				else
				{
					Point p = calculatePoint(i,dataValue,this.maxScaleValue);
					g.drawLine(lastPoint.x,lastPoint.y,p.x,p.y);
					lastPoint = p;
				}
			}
		}
		
		//
		// Draw first derivative data
		//

		if (this.displayDerivativeData)
		{
			g.setColor(Color.RED);
			
			int[] data = reader.getDerivativeData();
			
			Point lastPoint = null;
			int dataValue = 0;
			
			for (int i=0;i<data.length;i++)
			{
				dataValue = data[i];
				
				if (lastPoint==null)
				{
					lastPoint = calculatePoint(i,dataValue,maxDerivativeDistance);
				}
				else
				{
					Point p = calculatePoint(i,dataValue,maxDerivativeDistance);
					g.drawLine(lastPoint.x,lastPoint.y,p.x,p.y);
					lastPoint = p;
				}
			}
		}

		//
		// Draw scale
		//
		
		if (this.displayRadarData)
		{
			g.setColor(Color.BLACK);
	
			g.drawString("" + this.maxScaleValue + "mm", 17, this.getHeight()-15);
			
			g.drawLine(10, 5, 10, this.getHeight()-5);
			
			if (this.maxScaleValue>0)
			{
				int multiplier = (int) (((double) (this.getHeight()-10) / (double) this.maxScaleValue) * 100);
				for (int i=0; i<this.maxScaleValue/100; i++)
				{
					int y = i*multiplier;
					
					g.drawLine(10,5+y,15,5+y);
					
					if (i%10==0)
					{
						g.drawString("" + ((int) (i/10)) + "m", 20, 10+y);
					}
					
				}
			}
		}

		//
		// Draw derivative scale
		//
		
		if (this.displayDerivativeData)
		{
			g.setColor(Color.RED);
	
			g.drawString("" + maxDerivativeDistance, this.getWidth() - 60, this.getHeight()-15);
			
			g.drawLine(this.getWidth() - 10, 5, this.getWidth() - 10, this.getHeight()-5);
			
			if (maxDerivativeDistance>0)
			{
				int multiplier = (int) (((double) (this.getHeight()-10) / (double) maxDerivativeDistance) * 100);
				for (int i=0; i<maxDerivativeDistance/100; i++)
				{
					int y = i*multiplier;
					
					g.drawLine(this.getWidth() - 15,5+y,this.getWidth() - 10,5+y);
					
					if (i%5==0) 
					{
						g.drawString("" + i*100, this.getWidth() - 60, 10+y);
					}
					
				}
			}
		}
	}
	
	private void drawScaleArcs(Graphics g, int maxValue)
	{
		g.setColor(Color.BLUE);
		
		int division = (maxValue>=1000) ? 1000 : 100;
		
		int maxArc = maxValue - (maxValue%division);
		
		for (int distance=division; distance<=maxArc; distance+=division)
		{
			Point lastPoint = null;
			
			for (int i=0;i<180;i++)
			{	
				if (lastPoint==null)
				{
					Point p = calculatePoint(i,distance,maxValue);
					if (p.x>30 && p.x<this.getWidth()-30) lastPoint = p;
				}
				else
				{
					Point p = calculatePoint(i, distance, maxValue);
					
					if (p.x>30 && p.x<this.getWidth()-30)
					{
						g.drawLine(lastPoint.x,lastPoint.y,p.x,p.y);
						lastPoint = p;
					}
					else
					{
						lastPoint = null;
					}
				}
			}
		}

	}
	
	private static int lastGoodData(int[] data, int index)
	{
		for (int i=index;i>0;i--)
		{
			if (data[i]>0) return data[i];
		}
		
		return 0;
	}
	
	private static int maxDistance(int[] data)
	{
		int max = 0;
		for (int i=0;i<data.length;i++)
		{
			max = Math.max(max,data[i]);
		}
		
		return max;
	}
	
	private Point calculatePoint(int angle, int distance, int maxDistance)
	{
		int width = this.getWidth() - 10;
		int height = this.getHeight() - 10;
		int d;
		
		if (width>height)
		{
			d = height;
		}
		else
		{
			d = width / 2;
		}
		
		int midpoint = 5 + (width/2);
		
		double theta = Math.PI * ((180-angle)/180d);
		double hyp = d * ((double) distance/(double) maxDistance);
		int x = midpoint - (int) (hyp * Math.cos(theta));
		int y = 5 + (int) (hyp * Math.sin(theta));

		return new Point(x,y);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.repaint();
	}
	
	public void setDisplayRadarData(boolean flag)
	{
		this.displayRadarData = flag;
		this.repaint();
	}

	public boolean isRadarDataDisplayed()
	{
		return this.displayRadarData;
	}
	
	public void setDisplayDerivativeData(boolean flag)
	{
		this.displayDerivativeData = flag;
		this.repaint();
	}

	public boolean isDerivativeDataDisplayed()
	{
		return this.displayDerivativeData;
	}

	public void setDisplayScanCone(boolean flag)
	{
		this.displaySensorCone = flag;
		this.repaint();
	}

	public boolean isScanConeDisplayed()
	{
		return this.displaySensorCone;
	}
	
	public void bindScale(RadarScale scale)
	{
		this.scale = scale;
		this.setAutoScale(this.autoScale);
	}
	
	public boolean isAutoScale()
	{
		return this.autoScale;
	}
	
	public void setAutoScale(boolean flag)
	{
		this.autoScale = flag;
		this.scale.setEnabled(!this.autoScale);
	}
}
