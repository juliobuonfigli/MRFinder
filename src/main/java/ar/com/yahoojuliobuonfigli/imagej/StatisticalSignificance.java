package ar.com.yahoojuliobuonfigli.imagej;
//grafico Pearson vs mask threshold
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

public class StatisticalSignificance {

private ColocalizationCoefficients cc;
private ImageStack stackRed, stackGreen, stackBlue;
//private ImageProcessor STACKrED, STACKgREEN, STACKbLUE;
private boolean r, R, i, k, m;
private int[] red, green, blue;
private int seed, gImages, w, h, nRed, nGreen, nBlue;
private String level, c3;
private int[][] rRed, rGreen, rBlue; 
private double[] coefs;
private double promRed, promGreen, promBlue, dmRed, dmGreen, dmBlue, drRed, drGreen, drBlue, dRRed, 
			   dRGreen, dRBlue, dR3Red, dR3Green, dR3Blue;

public StatisticalSignificance(ColocalizationCoefficients cc, boolean r, boolean R, boolean i,
					boolean k, boolean m, int seed, int gImages, String level, String channel3)
	{
	c3=channel3;
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
	stackRed = createShiftingStack(red);
	rRed = stack2D(stackRed);
	promRed=ColocalizationCoefficients.prom(red);
	nRed=ColocalizationCoefficients.number(red);
	drRed=ColocalizationCoefficients.dr(red, promRed);
	dmRed=ColocalizationCoefficients.dm(red);
	dRRed=ColocalizationCoefficients.dR(red);
	dR3Red=ColocalizationCoefficients.dR3(red);
	green=cc.getGreen();
	promGreen=ColocalizationCoefficients.prom(green);
	nGreen=ColocalizationCoefficients.number(green);
	drGreen=ColocalizationCoefficients.dr(green, promGreen);
	dmGreen=ColocalizationCoefficients.dm(green);
	dRGreen=ColocalizationCoefficients.dR(green);
	dR3Green=ColocalizationCoefficients.dR3(green);
	if(c3.equals("None")) 
		{
		blue=red;
		stackGreen=stackRed;
		stackBlue=stackRed;
		rGreen=rRed;
		rBlue=rRed;
		}
	else
		{
		stackGreen = createShiftingStack(green);
		rGreen = stack2D(stackGreen);
		blue = cc.getBlue();
		stackBlue = createShiftingStack(blue);
		rBlue = stack2D(stackBlue);
		promBlue=ColocalizationCoefficients.prom(blue);
		nBlue=ColocalizationCoefficients.number(blue);
		drBlue=ColocalizationCoefficients.dr(blue, promBlue);
		dmBlue=ColocalizationCoefficients.dm(blue);
		dRBlue=ColocalizationCoefficients.dR(blue);
		dR3Blue=ColocalizationCoefficients.dR3(blue);
		}
	coefs=cc.AllCoef();
	double l = Math.sqrt(red.length);
	double s = l - Math.floor(l);
	if(s<0.25 || s>0.75)
		{
		w=(int)Math.round(l);
		h=w;
		}
	else
		{
		w=(int)Math.floor(l);
		h=w+1;		
		}
	}

public double returnP(double[] vec, double coef)
	{
	double Z, p;
	double[] prsd=new double[2];
	prsd=pd(vec);
	Z=(coef-prsd[0])/prsd[1];
	p=pValue(Z);
	return p;
	}

static double[] pd(double[] v) 
	{
	int L=v.length;
	double[] PD=new double[2];
	PD[0]=0.0;	
	for(int i=0; i<L; i++)
		PD[0]=PD[0]+v[i];
	PD[0]=PD[0]/L;
	PD[1]=0.0;	
	for(int j=0; j<L; j++)
		PD[1]=PD[1]+(v[j]-PD[0])*(v[j]-PD[0]);
	PD[1]=Math.sqrt(PD[1]/(L-1));
	return PD;
	}

static double pValue(double z)
	{
	double a, a1, s;   //poner una sentencia para limitar el numero de iteraciones
	final double e=0.00000000000001;
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
	s=s/(Math.sqrt(2*(Math.PI)));
	return s;
	}

public ImageStack createShiftingStack(int[] vec)
	{
	int cont, X, pp, val;
	int k = w/2;
	ImageStack st = ImageStack.create(w, h, w, 8); 
	for(int z=0; z<w; z++)
		{
		cont=0;
		for(int y=0; y<h; y++)
			{
			for(int x=0; x<w; x++)
				{
				if(x<k)
					X=x+(w-k);
					else
					X=Math.abs(x-k);
				pp=(int)Math.floor(cont/w)*w+cont%w;
		        if(pp>=vec.length) val=0; else val=vec[pp];  //agregado recientemente
				st.setVoxel(X, y, z, val); 
		        cont++;
				}
			}
		if(k>w)
			k=0;
		k++;
		}
	return st;
	}

public int[][] stack2D(ImageStack ST)
	{
	int[][] vec = new int[w][w*h];
	int i;
	for(int z=0; z<w; z++)
		{	
		i=0;
		for(int y=0; y<h; y++)
			{
			for(int x=0; x<w; x++)
				{
				vec[z][i]=(int)ST.getVoxel(x, y, z);
				i++;
				}
			}
		}
	return vec;
	}

public double[] pearsonVec(int[] C1, int[][] rc, int n, double dC1, double dC2, double pC1, double pC2)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(w-1)); 				
		vec[i] = ColocalizationCoefficients.PEARSON(C1, rc[pos], dC1, dC2, pC1, pC2);
		}
	return vec;
	}

