import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;


public class RadarToggle extends JPanel implements ActionListener
{
	private RadarDisplay display;
	private JCheckBox scale = new JCheckBox("Auto scale");
	private JCheckBox cone = new JCheckBox("Sensor cone");
	private JCheckBox radar = new JCheckBox("Radar data");
	private JCheckBox derivative = new JCheckBox("Derivative data");
	
	public RadarToggle(RadarDisplay display)
	{
		super();
		
		this.display = display;
		
		this.setLayout(new FlowLayout());
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Display"));
		this.add(scale);
		this.add(cone);
		this.add(radar);
		this.add(derivative);
		
		scale.addActionListener(this);
		cone.addActionListener(this);
		radar.addActionListener(this);
		derivative.addActionListener(this);
		
		scale.setSelected(display.isAutoScale());
		cone.setSelected(display.isScanConeDisplayed());
		radar.setSelected(display.isRadarDataDisplayed());
		derivative.setSelected(display.isDerivativeDataDisplayed());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		display.setAutoScale(scale.isSelected());
		display.setDisplayScanCone(cone.isSelected());
		display.setDisplayRadarData(radar.isSelected());
		display.setDisplayDerivativeData(derivative.isSelected());
	}
}
