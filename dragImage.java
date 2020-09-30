import java.awt.Image;
import javax.swing.ImageIcon;

public class dragImage {

    private int x;
    private int y;
    private Image image;
 
    public dragImage(String imageName) {
    	ImageIcon ii = new ImageIcon(imageName);
        image = ii.getImage();
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

	public Image getImage() {
		return image;
	}
    
}