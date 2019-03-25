package com.example.e3soft;

/**
 * Created by dna00 on 2016-04-07.
 */
public class Calculations {

    public static Double calculateSV(Double tmpResult, Double tmpCf, String tmpReading) {
        Double sv;
        sv = (tmpResult * tmpCf);
        if (tmpReading.equals("LEL")) {
            sv = ((sv * 1000) / 2);
        } else if (tmpReading.equals("GAS %")) {
            sv = (sv * 10000);
        }
        return sv;
    }

    public static String calculateLossBySource(Double sv, String tmpSource) {
        String lossT;
        Double lRa = 0.0;
        if (sv > 100000) {
            if (tmpSource.equals("Valve Stem") || tmpSource.equals("Valve Gland") || tmpSource.equals("Valve Bonnet")) {
                lRa = (0.14 * 8760);
            } else if (tmpSource.equals("Union")) {
                lRa = (0.084 * 8760);
            } else if (tmpSource.equals("Flange")) {
                lRa = (0.03 * 8760);
            } else if (tmpSource.equals("Open End")) {
                lRa = (0.079 * 8760);
            } else if (tmpSource.equals("Pump Seal")) {
                lRa = (0.16 * 8760);
            } else if (tmpSource.equals("Compressor Piston") || tmpSource.equals("Cap") || tmpSource.equals("Relief Valve")
                    || tmpSource.equals("Cylinder Cover") || tmpSource.equals("Plug") || tmpSource.equals("Hose") || tmpSource.equals("Pinhole")
                    || tmpSource.equals("Other Seal")) {
                lRa = (0.11 * 8760);
            } else {
                lossT = "Not Defined";
            }
        } else {
            if (tmpSource.equals("Valve Stem") || tmpSource.equals("Valve Gland") || tmpSource.equals("Valve Bonnet")) {
                Double tmplRa = Math.pow(sv, 0.746);
                lRa = 4.61E-6 * 8760 * tmplRa;
            } else if (tmpSource.equals("Flange")) {
                Double tmplRa = Math.pow(sv, 0.703);
                lRa = 4.61E-6 * 8760 * tmplRa;
            } else if (tmpSource.equals("Union") || tmpSource.equals("Cap") || tmpSource.equals("Plug") || tmpSource.equals("Hose") || tmpSource.equals("Pinhole")) {
                Double tmplRa = Math.pow(sv, 0.735);
                lRa = 1.53E-6 * 8760 * tmplRa;
            } else if (tmpSource.equals("Open End")) {
                Double tmplRa = Math.pow(sv, 0.704);
                lRa = 2.2E-6 * 8760 * tmplRa;
            } else if (tmpSource.equals("Pump Seal")) {
                Double tmplRa = Math.pow(sv, 0.61);
                lRa = 5.03E-5 * 8760 * tmplRa;
            } else {
                Double tmplRa = Math.pow(sv, 0.589);
                lRa = 2.29E-5 * 8760 * tmplRa;
            }
        }
        if (lRa == 1.0000000199E7) {
            lossT = "Not Defined";
        } else {
            lossT = Double.toString(lRa);
        }
        if (!lossT.equals("Not Defined")) {

            lossT = Double.toString(lRa);
        }
        return lossT;
    }
}
