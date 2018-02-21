package ar.com.yahoojuliobuonfigli.imagej;

import ij.measure.ResultsTable;
import ij.process.ImageProcessor;

public class MRFresults {

	public double[] coefs, pvalues;
	public boolean[] sig;
    public ResultsTable rt; 	
	public boolean r, R, i, k, m;
    
public MRFresults(ColocalizationCoefficients c, StatisticalSignificance s, boolean r, boolean R, boolean i, boolean k, boolean m) 
	{
	this.r=r; 
	this.R=R;
	this.i=i;
	this.k=k;
	this.m=m;
	coefs=c.AllCoef();
	s.PEARSON();
	createRT();
	}
	
public void createRT()
	{
	rt=new ResultsTable();
	rt.incrementCounter();
	rt.addValue("/", "Red/green");
	rt.addValue("Pearson", coefs[0]);
	rt.addValue("Overlap", coefs[3]);
	rt.showRowNumbers(false);
	}

public void showRT() 	{ rt.show("Results"); }
}