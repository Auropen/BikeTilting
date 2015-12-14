package domain;

public enum EColor {
	RED("R�D"),BLUE("BL�"),GREEN("GR�N"),YELLOW("GUL"),BROWN("BRUN"),
	ORANGE("ORANGE"),WHITE("HVID"),GRAY("GR�"),BLACK("SORT"),
	DARKRED("M�RKER�D"),DARKBLUE("M�RKEBL�"),DARKGREEN("M�RKEGR�N"),DARKBROWN("M�RKEBRUN"),
	LIGHTRED("LYSER�D"),LIGHTBLUE("LYSEBL�"),LIGHTGREEN("LYSEGR�N"),LIGHTBROWN("LYSEBRUN"),
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
