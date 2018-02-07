package ar.com.yahoojuliobuonfigli.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class StatisticalSignificance {

private ColocalizationCoefficients cc;
private ImagePlus stackRed, stackGreen, stackBlue;
private ImageProcessor STACKrED, STACKgREEN, STACKbLUE;
private boolean r, R, i, k, m;
private int[] red, green, blue;
private int seed, gImages;
private String level;
private int[][] rRed, rGreen, rBlue; 

public StatisticalSignificance(ColocalizationCoefficients cc, boolean r, boolean R, boolean i,
					boolean k, boolean m, int seed, int gImages, String level, String channel3)
	{
	this.seed=seed;
	this.gImages=gImages;
	this.level=level;
	this.r=r;
	this.R=R;
	this.i=i;
	this.k=k;
	this.m=m;
	this.cc=cc;
	red=cc.getRed();
	STACKrED=createShiftingStack(red);
	rRed=stack2D(STACKrED);
	green=cc.getGreen();
	if(channel3.equals("None")) 
		{
		blue=red;
		STACKbLUE=STACKrED;
		rBlue=rRed;
		STACKgREEN=STACKrED;
		rGreen=rRed;
		}
	else
		{
		blue=cc.getBlue();
		STACKbLUE=createShiftingStack(blue);
		rBlue=stack2D(STACKbLUE);
		STACKgREEN=createShiftingStack(green);
		rGreen=stack2D(STACKgREEN);
		}
	}

static double[] pd(double[] v) 
	{
	int l=v.length;
	double[] PD=new double[2];
	PD[0]=0.0;	
	for(int i=0; i<l; i++)
		PD[0]=PD[0]+v[i];
	PD[0]=PD[0]/l;
	PD[1]=0.0;	
	for(int j=0; j<l; j++)
		PD[1]=PD[1]+(v[j]-PD[0])*(v[j]-PD[0]);
	PD[1]=Math.sqrt(PD[1]/(l-1));
	return PD;
	}

public ImageProcessor createShiftingStack(int[] vec)
	{
	int cont, X, vp;
	int l = (int)Math.sqrt(vec.length);
	int k = l/2;
	ImagePlus st = IJ.createImage("stack", "8-bit random", l, l, l); 
	ImageProcessor ST = st.getProcessor(); 
	st.close();
	for(int z=1; z<l+1; z++)
		{
		IJ.setSlice(z);
		cont=0;
		for(int y=0; y<l; y++)
			{
			for(int x=0; x<l; x++)
				{
				if(x<k)
					X=x+(l-k);
					else
					X=Math.abs(x-k);
				vp=(int)Math.floor(cont/l)*l+cont%l;
		        ST.putPixelValue(X, y, vec[vp]); 
		        cont++;
				}
			}
		if(k>l)
			k=0;
		k++;
		}
	return ST;
	}

public ImagePlus returnShiftingStack(ImageProcessor img)
	{
	int l=img.getWidth();
	ImagePlus st = IJ.createImage("stack", "8-bit random", l, l, l); 
	st.setProcessor(img);
	return st;
	}
	
public int[][] stack2D(ImageProcessor ST)
	{
	int w=ST.getWidth();
	int h=ST.getHeight();		
	int[][] vec = new int[w][w*h];
	int i;
	for(int z=0; z<w; z++)
		{	
		ST.setSliceNumber(z);
		i=0;
		for(int y=0; y<h; y++)
			{
			for(int x=0; x<w; x++)
				{
				vec[z][i]=(int)ST.getPixelValue(x, y);
				i++;
				}
			}
		}
	return vec;
	}

public double[] pearsonVec(int[] C1, int[][] rc, int n)
	{
	int l = C1.length;
	double[] vec = new double[n];
	int[] C2 = new int[l];
 	int pos=0;
 	double pC1=ColocalizationCoefficients.prom(C1);
	double dC1=ColocalizationCoefficients.dr(C1, pC1);
	for(int i=0; i<n; i++)
 		{
 		pos=(int)Math.random()*l; 				
		for(int j=0; j<l; j++)
 			C2[j] = rc[pos][j];
		double pC2=ColocalizationCoefficients.prom(C2);
		double dC2=ColocalizationCoefficients.dr(C2, pC2);
		vec[i] = ColocalizationCoefficients.PEARSON(C1, C2, dC1, dC2, pC1, pC2);
 		}
	return vec;
	}
//y un vec para cada coeficiente
public double pValue(double z, double e)
	{
	double a, a1, s;
	double pi=Math.PI;
	int n=0;
	s=z;
	a=z;
	do
		{
		a=-a*z*z/(n+2);
		n=n+2;
		a1=a/(n+1);
		s=s+a1;
		}
	while(Math.abs(a1)>e);
	return s/Math.sqrt(2*pi);
	}

public double returnP(double[] vec, double coef)
	{
	double Z, p;
	double[] prsd=new double[2];
	prsd=pd(vec);
	Z=(coef-prsd[0])/prsd[1];
	p=pValue(Z, 0.00000001);
	return p;
	}
	
	
	
public ImagePlus returnStackRed() { return stackRed; }
public ImagePlus returnStackGreen() { return stackGreen; }	
public ImagePlus returnStackBlue() { return stackBlue; }
}
//public ImagePlus createShiftingStack(int[] vec, int w, int h)
//este va a ser el metodo sobrecargado para cuando no haya mascara y la seleccion quede rectagular

/*
public int[][] makeMatrix(int[] img) 
	{
	int w=(int)Math.floor(Math.sqrt(img.length));
	int h=w;
	int[][] matrix = new int[w][h]; 
	int i=0;
	for(int y=0; y<h; y++)
		{
		for(int x=0; x<w; x++)
			{
			matrix[x][y]=img[i];
			i++;
			}
		}
	return matrix; 
	}		

public ImagePlus createShiftingStack(int[] vec)
	{
	int cont, X, vp;
	int l = (int)Math.sqrt(vec.length);
	int k = l/2;
	ImagePlus st = IJ.createImage("stack", "8-bit random", l, l, l); 
	ImageProcessor ST = st.getProcessor(); 
	for(int z=1; z<l+1; z++)
		{
		IJ.setSlice(z);
		cont=0;
		for(int y=0; y<l; y++)
			{
			for(int x=0; x<l; x++)
				{
				if(x<k)
					X=x+(l-k);
					else
					X=Math.abs(x-k);
				vp=(int)Math.floor(cont/l)*l+cont%l;
		        ST.putPixelValue(X, y, vec[vp]); 
		        cont++;
				}
			}
		if(k>l)
			k=0;
		k++;
		}
	st.setProcessor(ST);
	return st;
	}

public int[] randomSlice(ImageProcessor ST) 
	{
	int w=ST.getWidth();
	int h=ST.getHeight();		
	int[] vec = new int[w*h];
	int z = (int)Math.random();
	ST.setSliceNumber(z);
	int i=0;
	for(int y=0; y<h; y++)
		{
		for(int x=0; x<w; x++)
			{
			vec[i]=(int)ST.getPixelValue(x, y);
			i++;
			}
		}
	return vec;
	}

*/

