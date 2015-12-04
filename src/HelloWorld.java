
import java.awt.Color;


public class HelloWorld {
	public static void main(String[] args) {
		
		Color c = Color.BLUE;
		int r = (int) (c.getRed()*255);
		int g = (int) (c.getGreen()*255);
		int b = (int) (c.getBlue()*255);
		
		
		System.out.println(r+","+g+","+b);
		
		Color c2 = new Color(r/255f, g/255f, b/255f, 1);
		
		System.out.println((int) (c2.getRed()*255)+","+(int) (c2.getGreen()*255)+","+(int) (c2.getBlue()*255));
	}
}
