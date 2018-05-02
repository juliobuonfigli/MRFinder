package ar.com.yahoojuliobuonfigli.imagej;

import ij.plugin.PlugIn;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class ColocalizationCoefficients {

private int[] red, green, blue;
private double drRed, drGreen, drBlue, promRed, promGreen, promBlue, dRRed, dRGreen, dRBlue, 
        dR3Red, dR3Green, dR3Blue, dmRed, dmGreen, dmBlue;
private int nRed, nGreen, nBlue;
private String channel3;
	
public ColocalizationCoefficients(int[] red, int[] green, int[] blue, String channel3) 
	{
	this.channel3=channel3;
	this.red=red;
	nRed=number(red);
	promRed=prom(red);
	drRed=dr(red, promRed);
	dRRed=dR(red);
	dR3Red=dR3(red);
	dmRed=dm(red);
	this.green=green;
	nGreen=number(green);
	promGreen=prom(green);
	drGreen=dr(green, promGreen);
	dRGreen=dR(green);
	dR3Green=dR3(green);
	dmGreen=dm(green);
	//if(channel3.equals("None")) {} else {
	this.blue=blue; 
	nBlue=number(blue);
	promBlue=prom(blue);
	drBlue=dr(blue, promBlue);
	dRBlue=dR(blue);
	dR3Blue=dR3(blue);
	dmBlue=dm(blue); 
	}

static int number(int[] vector) 
	{
	int num=0;
	for(int i=0; i<vector.length; i++)
		{
		if(vector[i]>0)
			num++;
		}
	return num;
	}

static double prom(int[] vector) 
	{
	double PROM=0.0;	
	for(int i=0; i<vector.length; i++)
		PROM=PROM+vector[i];
	return PROM/vector.length;
	}

static double dr(int[] vector, double pr) 
	{
	double den=0.0;	
	for(int i=0; i<vector.length; i++)
		den=den+(vector[i]-pr)*(vector[i]-pr);
	return den;
	}

static double dR(int[] vector) 
	{
	double den=0.0;	
	for(int i=0; i<vector.length; i++)
		den=den+vector[i]*vector[i];
	return den;
	}

static double dR3(int[] vector) 
{
double den=0.0;	
for(int i=0; i<vector.length; i++)
	den=den+vector[i]*vector[i]*vector[i];
return den;
}

static double dm(int[] vector)
	{
	double den=0.0;	
	for(int i=0; i<vector.length; i++)
		den=den+vector[i];
	return den;
	}
//COEFICIENTES
static double PEARSON(int[] c1, int[] c2, double denC1, double denC2, double C1pr, double C2pr)
	{ 
	double coef;
	double num=0;
	for(int i=0; i<c1.length; i++)
		num=num+(c1[i]-C1pr)*(c2[i]-C2pr);
	coef=num/Math.sqrt(denC1*denC2);
	return coef;
	}

static double OVERLAP(int[] c1, int[] c2, double denC1, double denC2)
	{ 
	double coef;
	double num=0;
	for(int i=0; i<c1.length; i++)
		num=num+c1[i]*c2[i];
	coef=num/Math.sqrt(denC1*denC2);
	return coef;
	}

static double OVERLAP(int[] c1, int[] c2, int[] c3)
	{ 
	double coef=0;
	double denC1=0;
	double denC2=0;
	double denC3=0;
	double num=0;
	for(int i=0; i<c1.length; i++)
		{
		denC1=denC1+c1[i]*c1[i]*c1[i];                                                                                                           
		denC2=denC2+c2[i]*c2[i]*c2[i];
		denC3=denC3+c3[i]*c3[i]*c3[i];  
		num=num+c1[i]*c2[i]*c3[i];
		}
	coef=num/Math.cbrt(denC1*denC2*denC3);
	return coef;
	}

static double ICQ(int[] c1, int[] c2, int n)
	{ 
	double N=(double)n;
	double num=0;
	double coef;
	for(int i=0; i<c1.length; i++)
		{
		if(c1[i]>0 && c2[i]>0)
			num++;
		}
	coef=100*num/N;
	return coef;
	}

static double ICQ(int[] c1, int[] c2, int[] c3, int n)
	{ 
	double N=(double)n;
	double num=0;
	double coef;
	for(int i=0; i<c1.length; i++)
		{
		if(c1[i]>0 && c2[i]>0 && c3[i]>0)
			num++;
		}
	coef=100*num/N;
	return coef;
	}

static double ICQ(int[] c1, int[] c2, int[] c3)
	{ 
	double num=0;
	double n=0;
	double coef;
	for(int i=0; i<c1.length; i++)
		{
		if(c1[i]>0 && c2[i]>0 && c3[i]>0)
			num++;
		if(c1[i]>0 || c2[i]>0 || c3[i]>0)
			n++;
		}
	coef=100*num/n;
	return coef;
	}

static double COEFK(int[] c1, int[] c2, double den)
	{ 
	double coef=0;
	for(int i=0; i<c1.length; i++)
		coef=coef+c1[i]*c2[i];
	return coef/den;
	}

static double COEFK(int[] c1, int[] c2, int[] c3, double den)
	{ 
	double coef=0;
	for(int i=0; i<c1.length; i++)
			coef=coef+c1[i]*c2[i]*c3[i];
	return coef/den;
	}

static double COEFM(int[] c1, int[] c2, double den)
	{ 
	double coef=0.0;
	for(int i=0; i<c1.length; i++)
		{
		if(c1[i]>0 && c2[i]>0)
			coef=coef+c1[i];
		}
	return coef/den;
	}

static double COEFM(int[] c1, int[] c2, int[] c3, double den)
	{ 
	double coef=0;
	for(int i=0; i<c1.length; i++)
		{
		if(c1[i]>0 && c2[i]>0 && c3[i]>0)
			coef=coef+c1[i];
		}
	return coef/den;
	}
//VECTORES
public double[] Pearson() 
	{ 
	double[] coef = new double[3];	
	coef[0]=PEARSON(red, green, drRed, drGreen, promRed, promGreen);
	if(channel3.equals("None")) 
		{} 
	else 
		{
		coef[1]=PEARSON(red, blue, drRed, drBlue, promRed, promBlue);
		coef[2]=PEARSON(green, blue, drGreen, drBlue, promGreen, promBlue);
		}
	return coef;
	}

public double[] Overlap()
	{ 
	double[] coef=new double[4];
	coef[0]=OVERLAP(red, green, dRRed, dRGreen);
	if(channel3.equals("None")) 
		{}
	else
		{
		coef[1]=OVERLAP(red, blue, dRRed, dRBlue);
		coef[2]=OVERLAP(green, blue, dRGreen, dRBlue);
		coef[3]=OVERLAP(red, green, blue);
		}
	return coef;
	}

public double[] icq()
	{ 
	double[] coef=new double[10];
	coef[0]= ICQ(red, green, nRed);
	coef[1]= ICQ(red, green, nGreen);
	if(channel3.equals("None"))
		{}
	else
		{
		coef[2]= ICQ(red, blue, nRed);
		coef[3]= ICQ(red, blue, nBlue);
		coef[4]= ICQ(green, blue, nGreen);
		coef[5]= ICQ(green, blue, nBlue);
		coef[6]= ICQ(red, green, blue, nRed);
		coef[7]= ICQ(red, green, blue, nGreen);
		coef[8]= ICQ(red, green, blue, nBlue);
		coef[9]= ICQ(red, green, blue);
		}
	return coef;
	}

public double[] coefK()
	{ 
	double[] coef=new double[9];
	coef[0]= COEFK(red, green, dRRed);
	coef[1]= COEFK(red, green, dRGreen);
	if(channel3.equals("None")) 
		{}
	else
		{
		coef[2]= COEFK(red, blue, dRRed);
		coef[3]= COEFK(red, blue, dRBlue);
		coef[4]= COEFK(green, blue, dRGreen);
		coef[5]= COEFK(green, blue, dRBlue);
		coef[6]= COEFK(red, green, blue, dR3Red);
		coef[7]= COEFK(red, green, blue, dR3Green);
		coef[8]= COEFK(red, green, blue, dR3Blue);
		}
	return coef;
	}

public double[] coefM()
	{ 
	double[] coef=new double[9];
	coef[0]= COEFM(red, green, dmRed);
	coef[1]= COEFM(green, red, dmGreen);
	if(blue.equals(null)) 
		{}
	else
		{
		coef[2]= COEFM(red, blue, dmRed);
		coef[3]= COEFM(blue, red, dmBlue);
		coef[4]= COEFM(green, blue, dmGreen);
		coef[5]= COEFM(blue, green, dmBlue);
		coef[6]= COEFM(red, green, blue, dmRed);
		coef[7]= COEFM(red, green, blue, dmGreen);
		coef[8]= COEFM(red, green, blue, dmBlue);
		}
	return coef;
	}

public double[] AllCoef() 
	{
	double[] coef=new double[35];
	double[] Pearson=new double[3];
	double[] Overlap=new double[4];
	double[] icq=new double[10];
	double[] coefK=new double[9];
	double[] coefM=new double[9];
	Pearson=Pearson();
	Overlap=Overlap();
	icq=icq();
	coefK=coefK();
	coefM=coefM();
		int i=0;
		for(int j=0; j<3; j++)
			{coef[i]=Pearson[j]; i++;}
		for(int j=0; j<4; j++)
			{coef[i]=Overlap[j]; i++;}
		for(int j=0; j<10; j++)
			{coef[i]=icq[j]; i++;}
		for(int j=0; j<9; j++)
			{coef[i]=coefK[j]; i++;}
		for(int j=0; j<9; j++)
			{coef[i]=coefM[j]; i++;}
	return coef;
	}
//getters
public int[] getRed() 
	{
	return red;
	}

public int[] getGreen() 
	{
	return green;
	}

public int[] getBlue() 
	{
	return blue;
	}

}




