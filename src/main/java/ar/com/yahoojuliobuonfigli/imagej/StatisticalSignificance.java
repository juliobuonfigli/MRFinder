package ar.com.yahoojuliobuonfigli.imagej;
import java.util.Random;

//grafico Pearson vs mask threshold
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

public class StatisticalSignificance {

private ColocalizationCoefficients cc;
private ImageStack stackRed, stackGreen, stackBlue;
private boolean r, R, i, k, m;
private int[] red, green, blue;
private int seed, gImages, w, h, nRed, nGreen, nBlue, halfW;
private String level, c3;
static int[][] rRed, rGreen, rBlue; 
private double[] coefs;
private double promRed, promGreen, promBlue, dmRed, dmGreen, dmBlue, drRed, drGreen, drBlue, dRRed, 
			   dRGreen, dRBlue, dR3Red, dR3Green, dR3Blue;
private Random rand;
double[] TEST1=new double[2];

public StatisticalSignificance(ColocalizationCoefficients cc, boolean r, boolean R, boolean i,
					boolean k, boolean m, int seed, int gImages, String level, String channel3)
	{
	red=cc.getRed();
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
	rand = new Random(seed);
	stackRed = createShiftingStack(red);
	rRed = new int[w][w*h];
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
	stackGreen = createShiftingStack(green);
	rGreen = new int[w][w*h];
	rGreen = stack2D(stackGreen);
	blue = cc.getBlue();
	stackBlue = createShiftingStack(blue);
	if(c3.equals("None")) 
		{
		blue=red;
		stackBlue=stackRed;
		rBlue=rRed;
		}
	else
		{
		rBlue = new int[w][w*h];
		rBlue = stack2D(stackBlue);
		promBlue=ColocalizationCoefficients.prom(blue);
		nBlue=ColocalizationCoefficients.number(blue);
		drBlue=ColocalizationCoefficients.dr(blue, promBlue);
		dmBlue=ColocalizationCoefficients.dm(blue);
		dRBlue=ColocalizationCoefficients.dR(blue);
		dR3Blue=ColocalizationCoefficients.dR3(blue);
		}
	coefs=cc.AllCoef();
	halfW=(int)Math.floor((double)w/2);
	TEST1=pd(test1());
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
	if(z<-5)
		z=-5;
	if(z>5)
		z=5;
	double a, a1, s;   //poner una sentencia para limitar el numero de iteraciones
	final double e=0.00000001;
	int n=0, cont=0;
	s=z;
	a=z;
	do
		{
		a=-a*z*z/(n+2);
		n=n+2;
		a1=a/(n+1);
		s=s+a1;
		cont++;
		if(cont>999999999)
			break;
		}
	while(Math.abs(a1)>e);
	s=0.5-Math.abs(s/(Math.sqrt(2*(Math.PI))));
	if(z<0)
		return -1*s;
	else
		return s;
	}

public ImageStack createShiftingStack(int[] vec) 
	{
	int k=w/2; 
	int cont, var, pp, val;
	ImageStack st = ImageStack.create(w, h, w, 8); 
	for(int z=0; z<w; z++)
		{
		cont=0;
		for(int y=0; y<h; y++)
			{
			for(int x=0; x<w; x++)
				{
				if(x<k)
					var=x+(w-k);
					else
					var=Math.abs(x-k);
				pp=(int)Math.floor(cont/w)*w+cont%w;
		        if(pp>=vec.length) val=0; else val=vec[pp];  
		        if(z%2==0)
					st.setVoxel(y, var, z, val); 
				else
					st.setVoxel(var, y, z, val); 
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
				//vec[z][i]=(int)(Math.random()*250);
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
		pos=rand.nextInt(w); 				
		vec[i] = ColocalizationCoefficients.PEARSON(C1, rc[pos], dC1, dC2, pC1, pC2);
		}
	return vec;
	}

public double[] PEARSON()
	{
	double[] v = new double[gImages];
	double[] pv = new double[3];
	v = pearsonVec(rGreen[0], rRed, gImages, dRGreen, dRRed, promGreen, promRed);
	pv[0]=returnP(v, coefs[0]);
	if(!c3.equals("None"))
		{
		v = pearsonVec(rBlue[0], rRed, gImages, drBlue, drRed, promBlue, promRed);
		pv[1]=returnP(v, coefs[1]);
		v = pearsonVec(rBlue[0], rGreen, gImages, drBlue, drGreen, promBlue, promGreen);
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
		pos=rand.nextInt(w); 				
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
		pos=rand.nextInt(w); 				
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
		p1=rand.nextInt(w); 		
		p2=rand.nextInt(w); 	
		vec[i] = ColocalizationCoefficients.OVERLAP(C1, rc1[p1], rc2[p2]);
		}
	return vec;
	}

public double[] OVERLAP()
	{
	double[] v = new double[gImages];
	double[] pv = new double[7];
	v = overlapVec(rGreen[halfW], rRed, gImages, dRGreen, dRRed);
	pv[0]=returnP(v, coefs[3]);
	if(!c3.equals("None"))
		{
		v = overlapVec(rRed[halfW], rBlue, gImages, dRRed, dRBlue);
		pv[1]=returnP(v, coefs[4]);
		v = overlapVec(rBlue[halfW], rGreen, gImages, dRBlue, dRGreen);
		pv[2]=returnP(v, coefs[5]);
		v = overlapVec(rBlue[halfW], rGreen[halfW], rRed, gImages);
		pv[3]=returnP(v, coefs[6]);
		v = overlapVec(rBlue[halfW], rRed[halfW], rGreen, gImages);
		pv[4]=returnP(v, coefs[6]);
		v = overlapVec(rRed[halfW], rGreen[halfW], rBlue, gImages);
		pv[5]=returnP(v, coefs[6]);
		v = overlapVec(rRed[halfW], rGreen, rBlue, gImages);
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
		pos=rand.nextInt(w); 				
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
		pos=rand.nextInt(w); 				
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
		p1=rand.nextInt(w); 	
		p2=rand.nextInt(w); 
		vec[i] = ColocalizationCoefficients.ICQ(C1, rc2[p1], rc2[p2]);
		}
	return vec;
	}

public double[] ICQ()
	{
	double[] v = new double[gImages];
	double[] pv = new double[10];
	v = icqVec(rGreen[halfW], rRed, gImages, nRed);
	pv[0]=returnP(v, coefs[7]);
	v = icqVec(rGreen[halfW], rRed, gImages, nGreen);
	pv[1]=returnP(v, coefs[8]);
	if(!c3.equals("None"))
		{
		v = icqVec(rBlue[halfW], rRed, gImages, nRed);
		pv[2]=returnP(v, coefs[9]);
		v = icqVec(rBlue[halfW], rRed, gImages, nBlue);
		pv[3]=returnP(v, coefs[10]);
		v = icqVec(rBlue[halfW], rGreen, gImages, nGreen);
		pv[4]=returnP(v, coefs[11]);
		v = icqVec(rBlue[halfW], rGreen, gImages, nBlue);
		pv[5]=returnP(v, coefs[12]);
		v = icqVec(rGreen[halfW], rBlue[halfW], rRed, gImages, nRed);
		pv[6]=returnP(v, coefs[13]);
		v = icqVec(rRed[halfW], rBlue[halfW], rGreen, gImages, nGreen);
		pv[7]=returnP(v, coefs[14]);
		v = icqVec(rRed[halfW], rGreen[halfW], rBlue, gImages, nBlue);
		pv[8]=returnP(v, coefs[15]);
		v = icqVec(rRed[halfW], rGreen, rBlue, gImages);
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
		pos=rand.nextInt(w); 				
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
		pos=rand.nextInt(w); 				
		vec[i] = ColocalizationCoefficients.COEFK(C1, C2, rc[pos], den);
		}
	return vec;
	}

public double[] COEFK()
	{
	double[] v = new double[gImages];
	double[] pv = new double[9];
	v = kVec(rGreen[halfW], rRed, gImages, dRRed);
	pv[0]=returnP(v, coefs[17]);
	v = kVec(rGreen[halfW], rRed, gImages, dRGreen);
	pv[1]=returnP(v, coefs[18]);
	if(!c3.equals("None"))
		{
		v = kVec(rBlue[halfW], rRed, gImages, dRRed);
		pv[2]=returnP(v, coefs[19]);
		v = kVec(rBlue[halfW], rRed, gImages, dRBlue);
		pv[3]=returnP(v, coefs[20]);
		v = kVec(rBlue[halfW], rGreen, gImages, dRGreen);
		pv[4]=returnP(v, coefs[21]);
		v = kVec(rBlue[halfW], rGreen, gImages, dRBlue);
		pv[5]=returnP(v, coefs[22]);
		v = kVec(rGreen[halfW], rBlue[halfW], rRed, gImages, dR3Red);
		pv[6]=returnP(v, coefs[23]);
		v = kVec(rRed[halfW], rBlue[halfW], rGreen, gImages, dR3Green);
		pv[7]=returnP(v, coefs[24]);
		v = kVec(rRed[halfW], rGreen[halfW], rBlue, gImages, dR3Blue);
		pv[8]=returnP(v, coefs[25]);
		}
	return pv;
	}

public double[] mVec(int[] C1, int[][] rc, int n, double den)
	{
	double[] vec = new double[n];
	int pos;
	for(int i=0; i<n; i++)
		{
		pos=rand.nextInt(w); 				
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
	pos=rand.nextInt(w); 				
	vec[i] = ColocalizationCoefficients.COEFM(C1, C2, rc[pos], den);
	}
return vec;
}

public double[] COEFM()
	{
	double[] v = new double[gImages];
	double[] pv = new double[9];
	v = mVec(rGreen[halfW], rRed, gImages, dmRed);
	pv[0]=returnP(v, coefs[26]);
	v = mVec(rGreen[halfW], rRed, gImages, dmGreen);
	pv[1]=returnP(v, coefs[27]);
	if(!c3.equals("None"))
		{
		v = mVec(rBlue[halfW], rRed, gImages, dmRed);
		pv[2]=returnP(v, coefs[28]);
		v = mVec(rBlue[halfW], rRed, gImages, dmBlue);
		pv[3]=returnP(v, coefs[29]);
		v = mVec(rBlue[halfW], rGreen, gImages, dmGreen);
		pv[4]=returnP(v, coefs[30]);
		v = mVec(rBlue[halfW], rGreen, gImages, dmBlue);
		pv[5]=returnP(v, coefs[31]);
		v = mVec(rGreen[halfW], rBlue[halfW], rRed, gImages, dmRed);
		pv[6]=returnP(v, coefs[32]);
		v = mVec(rRed[halfW], rBlue[halfW], rGreen, gImages, dmGreen);
		pv[7]=returnP(v, coefs[33]);
		v = mVec(rRed[halfW], rGreen[halfW], rBlue, gImages, dmBlue);
		pv[8]=returnP(v, coefs[34]);
		}
	return pv;
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

public double[] test1() { return  mVec(rGreen[0], rRed, gImages, dmRed); }
public double[] test2() { return  TEST1; }
public double[] test3() { return  Allpvalues(); }

}



