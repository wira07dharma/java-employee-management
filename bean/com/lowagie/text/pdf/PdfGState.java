/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lowagie.text.pdf;

/**
 *
 * @author IanRizky
 */
public class PdfGState extends PdfDictionary {
    /** A possible blend mode */
    public static final PdfName BM_NORMAL = new PdfName("Normal");
    /** A possible blend mode */
    public static final PdfName BM_COMPATIBLE = new PdfName("Compatible");
    /** A possible blend mode */
    public static final PdfName BM_MULTIPLY = new PdfName("Multiply");
    /** A possible blend mode */
    public static final PdfName BM_SCREEN = new PdfName("Screen");
    /** A possible blend mode */
    public static final PdfName BM_OVERLAY = new PdfName("Overlay");
    /** A possible blend mode */
    public static final PdfName BM_DARKEN = new PdfName("Darken");
    /** A possible blend mode */
    public static final PdfName BM_LIGHTEN = new PdfName("Lighten");
    /** A possible blend mode */
    public static final PdfName BM_COLORDODGE = new PdfName("ColorDodge");
    /** A possible blend mode */
    public static final PdfName BM_COLORBURN = new PdfName("ColorBurn");
    /** A possible blend mode */
    public static final PdfName BM_HARDLIGHT = new PdfName("HardLight");
    /** A possible blend mode */
    public static final PdfName BM_SOFTLIGHT = new PdfName("SoftLight");
    /** A possible blend mode */
    public static final PdfName BM_DIFFERENCE = new PdfName("Difference");
    /** A possible blend mode */
    public static final PdfName BM_EXCLUSION = new PdfName("Exclusion");
    
    /**
     * Sets the flag whether to apply overprint for stroking.
     * @param ov
     */
    public void setOverPrintStroking(boolean ov) {
        put(PdfName.OP, ov ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Sets the flag whether to apply overprint for non stroking painting operations.
     * @param ov
     */
    public void setOverPrintNonStroking(boolean ov) {
        put(PdfName.op, ov ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Sets the flag whether to toggle knockout behavior for overprinted objects.
     * @param ov - accepts 0 or 1
     */
    public void setOverPrintMode(int ov) {
        put(PdfName.OPM, new PdfNumber(ov==0 ? 0 : 1));
    }
    
    /**
     * Sets the current stroking alpha constant, specifying the constant shape or
     * constant opacity value to be used for stroking operations in the transparent
     * imaging model.
     * @param n
     */
    public void setStrokeOpacity(float n) {
        put(PdfName.CA, new PdfNumber(n));
    }
    
    /**
     * Sets the current stroking alpha constant, specifying the constant shape or
     * constant opacity value to be used for nonstroking operations in the transparent
     * imaging model.
     * @param n
     */
    public void setFillOpacity(float n) {
        put(PdfName.ca, new PdfNumber(n));
    }
    
    /**
     * The alpha source flag specifying whether the current soft mask
     * and alpha constant are to be interpreted as shape values (true)
     * or opacity values (false). 
     * @param v
     */
    public void setAlphaIsShape(boolean v) {
        put(PdfName.AIS, v ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }
    
    /**
     * Determines the behavior of overlapping glyphs within a text object
     * in the transparent imaging model.
     * @param v
     */
    public void setTextKnockout(boolean v) {
        put(PdfName.TK, v ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }
    
    /**
     * The current blend mode to be used in the transparent imaging model.
     * @param bm
     */
    public void setBlendMode(PdfName bm) {
        put(PdfName.BM, bm);
    }
    
}