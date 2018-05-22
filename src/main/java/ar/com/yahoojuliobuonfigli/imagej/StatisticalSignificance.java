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
private boolean r, R, i, k, m, scram;
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
					boolean k, boolean m, int seed, int gImages, String level, String channel3, boolean scram)
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
	this.scram=scram;
	rand = new Random(seed);
	if(scram==true)
		stackRed = createShiftingStack2(red);
	else
		stackRed = createShiftingStack(red);
	rRed = new int[gImages][w*h];
	rRed = stack2D(stackRed, red);
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
	if(scram==true)
		stackGreen = createShiftingStack2(green);
	else
		stackGreen = createShiftingStack(green);
	rGreen = new int[gImages][w*h];
	rGreen = stack2D(stackGreen, green);
	//blue = cc.getBlue();
	//stackBlue = createShiftingStack(blue); no enteiendo para que lo puse
 	if(c3.equals("None")) 
		{
		blue=red;
		stackBlue=stackRed;
		rBlue=rRed;
		}
	else
		{
		blue = cc.getBlue();
		if(scram==true)
			stackBlue = createShiftingStack(blue);
		else
			stackBlue = createShiftingStack(blue);
		rBlue = new int[gImages][w*h];
		rBlue = stack2D(stackBlue, blue);
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
	if(z<-3) z=-3;
	if(z>3)  z=3;
	double a, a1, s;   //poner una sentencia para limitar el numero de iteraciones
	final double e=0.00001;
	int n=0; //cont=0;
	s=z;
	a=z;
	do
		{
		a=-a*z*z/(n+2);
		n=n+2;
		a1=a/(n+1);
		s=s+a1;
		//cont++;
		//if(cont>999999999)
			//break;
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
	int cont, ind, val, X, Y, r1, r2;
	float  r3, r4, r5;
	ImageStack st = ImageStack.create(w, h, gImages, 8); 
	for(int z=0; z<gImages; z++)
		{
		cont=0;
		r1=rand.nextInt(w);
		r2=rand.nextInt(h);
		r3=rand.nextFloat();
		r4=rand.nextFloat();
		r5=rand.nextFloat();
		for(int y=0; y<h; y++)
			{
			for(int x=0; x<w; x++)
				{
				if(x+r1<w)
					X=x+r1;
				else
					X=x+r1-w;
				if(y+r2<h)
					Y=y+r2;
				else
					Y=y+r2-h;
				ind=(int)Math.floor(cont/w)*w+cont%w;
		        if(ind>=vec.length) 
		        	val=vec[rand.nextInt(vec.length)];  
		        else 
		        	val=vec[ind];  
		        if(r3<0.50) X=Math.abs(X-(w-1));
		        if(r4<0.50) Y=Math.abs(Y-(h-1));
		        if(r5<0.50)
		        	st.setVoxel(X, Y, z, val);
		        else
		        	st.setVoxel(Y, X, z, val);
		        cont++;
				}
			}
		}
	return st;
	}

public int[] randomVec(int[] v)
	{
	int n=v.length;
	boolean[] u=new boolean[n];                                                               
	for(int i=0; i<n; i++)
		u[i]=true;
	int[] rv=new int[n];
	int hh, e;
	hh=0;	
	while(hh<n)
		{
		e=rand.nextInt(n);
		if(u[e]==true)
			{
			rv[hh]=v[e];
			u[e]=false;
			hh++;
			}
		}
	return rv;
	}	

public ImageStack createShiftingStack2(int[] vec) 
	{
	int cont;
	int[] rv=new int[vec.length];
	ImageStack st = ImageStack.create(w, h, gImages, 8); 
	for(int z=0; z<gImages; z++)
		{
		cont=0;
		rv=randomVec(vec);
		for(int y=0; y<h; y++)
			{
			for(int x=0; x<w; x++)
				{
				if(cont<vec.length)
					st.setVoxel(x, y, z, rv[cont]);
				else
					st.setVoxel(x, y, z, rv[rand.nextInt(rv.length)]);
				cont++;
				}
			}
		}
	return st;
	}

public int[][] stack2D(ImageStack ST, int[] canal)
	{
	int[][] vec = new int[gImages][w*h];
	int i=0;
	for(int y=0; y<h; y++)
		{
		for(int x=0; x<w; x++)
			{
			if(i<canal.length)
				vec[0][i]=canal[i];
			else
				vec[0][i]=canal[rand.nextInt(canal.length)];
			i++;
			}
		}
	for(int z=1; z<gImages; z++)
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

public double[] pearsonVec(int[][] rc1, int[][] rc2, int n, double dC1, double dC2, double pC1, double pC2)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.PEARSON(rc1[i], rc2[i], dC1, dC2, pC1, pC2);
	return vec;
	}

public double[] PEARSON()
	{
	double[] v=new double[gImages];
	double[] pv=new double[3];
	v = pearsonVec(rGreen, rRed, gImages, drGreen, drRed, promGreen, promRed);
	pv[0]=returnP(v, coefs[0]);
	if(!c3.equals("None"))
		{
		v = pearsonVec(rBlue, rRed, gImages, drBlue, drRed, promBlue, promRed);
		pv[1]=returnP(v, coefs[1]);
		v = pearsonVec(rBlue, rGreen, gImages, drBlue, drGreen, promBlue, promGreen);
		pv[2]=returnP(v, coefs[2]);
		}
	return pv;
	}

public double[] overlapVec(int[][] rc1, int[][] rc2, int n, double dC1, double dC2)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.OVERLAP(rc1[i], rc2[i], dC1, dC2);
	return vec;
	}

