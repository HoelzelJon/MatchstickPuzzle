package jonathan.hoelzel.matchsticks.drawing;

import jonathan.hoelzel.matchsticks.Direction;
import jonathan.hoelzel.matchsticks.Util.Grid;
import jonathan.hoelzel.matchsticks.Util.Vector;
import jonathan.hoelzel.matchsticks.generator.Puzzle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PuzzleImageSaver {
    private static final int CELL_SIZE = 50;
    private static final int BORDER_SIZE = 25;

    private static final int FONT_BORDER_SIZE_WIDTH = 30;
    private static final int FONT_BORDER_SIZE_HEIGHT = 35;
    private static final int FONT_SIZE = 30;
    private static final int FONT_COLUMNS_X_OFFSET = 18;
    private static final int FONT_COLUMNS_Y_OFFSET = -20;
    private static final int FONT_ROWS_X_OFFSET = -40;
    private static final int FONT_ROWS_Y_OFFSET = 34;

    private static final int MATCHSTICK_STICK_THICKNESS = 10;
    private static final int MATCHSTICK_TAIL_LENGTH_PAST_CENTER = 8;


    private static final int MATCH_HEAD_WIDTH = 20;
    private static final int MATCH_HEAD_LENGTH = 36;
    private static final int MATCH_HEAD_X = -28;
    private static final int MATCH_HEAD_DEGREES_OPEN = 80;

    public void saveImages(List<Puzzle> puzzles, String nameBase) throws IOException {
        for (int i = 0; i < puzzles.size(); i ++) {
            Puzzle puzzle = puzzles.get(i);
            File outputFile = new File(nameBase + i + ".png");
            ImageIO.write(getImage(puzzle), "png", outputFile);
        }
    }

    private BufferedImage getImage(Puzzle puzzle) {
        Grid<Direction> headDirections = puzzle.getHeadDirections();
        int widthPixels = (headDirections.width() * CELL_SIZE) + (BORDER_SIZE * 2) + FONT_BORDER_SIZE_WIDTH;
        int heightPixels = (headDirections.height() * CELL_SIZE) + (BORDER_SIZE * 2) + FONT_BORDER_SIZE_HEIGHT;

        BufferedImage image = new BufferedImage(widthPixels, heightPixels, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(new Font("", Font.PLAIN, FONT_SIZE));

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, widthPixels, heightPixels);
        g2d.setColor(Color.BLACK);

        for (int x = 0; x <= headDirections.width(); x++) {
            int xPixel = toPixelPosition(x);
            g2d.drawLine(xPixel, toPixelPosition(0), xPixel, toPixelPosition(headDirections.height()));
            if (x < headDirections.width()) {
                g2d.drawString(Integer.toString(puzzle.getBurntPerColumn().get(x)),
                        xPixel + FONT_COLUMNS_X_OFFSET,
                        toPixelPosition(headDirections.height() + 1) + FONT_COLUMNS_Y_OFFSET);
            }
        }
        for (int y = 0; y <= headDirections.height(); y ++) {
            int yPixel = toPixelPosition(y);
            g2d.drawLine(toPixelPosition(0), yPixel, toPixelPosition(headDirections.width()), yPixel);
            if (y < headDirections.height()) {
                g2d.drawString(Integer.toString(puzzle.getBurntPerRow().get(y)),
                        toPixelPosition(headDirections.width() + 1) + FONT_ROWS_X_OFFSET,
                        yPixel + FONT_ROWS_Y_OFFSET);
            }
        }

        g2d.setStroke(new BasicStroke(2));
        for (Vector v : headDirections) {
            addMatchstickPartToCell(g2d, v, headDirections);
        }

        return image;
    }

    private void addMatchstickPartToCell(Graphics2D g2d, Vector pos, Grid<Direction> headDirections) {
        Direction dir = headDirections.get(pos);
        Vector towardsHead = pos.plus(dir.vector());
        Vector awayFromHead = pos.minus(dir.vector());

        CellPosition cell = new CellPosition(pos);

        AffineTransform transform = g2d.getTransform();
        g2d.translate(cell.middleX, cell.middleY);
        g2d.rotate(- dir.angle());

        if (!headDirections.inBounds(towardsHead) || !headDirections.get(towardsHead).equals(dir)) {
            addMatchstickHeadToCell(g2d);
        } else if (!headDirections.inBounds(awayFromHead) || !headDirections.get(awayFromHead).equals(dir)) {
            addMatchstickTailToCell(g2d);
        } else {
            addMatchstickMiddleToCell(g2d);
        }
        g2d.setTransform(transform);
    }

    private void addMatchstickHeadToCell(Graphics2D g2d) {
        g2d.draw(new Arc2D.Double(MATCH_HEAD_X,
                - MATCH_HEAD_WIDTH / 2,
                MATCH_HEAD_LENGTH,
                MATCH_HEAD_WIDTH,
                180 + (MATCH_HEAD_DEGREES_OPEN / 2),
                360 - MATCH_HEAD_DEGREES_OPEN,
                Arc2D.OPEN));
    }

    private void addMatchstickTailToCell(Graphics2D g2d) {
        g2d.drawLine(- MATCHSTICK_TAIL_LENGTH_PAST_CENTER, - MATCHSTICK_STICK_THICKNESS / 2, CELL_SIZE / 2, - MATCHSTICK_STICK_THICKNESS / 2);
        g2d.drawLine(- MATCHSTICK_TAIL_LENGTH_PAST_CENTER, MATCHSTICK_STICK_THICKNESS / 2, CELL_SIZE / 2, MATCHSTICK_STICK_THICKNESS / 2);
        g2d.drawLine(- MATCHSTICK_TAIL_LENGTH_PAST_CENTER, - MATCHSTICK_STICK_THICKNESS / 2, - MATCHSTICK_TAIL_LENGTH_PAST_CENTER, MATCHSTICK_STICK_THICKNESS / 2);
    }

    private void addMatchstickMiddleToCell(Graphics2D g2d) {
        g2d.drawLine(- CELL_SIZE / 2, - MATCHSTICK_STICK_THICKNESS / 2, CELL_SIZE / 2, - MATCHSTICK_STICK_THICKNESS / 2);
        g2d.drawLine(- CELL_SIZE / 2, MATCHSTICK_STICK_THICKNESS / 2, CELL_SIZE / 2, MATCHSTICK_STICK_THICKNESS / 2);
    }

    private static int toPixelPosition(int index) {
        return (index * CELL_SIZE) + BORDER_SIZE;
    }

    private static class CellPosition {
        private final int topY;
        private final int bottomY;
        private final int leftX;
        private final int rightX;
        private final int middleX;
        private final int middleY;

        private CellPosition(Vector pos) {
            topY = toPixelPosition(pos.getY());
            bottomY = toPixelPosition(pos.getY() + 1);
            leftX = toPixelPosition(pos.getX());
            rightX = toPixelPosition(pos.getX() + 1);
            middleX = (leftX + rightX) / 2;
            middleY = (topY + bottomY) / 2;
        }
    }
}
