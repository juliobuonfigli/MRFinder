package ar.com.yahoojuliobuonfigli.imagej;

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
//Rama: sinDispersion
//Estoy pensano que ruler la voy a borrar a la chucha y todo lo que estaba ahi lo voy a meter en el metodo run

public class MRF_main implements PlugIn {

	static String SelectedTitle, SelectedMT, SelectedMO1, SelectedMO2, SelectedST, SelectedSL, SelectedDF;
	private String i1name, i2name, i3name, i4name;
	static double Tpercentage, GeneratedI, Dratio, RandomS;
	static boolean Aintensity, Dsignal, Rimages, Smasks, Moptions, Erode;
	private String[] titles, titles2;	
	private int[] wList;
	static ImagePlus i1, i2, i3, i4;
	private ImagePlus imp;
	private String[] level = {"0,05", "0,01"};
	private String[] M_operator = {"AND", "OR"};
//	private String[] D_function = {"Linear", "Quadratic", "Geometric", "Aritmethic"};
	private String[] M_thresholds = {"No_threshold", "User_thresholds", "Default", "Huang", "Intermodes", "IsoData", "IJ_IsoData", "Li", 
			"MaxEntropy", "Mean", "MinError", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag", "Triangle", "Yen"};
    private String[] S_thresholds = {"No_threshold", "User_thresholds", "MRF_threshold", "Default", "Huang", "Intermodes", "IsoData", 
    		"IJ_IsoData", "Li", "MaxEntropy", "Mean", "MinError", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag", "Triangle", "Yen"};
  
    static double RedMaskValue, GreenMaskValue, BlueMaskValue, RedSignalValue, GreenSignalValue, BlueSignalValue;
    
    public boolean mainDialog() {
	    GenericDialog gd = new GenericDialog("Multiple Relationship Finder");
	   
		gd.addChoice("Channel_1 (red): ", titles, titles[0]);
	    gd.addChoice("Channel_2 (green): ", titles, titles[1]);
	    gd.addChoice("Channel_3 (blue): ", titles2, titles2[2]);
	    gd.addChoice("Primary mask: ", titles2, titles2[2]);
	    gd.addChoice("Masks thresholds: ",  M_thresholds, M_thresholds[0]);
	    gd.addChoice("Red/Green operator", M_operator, M_operator[1]);
	    gd.addChoice("ResultOfRG/Blue operator",  M_operator, M_operator[1]);
	    gd.addCheckbox("Use channel specific masks", false);
	    gd.addChoice("Signal thresholds: ", S_thresholds, S_thresholds[2]);
	    gd.addCheckbox("MORE OPTIONS", false);
	    gd.showDialog();
	    i1name = gd.getNextChoice();
        i2name = gd.getNextChoice();
        i3name = gd.getNextChoice();
        i4name = gd.getNextChoice();
        SelectedMT = gd.getNextChoice();
        SelectedMO1 = gd.getNextChoice();
        SelectedMO2 = gd.getNextChoice();
        Smasks = gd.getNextBoolean();
        SelectedST = gd.getNextChoice();
        Moptions = gd.getNextBoolean();
        
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
    
    public boolean moreOptionsDialog() {
      	 GenericDialog gd4 = new GenericDialog("More Options");
      	 
        gd4.addNumericField("MRF effective signal percentage: ", 50, 2);
 	    gd4.addCheckbox("Equalize intensities", false); //iguala los otros dos a la intensidad del menos intenso
 	   	gd4.addChoice("Significance level: ", level, level[0]);
	    gd4.addNumericField("Number of random coefficients: ", 30, 2);
 	    gd4.addCheckbox("Show examples of generated images", false);
 	    gd4.addNumericField("Random seed: ", 1, 3);
 	    gd4.showDialog();
      	
 	    Tpercentage = gd4.getNextNumber();
        Aintensity = gd4.getNextBoolean();
        SelectedSL = gd4.getNextChoice();
        GeneratedI = gd4.getNextNumber();
        Rimages = gd4.getNextBoolean();
        RandomS = gd4.getNextNumber();
              
      return true;
      } 
               
          
	static ImagePlus getRedChannel() { return i1; }
	static ImagePlus getGreenChannel () { return i2; }
	static ImagePlus getBlueChannel () { return i3; }
	static ImagePlus getPrimaryMask () { return i4; }
	//static String getSeletedTitle () { return SelectedTitle; }
	static String getMaskThreshold () { return SelectedMT; }
	static String getMaskOperator1 () { return SelectedMO1; }
	static String getMaskOperator2 () { return SelectedMO2; }
	static boolean getSpecificMask () { return Smasks; } 
	static String getSignalThreshold () { return SelectedST; }
//	static boolean getDisperseSignal () { return Dsignal; }
//	static double getDisperseRatio () { return Dratio; }
	//static boolean getMoreOptions () { return Moptions; }
	//gd2
	static double getRedMaskValue () { return RedMaskValue; }
	static double getGreenMaskValue () { return GreenMaskValue; }
	static double getBlueMaskValue () { return BlueMaskValue; }
	//gd3
	static double getRedSignalValue () { return RedSignalValue; }
	static double getGreenSignalValue () { return GreenSignalValue; }
	static double getBlueSignalValue () { return BlueSignalValue; }
	//gd4	
//	static String getDispersionFunction () { return SelectedDF; }
	static double getMFRthresholdPercentage () { return Tpercentage; } 
	static boolean getAveregaIntensity () { return  Aintensity; } 
//	static boolean getErode () { return  Erode; } 
	static String getSignificanceLevel () { return SelectedSL; }
	static double getGeneratedImages () { return GeneratedI; }
	static boolean getShowRandomImages () { return Rimages; }
	static double getRandomSeed () { return RandomS; }
	
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
		    titles = new String[wList.length];
		    for (int i=0; i<wList.length; i++) {
		        imp = WindowManager.getImage(wList[i]);
		        if (imp!=null)
		            titles[i] = imp.getTitle();
		        else
		            titles[i] = " "; }
		 
		    titles2 = new String[wList.length+1];
		    for(int i=0; i<wList.length; i++) { 
		    	titles2[i]=titles[i];    }
		    titles2[wList.length]="None";
		    
	    mainDialog();
	    
	   if(SelectedMT.equals("User_thresholds"))
	    	maskThresholdsDialog();
	   if(SelectedST.equals("User_thresholds"))
	    	signalThresholdsDialog();
  
	   Tpercentage = 50.00;
       Aintensity = false;
       SelectedSL = "0.05";
       GeneratedI = 30;
       Rimages = false;
       RandomS = 1;
	   
	   if(Moptions==true)
		    moreOptionsDialog();

/*	   SignalOptions so=new SignalOptions(i1, SelectedST, Aintensity, 
			   Dsignal, Tpercentage, Dratio, 12);
	   ImagePlus i5=so.disperse();
	   i5.show(); */

	}   
  // supongo que cuando termine voy a tener que borrar de aca para abajo
	public static void main(String[] args) {
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