public double[] PEARSON()
	{
	double[] v = new double[gImages];
	double[] pv = new double[3];
	v = pearsonVec(green, rRed, gImages, dRRed, dRGreen, promRed, promGreen);
	pv[0]=returnP(v, coefs[0]);
	if(c3.equals("None"))
		{
		v = pearsonVec(blue, rRed, gImages, drRed, drGreen, promRed, promGreen);
		pv[1]=returnP(v, coefs[1]);
		v = pearsonVec(blue, rGreen, gImages, drBlue, drGreen, promBlue, promGreen);
		pv[2]=returnP(v, coefs[2]);
		}
	return pv;
	}

public double[] overlapVec(int[] C1, int[][] rc, int n, double dC1, double dC2)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(w-1)); 				
		vec[i] = ColocalizationCoefficients.OVERLAP(C1, rc[pos], dC1, dC2);
		}
	return vec;
	}

public double[] overlapVec(int[] C1, int[] C2, int[][] rc, int n)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(w-1)); 				
		vec[i] = ColocalizationCoefficients.OVERLAP(C1, C2, rc[pos]);
		}
	return vec;
	}

public double[] overlapVec(int[] C1, int[][] rc1, int[][] rc2, int n)
	{
	double[] vec = new double[n];
	int p1, p2;
	for(int i=0; i<n; i++)
		{
		p1=(int)(Math.random()*(w-1)); 		
		p2=(int)(Math.random()*(w-1)); 	
		vec[i] = ColocalizationCoefficients.OVERLAP(C1, rc1[p1], rc2[p2]);
		}
	return vec;
	}

public double[] OVERLAP()
	{
	double[] v = new double[gImages];
	double[] pv = new double[7];
	v = overlapVec(green, rRed, gImages, dRRed, dRGreen);
	pv[0]=returnP(v, coefs[3]);
	if(c3.equals("None"))
		{
		v = overlapVec(blue, rRed, gImages, dRRed, dRGreen);
		pv[1]=returnP(v, coefs[4]);
		v = overlapVec(blue, rGreen, gImages, dRBlue, dRGreen);
		pv[2]=returnP(v, coefs[5]);
		v = overlapVec(blue, green, rRed, gImages);
		pv[3]=returnP(v, coefs[6]);
		v = overlapVec(blue, red, rGreen, gImages);
		pv[4]=returnP(v, coefs[6]);
		v = overlapVec(blue, green, rBlue, gImages);
		pv[5]=returnP(v, coefs[6]);
		v = overlapVec(red, rGreen, rBlue, gImages);
		pv[6]=returnP(v, coefs[6]);
		}
	return pv;
	}

