
import java.awt.Color;
import java.awt.image.BufferedImage;

/*
 * ImageProcessor class does all processing functionalitis.
 * Therefore, algorithms for filters are implemented in this class.
 */
public class ImageProcessor {

    NewJFrame window;
    BufferedImage image;

    public ImageProcessor(NewJFrame frame) {
        window = frame;
        image = ((ImagePanel) frame.mainPanel).image;

    }

    public void reloadImageRef() {
        image = ((ImagePanel) window.mainPanel).image;
    }

    public void pixelate(int limit) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; i += limit) {
            for (int j = 0; j < width; j += limit) {
                if (height - i < limit) {
                    i = height - limit;
                }
                if (width - j <= limit) {
                    j = width - limit;
                }

                int red = 0;
                int green = 0;
                int blue = 0;

                for (int k = 0; k < limit; k++) {
                    for (int l = 0; l < limit; l++) {
                        Color c = new Color(image.getRGB(j + l, i + k));
                        red += c.getRed();
                        c = new Color(image.getRGB(j + l, i + k));
                        green += c.getGreen();
                        c = new Color(image.getRGB(j + l, i + k));
                        blue += c.getBlue();
                    }
                }
                red /= limit * limit;
                green /= limit * limit;
                blue /= limit * limit;

                Color newColor = new Color(red, green, blue);

                for (int a = 0; a < limit; a++) {
                    for (int b = 0; b < limit; b++) {
                        image.setRGB(j + a, i + b, newColor.getRGB());
                    }
                }

            }
        }
        window.repaint();
    }

    public void grayscale() {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(image.getRGB(j, i));
                int red = (int) (c.getRed() / 3);
                int green = (int) (c.getGreen() / 3);
                int blue = (int) (c.getBlue() / 3);
                Color newColor = new Color(red + green + blue,
                        red + green + blue, red + green + blue);
                image.setRGB(j, i, newColor.getRGB());
            }
        }
        window.repaint();
    }

    public void transpose() {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage outImg = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outImg.setRGB(i, j, image.getRGB(j, i));
            }
        }
        ((ImagePanel) window.mainPanel).image = outImg;
        reloadImageRef();
        window.repaint();
    }

    void reduce(int limit) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage outImg = new BufferedImage(width / limit, height / limit, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i += limit) {
            for (int j = 0; j < width; j += limit) {
                if (height - i < limit) {
                    i = height - limit;
                }
                if (width - j <= limit) {
                    j = width - limit;
                }

                // RED
                int red = 0;
                int green = 0;
                int blue = 0;

                for (int k = 0; k < limit; k++) {
                    for (int l = 0; l < limit; l++) {
                        Color c = new Color(image.getRGB(j + l, i + k));
                        red += c.getRed();
                        c = new Color(image.getRGB(j + l, i + k));
                        green += c.getGreen();
                        c = new Color(image.getRGB(j + l, i + k));
                        blue += c.getBlue();
                    }
                }
                red /= limit * limit;
                green /= limit * limit;
                blue /= limit * limit;

                Color newColor = new Color(red, green, blue);

                outImg.setRGB((j / limit), (i / limit), newColor.getRGB());

            }

        }
        ((ImagePanel) window.mainPanel).image = outImg;
        reloadImageRef();
        window.repaint();
    }

    void enlarge(int limit) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage outImg = new BufferedImage(width * limit, height * limit, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (height - i < limit) {
                    break;
                }
                if (width - j <= limit) {
                    break;
                }

                Color c = new Color(image.getRGB(j, i));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                Color startColor = new Color(red, green, blue);

                c = new Color(image.getRGB(j + limit - 1, i + limit - 1));
                red += c.getRed();
                green += c.getGreen();
                blue += c.getBlue();

                Color avgColor = new Color(red / 2, green / 2, blue / 2);

                outImg.setRGB(j * limit, i * limit, startColor.getRGB());

                for (int k = 1; k < limit; k++) {
                    outImg.setRGB(j * limit + k, i * limit + k, avgColor.getRGB());
                }

            }

        }
        ((ImagePanel) window.mainPanel).image = outImg;
        reloadImageRef();
        window.repaint();
    }

}
