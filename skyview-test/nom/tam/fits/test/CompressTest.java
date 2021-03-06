package nom.tam.fits.test;
/*
 * This code is part of the Java FITS library developed 1996-2012 by T.A. McGlynn (NASA/GSFC)
 * The code is available in the public domain and may be copied, modified and used
 * by anyone in any fashion for any purpose without restriction. 
 * 
 * No warranty regarding correctness or performance of this code is given or implied.
 * Users may contact the author if they have questions or concerns.
 * 
 * The author would like to thank many who have contributed suggestions, 
 * enhancements and bug fixes including:
 * David Glowacki, R.J. Mathar, Laurent Michel, Guillaume Belanger,
 * Laurent Bourges, Rose Early, Jorgo Baker, A. Kovacs, V. Forchi, J.C. Segovia,
 * Booth Hartley and Jason Weiss.  
 * I apologize to any contributors whose names may have been inadvertently omitted.
 * 
 *      Tom McGlynn
 */

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import nom.tam.image.*;
import nom.tam.util.*;
import nom.tam.fits.*;

import java.net.URL;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/** Test reading .Z and .gz compressed files.
 */
public class CompressTest {

    @Test
    public void testgz() throws Exception {

        File fil = new File(".");
        System.out.println("File is:" + fil.getCanonicalPath());
        Fits f = new Fits("http://heasarc.gsfc.nasa.gov/FTP/asca/data/rev2/43021000/images/ad43021000gis25670_lo.totsky.gz");

        BasicHDU h = f.readHDU();
        int[][] data = (int[][]) h.getKernel();
        double sum = 0;
        for (int i = 0; i < data.length; i += 1) {
            for (int j = 0; j < data[i].length; j += 1) {
                sum += data[i][j];
            }
        }
        assertEquals("ZCompress", sum, 296915., 0);
    }

    @Test
    public void testZ() throws Exception {

        Fits f = new Fits("http://heasarc.gsfc.nasa.gov/FTP/rosat/data/pspc/processed_data/600000/rp600245n00/rp600245n00_im1.fits.Z");

        BasicHDU h = f.readHDU();
        short[][] data = (short[][]) h.getKernel();
        double sum = 0;
        for (int i = 0; i < data.length; i += 1) {
            for (int j = 0; j < data[i].length; j += 1) {
                sum += data[i][j];
            }
        }
        assertEquals("ZCompress", sum, 91806., 0);
    }

    @Test
    public void testStream() throws Exception {
        InputStream is;

        is = new FileInputStream("test.fits");
        assertEquals("Stream1", 300, streamRead(is, false, false));

        is = new FileInputStream("test.fits.Z");
        assertEquals("Stream2", 300, streamRead(is, false, false));

        is = new FileInputStream("test.fits.gz");
        assertEquals("Stream3", 300, streamRead(is, false, false));

        is = new FileInputStream("test.fits");
        assertEquals("Stream4", 300, streamRead(is, false, true));

        is = new FileInputStream("test.fits.Z");
        assertEquals("Stream5", 300, streamRead(is, false, true));

        is = new FileInputStream("test.fits.gz");
        assertEquals("Stream6", 300, streamRead(is, false, true));


        is = new FileInputStream("test.fits.Z");
        assertEquals("Stream7", 300, streamRead(is, true, true));

        is = new FileInputStream("test.fits.gz");
        assertEquals("Stream8", 300, streamRead(is, true, true));

        is = new FileInputStream("test.fits.bz2");
        assertEquals("Stream9", 300, streamRead(is, true, true));
    }

    @Test
    public void testFile() throws Exception {
        File is = new File("test.fits");
        assertEquals("File1", 300, fileRead(is, false, false));

        is = new File("test.fits.Z");
        assertEquals("File2", 300, fileRead(is, false, false));

        is = new File("test.fits.gz");
        assertEquals("File3", 300, fileRead(is, false, false));

        is = new File("test.fits");
        assertEquals("File4", 300, fileRead(is, false, true));

        is = new File("test.fits.Z");
        assertEquals("File7", 300, fileRead(is, true, true));

        is = new File("test.fits.gz");
        assertEquals("File8", 300, fileRead(is, true, true));

        is = new File("test.fits.bz2");
        assertEquals("File9", 300, fileRead(is, true, true));
    }

    @Test
    public void testString() throws Exception {
        String is = "test.fits";
        assertEquals("String1", 300, stringRead(is, false, false));

        is = "test.fits.Z";
        assertEquals("String2", 300, stringRead(is, false, false));

        is = "test.fits.gz";
        assertEquals("String3", 300, stringRead(is, false, false));

        is = "test.fits";
        assertEquals("String4", 300, stringRead(is, false, true));

        is = "test.fits.Z";
        assertEquals("String7", 300, stringRead(is, true, true));

        is = "test.fits.gz";
        assertEquals("String8", 300, stringRead(is, true, true));

        is = "test.fits.bz2";
        assertEquals("String8", 300, stringRead(is, true, true));

    }

    @Test
    public void testURL() throws Exception {
        String is = "test.fits";
        assertEquals("String1", 300, urlRead(is, false, false));

        is = "test.fits.Z";
        assertEquals("String2", 300, urlRead(is, false, false));

        is = "test.fits.gz";
        assertEquals("String3", 300, urlRead(is, false, false));

        is = "test.fits";
        assertEquals("String4", 300, urlRead(is, false, true));

        is = "test.fits.Z";
        assertEquals("String7", 300, urlRead(is, true, true));

        is = "test.fits.gz";
        assertEquals("String8", 300, urlRead(is, true, true));

        is = "test.fits.bz2";
        assertEquals("String8", 300, urlRead(is, true, true));
    }

    int urlRead(String is, boolean comp, boolean useComp)
            throws Exception {
        File fil = new File(is);

        String path = fil.getCanonicalPath();
        URL u = new URL("file://" + path);

        Fits f;
        if (useComp) {
            f = new Fits(u, comp);
        } else {
            f = new Fits(u);
        }
        short[][] data = (short[][]) f.readHDU().getKernel();

        return total(data);
    }

    int streamRead(InputStream is, boolean comp, boolean useComp)
            throws Exception {
        Fits f;
        if (useComp) {
            f = new Fits(is, comp);
        } else {
            f = new Fits(is);
        }
        short[][] data = (short[][]) f.readHDU().getKernel();
        is.close();

        return total(data);
    }

    int fileRead(File is, boolean comp, boolean useComp)
            throws Exception {
        Fits f;
        if (useComp) {
            f = new Fits(is, comp);
        } else {
            f = new Fits(is);
        }
        short[][] data = (short[][]) f.readHDU().getKernel();

        return total(data);
    }

    int stringRead(String is, boolean comp, boolean useComp)
            throws Exception {
        Fits f;
        if (useComp) {
            f = new Fits(is, comp);
        } else {
            f = new Fits(is);
        }
        short[][] data = (short[][]) f.readHDU().getKernel();

        return total(data);
    }

    int total(short[][] data) {
        int total = 0;
        for (int i = 0; i < data.length; i += 1) {
            for (int j = 0; j < data[i].length; j += 1) {
                total += data[i][j];
            }
        }
        return total;
    }
}
