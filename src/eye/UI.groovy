package eye

import java.awt.Color
import java.awt.Robot
import java.awt.Rectangle
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import java.awt.MouseInfo
import java.awt.image.BufferedImage
import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL
import javax.swing.*
import java.util.concurrent.atomic.AtomicLong

public class UI {

	def label
	def frame
	/* thumbnail version
	Robot robot = new Robot()
	BufferedImage screenShot  =robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()))
	def icon = new ImageIcon(screenShot)
	*/
	Long cacheExpiry = 33 // milliseconds
	def lastUpdated = new AtomicLong(0)

	UI(){
		frame = new JFrame();


		new SwingBuilder().edt {
			frame = frame(title:'Frame',
				alwaysOnTop: true,
				undecorated: true,
				background: new Color(0,0,0,0),
				size:Toolkit.getDefaultToolkit().getScreenSize(),
				show: true,
				windowClosing:{ System.exit(0) }) {
			}
		}
		frame.setLayout(null)
		label = new JLabel()
		// version with a thumbnail of the region
		// label = new JLabel(icon)
		label.setOpaque(true)
		label.setBackground(new Color(0,1,0,0.2))
		label.setSize([50, 50])
		frame.getContentPane().add(label)

	}

	void notify(dataFrame){
		def point = dataFrame.raw
		if(lastUpdated.get() + cacheExpiry < System.currentTimeMillis()){
			lastUpdated.set(System.currentTimeMillis())

			if(dataFrame.fix){
				label.setBackground(new Color(1,0,0,0.2))
			}else{
				label.setBackground(new Color(0,1,0,0.2))
			}

			point.x = (point.x - 25) as Integer
			point.y = (point.y - 25) as Integer

			label.setLocation(point.x, point.y)

			/*
			version with the thumbnail of the region
			def screenShot = robot.createScreenCapture(new Rectangle(new Point(p.x, p.y), new Dimension(50, 50)))
			icon.setImage(screenShot)
			*/

			label.updateUI()
			frame.repaint()
		}
	}
}
