import javafx.scene.paint.Color;

public class HelloWorld {
	public static void main(String[] args) {
		
		Color c = Color.CADETBLUE;
		int r = (int) (c.getRed()*255);
		int g = (int) (c.getGreen()*255);
		int b = (int) (c.getBlue()*255);
		
		
		System.out.println(r+","+g+","+b);
		
		Color c2 = new Color(r/255d, g/255d, b/255d, 1);
		
		System.out.println((int) (c2.getRed()*255)+","+(int) (c2.getGreen()*255)+","+(int) (c2.getBlue()*255));
	}
}
