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

private String MaskThreshold, MaskOperator1, MaskOperator2, SelectedM, Channel3;
private double redValue, greenValue, blueValue;
private ImagePlus red, green, blue, primaryMask;
private boolean [] Red, Green, Blue, Mask, ThresholdsMask;
private int h, w;
private boolean[] finalMask;
    
public Mask(ImagePlus primaryMask, ImagePlus red, ImagePlus green, ImagePlus blue, String MaskThreshold, 
	            String MaskOperator1, String MaskOperator2, double redValue, double greenValue, 
	            double blueValue, String Channel3, String SelectedM)
	{
	this.Channel3 = Channel3;
	this.SelectedM = SelectedM;
	this.MaskThreshold = MaskThreshold;
	this.red = red;
	this.green = green;
	this.blue = blue;
	IJ.run(red, "8-bit", ""); 
	IJ.run(green, "8-bit", "");
	IJ.run(blue, "8-bit", "");
	this.redValue = redValue;
	this.greenValue = greenValue;
	this.blueValue = blueValue;
	h = red.getHeight(); 
	w = red.getWidth(); 
	Red = new boolean[h*w];
	Green = new boolean[h*w];
	Blue = new boolean[h*w]; 
	this.primaryMask=primaryMask;
	Mask = new boolean[h*w]; 
	this.MaskOperator1 = MaskOperator1;
	this.MaskOperator2 = MaskOperator2;
	ThresholdsMask = new boolean[h*w]; 
	finalMask = new boolean[w*h];
	}

public boolean[] binarizeChannel(ImagePlus Channel, double value) 
    {
	ImageProcessor CHANNEL=Channel.getProcessor();
	boolean[] VECTOR = new boolean[w*h];
	if(!MaskThreshold.equals("No_thresholds") && !MaskThreshold.equals("User_thresholds")) 
		{
		AutoThresholder thresholder = new AutoThresholder();
		value = thresholder.getThreshold(MaskThreshold, CHANNEL.getHistogram());
		}
	int i=0;
	for(int y=0; y<h; y++)
		{
		for(int x=0; x<w; x++)
			{
			if(CHANNEL.getPixel(x, y)<value)
				VECTOR[i]=false; 
			else
				VECTOR[i]=true;
			i++;
			}
		}
	return VECTOR;
	}

static boolean[] operate(String op, boolean[] c1, boolean[] c2) 
	{
	boolean[] res=new boolean[c1.length];
	if(op.equals("AND"))
		{
		for(int i=0; i<c1.length; i++)
			{
			if(c1[i]==true && c2[i]==true)
				res[i] = true;
			else
				res[i] = false;
			}
		}
	else
		{
		for(int i=0; i<c1.length; i++)
			{
			if(c1[i]==true || c2[i]==true)
				res[i] = true;
			else
				res[i] = false;
			}
		}
	return res;
	}

public boolean[] createMask() 
	{
	if(MaskThreshold.equals("No_thresholds")) 
		 { 
		 for(int i=0; i<w*h; i++) 
			 ThresholdsMask[i] = true; 
		 } 
	else 
		 {
		 Red=binarizeChannel(red, redValue); 
		 Green=binarizeChannel(green, greenValue); 
		 ThresholdsMask = operate(MaskOperator1, Red, Green);
		 if(Channel3.equals("None")) 
		 	{} 
		else 
		 	{
			Blue=binarizeChannel(blue, blueValue); 
		    ThresholdsMask = operate(MaskOperator2, ThresholdsMask, Blue);
		 	}
		 }
		if(SelectedM.equals("None")) 
			finalMask = ThresholdsMask;  	
	    else 
	    	{
	    	Mask=binarizeChannel(primaryMask, 200); 
	    	finalMask = operate("AND", ThresholdsMask, Mask);
	    	}
	return finalMask;
	}
	 
public ImagePlus renderMask() 
	{
	ImagePlus mask = IJ.createImage("Final Mask", "8-bit random", w, h, 1); 	
	ImageProcessor MASK = mask.getProcessor();
	int[] FM = new int[w*h];
	for(int i=0; i<w*h; i++)
		{
		if(finalMask[i]==true)
			FM[i]=255;
		else
			FM[i]=0;
		}
	int j=0;
	for(int y=0; y<h; y++)
		{
		for(int x=0; x<w; x++)
			{
			MASK.putPixelValue(x, y, FM[j]); 
			j++;
			}
		}
	mask.setProcessor(MASK);
	return mask;
	}

}


/*public boolean[] binarizeChannel(ImagePlus Channel, double value) 
{
boolean[] VECTOR = new boolean[w*h];
if(MaskThreshold.equals("No_thresholds")) 
	{}
else
	{
	if(MaskThreshold.equals("User_thresholds")) 
		{
		//AutoThresholder thresholder = new AutoThresholder();
		//value = thresholder.getThreshold(threshold, IMAGE.getHistogram());
		IJ.setThreshold(Channel, value, 255);
		//IJ.run(Channel, "Convert to Mask", ""); 
		}
	else 
		{		
		IJ.setAutoThreshold(Channel, MaskThreshold);
	    //IJ.run(Channel, "Convert to Mask", ""); 
		}
	}
ImageProcessor CHANNEL=Channel.getProcessor(); 
int i=0;
for(int y=0; y<h; y++)
	{
	for(int x=0; x<w; x++)
		{
		if(CHANNEL.getPixel(x, y)==0)
			VECTOR[i]=false; 
		else
			VECTOR[i]=true;
		i++;
		}
	}
return VECTOR;
}

public Mask(ImagePlus primaryMask, ImagePlus red, ImagePlus green, ImagePlus blue, String MaskThreshold, 
        String MaskOperator1, String MaskOperator2, double redValue, double greenValue, 
        double blueValue, String SelectedM, String Channel3)
{
this.Channel3=Channel3;
this.SelectedM=SelectedM;
this.MaskThreshold=MaskThreshold;
this.red=red;
this.green=green;
IJ.run(red, "8-bit", ""); 
IJ.run(green, "8-bit", "");
this.redValue=redValue;
this.greenValue=greenValue;
h=red.getHeight(); 
w=red.getWidth(); 
Red = new boolean[h*w];
Green = new boolean[h*w];
if(blue.equals(null)) 
{} 
else 
{
this.blue=blue;
IJ.run(blue, "8-bit", "");
this.blueValue=blueValue;
Blue = new boolean[h*w]; 
}
if(primaryMask.equals(null)) 
{} 
else 
{
this.primaryMask=primaryMask;
Mask = new boolean[h*w]; 
}
this.MaskOperator1=MaskOperator1;
this.MaskOperator2=MaskOperator2;
ThresholdsMask = new boolean[h*w]; 
finalMask = new boolean[w*h];
}*/