public double[] icqVec(int[] C1, int[][] rc, int n, int N)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(w-1)); 				
		vec[i] = ColocalizationCoefficients.ICQ(C1, rc[pos], N);
		}
	return vec;
	}

public double[] icqVec(int[] C1, int[] C2, int[][] rc, int n, int N)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(w-1)); 				
		vec[i] = ColocalizationCoefficients.ICQ(C1, C2, rc[pos], N);
		}
	return vec;
	}

public double[] icqVec(int[] C1, int[][] rc1, int[][] rc2, int n)
	{
	double[] vec = new double[n];
	int p1, p2;
	for(int i=0; i<n; i++)
		{
		p1=(int)(Math.random()*(w-1)); 	
		p2=(int)(Math.random()*(w-1)); 
		vec[i] = ColocalizationCoefficients.ICQ(C1, rc2[p1], rc2[p2]);
		}
	return vec;
	}

public double[] ICQ()
	{
	double[] v = new double[gImages];
	double[] pv = new double[10];
	v = icqVec(green, rRed, gImages, nRed);
	pv[0]=returnP(v, coefs[7]);
	v = icqVec(green, rRed, gImages, nGreen);
	pv[1]=returnP(v, coefs[8]);
	if(c3.equals("None"))
		{
		v = icqVec(blue, rRed, gImages, nRed);
		pv[2]=returnP(v, coefs[9]);
		v = icqVec(blue, rRed, gImages, nBlue);
		pv[3]=returnP(v, coefs[10]);
		v = icqVec(blue, rGreen, gImages, nGreen);
		pv[4]=returnP(v, coefs[11]);
		v = icqVec(blue, rGreen, gImages, nBlue);
		pv[5]=returnP(v, coefs[12]);
		v = icqVec(green, blue, rRed, gImages, nRed);
		pv[6]=returnP(v, coefs[13]);
		v = icqVec(red, blue, rGreen, gImages, nGreen);
		pv[7]=returnP(v, coefs[14]);
		v = icqVec(red, green, rBlue, gImages, nBlue);
		pv[8]=returnP(v, coefs[15]);
		v = icqVec(blue, rGreen, rBlue, gImages);
		pv[9]=returnP(v, coefs[16]);
		}
	return pv;
	}

public double[] kVec(int[] C1, int[][] rc, int n, double den)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(w-1)); 				
		vec[i] = ColocalizationCoefficients.COEFK(C1, rc[pos], den);
		}
	return vec;
	}

public double[] kVec(int[] C1, int[] C2, int[][] rc, int n, double den)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(w-1)); 				
		vec[i] = ColocalizationCoefficients.COEFK(C1, C2, rc[pos], den);
		}
	return vec;
	}

public double[] COEFK()
	{
	double[] v = new double[gImages];
	double[] pv = new double[9];
	v = kVec(green, rRed, gImages, nRed);
	pv[0]=returnP(v, coefs[17]);
	v = kVec(green, rRed, gImages, nGreen);
	pv[1]=returnP(v, coefs[18]);
	if(c3.equals("None"))
		{
		v = kVec(blue, rRed, gImages, dRRed);
		pv[2]=returnP(v, coefs[19]);
		v = kVec(blue, rRed, gImages, dRBlue);
		pv[3]=returnP(v, coefs[20]);
		v = kVec(blue, rGreen, gImages, dRGreen);
		pv[4]=returnP(v, coefs[21]);
		v = kVec(blue, rGreen, gImages, dRBlue);
		pv[5]=returnP(v, coefs[22]);
		v = kVec(green, blue, rRed, gImages, dR3Red);
		pv[6]=returnP(v, coefs[23]);
		v = kVec(red, blue, rGreen, gImages, dR3Green);
		pv[7]=returnP(v, coefs[24]);
		v = kVec(red, green, rBlue, gImages, dR3Blue);
		pv[8]=returnP(v, coefs[25]);
		}
	return pv;
	}

