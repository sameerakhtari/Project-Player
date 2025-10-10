/**
 * 🌌 Leap Year Checker GUI Application
 * -------------------------------------
 * This Java Swing application allows users to enter a year and check
 * whether it's a leap year or not. It features:
 * - Dynamic glowing button animation
 * - Floating particle background
 * - Sound feedback (different tone for leap vs. non-leap)
 * - 🧹 Exit button with confirmation dialog
 *
 * @version Hacktoberfest 2025
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.sound.sampled.*;

public class LeapYearCheckerFinal extends JFrame {

    private JTextField yearInput;
    private JLabel resultLabel;
    private JButton checkButton, clearButton;
    private Timer glowTimer, particleTimer;
    private float glowPhase = 0f;
    private java.util.List<Particle> particles = new java.util.ArrayList<>();
    private Random random = new Random();
    private Color particleColor = new Color(0, 200, 255);

    public LeapYearCheckerFinal() {
        setTitle("🌌 Leap Year Checker");
        setSize(480, 360); // slightly taller for new button
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

        JLabel title = new JLabel("Leap Year Checker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 200, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel label = new JLabel("Enter Year:");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(new Color(230, 230, 230));
        panel.add(label, gbc);

        yearInput = new JTextField(10);
        yearInput.setBackground(new Color(30, 30, 30));
        yearInput.setForeground(Color.WHITE);
        yearInput.setCaretColor(Color.CYAN);
        yearInput.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 255)));
        yearInput.setFont(new Font("Consolas", Font.PLAIN, 16));
        gbc.gridx = 1;
        panel.add(yearInput, gbc);

        // "Check" button
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

        // "Clear" button
        clearButton = new JButton("Clear");
        styleButton(clearButton, new Color(255, 180, 0));
        clearButton.addActionListener(e -> clearFields());
        gbc.gridy++;
        panel.add(clearButton, gbc);

        // 🧹 "Exit" button (new feature)
        JButton exitButton = new JButton("Exit");
        styleButton(exitButton, new Color(255, 70, 70));
        exitButton.addActionListener(e -> confirmExit());
        gbc.gridy++;
        panel.add(exitButton, gbc);

        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(Color.WHITE);
        gbc.gridy++;
        panel.add(resultLabel, gbc);

        add(panel);
    }

    /** Button styling helper */
    private void styleButton(JButton button, Color baseColor) {
        button.setBackground(baseColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }

    /** Confirm before exiting the app */
    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /** Animate glow */
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

    /** Initialize particles */
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

    /** Check leap year logic */
    private void checkLeapYear() {
        try {
            int year = Integer.parseInt(yearInput.getText().trim());
            boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);

            playBeep(isLeap);

            if (isLeap) {
                resultLabel.setText(year + " is a Leap Year ✅");
                resultLabel.setForeground(new Color(0, 255, 128));
                particleColor = new Color(0, 255, 128);
            } else {
                resultLabel.setText(year + " is NOT a Leap Year ❌");
                resultLabel.setForeground(new Color(255, 80, 80));
                particleColor = new Color(255, 80, 80);
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("⚠️ Please enter a valid year!");
            resultLabel.setForeground(new Color(255, 200, 0));
            particleColor = new Color(255, 200, 0);
        }
    }

    /** Clears fields */
    private void clearFields() {
        yearInput.setText("");
        resultLabel.setText(" ");
        particleColor = new Color(0, 200, 255);
        repaint();
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

    /** Panel with particles */
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
