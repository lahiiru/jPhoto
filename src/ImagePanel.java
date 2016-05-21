
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * ImagePanel is the JPanel where editing image resides.
 * New object of this class is added to GUI class when program initializing.
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    public BufferedImage image;
    private Shape shape = null;
    Point startDrag, endDrag;
    private int lX = 0;
    private int lY = 0;
    private boolean move = true;
    private int offsetX = 0;
    private int offsetY = 0;
    private int moveOffsetX = 0;
    private int moveOffsetY = 0;
    
    public ImagePanel(String inputImage, JFrame mf1) {
        try {
            final JFrame mf = mf1;
            image = ImageIO.read(new File(inputImage));
                       
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startDrag = new Point(e.getX(), e.getY());
                    endDrag = startDrag;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                            offsetX=offsetX+moveOffsetX;
                            offsetY=offsetY+moveOffsetY;
                            moveOffsetX=0;
                            moveOffsetY=0;
                    if (endDrag != null && startDrag != null) {
                        try {
                            shape = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
                            //mf.updateSelectedRegion(image.getSubimage(startDrag.x, startDrag.y, e.getX()-startDrag.x, e.getY()-startDrag.y));
                            startDrag = null;
                            endDrag = null;

                            repaint();
                        } catch (Exception e1) {
                        }
                    }
                }
            });

            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    endDrag = new Point(e.getX(), e.getY());
                    repaint();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(offsetX==0 && offsetY==0){
            offsetX = getWidth()>image.getWidth()?getWidth()/2-image.getWidth()/2:0;
            offsetY = getHeight()>image.getHeight()?getHeight()/2-image.getHeight()/2:0;
        }
        Graphics2D g2 = (Graphics2D) g;
        
        g2.drawImage(image, offsetX+moveOffsetX, offsetY+moveOffsetY, null);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

        if (startDrag != null && endDrag != null) {
            if (move) {
                if (endDrag.x != startDrag.x && endDrag.y != startDrag.y) {
                    moveOffsetX = endDrag.x - startDrag.x;
                    moveOffsetY = endDrag.y - startDrag.y;
                }
                endDrag = null;
                //startDrag=null;
                return;
            }

            g2.setPaint(Color.LIGHT_GRAY);
            Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
            g2.draw(r);
            image.getGraphics().setColor(Color.GRAY);
            if (lX == 0) {
                lX = endDrag.x;
            }
            if (lY == 0) {
                lY = endDrag.y;
            }
            image.getGraphics().drawLine(endDrag.x, endDrag.y, lX, lY);
            lX = endDrag.x;
            lY = endDrag.y;
        }

        if (shape != null && !move) {
            g2.setPaint(Color.BLACK);
            g2.draw(shape);
            g2.setPaint(Color.YELLOW);
            g2.fill(shape);
        }

    }

    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2),
                Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

}
