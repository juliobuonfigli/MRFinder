package ar.com.yahoojuliobuonfigli.imagej;

//Rama: sinDispersion
//Todos los objetos de todas las clases se van a crear en la clase main: esto me va a permitir que todas las 
//clases puedan ser usadas en otros programas y que practicamente solo tenga que programar la clase main
//coefficientes: r, R, ICQ, k, M
//comparar dos grupos de estudio 
//agregar try catch

import ij.plugin.PlugIn;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class MRF_main implements PlugIn {

static String SelectedTitle, SelectedMT, SelectedMO1, SelectedMO2, SelectedST, SelectedSL, SelectedDF;
private String i1name, i2name, i3name, i4name;
static double Tpercentage, GeneratedI, Dratio, RandomS;
static boolean Aintensity, Dsignal, Rimages, Smasks, Moptions, bComposite, scram;
private boolean r, R, i, k, m;
private String[] titles, titles2;	
private int[] wList;
static ImagePlus I1, I2, I3, I4, im1, im2, im3, im4, it1, it2, it3, mask;
private ImagePlus imp;
private String[] level = {"0.05", "0.01"};
private String[] M_operator = {"AND", "OR"};
//private String[] D_function = {"Linear", "Quadratic", "Geometric", "Aritmethic"};
private String[] M_thresholds = {"No_thresholds", "User_thresholds", "Default", "Huang", "Intermodes", "IsoData", "IJ_IsoData", "Li", 
		"MaxEntropy", "Mean", "MinError", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag", "Triangle", "Yen"};
//private String[] S_thresholds = {"No_thresholds", "User_thresholds", "MRF_threshold", "Default", "Huang", "Intermodes", "IsoData", 
//   		"IJ_IsoData", "Li", "MaxEntropy", "Mean", "MinError", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag", "Triangle", "Yen"};

private String[] S_thresholds = {"No_thresholds", "User_thresholds", "Default", "Huang", "Intermodes", "IsoData", 
   		"IJ_IsoData", "Li", "MaxEntropy", "Mean", "MinError", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag", "Triangle", "Yen"};

//ojo aca lo acabo de cambiar de publico a privado  
private double RedMaskValue, GreenMaskValue, BlueMaskValue, RedSignalValue, GreenSignalValue, BlueSignalValue;
private ImageCalculator ic;

	
public MRF_main() 
	{
	RedMaskValue=0;
    GreenMaskValue=0;
    BlueMaskValue=0;
    RedSignalValue=0;
    GreenSignalValue=0; 
    BlueSignalValue=0; 
    ic = new ImageCalculator();
	}
  
public void binarize(ImagePlus img)
	{
	IJ.setThreshold(img, 1, 255);
	IJ.run(img, "Convert to Mask", ""); 
	}

public void mainDialog()
	{
	GenericDialog gd = new GenericDialog("Multiple Relationship Finder");
	gd.addChoice("Channel_1 (red): ", titles, titles[0]);
	gd.addChoice("Channel_2 (green): ", titles, titles[1]);
	gd.addChoice("Channel_3 (blue): ", titles2, titles2[2]);
	gd.addChoice("Primary mask: ", titles2, titles2[3]);
	gd.addChoice("Masks thresholds: ",  M_thresholds, M_thresholds[0]);
	gd.addChoice("Red/Green operator", M_operator, M_operator[1]);
	gd.addChoice("ResultOfRG/Blue operator",  M_operator, M_operator[1]);
	//gd.addCheckbox("Use channel specific masks", false);
	gd.addChoice("Signal thresholds: ", S_thresholds, S_thresholds[2]);
	//gd.addCheckbox("Disperse Signal", false);
	//gd.addNumericField("Dispesing ratio: ", 5, 2);
	gd.addCheckbox("MORE OPTIONS", false);
	gd.showDialog();
	if(gd.wasCanceled()) 
		return;
	i1name = gd.getNextChoice();
    i2name = gd.getNextChoice();
    i3name = gd.getNextChoice();
    i4name = gd.getNextChoice();
    SelectedMT = gd.getNextChoice();
    SelectedMO1 = gd.getNextChoice();
    SelectedMO2 = gd.getNextChoice();
    //Smasks = gd.getNextBoolean();
    SelectedST = gd.getNextChoice();
    //Dsignal = gd.getNextBoolean();
    //Dratio = gd.getNextNumber();
    Moptions = gd.getNextBoolean();
    }
    
public void maskThresholdsDialog() 
	{
    GenericDialog gd2 = new GenericDialog("Mask Thresholds");
 	gd2.addNumericField("Red: ", 50, 3);
 	gd2.addNumericField("Green: ", 50, 3);
 	gd2.addNumericField("Blue: ", 50, 3);
 	gd2.showDialog();
 	if(gd2.wasCanceled()) 
 		return;
 	RedMaskValue = gd2.getNextNumber();
    GreenMaskValue = gd2.getNextNumber();
    BlueMaskValue = gd2.getNextNumber();
    }
    
public void signalThresholdsDialog()
	{
   	GenericDialog gd3 = new GenericDialog("Signal Thresholds");
	gd3.addNumericField("Red: ", 150, 3);
	gd3.addNumericField("Green: ", 150, 3);
	gd3.addNumericField("Blue: ", 150, 3);
	gd3.showDialog();
	if(gd3.wasCanceled())
		return;
	RedSignalValue = gd3.getNextNumber();
    GreenSignalValue = gd3.getNextNumber();
    BlueSignalValue = gd3.getNextNumber();
    }
    
public void moreOptionsDialog() 
	{
    GenericDialog gd4 = new GenericDialog("More Options");
    //gd4.addChoice("Dispersion function: ", D_function, D_function[0]);
    //gd4.addNumericField("MRF effective signal percentage: ", 50, 2);
 	//gd4.addCheckbox("Equalize intensities", false); //iguala los otros dos a la intensidad del menos intenso
 	gd4.addChoice("Significance level: ", level, level[0]);
	gd4.addNumericField("Number of random coefficients: ", 30, 2);
 	gd4.addCheckbox("Show generated stack", true);
 	gd4.addCheckbox("Binary composite", true);
 	//gd4.addCheckbox("Show square colocalization map", false);
 	gd4.addNumericField("Random seed: ", 1, 3);
 	gd4.addMessage("Select coefficient to perform statistical significance");
 	gd4.addCheckbox("Pearson", false);
 	gd4.addCheckbox("Overlap", true);
 	gd4.addCheckbox("Intersection", false);
 	gd4.addCheckbox("Manders K", false);
 	gd4.addCheckbox("Manders M", false);
 	gd4.addMessage("Do instead of Van Stensel based significance");
 	gd4.addCheckbox("Pixel Scrambling", false);
 	gd4.showDialog();
 	if(gd4.wasCanceled()) 
 		return;
 	//SelectedDF = gd4.getNextChoice();
 	//Tpercentage = gd4.getNextNumber();
    //Aintensity = gd4.getNextBoolean();
    SelectedSL = gd4.getNextChoice();
    GeneratedI = gd4.getNextNumber();
    Rimages = gd4.getNextBoolean();
    bComposite = gd4.getNextBoolean();
    RandomS = gd4.getNextNumber();
    r=gd4.getNextBoolean();
    R=gd4.getNextBoolean();
    i=gd4.getNextBoolean();
    k=gd4.getNextBoolean();
    m=gd4.getNextBoolean();
    scram=gd4.getNextBoolean();
	} 
   
@Override
public void run(String arg0)
	{	
	//I1 = IJ.createImage("Red", "8-bit random", 51, 51, 1); 	I1.show();
	//I2 = IJ.createImage("Green", "8-bit random", 51, 51, 1); 	I2.show();
	//I3 = IJ.createImage("Blue", "8-bit random", 51, 51, 1);	I3.show();
	//I4 = IJ.createImage("Mask", "8-bit random", 51, 51, 1); 	I4.show();
	IJ.openImage("E:\\Escritorio\\red.tif").show();           //I1.show();
	IJ.openImage("E:\\Escritorio\\green.tif").show();         //I2.show();
	IJ.openImage("E:\\Escritorio\\blue.tif").show();          //I3.show();
	//IJ.openImage("E:\\Escritorio\\mask.tif").show();          //I4.show();
	
	wList = WindowManager.getIDList();
	if(wList==null || wList.length<1) 
		{
		IJ.showMessage("There must be at least two windows open");
		return; 
		}
	
	titles = new String[wList.length];
	for (int i=0; i<wList.length; i++) 
		{
		imp = WindowManager.getImage(wList[i]);
		if (imp!=null)
			titles[i] = imp.getTitle();
		else
		    titles[i] = " "; 
		}
	
	titles2 = new String[wList.length+1];
	for(int i=0; i<wList.length; i++) 
		titles2[i]=titles[i];    
		    
	titles2[wList.length]="None";
		    
	mainDialog();
	    
	if(SelectedMT.equals("User_thresholds"))
	   	maskThresholdsDialog();
	if(SelectedST.equals("User_thresholds"))
	   	signalThresholdsDialog();
  
    //SelectedDF = "Aritmethic";
	//Tpercentage = 50.00;
    //Aintensity = false;
    SelectedSL="0.05";
    GeneratedI=30;
    Rimages=true;
    RandomS=1;
	bComposite=true;
    m=false;
	r=false;
	R=true;
	k=false;
	i=false;
	scram=false;
	
	if(Moptions==true)
	    moreOptionsDialog();
	   
	I1 = WindowManager.getImage(i1name);   it1 = I1.duplicate();
    I2 = WindowManager.getImage(i2name);   it2 = I2.duplicate();
    if(i3name.equals("None")) 
    	it3=it2;
    else 
    	{
    	I3 = WindowManager.getImage(i3name);   
    	it3 = I3.duplicate(); 
    	}
             
    SignalOptions soRed=new SignalOptions(it1, SelectedST, Aintensity, Dsignal, Tpercentage, Dratio, RedSignalValue);
	soRed.umbralize();  
	SignalOptions soGreen=new SignalOptions(it2, SelectedST, Aintensity, Dsignal, Tpercentage, Dratio, GreenSignalValue);
	soGreen.umbralize(); 
	if(!i3name.equals("None")) 
		{ 
		SignalOptions soBlue=new SignalOptions(it3, SelectedST, Aintensity, Dsignal, Tpercentage, Dratio, BlueSignalValue);
		soBlue.umbralize();
		}
	  
    im1 = I1.duplicate();
    im2 = I2.duplicate();
    if(i3name.equals("None")) 
    	im3=im2; 
    else 
    	im3=I3.duplicate(); 
    
    if(i4name.equals("None")) 
    	im4=im2; 
    else 
    	{ 
    	I4 = WindowManager.getImage(i4name);   
    	im4 = I4.duplicate(); 
    	}
    
    Mask mask1=new Mask(im4, im1, im2, im3, SelectedMT, SelectedMO1, SelectedMO2, RedMaskValue, GreenMaskValue, BlueMaskValue, i3name, i4name);
	boolean[] MASK=mask1.createMask(); 
		
	Vectorization RedVec=new Vectorization(MASK, it1);
	int[] REDVEC=RedVec.makeVector();
	Vectorization GreenVec=new Vectorization(MASK, it2);
	int[] GREENVEC=GreenVec.makeVector();
	int[] BLUEVEC;
	if(i3name.equals("None"))
		BLUEVEC=GREENVEC;
	else
		{
		Vectorization BlueVec=new Vectorization(MASK, it3);
		BLUEVEC=BlueVec.makeVector();
		}
	
	ColocalizationCoefficients CC1=new ColocalizationCoefficients(REDVEC, GREENVEC, BLUEVEC, i3name); 
	
	int RS=(int)RandomS;
	int GI=(int)GeneratedI;
	StatisticalSignificance SS1=new StatisticalSignificance(CC1, r, R, i, k, m, RS, GI, SelectedSL, i3name, scram);
	
	MRFresults r1 = new MRFresults(CC1, SS1, r, R, i, k, m, SelectedSL, i3name);
	r1.showRT(); 
	
	/*catch(java.lang.ArithmeticException e) {
		IJ.error("Channel whitout signal"); }*/
	
	mask=mask1.renderMask(); 
		
	ImagePlus[] composite;
	if(i3name.equals("None"))
		{
		composite = new ImagePlus[2];
		if(bComposite==true)
			{
			binarize(it1); binarize(it2);
			}
		composite[0]=it1; composite[1]=it2;  
		it1 = ic.run("AND", it1, mask); 
		it2 = ic.run("AND", it2, mask); 
		}
	else
		{
		composite = new ImagePlus[3];
		if(bComposite==true)
			{
			binarize(it1); binarize(it2); binarize(it3);
			}
		composite[0]=it1; composite[1]=it2; composite[2]=it3; 
		it1 = ic.run("AND", it1, mask); 
		it2 = ic.run("AND", it2, mask); 
		it3 = ic.run("AND", it3, mask);
		}	
		
	ImagePlus COMPOSITE = RGBStackMerge.mergeChannels(composite, false);
	COMPOSITE.show();
	
	if(!SelectedMT.equals("No_thresholds")) mask.show();
	
	if(Rimages)
		{
		//ImageStack img2 = SS1.createShiftingStack(REDVEC);
		ImageStack img2 = SS1.returnStackRed();
		ImagePlus img3=IJ.createImage("Red", "8-bit random", 200, 200, 200); 
		img3.setStack("Stack", img2);
		img3.show();
		}
	//double[] tes=new double[38];
	/*tes=SS1.test3();
	for(int i=0; i<38; i++) 
		System.out.println(tes[i]);*/
	
	/*tes=r1.test2();
	for(int i=0; i<38; i++) 
		System.out.println(tes[i]);*/
	
	/*tes=CC1.AllCoef();
	for(int i=0; i<34; i++) 
		System.out.println(tes[i]);*/	
	//System.out.println(SS1.test());
	}
  
public static void main(String[] args) 
	{
	// set the plugins.dir property to make the plugin appear in the Plugins menu
	Class<?> clazz = MRF_main.class;
	String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
	String pluginsDir = url.substring("file:".length(), url.length() - clazz.getName().length() - ".class".length());
	System.setProperty("plugins.dir", pluginsDir);
	// start ImageJ
	new ImageJ();
	// run the plugin
	IJ.runPlugIn(clazz.getName(), "");
	}
}

