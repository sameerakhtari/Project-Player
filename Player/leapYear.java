/**
 * 🌌 Leap Year Checker GUI Application
 * -------------------------------------
 * This Java Swing application allows users to enter a year and check
 * whether it's a leap year or not. It features:
 * - Dynamic glowing button animation
 * - Floating particle background
 * - Sound feedback (different tone for leap vs. non-leap)
 *
 * @version Hacktoberfest 2025
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.sound.sampled.*;

public class LeapYearCheckerFinal extends JFrame {

    
    private JTextField yearInput; /** Input field for year entry */
    private JLabel resultLabel; /** Label to display the result */
    private JButton checkButton; /** Button to trigger leap year check */
    private Timer glowTimer, particleTimer; /** Timers for glow and particle animations */
    private float glowPhase = 0f; /** Controls color animation phase */
    private java.util.List<Particle> particles = new java.util.ArrayList<>(); /** List of animated background particles */
    private Random random = new Random(); /** Random number generator for particle positions */
    private Color particleColor = new Color(0, 200, 255); // default cyan /** Color used for particles and UI glow */

    /** Constructor sets up the window and animations */
    public LeapYearCheckerFinal() {
        setTitle("🌌 Leap Year Checker");
        setSize(480, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        initGlowAnimation();
        initParticles();
    }

    /** Initialize UI components */
    private void initUI() {
        JPanel panel = new ParticlePanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel title = new JLabel("Leap Year Checker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 200, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Input label
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel label = new JLabel("Enter Year:");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(new Color(230, 230, 230));
        panel.add(label, gbc);

        // Input field
        yearInput = new JTextField(10);
        yearInput.setBackground(new Color(30, 30, 30));
        yearInput.setForeground(Color.WHITE);
        yearInput.setCaretColor(Color.CYAN);
        yearInput.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 255)));
        yearInput.setFont(new Font("Consolas", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(yearInput, gbc);

        // Button
        checkButton = new JButton("Check");
        checkButton.setBackground(new Color(0, 180, 255));
        checkButton.setForeground(Color.BLACK);
        checkButton.setFocusPainted(false);
        checkButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        checkButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                checkButton.setBackground(new Color(0, 220, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                checkButton.setBackground(new Color(0, 180, 255));
            }
        });

        checkButton.addActionListener(e -> checkLeapYear());

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(checkButton, gbc);

        // Result
        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(Color.WHITE);
        gbc.gridy++;
        panel.add(resultLabel, gbc);

        add(panel);
    }

    /** Animate button glow */
    private void initGlowAnimation() {
        glowTimer = new Timer(50, e -> {
            glowPhase += 0.1f;
            float intensity = (float) ((Math.sin(glowPhase) + 1) / 2);
            int green = (int) (180 + 60 * intensity);
            int blue = (int) (255 - 40 * intensity);
            checkButton.setBackground(new Color(0, green, blue));
        });
        glowTimer.start();
    }

    /** Initialize moving particles */
    private void initParticles() {
        for (int i = 0; i < 40; i++) {
            particles.add(new Particle(random.nextInt(480), random.nextInt(300)));
        }

        particleTimer = new Timer(40, e -> {
            for (Particle p : particles) {
                p.y += p.speed;
                if (p.y > 300) {
                    p.y = 0;
                    p.x = random.nextInt(480);
                }
            }
            repaint();
        });
        particleTimer.start();
    }

    /** Play sound feedback */
    private void playBeep(boolean isLeap) {
        try {
            int freq = isLeap ? 880 : 440;
            int ms = 150;
            float sampleRate = 8000f;
            byte[] buf = new byte[(int) (ms * sampleRate / 1000)];
            for (int i = 0; i < buf.length; i++) {
                double angle = i / (sampleRate / freq) * 2.0 * Math.PI;
                buf[i] = (byte) (Math.sin(angle) * 127);
            }
            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)) {
                sdl.open(af);
                sdl.start();
                sdl.write(buf, 0, buf.length);
                sdl.drain();
            }
        } catch (Exception ignored) {}
    }

    /** Check year and trigger visual/audio feedback */
    /** Checks the input year and updates the UI with result */
    private void checkLeapYear() {
        try {
            int year = Integer.parseInt(yearInput.getText().trim());
            boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);

            playBeep(isLeap);

            if (isLeap) {
                resultLabel.setText(year + " is a Leap Year ✅");
                resultLabel.setForeground(new Color(0, 255, 128));
                particleColor = new Color(0, 255, 128); // green glow
            } else {
                resultLabel.setText(year + " is NOT a Leap Year ❌");
                resultLabel.setForeground(new Color(255, 80, 80));
                particleColor = new Color(255, 80, 80); // red glow
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("⚠️ Please enter a valid year!");
            resultLabel.setForeground(new Color(255, 200, 0));
            particleColor = new Color(255, 200, 0); // yellow glow for error
        }
    }

    /** Particle model */
    private class Particle {
        int x, y;
        float speed;

        Particle(int x, int y) {
            this.x = x;
            this.y = y;
            this.speed = 1f + random.nextFloat() * 2f;
        }
    }

    /** Panel rendering animated background */
    private class ParticlePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(15, 15, 15));
            g2.fillRect(0, 0, getWidth(), getHeight());

            for (Particle p : particles) {
                g2.setColor(new Color(
                    particleColor.getRed(),
                    particleColor.getGreen(),
                    particleColor.getBlue(),
                    130
                ));
                g2.fillOval(p.x, p.y, 5, 5);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LeapYearCheckerFinal().setVisible(true));
    }
}
