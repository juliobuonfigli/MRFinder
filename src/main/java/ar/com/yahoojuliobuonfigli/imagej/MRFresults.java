package ar.com.yahoojuliobuonfigli.imagej;

import java.text.DecimalFormat;
import ij.measure.ResultsTable;

public class MRFresults {

	private double[] coefs, pvalues;
	private ResultsTable rt; 	
	private boolean r, R, i, k, m;
	private String sl, c3;
	private double SL;
    private String[] sig, TL;
    private static DecimalFormat df2;
    
public MRFresults(ColocalizationCoefficients c, StatisticalSignificance s, boolean r, boolean R, 
		boolean i, boolean k, boolean m, String sl, String c3) 
	{
	df2 = new DecimalFormat("0.000E0");
	this.sl=sl;
	this.c3=c3;
	this.r=r; 
	this.R=R;
	this.i=i;
	this.k=k;
	this.m=m;
	if(sl.equals("0.01"))
		SL=0.01;
	else
		SL=0.05;
	coefs=c.AllCoef();
	pvalues=s.Allpvalues();
	sig=new String[38];
	for(int j=0; j<38; j++)
		sig[j]=Significance(SL, pvalues[j]);
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
			if(p==0)
				r="??: ";
			else
				r="SC: ";
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
	if(r) rt.addValue("r/p", sig[0]+df2.format(pvalues[0]));
	rt.addValue("Overlap", coefs[3]);
	if(R) rt.addValue("R/p", sig[3]+df2.format(pvalues[3]));
	rt.addValue("ICQ", coefs[7]);
	if(i) rt.addValue("i/p", sig[10]+df2.format(pvalues[10]));
	rt.addValue("Manders K", coefs[17]);
	if(k) rt.addValue("k/p", sig[20]+df2.format(pvalues[20]));
	rt.addValue("Manders M", coefs[26]);
	if(m) rt.addValue("M/p", sig[29]+df2.format(pvalues[29]));
	//if(!c3.equals("None")) rt.addValue("Triple Relationship", "  ");
	rt.incrementCounter();
	rt.addValue("    ", "Green/red");
	rt.addValue("Pearson", "  ");
	if(r) rt.addValue("r/p", "    ");
	rt.addValue("Overlap", "    ");
	if(R) rt.addValue("R/p", "    ");
	rt.addValue("ICQ", coefs[8]);
	if(i) rt.addValue("i/p", sig[11]+df2.format(pvalues[11]));
	rt.addValue("Manders K", coefs[18]);
	if(k) rt.addValue("k/p", sig[21]+df2.format(pvalues[21]));
	rt.addValue("Manders M", coefs[27]);
	if(m) rt.addValue("M/p", sig[30]+df2.format(pvalues[30]));
	//if(!c3.equals("None")) rt.addValue("Triple Relationship", "  ");
	if(!c3.equals("None")) 
		{
		rt.incrementCounter();
		rt.addValue("    ", "Red/blue");
		rt.addValue("Pearson", coefs[1]);
		if(r) rt.addValue("r/p", sig[1]+df2.format(pvalues[1]));
		rt.addValue("Overlap", coefs[4]);
		if(R) rt.addValue("R/p", sig[4]+df2.format(pvalues[4]));
		rt.addValue("ICQ", coefs[9]);
		if(i) rt.addValue("i/p", sig[12]+df2.format(pvalues[12]));
		rt.addValue("Manders K", coefs[19]);
		if(k) rt.addValue("k/p", sig[22]+df2.format(pvalues[22]));
		rt.addValue("Manders M", coefs[28]);
		if(m) rt.addValue("M/p", sig[31]+df2.format(pvalues[31]));
		//rt.addValue("Triple Relationship", "  ");
		rt.incrementCounter();
		rt.addValue("    ", "Blue/red");
		rt.addValue("Pearson", "  ");
		if(r) rt.addValue("r/p", "    ");
		rt.addValue("Overlap", "    ");
		if(R) rt.addValue("R/p", "    ");
		rt.addValue("ICQ", coefs[10]);
		if(i) rt.addValue("i/p", sig[13]+df2.format(pvalues[13]));
		rt.addValue("Manders K", coefs[20]);
		if(k) rt.addValue("k/p", sig[23]+df2.format(pvalues[23]));
		rt.addValue("Manders M", coefs[29]);
		if(m) rt.addValue("M/p", sig[32]+df2.format(pvalues[32]));
		//rt.addValue("Triple Relationship", "  ");
		rt.incrementCounter();
		rt.addValue("    ", "Green/blue");
		rt.addValue("Pearson", coefs[2]);
		if(r) rt.addValue("r/p", sig[2]+df2.format(pvalues[2]));
		rt.addValue("Overlap", coefs[5]);
		if(R) rt.addValue("R/p", sig[5]+df2.format(pvalues[5]));
		rt.addValue("ICQ", coefs[11]);
		if(i) rt.addValue("i/p", sig[14]+df2.format(pvalues[14]));
		rt.addValue("Manders K", coefs[21]);
		if(k) rt.addValue("k/p", sig[24]+df2.format(pvalues[24]));
		rt.addValue("Manders M", coefs[30]);
		if(m) rt.addValue("M/p", sig[33]+df2.format(pvalues[33]));
		//rt.addValue("Triple Relationship", "  ");
		rt.incrementCounter();
		rt.addValue("    ", "Blue/green");
		rt.addValue("Pearson", "  ");
		if(r) rt.addValue("r/p", "    ");
		rt.addValue("Overlap", "    ");
		if(R) rt.addValue("R/p", "    ");
		rt.addValue("ICQ", coefs[12]);
		if(i) rt.addValue("i/p", sig[15]+df2.format(pvalues[15]));
		rt.addValue("Manders K", coefs[22]);
		if(k) rt.addValue("k/p", sig[25]+df2.format(pvalues[25]));
		rt.addValue("Manders M", coefs[31]);
		if(m) rt.addValue("M/p", sig[34]+df2.format(pvalues[34]));
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
		if(R) rt.addValue("R/p", sig[6]+df2.format(pvalues[6]));
		rt.addValue("ICQ", coefs[13]);
		if(i) rt.addValue("i/p", sig[16]+df2.format(pvalues[16]));
		rt.addValue("Manders K", coefs[23]);
		if(k) rt.addValue("k/p", sig[26]+df2.format(pvalues[26]));
		rt.addValue("Manders M", coefs[32]);
		if(m) rt.addValue("M/p", sig[35]+df2.format(pvalues[35]));
		//rt.addValue("Triple Relationship", TL[0]);
		rt.incrementCounter();
		rt.addValue("    ", "Green");
		rt.addValue("Pearson", " ");
		if(r) rt.addValue("r/p", " ");
		rt.addValue("Overlap", coefs[6]);
		if(R) rt.addValue("R/p", sig[7]+df2.format(pvalues[7]));
		rt.addValue("ICQ", coefs[14]);
		if(i) rt.addValue("i/p", sig[17]+df2.format(pvalues[17]));
		rt.addValue("Manders K", coefs[24]);
		if(k) rt.addValue("k/p", sig[27]+df2.format(pvalues[27]));
		rt.addValue("Manders M", coefs[33]);
		if(m) rt.addValue("M/p", sig[36]+df2.format(pvalues[36]));
		//rt.addValue("Triple Relationship", TL[1]);
		rt.incrementCounter();
		rt.addValue("    ", "Blue");
		rt.addValue("Pearson", " ");
		if(r) rt.addValue("r/p", " ");
		rt.addValue("Overlap", coefs[6]);
		if(R) rt.addValue("R/p", sig[8]+df2.format(pvalues[8]));
		rt.addValue("ICQ", coefs[15]);
		if(i) rt.addValue("i/p", sig[18]+df2.format(pvalues[18]));
		rt.addValue("Manders K", coefs[25]);
		if(k) rt.addValue("k/p", sig[28]+df2.format(pvalues[28]));
		rt.addValue("Manders M", coefs[34]);
		if(m) rt.addValue("M/p", sig[37]+df2.format(pvalues[37]));
		//rt.addValue("Triple Relationship", TL[2]);
		rt.incrementCounter();
		rt.addValue("    ", "General");
		rt.addValue("Pearson", " ");
		if(r) rt.addValue("r/p", " ");
		rt.addValue("Overlap", coefs[6]);
		if(R) rt.addValue("R/p", sig[9]+df2.format(pvalues[9]));
		rt.addValue("ICQ", coefs[16]);
		if(i) rt.addValue("i/p", sig[19]+df2.format(pvalues[19]));
		rt.addValue("Manders K", " ");
		if(k) rt.addValue("k/p", " ");
		rt.addValue("Manders M", " ");
		if(m) rt.addValue("M/p", " "); 
		//rt.addValue("Triple Relationship", "  ");
		}
	rt.showRowNumbers(false);
	}

public void showRT() { rt.show("Results"); }
}

