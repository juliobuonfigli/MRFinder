package ar.com.yahoojuliobuonfigli.imagej;

import java.text.DecimalFormat;
import ij.measure.ResultsTable;

public class MRFresults {

	private double[] coefs, Pvalues;
	private ResultsTable rt; 	
	private boolean r, R, i, k, m;
	private String c3;
	//private String sl, c3;
	private double SL;
    private String[] sig, TL;
    private static DecimalFormat df2;
    private String[] pvalues;
    
public MRFresults(ColocalizationCoefficients c, StatisticalSignificance s, boolean r, boolean R, 
		boolean i, boolean k, boolean m, String sl, String c3) 
	{
	
	df2 = new DecimalFormat("0.000E0");
	//this.sl=sl;
	this.c3=c3;
	this.r=r; 
	this.R=R;
	this.i=i;
	this.k=k;
	this.m=m;
	if(sl.equals("0.01"))
		SL=0.010;
	else
		SL=0.050;
	coefs=c.AllCoef();
	Pvalues=s.Allpvalues();
	pvalues=new String[38];
	for(int x=0; x<38; x++)
		pvalues[x]=df2.format(Pvalues[x]);
	sig=new String[38];
	for(int j=0; j<38; j++)
		sig[j]=Significance(SL, Pvalues[j]);
	TL=Arrow(coefs);
	createRT();
	}

public String Significance(double s, double p)
	{
	String r;
	if(Math.abs(p)>s)
		r="NS: ";
	else
		{
		if(p<0)
			r="SE: ";
		else
			{
			if(p>0)
				r="SC: ";
			else
				r="NR: ";
			}
		}
	return r;
	}
		
public String[] Arrow(double[] c)
	{
	String[] r = new String[3];
	if(c[3]>c[4] && c[3]>c[5])
		{
		r[0]=" "; r[1]=" "; r[2]="<-";
		}
	if(c[4]>c[3] && c[4]>c[5])
		{
		r[0]=" "; r[1]="<-"; r[2]=" ";
		}
	if(c[5]>c[4] && c[5]>c[3])
		{
		r[0]="<-"; r[1]=" "; r[2]=" ";
		}
	if(c[3]==c[4] || c[3]==c[5] || c[4]==c[5])
		{
		r[0]=" "; r[1]=" "; r[2]=" ";
		}
	return r;
	}

public void createRT()
	{
	rt=new ResultsTable();
	rt.incrementCounter();
	rt.addValue("    ", "Red/green");
	rt.addValue("Pearson", coefs[0]);
	if(r) rt.addValue("r/p", sig[0]+pvalues[0]);
	rt.addValue("Overlap", coefs[3]);
	if(R) rt.addValue("R/p", sig[3]+pvalues[3]);
	rt.addValue("ICQ", coefs[7]);
	if(i) rt.addValue("i/p", sig[10]+pvalues[10]);
	rt.addValue("Manders K", coefs[17]);
	if(k) rt.addValue("k/p", sig[20]+pvalues[20]);
	rt.addValue("Manders M", coefs[26]);
	if(m) rt.addValue("M/p", sig[29]+pvalues[29]);
	//if(!c3.equals("None")) rt.addValue("Triple Relationship", "  ");
	rt.incrementCounter();
	rt.addValue("    ", "Green/red");
	rt.addValue("Pearson", "  ");
	if(r) rt.addValue("r/p", "    ");
	rt.addValue("Overlap", "    ");
	if(R) rt.addValue("R/p", "    ");
	rt.addValue("ICQ", coefs[8]);
	if(i) rt.addValue("i/p", sig[11]+pvalues[11]);
	rt.addValue("Manders K", coefs[18]);
	if(k) rt.addValue("k/p", sig[21]+pvalues[21]);
	rt.addValue("Manders M", coefs[27]);
	if(m) rt.addValue("M/p", sig[30]+pvalues[30]);
	//if(!c3.equals("None")) rt.addValue("Triple Relationship", "  ");
	if(!c3.equals("None")) 
		{
		rt.incrementCounter();
		rt.addValue("    ", "Red/blue");
		rt.addValue("Pearson", coefs[1]);
		if(r) rt.addValue("r/p", sig[1]+pvalues[1]);
		rt.addValue("Overlap", coefs[4]);
		if(R) rt.addValue("R/p", sig[4]+pvalues[4]);
		rt.addValue("ICQ", coefs[9]);
		if(i) rt.addValue("i/p", sig[12]+pvalues[12]);
		rt.addValue("Manders K", coefs[19]);
		if(k) rt.addValue("k/p", sig[22]+pvalues[22]);
		rt.addValue("Manders M", coefs[28]);
		if(m) rt.addValue("M/p", sig[31]+pvalues[31]);
		//rt.addValue("Triple Relationship", "  ");
		rt.incrementCounter();
		rt.addValue("    ", "Blue/red");
		rt.addValue("Pearson", "  ");
		if(r) rt.addValue("r/p", "    ");
		rt.addValue("Overlap", "    ");
		if(R) rt.addValue("R/p", "    ");
		rt.addValue("ICQ", coefs[10]);
		if(i) rt.addValue("i/p", sig[13]+pvalues[13]);
		rt.addValue("Manders K", coefs[20]);
		if(k) rt.addValue("k/p", sig[23]+pvalues[23]);
		rt.addValue("Manders M", coefs[29]);
		if(m) rt.addValue("M/p", sig[32]+pvalues[32]);
		//rt.addValue("Triple Relationship", "  ");
		rt.incrementCounter();
		rt.addValue("    ", "Green/blue");
		rt.addValue("Pearson", coefs[2]);
		if(r) rt.addValue("r/p", sig[2]+pvalues[2]);
		rt.addValue("Overlap", coefs[5]);
		if(R) rt.addValue("R/p", sig[5]+pvalues[5]);
		rt.addValue("ICQ", coefs[11]);
		if(i) rt.addValue("i/p", sig[14]+pvalues[14]);
		rt.addValue("Manders K", coefs[21]);
		if(k) rt.addValue("k/p", sig[24]+pvalues[24]);
		rt.addValue("Manders M", coefs[30]);
		if(m) rt.addValue("M/p", sig[33]+pvalues[33]);
		//rt.addValue("Triple Relationship", "  ");
		rt.incrementCounter();
		rt.addValue("    ", "Blue/green");
		rt.addValue("Pearson", "  ");
		if(r) rt.addValue("r/p", "    ");
		rt.addValue("Overlap", "    ");
		if(R) rt.addValue("R/p", "    ");
		rt.addValue("ICQ", coefs[12]);
		if(i) rt.addValue("i/p", sig[15]+pvalues[15]);
		rt.addValue("Manders K", coefs[22]);
		if(k) rt.addValue("k/p", sig[25]+pvalues[25]);
		rt.addValue("Manders M", coefs[31]);
		if(m) rt.addValue("M/p", sig[34]+pvalues[34]);
		//rt.addValue("Triple Relationship", "  ");
		rt.incrementCounter();
		rt.addValue("    ", "*******");
		rt.addValue("Pearson", "*******");
		if(r) rt.addValue("r/p", "*******");
		rt.addValue("Overlap", "*******");
		if(R) rt.addValue("R/p", "*******");
		rt.addValue("ICQ", "*******");
		if(i) rt.addValue("i/p","*******");
		rt.addValue("Manders K", "*******");
		if(k) rt.addValue("k/p", "*******");
		rt.addValue("Manders M", "*******");
		if(m) rt.addValue("M/p", "*******");
		//rt.addValue("Triple Relationship", "     ");
		rt.incrementCounter();
		rt.addValue("    ", "Red");
		rt.addValue("Pearson", " ");
		if(r) rt.addValue("r/p", " ");
		rt.addValue("Overlap", coefs[6]);
		if(R) rt.addValue("R/p", sig[6]+pvalues[6]);
		rt.addValue("ICQ", coefs[13]);
		if(i) rt.addValue("i/p", sig[16]+pvalues[16]);
		rt.addValue("Manders K", coefs[23]);
		if(k) rt.addValue("k/p", sig[26]+pvalues[26]);
		rt.addValue("Manders M", coefs[32]);
		if(m) rt.addValue("M/p", sig[35]+pvalues[35]);
		//rt.addValue("Triple Relationship", TL[0]);
		rt.incrementCounter();
		rt.addValue("    ", "Green");
		rt.addValue("Pearson", " ");
		if(r) rt.addValue("r/p", " ");
		rt.addValue("Overlap", coefs[6]);
		if(R) rt.addValue("R/p", sig[7]+pvalues[7]);
		rt.addValue("ICQ", coefs[14]);
		if(i) rt.addValue("i/p", sig[17]+pvalues[17]);
		rt.addValue("Manders K", coefs[24]);
		if(k) rt.addValue("k/p", sig[27]+pvalues[27]);
		rt.addValue("Manders M", coefs[33]);
		if(m) rt.addValue("M/p", sig[36]+pvalues[36]);
		//rt.addValue("Triple Relationship", TL[1]);
		rt.incrementCounter();
		rt.addValue("    ", "Blue");
		rt.addValue("Pearson", " ");
		if(r) rt.addValue("r/p", " ");
		rt.addValue("Overlap", coefs[6]);
		if(R) rt.addValue("R/p", sig[8]+pvalues[8]);
		rt.addValue("ICQ", coefs[15]);
		if(i) rt.addValue("i/p", sig[18]+pvalues[18]);
		rt.addValue("Manders K", coefs[25]);
		if(k) rt.addValue("k/p", sig[28]+pvalues[28]);
		rt.addValue("Manders M", coefs[34]);
		if(m) rt.addValue("M/p", sig[37]+pvalues[37]);
		//rt.addValue("Triple Relationship", TL[2]);
		rt.incrementCounter();
		rt.addValue("    ", "General");
		rt.addValue("Pearson", " ");
		if(r) rt.addValue("r/p", " ");
		rt.addValue("Overlap", coefs[6]);
		if(R) rt.addValue("R/p", sig[9]+pvalues[9]);
		rt.addValue("ICQ", coefs[16]);
		if(i) rt.addValue("i/p", sig[19]+pvalues[19]);
		rt.addValue("Manders K", " ");
		if(k) rt.addValue("k/p", " ");
		rt.addValue("Manders M", " ");
		if(m) rt.addValue("M/p", " "); 
		//rt.addValue("Triple Relationship", "  ");
		}
	rt.showRowNumbers(false);
	}

public String[] test1() {return pvalues;}
public double[] test2() {return Pvalues;}
public void showRT() { rt.show("Results"); }
}

