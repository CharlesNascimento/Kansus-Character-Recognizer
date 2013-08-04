package org.kansus.ocr;

import java.io.File;

/**
 * Classe auxiliar com as variáveis globais da aplicação.
 * 
 * @author Charles
 */
public class Globals {

	public static int threshold = 150;

	public static float shortRowFraction = 0.125f;
	public static float liberalPolicyAreaWhitespaceFraction = 0.95f;
	public static float minSpaceWidthAsFractionOfRowHeight = 0.30f;
	public static float minCharWidthAsFractionOfRowHeight = 0.16f;
	public static float minCharBreakWidthAsFractionOfRowHeight = 0.05f;

	public static File inputImage;
	public static String inputImageFormat = "";
}