public double[] COEFM()
	{
	double[] v = new double[gImages];
	double[] pv = new double[9];
	v = mVec(green, rRed, gImages, nRed);
	pv[0]=returnP(v, coefs[26]);
	v = mVec(green, rRed, gImages, nGreen);
	pv[1]=returnP(v, coefs[27]);
	if(c3.equals("None"))
		{
		v = mVec(blue, rRed, gImages, dmRed);
		pv[2]=returnP(v, coefs[28]);
		v = mVec(blue, rRed, gImages, dmBlue);
		pv[3]=returnP(v, coefs[29]);
		v = mVec(blue, rGreen, gImages, dmGreen);
		pv[4]=returnP(v, coefs[30]);
		v = mVec(blue, rGreen, gImages, dmBlue);
		pv[5]=returnP(v, coefs[31]);
		v = mVec(green, blue, rRed, gImages, dmRed);
		pv[6]=returnP(v, coefs[32]);
		v = mVec(red, blue, rGreen, gImages, dmGreen);
		pv[7]=returnP(v, coefs[33]);
		v = mVec(red, green, rBlue, gImages, dmBlue);
		pv[8]=returnP(v, coefs[34]);
		}
	return pv;
	}

public double[] mVec(int[] C1, int[][] rc, int n, double den)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(w-1)); 				
		vec[i] = ColocalizationCoefficients.COEFM(C1, rc[pos], den);
		}
	return vec;
	}

public double[] mVec(int[] C1, int[] C2, int[][] rc, int n, double den)
{
double[] vec = new double[n];
int pos;
for(int i=0; i<n; i++)
	{
	pos=(int)(Math.random()*(w-1)); 				
	vec[i] = ColocalizationCoefficients.COEFM(C1, C2, rc[pos], den);
	}
return vec;
}

public double[] Allpvalues() 
	{
	double[] pvalues=new double[38];
	double[] Pearson=new double[3];
	double[] Overlap=new double[7];
	double[] icq=new double[10];
	double[] coefK=new double[9];
	double[] coefM=new double[9];
	if(r==true) Pearson=PEARSON();
	if(R==true) Overlap=OVERLAP();
	if(i==true) icq=ICQ();
	if(k==true) coefK=COEFK();
	if(m==true) coefM=COEFM();
		int i=0;
		for(int j=0; j<3; j++)
			{pvalues[i]=Pearson[j]; i++;}
		for(int j=0; j<7; j++)
			{pvalues[i]=Overlap[j]; i++;}
		for(int j=0; j<10; j++)
			{pvalues[i]=icq[j]; i++;}
		for(int j=0; j<9; j++)
			{pvalues[i]=coefK[j]; i++;}
		for(int j=0; j<9; j++)
			{pvalues[i]=coefM[j]; i++;}
	return pvalues;
	}

public ImageStack returnStackRed() { return stackRed; }
public ImageStack returnStackGreen() { return stackGreen; }	
public ImageStack returnStackBlue() { return stackBlue; }

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

public ImageProcessor createShiftingStack(int[] vec)
	{
	int cont, X, pp;
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
				pp=(int)Math.floor(cont/l)*l+cont%l;
		        ST.putPixelValue(X, y, vec[pp]); 
		        cont++;
				}
			}
		if(k>l)
			k=0;
		k++;
		}
	//st.setProcessor(ST);
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
	for(int j=0; j<l; j++)
		C2[j] = rc[0][j];
	double pC2=ColocalizationCoefficients.prom(C2);
	double dC2=ColocalizationCoefficients.dr(C2, pC2);
	for(int i=0; i<n; i++)
 		{
 		pos=(int)Math.random()*l; 				
		for(int j=0; j<l; j++)
 			C2[j] = rc[pos][j];
		vec[i] = ColocalizationCoefficients.PEARSON(C1, C2, dC1, dC2, pC1, pC2);
 		}
	return vec;
	}