public double[] overlapVec(int[] C1, int[] C2, int[][] rc, int n)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.OVERLAP(C1, C2, rc[i]);
	return vec;
	}

public double[] overlapVec(int[][] rc1, int[][] rc2, int[][] rc3, int n)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.OVERLAP(rc1[i], rc2[i], rc3[i]);
	return vec;
	}

public double[] OVERLAP()
	{
	double[] v = new double[gImages];
	double[] pv = new double[7];
	v=overlapVec(rGreen, rRed, gImages, dRGreen, dRRed);
	pv[0]=returnP(v, coefs[3]);
	if(!c3.equals("None"))
		{
		v = overlapVec(rRed, rBlue, gImages, dRRed, dRBlue);
		pv[1]=returnP(v, coefs[4]);
		v = overlapVec(rBlue, rGreen, gImages, dRBlue, dRGreen);
		pv[2]=returnP(v, coefs[5]);
		v = overlapVec(rBlue[0], rGreen[0], rRed, gImages);
		pv[3]=returnP(v, coefs[6]);
		v = overlapVec(rBlue[0], rRed[0], rGreen, gImages);
		pv[4]=returnP(v, coefs[6]);
		v = overlapVec(rRed[0], rGreen[0], rBlue, gImages);
		pv[5]=returnP(v, coefs[6]);
		v = overlapVec(rRed, rGreen, rBlue, gImages);
		pv[6]=returnP(v, coefs[6]);
		}
	return pv;
	}

public double[] icqVec(int[][] rc1, int[][] rc2, int n, int N)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.ICQ(rc1[i], rc2[i], N);
	return vec;
	}

public double[] icqVec(int[] C1, int[] C2, int[][] rc, int n, int N)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.ICQ(C1, C2, rc[i], N);
	return vec;
	}

public double[] icqVec(int[][] rc1, int[][] rc2, int[][] rc3, int n)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.ICQ(rc1[i], rc2[i], rc3[i]);
	return vec;
	}

public double[] ICQ()
	{
	double[] v = new double[gImages];
	double[] pv = new double[10];
	v = icqVec(rGreen, rRed, gImages, nRed);
	pv[0]=returnP(v, coefs[7]);
	v = icqVec(rGreen, rRed, gImages, nGreen);
	pv[1]=returnP(v, coefs[8]);
	if(!c3.equals("None"))
		{
		v = icqVec(rBlue, rRed, gImages, nRed);
		pv[2]=returnP(v, coefs[9]);
		v = icqVec(rBlue, rRed, gImages, nBlue);
		pv[3]=returnP(v, coefs[10]);
		v = icqVec(rBlue, rGreen, gImages, nGreen);
		pv[4]=returnP(v, coefs[11]);
		v = icqVec(rBlue, rGreen, gImages, nBlue);
		pv[5]=returnP(v, coefs[12]);
		v = icqVec(rGreen[0], rBlue[0], rRed, gImages, nRed);
		pv[6]=returnP(v, coefs[13]);
		v = icqVec(rRed[0], rBlue[0], rGreen, gImages, nGreen);
		pv[7]=returnP(v, coefs[14]);
		v = icqVec(rRed[0], rGreen[0], rBlue, gImages, nBlue);
		pv[8]=returnP(v, coefs[15]);
		v = icqVec(rRed, rGreen, rBlue, gImages);
		pv[9]=returnP(v, coefs[16]);
		}
	return pv;
	}

