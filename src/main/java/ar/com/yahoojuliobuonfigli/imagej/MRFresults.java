package ar.com.yahoojuliobuonfigli.imagej;

import ij.measure.ResultsTable;
import ij.process.ImageProcessor;

public class MRFresults {

	public double[] coefs, pvalues;
	public boolean[] sig;
    public ResultsTable rt; 	
	public boolean r, R, i, k, m;
	public double[] coe;
	
public MRFresults(ColocalizationCoefficients c, StatisticalSignificance s, boolean r, boolean R, boolean i, boolean k, boolean m) 
	{
	this.r=r; 
	this.R=R;
	this.i=i;
	this.k=k;
	this.m=m;
	coefs=c.AllCoef();
	pvalues=s.Allpvalues();
	createRT();
	}

public void createRT()
	{
	rt=new ResultsTable();
	rt.incrementCounter();
	rt.addValue("    ", "Red/green");
	rt.addValue("Pearson", coefs[0]);
	rt.addValue("r/p", pvalues[0]);
	rt.addValue("Overlap", coefs[3]);
	rt.addValue("R/p", pvalues[3]);
	rt.addValue("ICQ", coefs[7]);
	rt.addValue("i/p", pvalues[10]);
	rt.addValue("Manders K", coefs[17]);
	rt.addValue("k/p", pvalues[20]);
	rt.addValue("Manders M", coefs[26]);
	rt.addValue("M/p", pvalues[29]);
	rt.incrementCounter();
	rt.addValue("    ", "Green/red");
	rt.addValue("Pearson", "  ");
	rt.addValue("r/p", "    ");
	rt.addValue("Overlap", "    ");
	rt.addValue("R/p", "    ");
	rt.addValue("ICQ", coefs[8]);
	rt.addValue("i/p", pvalues[11]);
	rt.addValue("Manders K", coefs[18]);
	rt.addValue("k/p", pvalues[21]);
	rt.addValue("Manders M", coefs[27]);
	rt.addValue("M/p", pvalues[30]);
	rt.incrementCounter();
	rt.addValue("    ", "Red/blue");
	rt.addValue("Pearson", coefs[1]);
	rt.addValue("r/p", pvalues[1]);
	rt.addValue("Overlap", coefs[4]);
	rt.addValue("R/p", pvalues[4]);
	rt.addValue("ICQ", coefs[9]);
	rt.addValue("i/p", pvalues[12]);
	rt.addValue("Manders K", coefs[19]);
	rt.addValue("k/p", pvalues[22]);
	rt.addValue("Manders M", coefs[28]);
	rt.addValue("M/p", pvalues[31]);
	rt.incrementCounter();
	rt.addValue("    ", "Blue/red");
	rt.addValue("Pearson", "  ");
	rt.addValue("r/p", "    ");
	rt.addValue("Overlap", "    ");
	rt.addValue("R/p", "    ");
	rt.addValue("ICQ", coefs[10]);
	rt.addValue("i/p", pvalues[13]);
	rt.addValue("Manders K", coefs[20]);
	rt.addValue("k/p", pvalues[23]);
	rt.addValue("Manders M", coefs[29]);
	rt.addValue("M/p", pvalues[32]);
	rt.incrementCounter();
	rt.addValue("    ", "Green/blue");
	rt.addValue("Pearson", coefs[2]);
	rt.addValue("r/p", pvalues[2]);
	rt.addValue("Overlap", coefs[5]);
	rt.addValue("R/p", pvalues[5]);
	rt.addValue("ICQ", coefs[11]);
	rt.addValue("i/p", pvalues[14]);
	rt.addValue("Manders K", coefs[21]);
	rt.addValue("k/p", pvalues[24]);
	rt.addValue("Manders M", coefs[30]);
	rt.addValue("M/p", pvalues[33]);
	rt.incrementCounter();
	rt.addValue("    ", "Blue/green");
	rt.addValue("Pearson", "  ");
	rt.addValue("r/p", "    ");
	rt.addValue("Overlap", "    ");
	rt.addValue("R/p", "    ");
	rt.addValue("ICQ", coefs[12]);
	rt.addValue("i/p", pvalues[15]);
	rt.addValue("Manders K", coefs[22]);
	rt.addValue("k/p", pvalues[25]);
	rt.addValue("Manders M", coefs[31]);
	rt.addValue("M/p", pvalues[34]);
	rt.incrementCounter();
	rt.addValue("    ", "*******");
	rt.addValue("Pearson", "*******");
	rt.addValue("r/p", "*******");
	rt.addValue("Overlap", "*******");
	rt.addValue("R/p", "*******");
	rt.addValue("ICQ", "*******");
	rt.addValue("i/p","*******");
	rt.addValue("Manders K", "*******");
	rt.addValue("k/p", "*******");
	rt.addValue("Manders M", "*******");
	rt.addValue("M/p", "*******");
	rt.incrementCounter();
	rt.addValue("    ", "Red");
	rt.addValue("Pearson", " ");
	rt.addValue("r/p", " ");
	rt.addValue("Overlap", coefs[6]);
	rt.addValue("R/p", pvalues[6]);
	rt.addValue("ICQ", coefs[13]);
	rt.addValue("i/p", pvalues[16]);
	rt.addValue("Manders K", coefs[23]);
	rt.addValue("k/p", pvalues[26]);
	rt.addValue("Manders M", coefs[32]);
	rt.addValue("M/p", pvalues[35]);
	rt.incrementCounter();
	rt.addValue("    ", "Green");
	rt.addValue("Pearson", " ");
	rt.addValue("r/p", " ");
	rt.addValue("Overlap", coefs[6]);
	rt.addValue("R/p", pvalues[7]);
	rt.addValue("ICQ", coefs[14]);
	rt.addValue("i/p", pvalues[17]);
	rt.addValue("Manders K", coefs[24]);
	rt.addValue("k/p", pvalues[27]);
	rt.addValue("Manders M", coefs[33]);
	rt.addValue("M/p", pvalues[36]);
	rt.incrementCounter();
	rt.addValue("    ", "Blue");
	rt.addValue("Pearson", " ");
	rt.addValue("r/p", " ");
	rt.addValue("Overlap", coefs[6]);
	rt.addValue("R/p", pvalues[8]);
	rt.addValue("ICQ", coefs[15]);
	rt.addValue("i/p", pvalues[18]);
	rt.addValue("Manders K", coefs[25]);
	rt.addValue("k/p", pvalues[28]);
	rt.addValue("Manders M", coefs[34]);
	rt.addValue("M/p", pvalues[37]);
	rt.incrementCounter();
	rt.addValue("    ", "General");
	rt.addValue("Pearson", " ");
	rt.addValue("r/p", " ");
	rt.addValue("Overlap", coefs[6]);
	rt.addValue("R/p", pvalues[9]);
	rt.addValue("ICQ", coefs[16]);
	rt.addValue("i/p", pvalues[19]);
	rt.addValue("Manders K", " ");
	rt.addValue("k/p", " ");
	rt.addValue("Manders M", " ");
	rt.addValue("M/p", " ");
	rt.showRowNumbers(false);
	}

public void showRT() { rt.show("Results"); }
}