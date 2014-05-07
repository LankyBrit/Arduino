import javax.swing.JSlider;

public class RadarScale extends JSlider
{
	private RadarDisplay display;
	
	public RadarScale(RadarDisplay display)
	{
		super(JSlider.VERTICAL);
		this.setMinimum(RadarReader.MIN_DISTANCE);
		this.setMaximum(RadarReader.MAX_DISTANCE);
		this.display = display;
		this.setInverted(true);
	}
}
