/*
 * Java Terrain and Stellar System Ports
 *
 * Copyright (C) 2006 Martin H. Smith based on work by original
 * authors.
 *
 * Released under the terms of the GNU General Public License
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * Linking TerraJ statically or dynamically with other modules is making a
 * combined work based on TerraJ. Thus, the terms and conditions of the
 * GNU General Public License cover the whole combination.
 *
 * In addition, as a special exception, the copyright holders of TerraJ
 * give you permission to combine this program with free software programs
 * or libraries that are released under the GNU LGPL and with code included
 * in the standard release of JOGL, Java Getopt and FreeMarker under the BSD
 * license (or modified versions of such code, with unchanged license) and with
 * Apache Commons and Log4J libraries under the Apache license (or modified versions
 * of such code. You may copy and distribute such a system following the terms
 * of the GNU GPL for TerraJ and the licenses of the other code concerned,
 * provided that you include the source code of that other code when and as the
 * GNU GPL requires distribution of source code.
 *
 * Note that people who make modified versions of TerraJ are not obligated to grant
 * this special exception for their modified versions; it is their choice whether
 * to do so. The GNU General Public License gives permission to release a modified
 * version without this exception; this exception also makes it possible to release
 * a modified version which carries forward this exception.
 */

/*
 * UIUtils.java
 *
 * Created on 19 April 2006, 21:21
 */

package com.alvermont.terraj.stargen.ui;

import com.alvermont.terraj.stargen.Constants;
import com.alvermont.terraj.stargen.Enviro;
import com.alvermont.terraj.stargen.Planet;
import com.alvermont.terraj.stargen.PlanetType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Some utility functions useful to the UI
 *
 * @author  martin
 * @version $Id: UIUtils.java,v 1.7 2006/07/06 06:58:35 martin Exp $
 */
