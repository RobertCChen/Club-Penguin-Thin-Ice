import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;


public class Puffle {

    private int x;
    private int y;
    private Image image;


    public Puffle() {
        initPuffle();
    }

    private void initPuffle() {

        ImageIcon ii = new ImageIcon("images/puffle.gif");
        image = ii.getImage();
        x = 30;
        y = 30;
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

    public void keyPressed(KeyEvent e) {

    	if (Board.field[y/30][x/30] == 5) {
    		Board.hasKey = true;
    	}

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_PERIOD && Board.Level < 10) {
        	Board.Level++;
    		Board.setField();
        }

        if (key == KeyEvent.VK_COMMA && Board.Level > 0) {
        	Board.Level--;
    		Board.setField();
        }

        boolean notBlocked = false;

        if (key == KeyEvent.VK_LEFT && unblocked(y/30, x/30 - 1)) {
        	changeBlock(y/30, x/30);
        	x -= 30;
        	notBlocked = true;
        }
        else if (key == KeyEvent.VK_RIGHT && unblocked(y/30, x/30 + 1)) {
        	changeBlock(y/30, x/30);
        	x += 30;
        	notBlocked = true;
        }
        else if (key == KeyEvent.VK_UP && unblocked(y/30 - 1, x/30)) {
        	changeBlock(y/30, x/30);
        	y -= 30;
        	notBlocked = true;
        }
        else if (key == KeyEvent.VK_DOWN && unblocked(y/30 + 1, x/30)) {
        	changeBlock(y/30, x/30);
        	y += 30;
        	notBlocked = true;
        }

        if (y < 30*15 && Board.field[y/30][x/30] == 3 && notBlocked) {
        	Board.field[y/30][x/30] = 7;
        	if (Board.status.equals("game")) {
        		if (x == (Board.level[Board.Level].indexOf('3') % 19) * 30 &&
        				y == (Board.level[Board.Level].indexOf('3') / 19 * 30)) {
            		x = (Board.level[Board.Level].lastIndexOf('3') % 19) * 30;
            		y = (Board.level[Board.Level].lastIndexOf('3') / 19) * 30;
            	}
            	else {
            		x = (Board.level[Board.Level].indexOf('3') % 19) * 30;
            		y = (Board.level[Board.Level].indexOf('3') / 19) * 30;
            	}
        	} else if (Board.status.equals("sandbox")) {
        		if (x == (Board.custom.indexOf('3') % 19) * 30 &&
        				y == (Board.custom.indexOf('3') / 19 * 30)) {
            		x = (Board.custom.lastIndexOf('3') % 19) * 30;
            		y = (Board.custom.lastIndexOf('3') / 19) * 30;
            	}
            	else {
            		x = (Board.custom.indexOf('3') % 19) * 30;
            		y = (Board.custom.indexOf('3') / 19) * 30;
            	}
        	}


        }

    }

    private boolean unblocked(int row, int col) {
    	if (Board.field[row][col] == 1 ||
			Board.field[row][col] == 2 ||
			Board.field[row][col] == 3 ||
			Board.field[row][col] == 4 ||
			Board.field[row][col] == 5) {
    		return true;
    	}
    	if (Board.field[row][col] == 6 && Board.hasKey) {
    		Board.field[row][col] = 1;
    	}
    	return false;
    }

    private void changeBlock(int row, int col) {
    	if (Board.field[row][col] == 1) {
    		Board.field[row][col] = 100;
    	}
        else if (Board.field[row][col] == 2) {
    		Board.field[row][col]--;
    	}
    	else if (Board.field[row][col] == 3) {
    		Board.field[row][col] = 7;
    	}
    	else if (Board.field[row][col] == 5) {
    		Board.field[row][col] = 100;
    	}
    }
}
