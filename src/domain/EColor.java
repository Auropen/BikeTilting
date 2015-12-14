package domain;

public enum EColor {
	RED("RØD"),BLUE("BLÅ"),GREEN("GRØN"),YELLOW("GUL"),BROWN("BRUN"),
	ORANGE("ORANGE"),WHITE("HVID"),GRAY("GRÅ"),BLACK("SORT"),
	DARKRED("MØRKERØD"),DARKBLUE("MØRKEBLÅ"),DARKGREEN("MØRKEGRØN"),DARKBROWN("MØRKEBRUN"),
	LIGHTRED("LYSERØD"),LIGHTBLUE("LYSEBLÅ"),LIGHTGREEN("LYSEGRØN"),LIGHTBROWN("LYSEBRUN"),
	TURQUOISE("TURKIS"),PINK("PINK"),LIME("LIME"),PURPLE("LILLA");
	
	private String danishWord;
	
	private EColor(String danishWord) {
		this.danishWord = danishWord;
	}
	
	public String danishValue() {
		return danishWord;
	}
	
	public static String getDanishValue(String s) {
		for (EColor c : values()) {
			if (c.name().equalsIgnoreCase(s))
				return c.danishValue();
		}
		return null;
	}
}
