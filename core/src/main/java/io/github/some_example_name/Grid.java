package io.github.some_example_name;


public class Grid {
    public static final int SIZE = 21;
    TileType[][] tiles = new TileType[SIZE][SIZE];
    Visibility[][] fog = new Visibility[SIZE][SIZE];

    public Grid() {
        mazeGenerate();
        FillMazeThings();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                fog[x][y] = Visibility.UNSEEN;
            }
        }
    }


    public void mazeGenerate() {

        //everything walls
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                tiles[x][y] = TileType.WALL;

            }
        }

        //create stack
        Stack stack = new Stack(SIZE * SIZE);

        //starting cell
        int startX = 1;
        int startY = 1;

        //set start pos to floor
        tiles[startX][startY] = TileType.FLOOR;

        //change x and y as value as idk how to make my stack to do it properly
        stack.push(startX * SIZE + startY);

        //DFS loop
        while (!stack.isEmpty()) {

            // get current cell
            int current = stack.peek();
            int x = current / SIZE;
            int y = current % SIZE;

            // track unvisited neighbours
            int[] nx = new int[4];
            int[] ny = new int[4];
            int count = 0;

            // check UP
            if (y - 2 > 0 && tiles[x][y - 2] == TileType.WALL) {
                nx[count] = x;
                ny[count] = y - 2;
                count++;
            }

            // check DOWN
            if (y + 2 < SIZE - 1 && tiles[x][y + 2] == TileType.WALL) {
                nx[count] = x;
                ny[count] = y + 2;
                count++;
            }

            // check LEFT
            if (x - 2 > 0 && tiles[x - 2][y] == TileType.WALL) {
                nx[count] = x - 2;
                ny[count] = y;
                count++;
            }

            // check RIGHT
            if (x + 2 < SIZE - 1 && tiles[x + 2][y] == TileType.WALL) {
                nx[count] = x + 2;
                ny[count] = y;
                count++;
            }

            //if i found at least one unvisited neighbour
            if (count > 0) {

                // pick one at random
                int r = (int)(Math.random() * count);
                int nextX = nx[r];
                int nextY = ny[r];

                // remove wall between current and neighbour
                int wallX = (x + nextX) / 2;
                int wallY = (y + nextY) / 2;

                tiles[wallX][wallY] = TileType.FLOOR;
                tiles[nextX][nextY] = TileType.FLOOR;

                // push neighbour onto stack
                stack.push(nextX * SIZE + nextY);

            } else {
                stack.pop();
            }


        }
    }


    private void FillMazeThings(){
        int goldTarget = 3;
        int trapTarget = 4;
        int explodeTarget = 2;
        int supersightTarget = 1;

        int goldPlaced = 0;
        int trapsPlaced = 0;
        int explodePlaced = 0;
        int supersightPlaced = 0;

        // place gold
        while (goldPlaced < goldTarget) {

            int x = (int)(Math.random() * SIZE);
            int y = (int)(Math.random() * SIZE);

            if (tiles[x][y] == TileType.FLOOR) {

                // don't place on player start
                if (x == 1 && y == 1) {
                    continue;
                }

                tiles[x][y] = TileType.GOLD;
                goldPlaced++;
            }
        }

        // place traps
        while (trapsPlaced < trapTarget) {

            int x = (int)(Math.random() * SIZE);
            int y = (int)(Math.random() * SIZE);

            if (tiles[x][y] == TileType.FLOOR) {

                if (x == 1 && y == 1) {
                    continue;
                }

                tiles[x][y] = TileType.TRAP;
                trapsPlaced++;
            }
        }

        // place explode's
        while (explodePlaced < explodeTarget) {

            int x = (int)(Math.random() * SIZE);
            int y = (int)(Math.random() * SIZE);

            if (tiles[x][y] == TileType.FLOOR) {

                // don't place on player start
                if (x == 1 && y == 1) {
                    continue;
                }

                tiles[x][y] = TileType.EXPLODE;
                explodePlaced++;
            }
        }

        // place supersight
        while (supersightPlaced < supersightTarget) {

            int x = (int)(Math.random() * SIZE);
            int y = (int)(Math.random() * SIZE);

            if (tiles[x][y] == TileType.FLOOR) {

                // don't place on player start
                if (x == 1 && y == 1) {
                    continue;
                }

                tiles[x][y] = TileType.SUPERSIGHT;
                supersightPlaced++;
            }
        }

        tiles[19][19] = TileType.EXIT;
    }

    public void UpdateFog(int px, int py, int radius){

        // fade old visible tiles
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if(fog[x][y] == Visibility.VISIBLE){
                    fog[x][y] = Visibility.SEEN;
                }
            }
        }

        fog[px][py] = Visibility.VISIBLE;

        int[] dx = { 1, -1, 0, 0 };
        int[] dy = { 0, 0, 1, -1 };

        for (int d = 0; d < 4; d++) {
            for (int i = 1; i <= radius; i++) {

                int nx = px + dx[d] * i;
                int ny = py + dy[d] * i;

                if (!isInside(nx, ny)) break;

                fog[nx][ny] = Visibility.VISIBLE;

                // wall blocks sight
                if (tiles[nx][ny] == TileType.WALL) break;
            }
        }
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }


}
