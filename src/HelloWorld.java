import javafx.scene.paint.Color;

public class HelloWorld {
	public static void main(String[] args) {
		System.out.println("Hello World");
		System.out.println(createSQLQuery("tblUsers", "fldFName", "bo", "fldLName", "boensen", "fldAccessLevel", 10.5));
		
		Color c = Color.CADETBLUE;
		int r = (int) (c.getRed()*255);
		int g = (int) (c.getGreen()*255);
		int b = (int) (c.getBlue()*255);
		
		
		System.out.println(r+","+g+","+b);
		
		Color c2 = new Color(r/255d, g/255d, b/255d, 1);
		
		System.out.println((int) (c2.getRed()*255)+","+(int) (c2.getGreen()*255)+","+(int) (c2.getBlue()*255));
	}
	
	public static String createSQLQuery(String table, Object... data) {
		String sql = "INSERT INTO " + table + "(";
		for (int i = 0; i < data.length; i += 2) sql += data[i] + ",";
		sql = sql.substring(0, sql.length()-1) + ") VALUES (";
		for (int i = 1; i < data.length; i += 2) sql += ((isNumber(data[i])) ? data[i] : "'"+data[i]+"'") + ",";
		return sql.substring(0, sql.length()-1) + ")";
	}
	
	public static String selectAllSQLQuery(String table) {
		return "SELECT * FROM " + table + ";";
	}
	
	private static boolean isNumber(Object o) {
		return o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float;
	}
}
