package ar.com.yahoojuliobuonfigli.imagej;

//la clase vectorización tendra como un parametro de entrada un vector boolean que es la mascara

import ij.plugin.PlugIn;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class Mask {

	private String MaskThreshold, MaskOperator;
	private double redValue, greenValue, blueValue;
	private ImagePlus red, green, blue, primaryMask;
	private double [] Red, Green, Blue, Mask, ThresholdsMask;
	private int h, w, s=4;

	public Mask(ImagePlus primaryMask, ImagePlus red, ImagePlus green, ImagePlus blue, String MaskThreshold, 
			String MaskOperator, double redValue, double greenValue, double blueValue ) {
		this.red=red;
		this.green=green;
		this.blue=blue;
		this.primaryMask=primaryMask;
		this.MaskThreshold=MaskThreshold;
		this.MaskOperator=MaskOperator;
		this.redValue=redValue;
		this.greenValue=greenValue;
		this.blueValue=blueValue;
		IJ.run(red, "8-bit", ""); 
		IJ.run(green, "8-bit", "");
		IJ.run(blue, "8-bit", "");	
		if(red.equals(null))
		{h=green.getHeight(); w=green.getWidth(); }
		else
		{h=red.getHeight(); w=red.getWidth(); }
		Red = new double[h*w];
		Green = new double[h*w];
		Blue = new double[h*w];
		Mask = new double[h*w];
		ThresholdsMask = new double[h*w];
	}

	public double[] binarizeAndVectorizeChannel(ImagePlus Channel, double value) {
		double[] vector = new double[w*h];
		ImageProcessor CHANNEL;
		if(MaskThreshold.equals("User_threshold")) {
			IJ.setThreshold(Channel, value, 255);
			IJ.run(Channel, "Convert to Mask", ""); }
		else {		
			IJ.setAutoThreshold(Channel, MaskThreshold);
			IJ.run(Channel, "Convert to Mask", ""); }
		CHANNEL=Channel.getProcessor(); 
		int i=0;
		for(int y=0; y<h; y++)
		{
			for(int x=0; x<w; x++)
			{
				vector[i]=CHANNEL.getPixel(x, y);
				i++;
			}
		}
		return vector;
	}

	public boolean[] createMask() {
		
		double[] finalMask = new double[w*h];
		boolean[] booleanFinalMask = new boolean[w*h];

		if(MaskThreshold.equals("No_thresold")) 
		{ for(int i=0; i<w*h; i++) ThresholdsMask[i] = 255; } 
		else {
			if(red.equals(null)) {s=1;} else { Red=binarizeAndVectorizeChannel(red, redValue); }
			if(green.equals(null)) {s=2;} else { Green=binarizeAndVectorizeChannel(green, greenValue); }
			if(blue.equals(null)) {s=3;} else { Blue=binarizeAndVectorizeChannel(blue, blueValue); }

			if(MaskOperator.equals("AND"))
			{
				switch(s) {
				case 1: 
					for(int i=0; i<w*h; i++)
						ThresholdsMask[i] = Green[i]*Blue[i];
					break;
				case 2: 
					for(int i=0; i<w*h; i++)
						ThresholdsMask[i] = Red[i]*Blue[i];
					break;
				case 3: 
					for(int i=0; i<w*h; i++)
						ThresholdsMask[i] = Green[i]*Red[i];
					break;
				case 4: 
					for(int i=0; i<w*h; i++)
						ThresholdsMask[i] = Red[i]*Green[i]*Blue[i];
					break;
				}
			}
			else
			{
				switch(s) {
				case 1: 
					for(int i=0; i<w*h; i++)
						ThresholdsMask[i] = Green[i]+Blue[i];
				case 2: 
					for(int i=0; i<w*h; i++)
						ThresholdsMask[i] = Red[i]+Blue[i];
				case 3: 
					for(int i=0; i<w*h; i++)
						ThresholdsMask[i] = Green[i]+Red[i];
				case 4: 
					for(int i=0; i<w*h; i++)
						ThresholdsMask[i] = Red[i]+Green[i]+Blue[i];			}
			}
		}
		if(primaryMask.equals(null)) { finalMask = ThresholdsMask; } 	else {
			Mask=binarizeAndVectorizeChannel(primaryMask, 200); 
			for(int i=0; i<w*h; i++)
				finalMask[i] = ThresholdsMask[i]*Mask[i]; 

		for(int i=0; i<w*h; i++)
			if(finalMask[i]>0)
				booleanFinalMask[i] = true;
			else
				booleanFinalMask[i] = false;
				
		}
		return booleanFinalMask;
	}
}




