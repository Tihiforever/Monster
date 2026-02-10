package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {

    public int x;
    public int y;
    public int health;
    public int gold;
    public int explode;
    public int supersight;

    public Player() {
        x = 1;
        y = 1;
        health = 3;
        gold = 0;
        explode = 1;
        supersight = 0;
    }

    public void move(int dx, int dy, Grid grid) {

        int newX = x + dx;
        int newY = y + dy;

        //bounds check FIRST
        if (!grid.isInside(newX, newY)) return;

        //wall collision
        if (grid.tiles[newX][newY] == TileType.WALL) return;

        //move player
        x = newX;
        y = newY;

        //trap check
        if (grid.tiles[x][y] == TileType.TRAP) {
            health--;
            grid.tiles[x][y] = TileType.FLOOR;
        }

        //gold check
        if (grid.tiles[x][y] == TileType.GOLD) {
            gold++;
            grid.tiles[x][y] = TileType.FLOOR;
        }

        //explode check
        if (grid.tiles[x][y] == TileType.EXPLODE) {
            explode++;
            grid.tiles[x][y] = TileType.FLOOR;
        }

        //supersight check
        if (grid.tiles[x][y] == TileType.SUPERSIGHT) {
            supersight++;
            grid.tiles[x][y] = TileType.FLOOR;
        }

        grid.UpdateFog(x,y,5);
    }
    public boolean checkwin(Grid grid, ShapeRenderer sr){
        if(x == 19 && y == 19 & gold == 3){
            System.out.println("You win");
            return true;
        }
        return false;
    }

}
