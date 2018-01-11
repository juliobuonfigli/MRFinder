package ar.com.yahoojuliobuonfigli.imagej;

import ij.ImagePlus;

public class RulerMRF {

	//Channels 
	private ImagePlus red, green, blue;
	//mask
	private ImagePlus PrimaryMask;
    private String MaskThreshold, MaskOperator;
	private double RedMaskValue, GreenMaskValue, BlueMaskValue;
	//signal
	private String SignalThreshold;
	private boolean BooleanAverageIntensity, DisperseSignal;
	private double ThresholdPercentage, DoubleAverageIntensity, DisperseRatio, RedSignalValue, GreenSignalValue, BlueSignalValue;
	//significance
	private String RandomCriterion, SignificanceLevel;
	private double GeneratedImages, RandomSeed;
	private boolean RandomImages;
	//Out of constructor
	private boolean Triple, Mask;

	
public RulerMRF() {
red=MRF_main.getRedChannel();
green=MRF_main.getGreenChannel();
blue=MRF_main.getBlueChannel();
PrimaryMask=MRF_main.getPrimaryMask();
MaskThreshold=MRF_main.getMaskThreshold();
MaskOperator=MRF_main.getMaskOperator();
RedMaskValue=MRF_main.getRedMaskValue();
GreenMaskValue=MRF_main.getGreenMaskValue();
BlueMaskValue=MRF_main.getBlueMaskValue();
SignalThreshold=MRF_main.getSignalThreshold();
RedSignalValue=MRF_main.getRedSignalValue();
GreenSignalValue=MRF_main.getGreenSignalValue();
BlueSignalValue=MRF_main.getBlueSignalValue();
ThresholdPercentage=MRF_main.getMFRthresholdPercentage();
BooleanAverageIntensity=MRF_main.getAveregaIntensity();
DoubleAverageIntensity=MRF_main.getAverageIntensityValue();
DisperseSignal=MRF_main.getDisperseSignal();
DisperseRatio=MRF_main.getDisperseRatio();
RandomCriterion=MRF_main.getRandomizationCriterion();
SignificanceLevel=MRF_main.getSignificanceLevel();
GeneratedImages=MRF_main.getGeneratedImages();
RandomSeed=MRF_main.getRamdonSeed();
RandomImages=MRF_main.getShowRandomImages();
}

/*
public MRFresults runMRF() { 

if(red.equals(null) || green.equals(null) ||blue.equals(null))
	Triple=false;
	else
	Triple=true;
	
if(PrimaryMask.equals(null) && MaskThreshold.equals("No_threshold"))
	Mask=false;
	else
	Mask=true;
	
MRFresults results = new MRFResults();
return results;
	
}
//devuelve un objeto de tipo MRFresults
*/
}
