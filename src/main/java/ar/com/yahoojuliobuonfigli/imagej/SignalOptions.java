package ar.com.yahoojuliobuonfigli.imagej;

import ij.plugin.PlugIn;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

//falta la entrada a funciones de la funcion de dispersion
//mejor que estos metodos devuelvan void xq no sirve crear una imagen para cada metodo
public class SignalOptions {

private ImagePlus image;
private ImageProcessor IMAGE;
private String threshold;
private boolean booleanAI, booleanDS;
private double TP, AI, DR, value;
private double[] Image;
private int dr, h, w;

public SignalOptions(ImagePlus image, String threshold, boolean booleanAI, 
		boolean booleanDS, double TP, double DR, double value) 
	{	
	this.image=image;
	this.threshold=threshold;
	this.booleanAI=booleanAI;
	this.booleanDS=booleanDS;
	this.TP=TP;
//	this.AI=AI;
	this.DR=DR;
	this.value=value;	
	dr=(int)DR;   //hice cast. Es decir transforme un float en entero
	IMAGE=image.getProcessor();
	h=image.getHeight(); 
	w=image.getWidth(); 
	}

//metodo de funcion de dispersion
static double Function(int r, int e, double d, double f) 
	{
	double v=0;
	switch(e) 
		{
		case 1:
			v=1/(d+1); break;
		case 2:
			v=1/(d*d+1); break;
		case 3:
			v=1/Math.pow(2, d); break;
		case 4:
			v=1-1*f*(d+1)/r; break;	
		}
	 if(v<0)
		v=0;
	 return v;
	}

//metodo generador de PSF que va a dispersar
static double[] PSF(int r, int e, double f) 
	{
	int l=2*r-1; 
	int c= r-1;
	double d;
	double[] psf=new double[l*l];
	int i=0;
	for (int y = 0; y < l; y++) 
		{
	    for (int x = 0; x < l; x++) 
	    	{
	        d = Math.sqrt(Math.pow(c - x, 2) + Math.pow(c - y, 2));
			if (d >= r) 
	        	psf[i]=0;
	    	else 
	    		psf[i]=Function(r, e, d, f);   
	       i++;
	    	}
		}
	return psf;	
	}

//metodo que dispersa imagen
public void disperse() 
	{
	int l=2*dr-1;
	double[] Psf=new double[l*l];
	Psf=PSF(dr, 4, 1);
	int an = IMAGE.getWidth();               
	int al = IMAGE.getHeight();
	double[][] img=new double[an+l][al+l];
	double[] vector=new double[an*al];
	for(int y=0; y<al+l; y++) 
		{
		for(int x=0; x<an+l; x++) 
			img[x][y]=0; 
		}              
	for(int y=l/2; y<al+(l/2); y++) 
		{
		for(int x=l/2; x<an+(l/2); x++) 
			img[x][y]=IMAGE.getPixel(x-l/2, y-l/2); 
		} 
		
	int N=0;
	int M=0;
	double suma;
	int i, j=0;
	for(int k=0; k<al*an; k++)
		{
		suma=0;
		i=0;
		for(int n=N; n<N+l; n++)
			{
			for(int m=M; m<M+l; m++)
				{
				suma=suma+Psf[i]*img[m][n]/255;
				i++;
				}
			}
		M++;
		//if(suma>255) suma=255; //comentar
		if(M>an-1)
			{
			M=0; 
			N++;
			}
		vector[j]=suma;
		j++;
		}
	j=0;
	for(int y=0; y<al; y++) 
		{
		for(int x=0; x<an; x++) 
			{
			IMAGE.putPixelValue(x, y, vector[j]); 
			j++;
			}
		}
	image.setProcessor(IMAGE);
	}

//public void average()
public void umbralize() 
	{
	if(threshold.equals("No_thresholds") || threshold.equals("User_thresholds")) 
		{}
	else 
		{
		if(threshold.equals("MRF_threshold"))
			{  //IMAGE.performMRFthreshold(); tengo que escribir este metodo
			}
		else
			{
			AutoThresholder thresholder = new AutoThresholder();
			value = thresholder.getThreshold(threshold, IMAGE.getHistogram());
			}
		}
	float pix=0;
	for(int y=0; y<h; y++)
		{
		for(int x=0; x<h; x++)
			{
			pix=IMAGE.getPixelValue(x, y);
			if(pix<value)
				IMAGE.putPixelValue(x, y, 0); 
			}
		}
	}

}

/* version de creacion de PSF pero cargada en un vector de dos dimensiones
static double[][] PSF(int r, int e, double f) {
	 int l=2*r-1; 
	 int c= r-1;
	 double d;
	 double v;
	 double[][] psf=new double[l][l];  
	 for (int y = 0; y < l; y++) {
	    for (int x = 0; x < l; x++) {
	        d = Math.sqrt(Math.pow(c - x, 2) + Math.pow(c - y, 2));
			if (d >= r) 
	        	v=0;
	    	else 
	        	v=Function(r, e, d, f);       
	       psf[x][y]=v;
	    }
	}
return psf;	
} */

