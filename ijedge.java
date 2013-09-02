import java.awt.*;
import java.io.*;
import java.lang.*;

//ImageJ import
import ij.*;
import ij.plugin.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.BMP_Writer;

public class visionartificial implements PlugIn {

  public void run(String arg) {
  
    //Version check (pretty old)
    if (IJ.versionLessThan("1.32g")) 
        return;
  
    //RGB image check
    if (IJ.getImage().getBitDepth()!=24)
    {
       IJ.showMessage("RGB image needed");
       IJ.run("Close");
    }
  
    //From RGB to HSB
    IJ.run("HSB Stack");
    IJ.run("Find Edges", "stack");
    IJ.run("Convert Stack to Images");
    IJ.selectWindow("Saturation");
  
    int w = IJ.getImage().getWidth();
    int h = IJ.getImage().getHeight();
    IJ.showMessage(w+ " width and "+h+" height");
  
    //Separate Hue, saturation and brightness
    IJ.selectWindow("Hue");
    int [] valor;
    int [][] tono = new int[h][w];
  
    for (int y = 0; y < (h); y++){
      for (int x = 0; x < (w ); x++) {
        valor = IJ.getImage().getPixel(x, y);
        int vPixel = valor[0];
        tono[y][x]= vPixel;
      }
    }
  
    IJ.selectWindow("Saturation");
    int [][] saturacion = new int[h][w];
  
    for (int y = 0; y < (h); y++){
      for (int x = 0; x < (w ); x++) {
           valor = IJ.getImage().getPixel(x, y);
           int vPixel = valor[0];
           saturacion[y][x]= vPixel;
        }
      }
  
  
    IJ.selectWindow("Brightness");
    int [][] brillo = new int[h][w];
  
    for (int y = 0; y < (h); y++){
      for (int x = 0; x < (w ); x++) {
        valor = IJ.getImage().getPixel(x, y);
        int vPixel = valor[0];
        brillo[y][x]= vPixel;
      }
    }
  
     IJ.run("Close");
     IJ.run("Close");
     IJ.run("Close");
  
    //Outline calculation
    
    int [][] matrizFinal = new int[h][w];
    for (int y = 0; (y < h); y++){
      for (int x = 0; (x < w ); x++) {
  	    if (tono[y][x] > 100) {
  	      matrizFinal[y][x] = 255;
  	    } else if (((brillo[y][x] > 100) & (tono[y][x] <300)) ||
  	               ((brillo[y][x] > 50)  & (brillo[y][x]<150))){
          matrizFinal[y][x] = 150;
  		  } else {
  			  matrizFinal[y][x] = 0;
        }
  		}
    }//fori
  
    //Save and show result
    
    ByteProcessor imgFinal = new ByteProcessor (w, h);
  
    imgFinal.createProcessor(w,h);
  
    for (int y = 0; (y < h); y++){
      for (int x = 0; (x < w ); x++) {
        imgFinal.putPixel(x, y, matrizFinal[y][x]);
      }
    }
  
    ImagePlus outlineimage = new ImagePlus("ContornoImagen", imgFinal);
  	outlineimage.show();
  	
    IJ.selectWindow("ContornoImagen");
    IJ.saveAs("BMP", "Contorno.bmp");
  }
}
