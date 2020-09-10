/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import data.Move;
import data.Request;
import data.RequestKey;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author ADMIN
 */
public final class GamePanel extends JPanel implements MouseListener {

    private static final int CELLS_TO_WIN = 3;
    private final Client client;
    private final int size;
    private final MyLabel[][] cells;
    private final JLabel labelPlayerX = new JLabel();
    private final JLabel labelTurn = new JLabel();
    private boolean yourTurn;
    private String currentTurn = "X";

    public GamePanel(Client client, int size) {
        this.client = client;
        this.size = size;
        cells = new MyLabel[size][size];
        if (client.isPlayerX()) {
            yourTurn = true;
            labelPlayerX.setText(client.getName() + ": X-player");
        } else {
            labelPlayerX.setText(client.getName() + ": O-player");
        }
        labelTurn.setText("Turn: " + currentTurn);

        setBorder(BorderFactory.createEmptyBorder(10, 20, 27, 20));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.LINE_START;
        add(labelPlayerX, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        add(labelTurn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                cells[row][column] = new MyLabel();
                cells[row][column].addMouseListener(this);
                add(cells[row][column], gbc);
                gbc.gridx++;
            }
            gbc.gridx = 0;
            gbc.gridy++;
        }
    }

    public void setOpponentMove(Move move) {
        int row = move.getRow();
        int column = move.getColumn();
        cells[row][column].setText(currentTurn);
        currentTurn = currentTurn.equals("X") ? "O" : "X";
        labelTurn.setText("Turn: " + currentTurn);
        yourTurn = true;
    }

    public void showOpponentExitedGameMessage() {
        JOptionPane.showMessageDialog(this, client.getOpponentName() + " has exited the game");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!yourTurn) {
            return;
        }
        Object source = e.getSource();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (source == cells[row][column]) {
                    handleCellClicked(row, column);
                }
            }
        }
    }

    private void handleCellClicked(int row, int column) {
        if (!cells[row][column].getText().isEmpty()) {
            return;
        }
        yourTurn = false;
        cells[row][column].setText(currentTurn);
        Move move = new Move(row, column);
        Request request = new Request(RequestKey.SEND_MOVE, move);
        client.getClientThread().sendRequestToServer(request);
        if (checkWinCondition(row, column)) {
            request = new Request(RequestKey.SEND_I_WIN, null);
            client.getClientThread().sendRequestToServer(request);
        } else if (checkDrawCondition()) {
            request = new Request(RequestKey.SEND_DRAW, null);
            client.getClientThread().sendRequestToServer(request);
        } else {
            currentTurn = currentTurn.equals("X") ? "O" : "X";
            labelTurn.setText("Turn: " + currentTurn);
        }
    }

    private boolean checkWinCondition(int row, int column) {
        return checkHorizontal(row, column)
                || checkVertical(row, column)
                || checkLeftDiagonal(row, column)
                || checkRightDiagonal(row, column);
    }

    private boolean checkHorizontal(int currentRow, int currentColumn) {
        int minColumn = currentColumn - CELLS_TO_WIN + 1;
        for (int outerColumn = minColumn; outerColumn <= currentColumn; outerColumn++) {
            if (checkHorizontalRow(currentRow, outerColumn)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkHorizontalRow(int currentRow, int outerColumn) {
        for (int innerColumn = outerColumn; innerColumn < outerColumn + CELLS_TO_WIN; innerColumn++) {
            if (innerColumn < 0 || innerColumn >= size) {
                return false;
            }
            if (!cells[currentRow][innerColumn].getText().equals(currentTurn)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkVertical(int currentRow, int currentColumn) {
        int minRow = currentRow - CELLS_TO_WIN + 1;
        for (int outerRow = minRow; outerRow <= currentRow; outerRow++) {
            if (checkVerticalRow(outerRow, currentColumn)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalRow(int outerRow, int currentColumn) {
        for (int innerRow = outerRow; innerRow < outerRow + CELLS_TO_WIN; innerRow++) {
            if (innerRow < 0 || innerRow >= size) {
                return false;
            }
            if (!cells[innerRow][currentColumn].getText().equals(currentTurn)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLeftDiagonal(int currentRow, int currentColumn) {
        int minRow = currentRow - CELLS_TO_WIN + 1;
        int minColumn = currentColumn - CELLS_TO_WIN + 1;
        for (int outerRow = minRow, outerColumn = minColumn; outerRow <= currentRow && outerColumn <= currentColumn; outerRow++, outerColumn++) {
            if (checkLeftDiagonalRow(outerRow, outerColumn)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLeftDiagonalRow(int outerRow, int outerColumn) {
        for (int innerRow = outerRow, innerColumn = outerColumn; innerRow < outerRow + CELLS_TO_WIN && innerColumn < outerColumn + CELLS_TO_WIN; innerRow++, innerColumn++) {
            if (innerRow < 0 || innerRow >= size || innerColumn < 0 || innerColumn >= size) {
                return false;
            }
            if (!cells[innerRow][innerColumn].getText().equals(currentTurn)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRightDiagonal(int currentRow, int currentColumn) {
        int minRow = currentRow - CELLS_TO_WIN + 1;
        int maxColumn = currentColumn + CELLS_TO_WIN - 1;
        for (int outerRow = minRow, outerColumn = maxColumn; outerRow <= currentRow && outerColumn >= currentColumn; outerRow++, outerColumn--) {
            if (checkRightDiagonalRow(outerRow, outerColumn)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRightDiagonalRow(int outerRow, int outerColumn) {
        for (int innerRow = outerRow, innerColumn = outerColumn; innerRow < outerRow + CELLS_TO_WIN && innerColumn > outerColumn - CELLS_TO_WIN; innerRow++, innerColumn--) {
            if (innerRow < 0 || innerRow >= size || innerColumn < 0 || innerColumn >= size) {
                return false;
            }
            if (!cells[innerRow][innerColumn].getText().equals(currentTurn)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDrawCondition() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (cells[row][column].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
