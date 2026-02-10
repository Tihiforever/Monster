package io.github.some_example_name;
import java.util.Random;

public class Monster {
    Random r = new Random();
    public int x;
    public int y;

    public Monster() {
        x = 19;
        y = 19;
    }

    public void moveTowardAStar(Player player, Grid grid) {
        int size = Grid.SIZE;

        //create list of nodes
        boolean[][] closed = new boolean[size][size];
        PriorityQue open = new PriorityQue(size);
        int[][] parentX = new int[size][size];
        int[][] parentY = new int[size][size];

        int startX = x;
        int startY = y;

        int g = 0;
        int h = Math.abs(startX - player.x) + Math.abs(startY - player.y);
        int f = g + h;
        open.push(startX * size + startY, f);

        while(!open.isEmpty()){
            int current = open.pop();
            int cx = current / size;
            int cy = current % size;

            if(cx == player.x && cy == player.y){
                int tx = cx;
                int ty = cy;

                while (!(parentX[tx][ty] == startX && parentY[tx][ty] == startY)) {
                    int px = parentX[tx][ty];
                    int py = parentY[tx][ty];
                    tx = px;
                    ty = py;
                }

                x = tx;
                y = ty;
                return;
            }

            closed[cx][cy] = true;

            for (int i = 0; i < 4; i++) {
                int nx = cx;
                int ny = cy;

                if (i == 0) nx++;
                else if (i == 1) nx--;
                else if (i == 2) ny++;
                else if (i == 3) ny--;

                if (!grid.isInside(nx, ny)) continue;
                if (grid.tiles[nx][ny] == TileType.WALL) continue;
                if (closed[nx][ny]) continue;

                parentX[nx][ny] = cx;
                parentY[nx][ny] = cy;

                int ng = g + 1;
                int nh = Math.abs(nx - player.x) + Math.abs(ny - player.y);
                int nf = ng + nh;

                open.push(nx * size + ny, nf);
            }


        }
    }


}
