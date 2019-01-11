/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrintTools;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import javax.swing.RepaintManager;
/**
 *
 * @author schmidtu
 */
public class PrintUtilities implements Printable {
   
    private List<Component> componentsToPrint;

 
    public static void printComponents( List<Component> componentsToPrint ) {
        new PrintUtilities( componentsToPrint ).print();
    }
   
   
    public PrintUtilities( List<Component> componentsToPrint ) {
        this.componentsToPrint = componentsToPrint;
    }
   
   
    public void print() {
       
      PrinterJob printJob = PrinterJob.getPrinterJob();
     
      PageFormat pageFormat = printJob.defaultPage();  //new PageFormat();
      //pageFormat.setOrientation( PageFormat.LANDSCAPE );   //Längs- oder Querformat (Standard: längs)
     
      Paper a4PortraitPaper = new Paper();
      final double cm2inch = 0.3937;  // 1in = 2.54cm
      double paperHeight = 29.7 * cm2inch;
      double paperWidth = 21.0 * cm2inch;
      double margin = 1.5 * cm2inch;
       
      a4PortraitPaper.setSize( paperWidth * 72.0, paperHeight * 72.0 );
      a4PortraitPaper.setImageableArea( margin * 72.0, margin * 72.0,
                          ( paperWidth - 2 * margin ) * 72.0,
                          ( paperHeight - 2 * margin ) * 72.0 );

      pageFormat.setPaper( a4PortraitPaper );
     
      printJob.setPrintable( this, pageFormat );
     
     
      if ( printJob.printDialog() )
        try {
          printJob.print();
        } catch( PrinterException pe ) {
          System.out.println( "Error printing: " + pe );
          System.out.println( "Error printing (Message): " + pe.getMessage() );
          System.out.println( "Error printing (Localized Message): " + pe.getLocalizedMessage() );
          System.out.println( "Error printing (Cause): " + pe.getCause() );
        }
       
    }

   
    public int print( Graphics g, PageFormat pageFormat, int pageIndex ) {
       
        double gBreite, gHoehe;
        int b, h;
        double skalierung = 0.0;
       
        System.out.println("page index: " + pageIndex);

        Graphics2D g2d = (Graphics2D)g;
        g2d.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
           
        gBreite = pageFormat.getImageableWidth();
        gHoehe = pageFormat.getImageableHeight();
       

        if ( pageIndex < componentsToPrint.size() ) {
           
            Component c = componentsToPrint.get( pageIndex );
           
            // ***** Skalierung *****
           
            b = c.getWidth();
            h = c.getHeight();
               
            skalierung = gBreite / b;
               
            g2d.scale( skalierung, skalierung );
               
            // ***** Ende Skalierung *****

            disableDoubleBuffering( componentsToPrint.get( pageIndex ) );
            componentsToPrint.get( pageIndex ).paint( g2d );
            enableDoubleBuffering( componentsToPrint.get( pageIndex ) );
            return PAGE_EXISTS;
               
        }
        else
            return NO_SUCH_PAGE;

       
    }

   
    public static void disableDoubleBuffering( Component c ) {
      RepaintManager currentManager = RepaintManager.currentManager( c );
      currentManager.setDoubleBufferingEnabled( false );
    }

   
    public static void enableDoubleBuffering( Component c ) {
      RepaintManager currentManager = RepaintManager.currentManager( c );
      currentManager.setDoubleBufferingEnabled( true );
    }
  }