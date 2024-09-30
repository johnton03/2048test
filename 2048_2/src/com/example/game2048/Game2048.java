package com.example.game2048;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class Game2048 extends JPanel {
    private static final int SIZE = 4;
    private static final int TILE_SIZE = 100;
    private static final int GAP_SIZE = 15;
    private int[][] board;
    private int[][] previousBoard;
    private int score;
    private int previousScore;
    private boolean isGameOver;
    private int highestScore;

    private JLabel scoreLabel;
    private JLabel highestScoreLabel;

    public Game2048(JLabel scoreLabel, JLabel highestScoreLabel) {
        this.scoreLabel = scoreLabel;
        this.highestScoreLabel = highestScoreLabel;

        setPreferredSize(new Dimension(SIZE * TILE_SIZE + (SIZE + 1) * GAP_SIZE, SIZE * TILE_SIZE + (SIZE + 1) * GAP_SIZE));
        setBackground(new Color(0xbbada0));
        setFocusable(true);

        board = new int[SIZE][SIZE];
        previousBoard = new int[SIZE][SIZE];
        score = 0;
        previousScore = 0;
        isGameOver = false;
        highestScore = 0;

        addRandomTile();
        addRandomTile();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isGameOver) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                        case KeyEvent.VK_UP:
                            move('w');
                            break;
                        case KeyEvent.VK_S:
                        case KeyEvent.VK_DOWN:
                            move('s');
                            break;
                        case KeyEvent.VK_A:
                        case KeyEvent.VK_LEFT:
                            move('a');
                            break;
                        case KeyEvent.VK_D:
                        case KeyEvent.VK_RIGHT:
                            move('d');
                            break;
                        case KeyEvent.VK_Z:
                            undo();
                            break;
                    }
                    updateLabels();
                    repaint();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(new Color(0xbbada0));
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                drawTile(g, board[i][j], i, j);
            }
        }

        if (isGameOver) {
            g.setColor(new Color(255, 0, 0, 128));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("游戏结束！", 50, getHeight() / 2 - 50);
            g.drawString("当前得分: " + score, 50, getHeight() / 2);
            g.drawString("最高得分: " + highestScore, 50, getHeight() / 2 + 50);
        }
    }

    private void drawTile(Graphics g, int value, int row, int col) {
        int x = col * TILE_SIZE + (col + 1) * GAP_SIZE;
        int y = row * TILE_SIZE + (row + 1) * GAP_SIZE;

        g.setColor(getTileColor(value));
        g.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, 15, 15);

        g.setColor(getFontColor(value));
        g.setFont(new Font("Arial", Font.BOLD, 36));

        if (value != 0) {
            String s = String.valueOf(value);
            FontMetrics fm = getFontMetrics(g.getFont());
            int asc = fm.getAscent();
            int dec = fm.getDescent();
            g.drawString(s, x + (TILE_SIZE - fm.stringWidth(s)) / 2, y + (asc + (TILE_SIZE - (asc + dec)) / 2));
        }
    }

    private Color getTileColor(int value) {
        switch (value) {
            case 2: return new Color(0xeee4da);
            case 4: return new Color(0xede0c8);
            case 8: return new Color(0xf2b179);
            case 16: return new Color(0xf59563);
            case 32: return new Color(0xf67c5f);
            case 64: return new Color(0xf65e3b);
            case 128: return new Color(0xedcf72);
            case 256: return new Color(0xedcc61);
            case 512: return new Color(0xedc850);
            case 1024: return new Color(0xedc53f);
            case 2048: return new Color(0xedc22e);
        }
        return new Color(0xcdc1b4);
    }

    private Color getFontColor(int value) {
        return value < 16 ? new Color(0x776e65) : new Color(0xf9f6f2);
    }

    private void addRandomTile() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(SIZE);
            col = rand.nextInt(SIZE);
        } while (board[row][col] != 0);
        board[row][col] = rand.nextInt(10) == 0 ? 4 : 2;
    }

    private void move(char direction) {
        if (isGameOver) return;

        savePreviousState();
        boolean moved = false;
        switch (direction) {
            case 'w': moved = moveUp(); break;
            case 's': moved = moveDown(); break;
            case 'a': moved = moveLeft(); break;
            case 'd': moved = moveRight(); break;
        }

        if (moved) {
            addRandomTile();
            if (checkGameOver()) {
                isGameOver = true;
                highestScore = Math.max(highestScore, score);
                if (!showGameOverDialog()) {
                    System.exit(0); // Exit the application
                } else {
                    newGame(); // Restart game if the user chooses to
                }
            }
        }
    }

    private boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            int[] newRow = new int[SIZE];
            int index = 0;
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != 0) {
                    newRow[index++] = board[i][j];
                }
            }
            for (int j = 0; j < SIZE - 1; j++) {
                if (newRow[j] == newRow[j + 1] && newRow[j] != 0) {
                    newRow[j] *= 2;
                    score += newRow[j];
                    newRow[j + 1] = 0;
                }
            }
            int[] finalRow = new int[SIZE];
            index = 0;
            for (int j = 0; j < SIZE; j++) {
                if (newRow[j] != 0) {
                    finalRow[index++] = newRow[j];
                }
            }
            if (!java.util.Arrays.equals(board[i], finalRow)) {
                moved = true;
                board[i] = finalRow;
            }
        }
        return moved;
    }

    private boolean moveRight() {
        rotateBoard();
        rotateBoard();
        boolean moved = moveLeft();
        rotateBoard();
        rotateBoard();
        return moved;
    }

    private boolean moveUp() {
        rotateBoard();
        rotateBoard();
        rotateBoard();
        boolean moved = moveLeft();
        rotateBoard();
        return moved;
    }

    private boolean moveDown() {

        rotateBoard();
        boolean moved = moveLeft();
        rotateBoard();
        rotateBoard();
        rotateBoard();
        return moved;
    }

    private void rotateBoard() {
        int[][] newBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newBoard[i][j] = board[SIZE - 1 - j][i];
            }
        }
        board = newBoard;
    }

    private boolean checkGameOver() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) return false;
                if (j < SIZE - 1 && board[i][j] == board[i][j + 1]) return false;
                if (i < SIZE - 1 && board[i][j] == board[i + 1][j]) return false;
            }
        }
        return true;
    }

    private void savePreviousState() {
        previousBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, previousBoard[i], 0, SIZE);
        }
        previousScore = score;
    }

    public void undo() {
        if (previousBoard != null) {
            board = new int[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) {
                System.arraycopy(previousBoard[i], 0, board[i], 0, SIZE);
            }
            score = previousScore;
            previousBoard = null;
            repaint();
        }
    }

    public void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("game2048.sav"))) {
            out.writeObject(board);
            out.writeInt(score);
            out.writeBoolean(isGameOver);
            out.writeInt(highestScore);  // Save highest score
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("game2048.sav"))) {
            board = (int[][]) in.readObject();
            score = in.readInt();
            isGameOver = in.readBoolean();
            highestScore = in.readInt();  // Load highest score
            repaint();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void newGame() {
        board = new int[SIZE][SIZE];
        previousBoard = null;
        score = 0;
        previousScore = 0;
        isGameOver = false;
        addRandomTile();
        addRandomTile();
        updateLabels(); // Update labels with new game data
        repaint();
    }

    public void showGameRules() {
        String rules = "2048 游戏规则：\n\n" +
                "使用方向键（WASD）移动方块。\n" +
                "当两个相同的方块碰撞时，它们会合并成一个！\n" +
                "将方块合并到 2048 即为胜利！";
        JOptionPane.showMessageDialog(this, rules, "游戏规则", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateLabels() {
        scoreLabel.setText("当前得分: " + score);
        highestScoreLabel.setText("最高得分: " + highestScore);
    }

    private boolean showGameOverDialog() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "游戏结束！是否重新开始游戏？",
                "游戏结束",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return response == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("2048");

        JLabel scoreLabel = new JLabel("当前得分: 0", JLabel.RIGHT);
        JLabel highestScoreLabel = new JLabel("最高得分: 0", JLabel.RIGHT);

        Game2048 game = new Game2048(scoreLabel, highestScoreLabel);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("游戏");

        JMenuItem newGameItem = new JMenuItem("新游戏");
        JMenuItem saveGameItem = new JMenuItem("保存游戏");
        JMenuItem loadGameItem = new JMenuItem("加载游戏");
        JMenuItem undoItem = new JMenuItem("撤销");
        JMenuItem gameRulesItem = new JMenuItem("游戏规则");

        newGameItem.addActionListener(e -> game.newGame());
        saveGameItem.addActionListener(e -> game.saveGame());
        loadGameItem.addActionListener(e -> game.loadGame());
        undoItem.addActionListener(e -> game.undo());
        gameRulesItem.addActionListener(e -> game.showGameRules());

        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        gameMenu.add(undoItem);
        gameMenu.add(gameRulesItem);

        // Create a panel to hold the score labels and add it to the menu bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.add(scoreLabel);
        statusPanel.add(highestScoreLabel);

        menuBar.add(gameMenu);
        menuBar.add(Box.createHorizontalGlue()); // Push the score panel to the right
        menuBar.add(statusPanel);

        frame.setJMenuBar(menuBar);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

