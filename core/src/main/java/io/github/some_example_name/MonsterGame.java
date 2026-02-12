package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MonsterGame extends ApplicationAdapter {

    ShapeRenderer shape;
    Grid grid;
    Player player;
    Monster monster;
    BitmapFont font;
    SpriteBatch batch;

    final int TILE_SIZE = 40;
    boolean gameOver = false;
    boolean win = false;
    float monsterTimer = 0f;

    @Override
    public void create() {
        shape = new ShapeRenderer();
        grid = new Grid();
        player = new Player();
        monster = new Monster();
        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    @Override
    public void render() {


        if (!gameOver) {
            if (player.health <= 0 ||
                (player.x == monster.x && player.y == monster.y) ||
                (player.x == 19 && player.y == 19 && player.gold == 3)){
                gameOver = true;
            }
        }

        if (!gameOver) {
            handleInput();

            monsterTimer += Gdx.graphics.getDeltaTime();
            if (monsterTimer > 0.5f) {
                monster.moveTowardAStar(player, grid);
                monsterTimer = 0f;
            }
        }


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shape.begin(ShapeRenderer.ShapeType.Filled);


        // draw grid
        for (int x = 0; x < Grid.SIZE; x++) {
            for (int y = 0; y < Grid.SIZE; y++) {

                // set everything grey
                Color baseColor = Color.GRAY;

                //set colours too stuff
                if (grid.tiles[x][y] == TileType.WALL) baseColor = Color.WHITE;
                else if (grid.tiles[x][y] == TileType.FLOOR) baseColor = Color.DARK_GRAY;
                else if (grid.tiles[x][y] == TileType.GOLD) baseColor = Color.GOLD;
                else if (grid.tiles[x][y] == TileType.TRAP) baseColor = Color.RED;
                else if (grid.tiles[x][y] == TileType.EXPLODE) baseColor = Color.PURPLE;
                else if (grid.tiles[x][y] == TileType.SUPERSIGHT) baseColor = Color.PINK;
                else if (grid.tiles[x][y] == TileType.EXIT) baseColor = Color.ORANGE;

                // apply fog
                if (grid.fog[x][y] == Visibility.UNSEEN) {
                    shape.setColor(Color.BLACK);
                }

                else if (grid.fog[x][y] == Visibility.SEEN) {
                    shape.setColor(baseColor.r * 0.4f, baseColor.g * 0.4f, baseColor.b * 0.4f, 1);
                }

                else { // VISIBLE
                    shape.setColor(baseColor);
                }

                shape.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }


        // player (green)
        if(player.health == 3)shape.setColor(0.2f, 0.8f, 0.2f, 1f);
        if(player.health == 2)shape.setColor(0.15f, 0.6f, 0.15f, 1f);
        if(player.health == 1)shape.setColor(0.1f, 0.4f, 0.1f, 1f);
        shape.rect(player.x * TILE_SIZE, player.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // draw monster ONLY if visible (blue)
        if (grid.fog[monster.x][monster.y] == Visibility.VISIBLE) {
            shape.setColor(Color.BLUE);
            shape.rect(monster.x * TILE_SIZE, monster.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }


        shape.end();


        //error and stats log
        batch.begin();
        font.getData().setScale(2f);
        font.draw(batch, "Gold: "+player.gold+"/3", 20,800);
        font.draw(batch, "Explodes: "+player.explode+"   Supersights:  "+player.supersight, 150,800);
        batch.end();



        // game over
        if (gameOver) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            font.getData().setScale(6f);
            if(player.x == 19 && player.y == 19 && player.gold == 3){
                font.draw(batch, "YOU WIN", 200, 550);
            } else {
                font.draw(batch, "GAME OVER", 160, 550);
            }
            font.getData().setScale(2f);
            font.draw(batch, "Press R to Restart", 30, 60);
            batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                create(); // soft restart
                gameOver = false;
            }
        }


    }

    private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.Q) && player.supersight > 0) {
            grid.fog[monster.x][monster.y] = Visibility.VISIBLE;
            player.supersight--;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.E) && player.explode >= 1){
            if (grid.tiles[player.x][player.y+1] == TileType.WALL) {
                grid.tiles[player.x][player.y + 1] = TileType.FLOOR;
            }
            if (grid.tiles[player.x][player.y-1] == TileType.WALL) {
                grid.tiles[player.x][player.y - 1] = TileType.FLOOR;
            }
            if (grid.tiles[player.x+1][player.y] == TileType.WALL) {
                grid.tiles[player.x+1][player.y] = TileType.FLOOR;
            }
            if (grid.tiles[player.x-1][player.y] == TileType.WALL) {
                grid.tiles[player.x-1][player.y] = TileType.FLOOR;
            }

            player.explode--;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
            player.move(0, 1, grid);

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S))
            player.move(0, -1, grid);

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A))
            player.move(-1, 0, grid);

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D))
            player.move(1, 0, grid);
    }

    @Override
    public void dispose() {
        shape.dispose();
        font.dispose();
        batch.dispose();
    }
}
