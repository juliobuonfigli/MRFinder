package ar.com.yahoojuliobuonfigli.imagej;

//la clase vectorizacion tendra como un parametro de entrada un vector boolean que es la mascara
//la mascara va a entregar tres mascaras para cada canal, cuando mascarasIndividuales 
//sea falso las tres mascaras seran iguales 
//vamos a hacer una mascara unica no convo no dispersion, no van stensel 

import ij.plugin.PlugIn;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class Mask {

	private String MaskThreshold, MaskOperator1, MaskOperator2;
	private double redValue, greenValue, blueValue;
	private ImagePlus red, green, blue, primaryMask;
	private boolean [] Red, Green, Blue, Mask, ThresholdsMask;
	private int h, w;
   // private boolean operate;
    
	public Mask(ImagePlus primaryMask, ImagePlus red, ImagePlus green, ImagePlus blue, String MaskThreshold, 
	            String MaskOperator1, String MaskOperator2, double redValue, double greenValue, 
	            double blueValue)
		{
		this.red=red;
		this.green=green;
		this.blue=blue;
		this.primaryMask=primaryMask;
		this.MaskThreshold=MaskThreshold;
		this.MaskOperator1=MaskOperator1;
		this.MaskOperator2=MaskOperator2;
		this.redValue=redValue;
		this.greenValue=greenValue;
		this.blueValue=blueValue;
		IJ.run(red, "8-bit", ""); 
		IJ.run(green, "8-bit", "");
		if(blue.equals(null)) {} else
		{IJ.run(blue, "8-bit", "");}	
		h=red.getHeight(); 
		w=red.getWidth(); 
		Red = new boolean[h*w];
		Green = new boolean[h*w];
		Blue = new boolean[h*w];
		Mask = new boolean[h*w];
		ThresholdsMask = new boolean[h*w]; 
	
		}

	    public boolean[] binarizeChannel(ImagePlus Channel, double value) 
	    {
		boolean[] VECTOR = new boolean[w*h];
		if(MaskThreshold.equals("User_threshold")) {
			IJ.setThreshold(Channel, value, 255);
			IJ.run(Channel, "Convert to Mask", ""); }
		else {		
			IJ.setAutoThreshold(Channel, MaskThreshold);
			IJ.run(Channel, "Convert to Mask", ""); }
		ImageProcessor CHANNEL=Channel.getProcessor(); 
		int i=0;
		for(int y=0; y<h; y++)
			{
			for(int x=0; x<w; x++)
				{
				if(CHANNEL.getPixel(x, y)==255)
					VECTOR[i]=true;
				else
					VECTOR[i]=false;
				i++;
				}
			}
		return VECTOR;
		}

	public boolean[] createMask() 
	{
		
		boolean[] finalMask = new boolean[w*h];
		
		if(MaskThreshold.equals("No_thresold")) 
			 { for(int i=0; i<w*h; i++) ThresholdsMask[i] = true; } 
			 else 
			 {
			 Red=binarizeChannel(red, redValue); 
			 Green=binarizeChannel(green, greenValue); 
			if(blue.equals(null)) {} else { Blue=binarizeChannel(blue, blueValue); }
     		if(MaskOperator1.equals("AND"))
				{
				for(int i=0; i<w*h; i++)
					{
					if(Red[i]==true && Green[i]==true)
						ThresholdsMask[i] = true;
					else
						ThresholdsMask[i] = false;
					}
				}
			else
				{
				for(int i=0; i<w*h; i++)
					{
					if(Red[i]==true || Green[i]==true)
						ThresholdsMask[i] = true;
					else
						ThresholdsMask[i] = false;
					}
				}
     		if(blue.equals(null)) {} else
     			{
     			if(MaskOperator2.equals("AND"))
     				{
     				for(int i=0; i<w*h; i++)
     					{
     					if(ThresholdsMask[i]==true && Blue[i]==true)
     						ThresholdsMask[i] = true;
     					else
     						ThresholdsMask[i] = false;
     					}
     				}
     			else
     				{
     				for(int i=0; i<w*h; i++)
     					{
     					if(ThresholdsMask[i]==true || Blue[i]==true)
     						ThresholdsMask[i] = true;
     					else
     						ThresholdsMask[i] = false;
     					}
     				}
     			}
			}
     		if(primaryMask.equals(null)) { finalMask = ThresholdsMask; } 	else {
			Mask=binarizeChannel(primaryMask, 200); 
			for(int i=0; i<w*h; i++)
				{
				if(ThresholdsMask[i]==true && Mask[i]==true)
					finalMask[i] = true;
				else
					finalMask[i] = false;
				}

		return finalMask;
			}
			return finalMask;
		}
	}




