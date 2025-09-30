import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ColorfulGuessingGame extends JFrame {

    // Panels
    private JPanel startPanel, gamePanel;
    private JLabel infoLabel, attemptsLabel, scoreLabel;
    private JTextField guessField;
    private JButton guessButton;

    // Game state
    private int numberToGuess;
    private int attemptsUsed;
    private int maxAttempts;
    private int totalScore;
    private Random random = new Random();

    public ColorfulGuessingGame() {
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        // Start page
        showStartPage();
    }

    private void showStartPage() {
        getContentPane().removeAll();

        startPanel = new JPanel(new BorderLayout(20, 20));
        startPanel.setBackground(new Color(240, 248, 255));
        startPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Number Guessing Game", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(25, 25, 112));
        startPanel.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 15, 15));
        center.setBackground(new Color(240, 248, 255));

        JLabel subtitle = new JLabel("Choose Difficulty Level:", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(70, 70, 70));
        center.add(subtitle);

        JButton easyBtn = styledButton("Easy (10 attempts)", new Color(144, 238, 144));
        JButton mediumBtn = styledButton("Medium (7 attempts)", new Color(255, 215, 0));
        JButton hardBtn = styledButton("Hard (5 attempts)", new Color(240, 128, 128));

        center.add(easyBtn);
        center.add(mediumBtn);
        center.add(hardBtn);

        startPanel.add(center, BorderLayout.CENTER);

        add(startPanel);
        revalidate();
        repaint();
        setVisible(true);

        // Button actions
        easyBtn.addActionListener(e -> startGame(10));
        mediumBtn.addActionListener(e -> startGame(7));
        hardBtn.addActionListener(e -> startGame(5));
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void startGame(int attempts) {
        this.maxAttempts = attempts;
        this.attemptsUsed = 0;
        this.numberToGuess = random.nextInt(100) + 1;

        getContentPane().removeAll();

        gamePanel = new JPanel(new BorderLayout(20, 20));
        gamePanel.setBackground(new Color(255, 250, 240));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        infoLabel = new JLabel("Guess a number between 1 and 100", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoLabel.setForeground(new Color(25, 25, 112));
        gamePanel.add(infoLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(new Color(255, 250, 240));

        guessField = new JTextField(10);
        guessField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        guessButton = styledButton("Guess", new Color(173, 216, 230));

        inputPanel.add(new JLabel("Enter your guess: "));
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        gamePanel.add(inputPanel, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new GridLayout(1, 2));
        statusPanel.setBackground(new Color(255, 250, 240));

        attemptsLabel = new JLabel("Attempts: 0 / " + maxAttempts);
        attemptsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        scoreLabel = new JLabel("Score: " + totalScore, SwingConstants.RIGHT);
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        statusPanel.add(attemptsLabel);
        statusPanel.add(scoreLabel);
        gamePanel.add(statusPanel, BorderLayout.SOUTH);

        add(gamePanel);
        revalidate();
        repaint();

        // Actions
        guessButton.addActionListener(e -> handleGuess());
        guessField.addActionListener(e -> handleGuess()); // Enter key
    }

    private void handleGuess() {
        String text = guessField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a number.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (guess < 1 || guess > 100) {
            JOptionPane.showMessageDialog(this, "Number must be between 1 and 100.", "Out of Range", JOptionPane.WARNING_MESSAGE);
            return;
        }

        attemptsUsed++;
        if (guess == numberToGuess) {
            int score = (maxAttempts - attemptsUsed + 1) * 10;
            totalScore += Math.max(score, 0);
            infoLabel.setText("Correct! The number was " + numberToGuess);
            JOptionPane.showMessageDialog(this, "You guessed it in " + attemptsUsed + " tries!\nScore: " + score, "You Win!", JOptionPane.INFORMATION_MESSAGE);
            endRound();
        } else if (attemptsUsed >= maxAttempts) {
            infoLabel.setText("No attempts left! The number was " + numberToGuess);
            JOptionPane.showMessageDialog(this, "Out of attempts! Number was " + numberToGuess, "Game Over", JOptionPane.ERROR_MESSAGE);
            endRound();
        } else {
            if (guess < numberToGuess) {
                infoLabel.setText("Higher than " + guess);
            } else {
                infoLabel.setText("Lower than " + guess);
            }
        }

        attemptsLabel.setText("Attempts: " + attemptsUsed + " / " + maxAttempts);
        scoreLabel.setText("Score: " + totalScore);
        guessField.setText("");
    }

    private void endRound() {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);

        int option = JOptionPane.showConfirmDialog(this, "Play again?", "Next Round", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            showStartPage();
        } else {
            JOptionPane.showMessageDialog(this, "Final Score: " + totalScore, "Thanks for playing!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ColorfulGuessingGame());
    }
}
