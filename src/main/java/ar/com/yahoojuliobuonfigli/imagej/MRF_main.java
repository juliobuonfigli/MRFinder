package ar.com.yahoojuliobuonfigli.imagej;

//cambio para chequear git

import ij.plugin.PlugIn;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

//estructura de esta clase: run, main, dialogos, getters, instancia objeto ruler, instancia resultados
//la clase ruler va a tomar todos los datos introducidos en los cuadros de diálogo y va a controlar el diagrama de flujo 
//puede que sea una clase estática 
//primeraRama

public class MRF_main implements PlugIn {

	static String SelectedTitle, SelectedMT, SelectedMO, SelectedST, SelectedRC, SelectedSL;
	private String i1name, i2name, i3name, i4name;
	static double Tpercentage, AverageI, GeneratedI, Dratio, RandomS;
	static boolean Aintensity, Dsignal, Rimages;
	private String[] titles;	
	private int[] wList;
	static ImagePlus i1, i2, i3, i4;
	private ImagePlus imp;
	private String[] criterion = {"Do not perform", "MRF_criterion", "Randomize_all_channels"};
	private String[] level = {"0,05", "0,01"};
	private String[] M_operator = {"AND", "OR"};
	
	private String[] M_thresholds = {"No_threshold", "User_thresholds", "Default", "Huang", "Intermodes", "IsoData", "IJ_IsoData", "Li", 
			"MaxEntropy", "Mean", "MinError", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag", "Triangle", "Yen"};
    private String[] S_thresholds = {"No_threshold", "User_thresholds", "MRF_threshold", "Default", "Huang", "Intermodes", "IsoData", 
    		"IJ_IsoData", "Li", "MaxEntropy", "Mean", "MinError", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag", "Triangle", "Yen"};
	private RulerMRF getToWorkComputer;
    static double RedMaskValue=0, GreenMaskValue=0, BlueMaskValue=0, RedSignalValue=0, GreenSignalValue=0, BlueSignalValue=0;
    
   
    public boolean mainDialog() {
	    GenericDialog gd = new GenericDialog("Multiple Relationship Finder");
	    gd.addMessage("CHANNELS:");
		gd.addChoice("Channel_1 (red): ", titles, titles[0]);
	    gd.addChoice("Channel_2 (green): ", titles, titles[1]);
	    gd.addChoice("Channel_3 (blue): ", titles, titles[2]);
	    gd.addMessage("MASK OPTIONS:");
	    gd.addChoice("Primary mask: ", titles, titles[3]);
	    gd.addChoice("Masks thresholds: ",  M_thresholds, M_thresholds[0]);
	    gd.addChoice("Masks operator: ", M_operator, M_operator[1]);
	    gd.addMessage("SIGNAL OPTIONS:");
	    gd.addChoice("Signal thresholds: ", S_thresholds, S_thresholds[2]);
	    gd.addNumericField("MRF threshold percentage: ", 50, 2);
	    gd.addCheckbox("Average intensities", false);
	    gd.addNumericField("Average: ", 50, 3);
	    gd.addCheckbox("Disperse signal", false);
	    gd.addNumericField("Dispertion ratio: ", 30, 2);
	    gd.addMessage("STATISTICAL SIGNIFICANCE:");
	    gd.addChoice("Randomization criterion: ", criterion, criterion[1]);
	    gd.addNumericField("Number of generated images: ", 30, 3);
	    gd.addChoice("Significance level: ", level, level[0]);
	    gd.addCheckbox("Show examples of random images", false);
	    gd.addNumericField("Random seed: ", 1, 3);
	    gd.showDialog();
	    i1name = gd.getNextChoice();
        i2name = gd.getNextChoice();
        i3name = gd.getNextChoice();
        i4name = gd.getNextChoice();
        SelectedMT = gd.getNextChoice();
        SelectedMO = gd.getNextChoice();
        SelectedST = gd.getNextChoice();
        Tpercentage = gd.getNextNumber();
        Aintensity = gd.getNextBoolean();
        AverageI = gd.getNextNumber();
        Dsignal = gd.getNextBoolean();
        Dratio = gd.getNextNumber();
        SelectedRC = gd.getNextChoice();
        GeneratedI = gd.getNextNumber();
        SelectedSL = gd.getNextChoice();
        Rimages = gd.getNextBoolean();
        RandomS = gd.getNextNumber();
        
        i1 = WindowManager.getImage(i1name);
        i2 = WindowManager.getImage(i2name);
        i3 = WindowManager.getImage(i3name);
        i4 = WindowManager.getImage(i4name);
	    
        return true;
	    }
	
    public boolean maskThresholdsDialog() {
    	 GenericDialog gd2 = new GenericDialog("Mask Thresholds");
 	    gd2.addNumericField("Red: ", 50, 3);
 	    gd2.addNumericField("Green: ", 50, 3);
 	    gd2.addNumericField("Blue: ", 50, 3);
 	    gd2.showDialog();
 	    RedMaskValue = gd2.getNextNumber();
        GreenMaskValue = gd2.getNextNumber();
        BlueMaskValue = gd2.getNextNumber();
    return true;
    }
    
    public boolean signalThresholdsDialog() {
   	 GenericDialog gd3 = new GenericDialog("Signal Thresholds");
	    gd3.addNumericField("Red: ", 50, 3);
	    gd3.addNumericField("Green: ", 50, 3);
	    gd3.addNumericField("Blue: ", 50, 3);
	    gd3.showDialog();
	    RedSignalValue = gd3.getNextNumber();
       GreenSignalValue = gd3.getNextNumber();
       BlueSignalValue = gd3.getNextNumber();
   return true;
   }
            
  /*    if(i1.equals(null))
		return null;
		else
		return i1; }*/
        
	static ImagePlus getRedChannel() { return i1; }
	static ImagePlus getGreenChannel () { return i2; }
	static ImagePlus getBlueChannel () { return i3; }
	static ImagePlus getPrimaryMask () { return i4; }
	static String getSeletedTitle () { return SelectedTitle; }
	static String getMaskThreshold () { return SelectedMT; }
	static String getMaskOperator () { return SelectedMO; }
	static String getSignalThreshold () { return SelectedST; }
	static double getMFRthresholdPercentage () { return Tpercentage; } 
	static boolean getAveregaIntensity () { return  Aintensity; } 
	static double getAverageIntensityValue () { return AverageI; }
	static boolean getDisperseSignal () { return Dsignal; }
	static double getDisperseRatio () { return Dratio; }
	static String getRandomizationCriterion () { return SelectedRC; }
	static double getGeneratedImages () { return GeneratedI; }
	static String getSignificanceLevel () { return SelectedSL; }
	static boolean getShowRandomImages () { return Rimages; }
	static double getRamdonSeed () { return RandomS; }
	static double getRedMaskValue () { return RedMaskValue; }
	static double getGreenMaskValue () { return GreenMaskValue; }
	static double getBlueMaskValue () { return BlueMaskValue; }
	static double getRedSignalValue () { return RedSignalValue; }
	static double getGreenSignalValue () { return GreenSignalValue; }
	static double getBlueSignalValue () { return BlueSignalValue; }
		
	
	@Override
	public void run(String arg0) {	
					
		
		i1 = IJ.createImage("Red", "8-bit random", 20, 20, 1); 		i1.show();
		i2 = IJ.createImage("Green", "8-bit random", 51, 51, 1); 	i2.show();
		i3 = IJ.createImage("Blue", "8-bit random", 51, 51, 1);		i3.show();
		i4 = IJ.createImage("Mask", "8-bit random", 51, 51, 1); 	i4.show();
		//i1 = IJ.openImage("E:\\Escritorio\\prueba1.tif");           i1.show();
		
		 wList = WindowManager.getIDList();
		 
		    if (wList==null || wList.length<1) {
		        IJ.showMessage("There must be at least two windows open");
		        return;      }
		    titles = new String[wList.length+1];
		    for (int i=0; i<wList.length; i++) {
		        imp = WindowManager.getImage(wList[i]);
		        if (imp!=null)
		            titles[i] = imp.getTitle();
		        else
		            titles[i] = " "; }
		    titles[wList.length]="None";
		    
	    mainDialog();
	    
	    if(SelectedMT.equals("User_thresholds"))
	    	maskThresholdsDialog();
	   if(SelectedST.equals("User_thresholds"))
	    	signalThresholdsDialog();

	   SignalOptions so=new SignalOptions(i1, SelectedST, Aintensity, 
			   Dsignal, Tpercentage, AverageI, Dratio, 12);
	   ImagePlus i5=so.disperse();
	   i5.show();
	   
	//getToWorkComputer = new RulerMRF();
//	getToWorkComputer.runMRF();
	}
	
	
	public static void main(String[] args) {
		// set the plugins.dir property to make the plugin appear in the Plugins menu
		Class<?> clazz = MRF_main.class;
		String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
		String pluginsDir = url.substring("file:".length(), url.length() - clazz.getName().length() - ".class".length());
		System.setProperty("plugins.dir", pluginsDir);

		// start ImageJ
		new ImageJ();
		
		// open the Clown sample
		//ImagePlus image = IJ.openImage("http://imagej.net/images/clown.jpg");
		//image.show();

		// run the plugin
		IJ.runPlugIn(clazz.getName(), "");
		
	}
}