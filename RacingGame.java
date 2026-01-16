import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class FrankRacingGame extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrankRacingGame().setVisible(true));
    }

    public FrankRacingGame() {
        setTitle("Frank's Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(new GamePanel());
        pack();
        setLocationRelativeTo(null);
    }
}


class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {

    static final int W = 1000;
    static final int H = 700;

    private final Timer timer = new Timer(16, this); // ~60 FPS
    private long lastNanos = System.nanoTime();

    enum State { MENU, COUNTDOWN, RACE, EXPLODE, RESULTS }
    private State state = State.MENU;

    private int countdownMs = 5000;

    private final Set<Integer> keys = new HashSet<>();

    private PlayerCar player;
    private Track currentTrack;
    private java.util.List<NPCCar> npcs = new ArrayList<>();
    private java.util.List<BoostPad> boosts = new ArrayList<>();
    private java.util.List<Obstacle> obstacles = new ArrayList<>();

    private long raceStartTime;
    private long raceEndTime;
    private int place = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(W, H));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long now = System.nanoTime();
        double dt = (now - lastNanos) / 1_000_000_000.0;
        lastNanos = now;

        update(dt);
        repaint();
    }

