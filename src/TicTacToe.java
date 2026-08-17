import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;
import javax.imageio.ImageIO;

import javax.sound.sampled.*;
import java.io.File;


public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 650;

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton restartButton = new JButton("Restart");
    JButton musicButton = new JButton("Music: Open");
    Clip bgMusic;
    boolean isMusicPlaying = false;

    JButton[][] board = new JButton[3][3];
    String x = "X";
    String o = "O";
    String currentPlayer = x;

    boolean gameOver = false;
    int turn = 0;

    public TicTacToe(){
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(Color.decode("#dcedc8"));
        textLabel.setForeground(Color.black);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic Tac Toe");
        textLabel.setOpaque(true);

        restartButton.setFocusable(false);
        restartButton.setBackground(Color.decode("#dcedc8"));
        restartButton.setBorder(BorderFactory.createEmptyBorder());

        musicButton.setFont(new Font("Arial", Font.BOLD, 15));
        musicButton.setFocusable(false);
        musicButton.setBackground(Color.decode("#dcedc8"));
        musicButton.setForeground(Color.black);
        musicButton.setBorder(BorderFactory.createEmptyBorder());

        musicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bgMusic != null) {
                    if (isMusicPlaying) {
                        bgMusic.stop();
                        isMusicPlaying = false;
                        musicButton.setText("Music: OFF");
                    } else {
                        bgMusic.start();
                        bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
                        isMusicPlaying = true;
                        musicButton.setText("Music: OPEN");
                    }
                }
            }
        });

        try {
            URL imageUrl = new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTWRtpaUBNmNkRve369AUU1bRhscZSVQzQLNcRvu9Xk9A&s=10");
            Image img = ImageIO.read(imageUrl);

            Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            restartButton.setIcon(new ImageIcon(scaledImg));
            restartButton.setText("");
        } catch (Exception ex) {
            System.out.println("Icon can not download: " + ex.getMessage());
            restartButton.setText("Restart");
        }

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        textPanel.add(musicButton, BorderLayout.WEST);
        textPanel.add(restartButton, BorderLayout.EAST);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.decode("#dcedc8"));
        frame.add(boardPanel);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                JButton tile = new JButton();
                 board[i][j] = tile;
                 boardPanel.add(tile);

                 tile.setBackground(Color.decode("#dcedc8"));
                 tile.setFont(new Font("Arial", Font.BOLD, 120));
                 tile.setForeground(Color.black);
                 tile.setFocusable(false);


                 tile.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                         if (gameOver) return;

                         JButton tile = (JButton) e.getSource();

                         if (tile.getText() == ""){
                             tile.setText(currentPlayer);
                             turn++;
                             checkWinner();
                             if (!gameOver){
                                 if (currentPlayer == x){currentPlayer = o;
                                 } else {currentPlayer = x;}

                                 textLabel.setText(currentPlayer + " 's turn");
                             }
                         }
                     }
                 });
            }
        }

        frame.setVisible(true);
        loadAndPlayMusic();
        chooseStartingPlayer();

    }

    public void checkWinner(){
        for (int i = 0; i < 3; i++){
            if (board[i][0].getText() == "") continue;

            if (board[i][0].getText() == board[i][1].getText() &&
                board[i][1].getText() == board[i][2].getText()){

                for (int j = 0; j < 3; j++){
                    setWinner(board[i][j]);
                }
                gameOver = true;
                return;
            }
        }

        for (int j = 0; j < 3; j++){
            if (board[0][j].getText() == "") continue;

            if (board[0][j].getText() == board[1][j].getText() &&
                    board[1][j].getText() == board[2][j].getText()){

                for (int k = 0; k < 3; k++){
                    setWinner(board[k][j]);
                }
                gameOver = true;
                return;
            }
        }

        if (board[0][0].getText() == board[1][1].getText() &&
            board[1][1].getText() == board[2][2].getText() &&
            board[0][0].getText() != ""){

            for (int i = 0; i < 3; i++){
                setWinner(board[i][i]);
            }
            gameOver = true;
            return;
        }

        if (board[0][2].getText() == board[1][1].getText() &&
                board[1][1].getText() == board[2][0].getText() &&
                board[0][2].getText() != ""){

            setWinner(board[0][2]);
            setWinner(board[1][1]);
            setWinner(board[2][0]);

            gameOver = true;
            return;
        }

        if (turn == 9){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    setTie(board[i][j]);
                }
            }
            gameOver = true;
            return;
        }
    }

    public void setWinner(JButton tile){
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        textLabel.setText(currentPlayer + " is the winner!");
    }

    public void setTie(JButton tile){
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
        textLabel.setText("Tie!");
    }

    public void resetGame(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setText("");
                board[i][j].setBackground(Color.decode("#dcedc8"));
                board[i][j].setForeground(Color.black);
            }
        }
        gameOver = false;
        turn = 0;
        chooseStartingPlayer();
    }

    public void chooseStartingPlayer() {
        Object[] options = {"X", "O"};

        int result = JOptionPane.showOptionDialog(frame,
                "X or O ?",
                "---Choose The Player---",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (result == 1) {
            currentPlayer = o;
        } else {
            currentPlayer = x;
        }
        textLabel.setText(currentPlayer + "'s turn");
    }

    public void loadAndPlayMusic() {
        try {
            File musicPath = new File("src/Wish You Were Here.wav");

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                bgMusic = AudioSystem.getClip();
                bgMusic.open(audioInput);

                bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
                bgMusic.start();
                isMusicPlaying = true;
            } else {
                System.out.println("Can't find the music. Try again.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}