public class UIUtils
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(UIUtils.class);
    
    private static Enviro enviro = new Enviro();
    
    /** Creates a new instance of UIUtils */
    public UIUtils()
    {
    }
    
    /**
     * Get the size in pixels to be used for displaying a planet icon. This
     * will be derived from its actual size. 
     *
     * @param planet The planet to get the icon size for
     * @return The size in pixels to be used for the planet. This assumes the
     * icons will be square
     */
    public static int getSizeInPixels(Planet planet)
    {
        int pixels = ((int)( Math.sqrt(planet.getRadius() / 
                Constants.KM_EARTH_RADIUS) * 30.0)) + 1;
        
        if (planet.getType() == PlanetType.ASTEROIDS)
        {
            pixels = (int)(25.0 + ( 5.0 * Math.log((planet.getMass() *
                    Constants.SUN_MASS_IN_EARTH_MASSES) /
                    Constants.ASTEROID_MASS_LIMIT)));
        }
        
        pixels = Math.max(pixels, 3);
        
        return pixels;
    }
    
    /**
     * Get an information string about the planet. This will include its type,
     * mass and orbital zone where appropriate.
     * 
     * @param planet The planet to get the information for
     * @return A short descriptive string about the planet suitable for use
     * as a tooltip or alt text in a web page
     */
    public static String getInfo(Planet planet)
    {
        if ((planet.getType() == PlanetType.GASGIANT)
                || (planet.getType() == PlanetType.SUBGASGIANT)
                || (planet.getType() == PlanetType.SUBSUBGASGIANT))
        {
            double estTemp = enviro.getEstTemp(planet.getPrimary().getREcosphere(),
                                planet.getOrbitZone(), planet.getAlbedo());
            
            return String.format("%s: %.1fEM (c. %.0f&#176;)",
                    planet.getType().getPrintText(), 
                    planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES,
                    estTemp);
        }
        else if (planet.getType() == PlanetType.UNKNOWN)
        {
            return String.format("%s: %.1fEM, %.2fEM from gas (%.1f%c) Zone = %d",
                    planet.getType().getPrintText(), 
                    planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES,
                    planet.getGasMass() * Constants.SUN_MASS_IN_EARTH_MASSES,
                    100.0 * (planet.getGasMass() / planet.getMass()), '%',
                    planet.getOrbitZone());
        }
        else
        {
            return String.format("%s: %.2fEM, %.2fg, %.1f&#176; Zone = %d",
                    planet.getType().getPrintText(), 
                    planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES, 
                    planet.getSurfaceGravity(),
                    (planet.getSurfaceTemperature() - Constants.FREEZING_POINT_OF_WATER),
                    planet.getOrbitZone());
        }
    }
    
    /**
     * Get an input stream to an image for a planet via a resource 
     *
     * @param imageName The name of the planetary resource to be located
     * @return An input stream to the resource data or null if it was not
     * found
     */
    public static InputStream getImageResourceStream(String imageName)
    {        
        InputStream stream = UIUtils.class.getResourceAsStream("/com/alvermont/terraj/stargen/images/" +
                imageName + "Planet.png");
        
        if (stream == null)
        {
            stream = UIUtils.class.getResourceAsStream("/com/alvermont/terraj/stargen/images/" +
                    imageName + ".png");            
        }
        
        return stream;
    }
    
    /**
     * Get the name of a planetary image to be used in HTML format output 
     *
     * @param planet The planet to get the image name for
     * @return A string suitable for use in HTML as the target of an img src
     */
    public static String getHtmlImageName(Planet planet)
    {
        String name = planet.getType().getPrintText();

        return name + "Planet.png";
    }
    
    /**
     * Get an image by name using the resource mechanism
     * 
     * @param imageName The name of the image to be obtained
     * @throws java.io.IOException If there is an error reading the image
     * @return A <code>BufferedImage</code> containing the image
     */
    public static BufferedImage getImage(String imageName) throws IOException
    {
        return ImageIO.read(getImageResourceStream(imageName));
    }
    
    /**
     * Scale an image to a particular X and Y size in pixels
     * 
     * @param image The image to be scaled
     * @param pixelsX The desired horizontal size in pixels
     * @param pixelsY The desired vertical size in pixels
     * @return A new image scaled to the requested dimensions
     */
    public static BufferedImage scaleImage(BufferedImage image, int pixelsX, int pixelsY)
    {
        int actualWidth = image.getWidth() ;
        int actualHeight = image.getHeight() ;
        
        // work out the scale factor
        
        double sx = (float)(pixelsX) / actualWidth ;
        double sy = (float)(pixelsY) / actualHeight ;
        
        BufferedImage nbi = new BufferedImage( pixelsX , pixelsY , 
                image.getType()) ;
        
        Graphics2D g2 = nbi.createGraphics() ;
        AffineTransform at = AffineTransform.getScaleInstance( sx , sy ) ;

        g2.setTransform(at) ;
        
        g2.drawImage(image , 0 , 0 , null);
        
        return nbi;
    }
    
    /**
     * Get a list of images to be used for a list of planets 
     *
     * @param planets The list of planets to get images for
     * @throws java.io.IOException If there is an error building the images
     * @return A list containing an image for each planet in the same order
     * as the input list
     */
    public static List<BufferedImage> getPlanetImages(List<Planet> planets)
        throws IOException
    {
        List<BufferedImage> images = new ArrayList<BufferedImage>();
        
        for (Planet p : planets)
        {
            try
            {
                String name = p.getType().getPrintText(p.getType());
                
                BufferedImage bi = UIUtils.getImage(name);
                
                bi = UIUtils.scaleImage(bi, UIUtils.getSizeInPixels(p),
                                        UIUtils.getSizeInPixels(p));
                
                images.add(bi);
            }
            catch (IOException ex)
            {
                log.error("Error loading icon: " + ex);
                
                throw ex;
            }
        }
        
        return images;
    }
    
    /**
     * Build a list of label objects from a list of planets 
     *
     * @param planets The list of planets to build labels for
     * @throws java.io.IOException If there is an error building the labels
     * @return A list of <code>JLabel</code> objects in the same order as the
     * input list
     */
    public static List<JLabel> buildImages(List<Planet> planets) throws IOException
    {
        List<JLabel> labels = new ArrayList<JLabel>();
        
        List<BufferedImage> images = getPlanetImages(planets);
        
        Planet p = planets.get(0);
        
        for (BufferedImage bi : images)
        {
            ImageIcon icon = new ImageIcon(bi);

            JLabel label = new JLabel(icon);
            label.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
            label.setMinimumSize(new Dimension(bi.getWidth(), bi.getHeight()));

            label.setToolTipText("<html>" + UIUtils.getInfo(p) + "</html>");

            labels.add(label);             

            log.debug("Added icon for planet " + p.getNumber() + " size " +
                        bi.getWidth() + "," + bi.getHeight());

            p = p.getNextPlanet();
        }
        
        return labels;
    }
    
    /**
     * Get a JLabel object to display with star details
     * 
     * @throws java.io.IOException If there is an error building the list
     * @return A <code>JLabel</code> representing the star
     */
    public static JLabel getSunLabel() throws IOException
    {
        BufferedImage bi = UIUtils.getImage("Sun");

        bi = UIUtils.scaleImage(bi, 30, 120);

        ImageIcon icon = new ImageIcon(bi);

        JLabel label = new JLabel(icon);
        label.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
        label.setMinimumSize(new Dimension(bi.getWidth(), bi.getHeight()));

        label.setToolTipText("The Star");
        
        return label;
    }
    
    /**
     * Combine a list of images horizontally into one new image.
     * 
     * @param images The list of images to be combined
     * @return A new image, as wide horizontally as the sum of the input images,
     * containing all the input images
     */
    public static BufferedImage combineImagesHorizontal(List<BufferedImage> images)
    {
        int imageType = -1;
        int height = 0;
        int width = 0;
        
        // first work out the sizing and image type, we assume the images
        // are all compatible.
        
        for (BufferedImage image : images)
        {
            if (imageType == -1)
            {
                imageType = image.getType();
            }
            
            width += image.getWidth();
            
            height = Math.max(height, image.getHeight());
        }
        
        // create the new image and clear it to black
        
        BufferedImage bi = new BufferedImage(width, height, imageType);
        
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        
        // merge the images into the new one
        
        int xpos = 0;
        
        for (BufferedImage image : images)
        {
            int ypos = (height - image.getHeight()) / 2;
            
            g.drawImage(image, xpos, ypos, null);
            
            xpos += image.getWidth();
        }
        
        return bi;
    }
}
