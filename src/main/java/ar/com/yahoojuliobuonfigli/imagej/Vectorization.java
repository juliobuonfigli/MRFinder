package ar.com.yahoojuliobuonfigli.imagej;

//Clases: vectoriza, mascara, umbraliza, promediaYconvoluciona, resultadosGraficos, resultadosAnaliticos, resultados, gobierna, mainYdialogos, significancia, colocalizacion 

import ij.plugin.PlugIn;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class Vectorization {

private ImagePlus channel;
private boolean[] mask;
private int h, w, l;

	public Vectorization(boolean[] mask, ImagePlus channel) {
	this.channel=channel;
	IJ.run(channel, "8-bit", ""); 
	this.mask=mask; 
	h=channel.getHeight();
	w=channel.getWidth();
	l=0;
	for(int i=0; i<w*h; i++)
		{if(mask[i]==true) 
			l++;}
	}

	public double[] makeVector() {
		double[] vector = new double[l];
		ImageProcessor CHANNEL = channel.getProcessor();
		int i=0, j=0;
		for(int y=0; y<h; y++)
		{
			for(int x=0; x<w; x++)
			{
				if(mask[i]==true)
				{
					vector[j]=CHANNEL.getPixel(x, y); 
					j++;
				}
				i++;
			}
		}
		return vector;
	}
	
	static double[][] makeArray(ImageProcessor img, int w, int h) {
		double[][] matrix = new double[w][h]; 
		for(int y=0; y<h; y++)
		{
			for(int x=0; x<w; x++)
				matrix[x][y]=img.getPixel(x, y);
		}
		return matrix; 
	}
	static double[] makeArray(ImageProcessor img, int w, int h, int s) {
		double[] matrix = new double[s]; 
		for(int y=0; y<h; y++)
		{
			for(int x=0; x<w; x++)
				matrix[s]=img.getPixel(x,  y);
		}
		return matrix; 
	}

}