public double[] kVec(int[][] rc1, int[][] rc2, int n, double den)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.COEFK(rc1[i], rc2[i], den);
	return vec;
	}

public double[] kVec(int[] C1, int[] C2, int[][] rc, int n, double den)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.COEFK(C1, C2, rc[i], den);
	return vec;
	}

public double[] COEFK()
	{
	double[] v = new double[gImages];
	double[] pv = new double[9];
	v = kVec(rGreen, rRed, gImages, dRRed);
	pv[0]=returnP(v, coefs[17]);
	v = kVec(rGreen, rRed, gImages, dRGreen);
	pv[1]=returnP(v, coefs[18]);
	if(!c3.equals("None"))
		{
		v = kVec(rBlue, rRed, gImages, dRRed);
		pv[2]=returnP(v, coefs[19]);
		v = kVec(rBlue, rRed, gImages, dRBlue);
		pv[3]=returnP(v, coefs[20]);
		v = kVec(rBlue, rGreen, gImages, dRGreen);
		pv[4]=returnP(v, coefs[21]);
		v = kVec(rBlue, rGreen, gImages, dRBlue);
		pv[5]=returnP(v, coefs[22]);
		v = kVec(rGreen[0], rBlue[0], rRed, gImages, dR3Red);
		pv[6]=returnP(v, coefs[23]);
		v = kVec(rRed[0], rBlue[0], rGreen, gImages, dR3Green);
		pv[7]=returnP(v, coefs[24]);
		v = kVec(rRed[0], rGreen[0], rBlue, gImages, dR3Blue);
		pv[8]=returnP(v, coefs[25]);
		}
	return pv;
	}

public double[] mVec(int[][] rc1, int[][] rc2, int n, double den)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.COEFM(rc1[i], rc2[i], den);
	return vec;
	}

public double[] mVec(int[] C1, int[] C2, int[][] rc, int n, double den)
	{
	double[] vec = new double[n];
	for(int i=0; i<n; i++)
		vec[i] = ColocalizationCoefficients.COEFM(C1, C2, rc[i], den);
	return vec;
	}

public double[] COEFM()
	{
	double[] v = new double[gImages];
	double[] pv = new double[9];
	v = mVec(rGreen, rRed, gImages, dmRed);
	pv[0]=returnP(v, coefs[26]);
	v = mVec(rGreen, rRed, gImages, dmGreen);
	pv[1]=returnP(v, coefs[27]);
	if(!c3.equals("None"))
		{
		v = mVec(rBlue, rRed, gImages, dmRed);
		pv[2]=returnP(v, coefs[28]);
		v = mVec(rBlue, rRed, gImages, dmBlue);
		pv[3]=returnP(v, coefs[29]);
		v = mVec(rBlue, rGreen, gImages, dmGreen);
		pv[4]=returnP(v, coefs[30]);
		v = mVec(rBlue, rGreen, gImages, dmBlue);
		pv[5]=returnP(v, coefs[31]);
		v = mVec(rGreen[0], rBlue[0], rRed, gImages, dmRed);
		pv[6]=returnP(v, coefs[32]);
		v = mVec(rRed[0], rBlue[0], rGreen, gImages, dmGreen);
		pv[7]=returnP(v, coefs[33]);
		v = mVec(rRed[0], rGreen[0], rBlue, gImages, dmBlue);
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

public double[] test1() { return  mVec(rGreen, rRed, gImages, dmRed); }
public double[] test2() { return  TEST1; }
public double[] test3() { return  Allpvalues(); }
public double test() { return ColocalizationCoefficients.OVERLAP(rRed[0], rGreen[0], dRGreen, dRRed);}


}


/*
public ImageStack createShiftingStack(int[] vec) 
	{
	int cont, var, pp, val, k;
	ImageStack st = ImageStack.create(w, h, w+h+2, 8); 
	k=0; 
	for(int z=0; z<w+h+2; z++)
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
		        if(pp>=vec.length) 
		        	val=rand.nextInt(256);  
		        else 
		        	val=vec[pp];  
		        if(z%2==0)
		        	st.setVoxel(var, y, z, val);
				else
					st.setVoxel(y, var, z, val); 
				cont++;
				}
			}
		if(k>w)
			k=0;
		k++;
		}
	return st;
	}*/
