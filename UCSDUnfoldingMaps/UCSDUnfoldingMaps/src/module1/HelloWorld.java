package module1;

import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

import javax.xml.crypto.dsig.SignatureMethod;

/** HelloWorld
  * An application with two maps side-by-side zoomed in on different locations.
  * Author: UC San Diego Coursera Intermediate Programming team
  * @author Your name here
  * Date: July 17, 2015
  * */
public class HelloWorld extends PApplet
{
	/** Your goal: add code to display second map, zoom in, and customize the background.
	 * Feel free to copy and use this code, adding to it, modifying it, etc.  
	 * Don't forget the import lines above. */

	// You can ignore this.  It's to keep eclipse from reporting a warning
	private static final long serialVersionUID = 1L;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// IF YOU ARE WORKING OFFLINE: Change the value of this variable to true
	private static final boolean offline = false;
	
	/** The map we use to display our home town: La Jolla, CA */
	UnfoldingMap map1;
	
	/** The map you will use to display your home town */ 
	UnfoldingMap map2;

	Location fresno = new Location(36.7d, -119.9d);
	SimplePointMarker myMarket = new SimplePointMarker(fresno);
	public void setup() {
		size(800, 600, P2D);  // Set up the Applet window to be 800x600
		                      // The OPENGL argument indicates to use the 
		                      // Processing library's 2D drawing
		                      // You'll learn more about processing in Module 3

		// This sets the background color for the Applet.  
		// Play around with these numbers and see what happens!
		this.background(200, 200, 200);
		
		// Select a map provider

//		AbstractMapProvider provider = new Google.GoogleTerrainProvider();


		// Set a zoom level
		int zoomLevel = 10;
		
		if (offline) {
			// If you are working offline, you need to use this provider 
			// to work with the maps that are local on your computer.

			// im not using any provider because the number of requests are very limited after 24 hours we can again use these providers
//			provider = new Google.GoogleSimplifiedProvider();

			// 3 is the maximum zoom level for working offline
			zoomLevel = 3;
		}
		
		// Create a new UnfoldingMap to be displayed in this window.  
		// The 2nd-5th arguments give the map's x, y, width and height
		// When you create your map we want you to play around with these 
		// arguments to get your second map in the right place.
		// The 6th argument specifies the map provider.  
		// There are several providers built-in.
		// Note if you are working offline you must use the MBTilesMapProvider
		map1 = new UnfoldingMap(this, 50, 50, 350, 500);

		// The next line zooms in and centers the map at 
	    // 32.9 (latitude) and -117.2 (longitude)
	    map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));
		
		// This line makes the map interactive

		// TODO: Add code here that creates map2 
		// Then you'll modify draw() below

		map2 = new UnfoldingMap(this, 420, 50, 350, 500);
		map2.zoomAndPanTo(zoomLevel, fresno);
		map2.addMarker(myMarket);
		MapUtils.createDefaultEventDispatcher(this, map1, map2);

	}

	/** Draw the Applet window.  */
	public void draw() {
		// So far we only draw map1...
		// TODO: Add code so that both maps are displayed
		background(0);


		map1.draw();
		map2.draw();


		ScreenPosition fresnoPos = myMarket.getScreenPosition(map2);
		strokeWeight(16);
		stroke(67, 211, 227, 100);
		noFill();
		ellipse(fresnoPos.x, fresnoPos.y, 36, 36);
		fill(0);
		text("Fresno", fresnoPos.x - textWidth("Fresno")/2, fresnoPos.y + 4);

	}


	public static void main(String[] args) {
		PApplet.main(new String[] {"module1.HelloWorld"});
	}
	
}