public double[] pearsonVec(int[] C1, int[][] rc, int n)
	{
	double[] vec = new double[n];
	int[] C2 = new int[l*l];
 	int pos=0;
 	double pC1=ColocalizationCoefficients.prom(C1);
	double dC1=ColocalizationCoefficients.dr(C1, pC1);
	for(int j=0; j<l*l; j++)
		C2[j] = rc[1][j];
	double pC2=ColocalizationCoefficients.prom(C2);
	double dC2=ColocalizationCoefficients.dr(C2, pC2);
	for(int i=0; i<n; i++)
 		{
 		pos=(int)Math.random()*(l-1); 				
		for(int j=0; j<l*l; j++)
 			C2[j] = rc[pos][j];
		vec[i] = ColocalizationCoefficients.PEARSON(C1, C2, dC1, dC2, pC1, pC2);
 		}
	return vec;
	}


public double[] pearsonVec(int[] C1, int[][] rc, int n)
	{
	double[] vec = new double[n];
	int pos=1;
	double pC1=ColocalizationCoefficients.prom(C1);
	double dC1=ColocalizationCoefficients.dr(C1, pC1);
	double pC2=ColocalizationCoefficients.prom(rc[pos]);
	double dC2=ColocalizationCoefficients.dr(rc[pos], pC2);
	for(int i=0; i<n; i++)
		{
		pos=(int)Math.random()*(l-1); 				
		vec[i] = ColocalizationCoefficients.PEARSON(C1, rc[pos], dC1, dC2, pC1, pC2);
		}
	return vec;
	}

public double[] pearsonVec(int[] C1, int[][] rc, int n)
	{
	double[] vec = new double[n];
	double pC1=35.23;//ColocalizationCoefficients.prom(C1);
	double dC1=12.32;//ColocalizationCoefficients.dr(C1, pC1);
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.PEARSON(C1, C1, dC1, dC1, pC1, pC1);
	return vec;
	}

public double SSResults()
	{
	/*int[][] img = new int[l][l*l]; 
	for(int i=0; i<l; i++)
		for(int j=0; j<l*l; j++)
			img[i][j]=(int)Math.round(255*Math.random());
		
	/*double[] vec=new double[gImages];
	for(int k=0; k<gImages; k++)
		vec[k]=Math.random();
	ImageStack IMG = createShiftingStack(red);
	int[][] img = stack2D(IMG);
	/*double[] vec = new double[30];
	for(int i=0; i<30; i++)
		vec[i]=(int)(Math.random()*(l-1));
	//	vec[i]=img[i*l][(int)Math.round(Math.random()*(l*l-1))];
	double[] vec = pearsonVec(green, img, gImages);
	double p = returnP(vec, coefs[0]);
	//double p = ColocalizationCoefficients.PEARSON(red, green, 12.3, 12.3, 25.6, 26.7);
	return p;
	//return vec;
	}
public double[] pearsonVec(int[] C1, int[][] rc, int n)
	{
	double[] vec = new double[n];
	int pos=1;
	double pC1=ColocalizationCoefficients.prom(C1);
	double dC1=ColocalizationCoefficients.dr(C1, pC1);
	double pC2=ColocalizationCoefficients.prom(rc[pos]); 
	double dC2=ColocalizationCoefficients.dr(rc[pos], pC2);
	for(int i=0; i<n; i++)
		{
		pos=(int)(Math.random()*(l-1)); 				
		vec[i] = ColocalizationCoefficients.PEARSON(C1, rc[pos], dC1, dC2, pC1, pC2);
		}
	return vec;
	}


public double[] kVec(int[] C1, int[][] rc1, int[][] rc2, int n, double den)
	{
	double[] vec = new double[n];
	int p1, p2;
	for(int i=0; i<n; i++)
		{
		p1=(int)(Math.random()*(w-1)); 		
		p2=(int)(Math.random()*(w-1));
		vec[i] = ColocalizationCoefficients.COEFK(C1, rc1[p1], rc2[p2], den);
		}
	return vec;
	}

*/

