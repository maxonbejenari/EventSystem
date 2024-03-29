import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Window extends Canvas {

    private BufferStrategy bs;
    private Graphics g;
    private JFrame frame;
    private List<Layer> layers = new ArrayList<>();

    // Create Window
    public Window (String name, int width, int height) {
        setPreferredSize(new Dimension(width,height));
        init(name);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                MousePressedEvent event = new MousePressedEvent(e.getButton(), e.getX(), e.getY());
                onEvent(event);
            }

            public void mouseReleased(MouseEvent e) {
                MouseReleasedEvent event = new MouseReleasedEvent(e.getButton(), e.getX(), e.getY());
                onEvent(event);
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                MouseMotionEvent event = new MouseMotionEvent(e.getX(), e.getY(), false);
                onEvent(event);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                MouseMotionEvent event = new MouseMotionEvent(e.getX(), e.getY(), true);
                onEvent(event);
            }
        });

        render();
    }

    private void init(String name) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void render() {
        if (bs == null)
            createBufferStrategy(3);
        bs = getBufferStrategy();

        g = bs.getDrawGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        onRender(g);

        // Clean system resources
        g.dispose();

        bs.show();

        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {

        }

        EventQueue.invokeLater(() -> render());
    }

    private void onRender(Graphics g) {
        for (int i = 0; i  < layers.size(); i++)
            layers.get(i).onRender(g);
    }

    private void onEvent(Event event) {
        for (int i = layers.size() - 1; i >= 0; i--)
            layers.get(i).onEvent(event);
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }
}
