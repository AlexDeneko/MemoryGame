package com.example.memorygame;

import java.util.ArrayList;
import java.util.Random;

public class BoardGame {
    private int[][] boardGame;
    private static final int ROWS = 3;
    private static final int COLUMNS = 4;

    public BoardGame() {
        ArrayList<Integer> images = imagesToChoose();//create an arrayList
        int indexInArray;//and index to get image from array
        boardGame = new int[ROWS][COLUMNS];//create new board game in this size
        for (int i = 0; i < ROWS; i++)//run on all rows
            for (int j = 0; j < COLUMNS; j++) {//and all columns
                indexInArray = randomIndex(images.size());//get a random index
                boardGame[i][j] = images.get(indexInArray);//put an image to the cell in board game from array this random index
                images.remove(indexInArray);//delete the image from array with the same index
            }
    }

    public ArrayList<Integer> imagesToChoose() {
        ArrayList<Integer> images = new ArrayList<>();//create an arrayList and put images from drawable
        images.add(R.drawable.apple);
        images.add(R.drawable.apple);
        images.add(R.drawable.banana);
        images.add(R.drawable.banana);
        images.add(R.drawable.cherries);
        images.add(R.drawable.cherries);
        images.add(R.drawable.dragon);
        images.add(R.drawable.dragon);
        images.add(R.drawable.pineapple);
        images.add(R.drawable.pineapple);
        images.add(R.drawable.strawberry);
        images.add(R.drawable.strawberry);
        return images;
    }

    public int randomIndex(int maxIndex) {
        Random random = new Random();//create new random function
        return random.nextInt(maxIndex);//return soe random index from zero to max index
    }

    public int getCard(int row, int column) {
        return boardGame[row][column];//return the numerical id of image from board game
    }

    public int getColumn(String positionCard) {
        return Character.getNumericValue(positionCard.charAt(positionCard.length() - 1));//get the column
    }

    public int getRow(String positionCard) {
        return Character.getNumericValue(positionCard.charAt(positionCard.length() - 2));//get the row
    }
}
