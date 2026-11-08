import java.util.Arrays;
import java.util.Scanner;

public class gameSuite {

    public static void main(String[]args) {
        Scanner input = new Scanner(System.in);
        String option;


        do {

            System.out.println("Main menu:");
            System.out.println("1. Maze");
            System.out.println("2. Maze Design");
            System.out.println("3. Maze Race");
            System.out.println("4. Sudoku");
            System.out.println("5. Memory Card");
            System.out.println("6. Exit");
            System.out.println("Enter your option: ");

            option = input.nextLine().trim();

            switch (option) {
                case "1":
                    maze(input);
                    break;
                case "2":
                    designMaze(input);
                    break;
                case "3":
                    mazeRace(input);
                    break;
                case "4":
                    sudoku(input);
                    break;
                case "5":
                    memory(input);
                    break;
                case "6":
                    System.out.println("Exiting the game......");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid input, please try again.");
            }
        } while (true);

    }

    //游戏1
    public static void maze(Scanner input) {
        System.out.println("Welcome to the Maze game!");
        System.out.println("P is you, G is the end. Operate P to reach the end! There are a total of 5 levels in the game.");
        System.out.println("Enter b to return to the menu. ");
        System.out.println("Enter e to exit the game");
        int level=1;
        int rows=level*5;
        int cols=level*5;
        char [][] maze=new char[rows][cols];
        int[] playerPosition = {1, 1};
        int maxLevel=5;
        start(level,rows,cols,maze,playerPosition,maxLevel,input);
    }
    public static void start(int level,int rows,int cols,char[][]maze,int [] playerPosition,int maxLevel,Scanner input) {

        boolean run=true;
        while (run && level <=maxLevel) {
            System.out.println("Level: "+level+"/5");
            //根据level生成迷宫
            int [] goal= {level*5-2,level*5-2};
            maze=generateMaze(level, goal);
            //重置玩家位置
            playerPosition[0] = 1;
            playerPosition[1] = 1;
            while(true) {
                //显示迷宫
                displayMaze(maze.length, maze[0].length, maze);
                System.out.println("Please enter the direction,w (up), s (down), a (left), d (right) :");
                //判断是否有输入
                String directionInput = input.nextLine().trim();
                if (directionInput.isEmpty()) {
                    System.out.println("Invalid input. Please enter one of the directions: w, s, a, d.");
                    continue;
                }
                //判断是否退出游戏
                if (directionInput.equalsIgnoreCase("e")) {
                    System.out.println("Exiting the game......");
                    input.close();
                    System.exit(0);
                }
                //判断是否返回主菜单
                if (directionInput.equalsIgnoreCase("b")) {
                    System.out.println("Returning to main menu...");
                    return;
                }

                char direction = directionInput.charAt(0);
                //判断是否到终点
                if (move(direction,playerPosition,maze,maze.length, maze[0].length)) {
                    System.out.println("Congratulations on completing Level "+level+" !");
                    level++;
                    break;
                }
            }
            if (level==6)
                return;

        }

    }

    public static char[][] generateMaze(int level, int[] goal) {
        int rows=level*5;
        int cols=level*5;
        char[][] newMaze = new char[rows][cols];
        //将整个迷宫都变成墙
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newMaze[i][j] = '#';
            }
        }
        //通过深度优先搜索生成道路
        createPath(newMaze,level,goal);
        //设置玩家位置和终点
        newMaze[1][1]='P';
        newMaze[goal[0]][goal[1]]='G';

        return newMaze;
    }
    public static void createPath(char[][] maze, int level,int [] goal) {
        int rows = level * 5;
        int cols = level * 5;
        //记录访问过的位置
        boolean[][] visited = new boolean[rows][cols];

        // 通过递归生成迷宫道路
        dfs(maze, visited, 1, 1, rows, cols);

        //确保玩家位置和终点未被覆盖
        maze[1][1] = 'P';
        maze[goal[0]][goal[1]] = 'G';
        //确保终点附近有路

        if (maze[goal[0] - 1][goal[1]] == '#' && maze[goal[0]][goal[1] - 1] == '#') {
            maze[goal[0] - 1][goal[1]] = ' ';
            maze[goal[0]][goal[1] - 1] = ' ';
        }
    }

    private static void dfs(char[][] maze, boolean[][] visited, int row, int col, int rows, int cols) {
        visited[row][col] = true;


        // 随机方向
        int[][] directions = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}}; // 每次跨两格
        shuffle(directions);

        //尝试方向
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            // 如果新位置未访问过打通墙壁
            if (newRow > 0 && newCol > 0 && newRow < rows-1  && newCol < cols-1  && !visited[newRow][newCol]) {

                maze[row + direction[0] / 2][col + direction[1] / 2] = ' ';
                maze[newRow][newCol] = ' ';
                dfs(maze, visited, newRow, newCol, rows, cols);
            }
        }
    }


    public static void shuffle(int [][]direction) {
        for(int i=0;i<direction.length;i++) {
            int index=(int)(Math.random()*direction.length);
            int [] temp=direction[i];
            direction[i]=direction[index];
            direction[index]=temp;
        }
    }
    public static void displayMaze(int rows,int cols,char[][]maze) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }
    public static boolean move(char direction,int [] playerPosition,char[][]maze,int rows,int cols) {

        int newRow = playerPosition[0];
        int newCol = playerPosition[1];
        //判断玩家行走方向
        switch (direction) {
            case 'w': newRow--;break;
            case 's': newRow++;break;
            case 'a': newCol--;break;
            case 'd': newCol++;break;
            case 'W': newRow--;break;
            case 'S': newRow++;break;
            case 'A': newCol--;break;
            case 'D': newCol++;break;
            default: System.out.println("Invalid input, please use w (up), s (down), a (left), d (right)");
                return false;

        }
        //判断是否可以走
        if (newRow >= 0 && newRow < rows - 1 && newCol >= 0 && newCol < cols - 1 && maze[newRow][newCol] != '#') {
            //判断是否到终点
            if (maze[newRow][newCol] == 'G') {
                maze[playerPosition[0]][playerPosition[1]] = ' ';
                playerPosition[0] = newRow;
                playerPosition[1] = newCol;
                return true;
            }
            //移动P
            maze[playerPosition[0]][playerPosition[1]] = ' ';
            playerPosition[0] = newRow;
            playerPosition[1] = newCol;
            maze[playerPosition[0]][playerPosition[1]] = 'P';
        }
        else {
            System.out.println("Hit the wall, please try again.");

        }
        return false;

    }


    //游戏2
    public static void designMaze(Scanner input) {
        System.out.println("Welcome to Design Maze Game!");
        System.out.println("Please design a maze and I will try it.");
        System.out.println("Enter b to return to the menu. ");
        System.out.println("Enter e to exit the game");
        char[][] maze = design(input);
        //返回
        if (maze == null || maze.length == 0 || maze[0].length == 0)
            return;

        //判断是否解决游戏
        if (solveMaze(maze)) {
            System.out.println("Maze solved!");
            displayMaze( maze.length,maze[0].length,maze);
        }
        else {
            System.out.println("No solution found.");
        }

    }


    public static char[][] design(Scanner input) {
        int rows=0;
        int cols=0;
        rows = getValidNumberInput(input,"rows");
        if (rows==-1)
            return new char [][]{{}};
        cols = getValidNumberInput(input,"cols");
        if (cols==-1)
            return new char [][]{{}};

        char [][] maze = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(maze[i], ' ');
        }

        //判断是否要重来
        while(true) {
            int countP=0;
            int countG=0;
            System.out.println("Please enter the maze, using '#' for walls and ' ' for paths. Use 'P' for the start and 'G' for the goal:");
            //记录迷宫
            for (int i = 0; i < rows; i++) {
                String row = getValidMazeRowInput(input,cols);
                if(row.equals("-1"))
                    return new char [][]{{}};

                for (int j = 0; j < Math.min(row.length(), cols); j++) {
                    maze[i][j] = row.charAt(j);
                }
            }
            //判断P,G的个数
            for (int i = 0; i < maze.length;i++) {
                for (int j = 0; j < maze[0].length;j++) {
                    char ch = maze[i][j];
                    if (ch == 'P') {
                        countP++;
                    }
                    if (ch == 'G') {
                        countG++;
                    }
                }
            }
            if (countP == 0 || countG == 0) {
                System.out.println("You don't have a P or a G. Please try again.");

                for (char[] row : maze) {
                    Arrays.fill(row, ' ');
                }
                continue;
            }
            if (countP > 1 || countG > 1) {
                System.out.println("You already have a P or G. Please try again.");

                // 将每一行的元素都重置
                for (char[] row : maze) {
                    java.util.Arrays.fill(row, ' ');
                }
                continue;
            }
            break;
        }
        return maze;
    }
    public static int getValidNumberInput(Scanner input,String name) {
        while(true) {

            System.out.println("Please enter the  "+name+" : ");
            String value = input.nextLine().trim();
            //判断是否有输入
            if (value.isEmpty()) {
                System.out.println("Invalid input. Please enter again :");
                continue;
            }
            //判断是否退出游戏
            if (value.equalsIgnoreCase("e")) {
                System.out.println("Exiting the game......");
                input.close();
                System.exit(0);
            }
            //判断是否返回主菜单
            if (value.equalsIgnoreCase("b") ) {
                System.out.println("Returning to main menu...");
                return -1;
            }
            //判断是否是数字
            boolean isDigit=true;
            for (int i = 0; i < value.length(); i++) {
                char ch = value.charAt(i);
                if(!Character.isDigit(ch)) {
                    System.out.println("Invalid input.");
                    isDigit=false;
                    break;
                }

            }
            if (isDigit) {
                return Integer.parseInt(value);
            }
        }
    }
    public static String getValidMazeRowInput(Scanner input,int cols) {
        //判断每行输入
        while(true) {
            String row= input.nextLine().trim();
            //判断是否有输入
            if (row.isEmpty()) {
                System.out.println("Invalid input. Please enter the maze, using '#' for walls and ' ' for paths. Use 'P' for the start and 'G' for the goal:");
                continue;
            }
            //判断是否退出游戏
            if (row.equalsIgnoreCase("e") ) {
                System.out.println("Exiting the game......");
                input.close();
                System.exit(0);
            }
            //判断是否返回主菜单
            if (row.equalsIgnoreCase("b") ) {
                System.out.println("Returning to main menu...");
                return "-1";
            }
            // 判断输入是否合法
            boolean isValid = true;
            for (int j = 0; j < row.length(); j++) {
                char ch = row.charAt(j);
                if(ch!='#' && ch!=' ' && ch!='P' && ch!='G') {
                    isValid = false;
                    break;
                }
            }
            if (isValid && row.length() <= cols) {
                return row;
            } else {
                System.out.println("Invalid input. Please try again:");
            }
        }
    }
    // 解决迷宫，使用A*算法
    public static boolean solveMaze(char[][] maze) {

        // 查找起点P和终点G的位置
        int[] start = findPosition(maze, 'P');
        int[] goal = findPosition(maze, 'G');

        if (start == null || goal == null) {
            System.out.println("P or G not found!");
            return false;
        }
        //记录访问过的位置
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        //寻找道路
        return findPath(start, goal,maze,visited);

    }
    public static boolean findPath(int [] current,int [] goal,char [][]maze,boolean[][] visited) {
        visited[current[0]][current[1]] = true;
        maze[current[0]][current[1]] = 'O';
        //标记
        if (current[0] == goal[0] && current[1] == goal[1]) {
            maze[current[0]][current[1]] = 'O';
            return true;
        }
        //计算四个方向的预估代价
        int [] cost= {10000000,10000000,10000000,10000000};
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i=0; i<4;i++) {
            int newRow=current[0]+directions[i][0];
            int newCol=current[1]+directions[i][1];
            if (maze[newRow][newCol]!='P' && newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length && maze[newRow][newCol] != '#'&& maze[newRow][newCol] != 'P') {
                //用曼哈顿距离作为代价
                cost[i]=manhattanDistance( new int[] {newRow,newCol},  goal);
            }

        }
        //给四个方向的代价排序
        int[] sortIndex= {0,1,2,3};
        for (int i = 0; i < 3; i++) {

            int currentDistance=cost[i];
            int currentMinIndex = i;

            for (int j = i + 1; j < 4; j++) {
                if (currentDistance > cost[j]) {
                    currentDistance = cost[j];
                    currentMinIndex = j;
                }
            }
            int temp = sortIndex[i];
            sortIndex[i] = sortIndex[currentMinIndex];
            sortIndex[currentMinIndex] = temp;
        }
        //从最小的代价开始尝试
        for(int i:sortIndex) {
            int currentRow = current[0] + directions[i][0];
            int currentCol = current[1] + directions[i][1];

            if (currentRow >= 0 && currentRow < maze.length && currentCol >= 0 && currentCol < maze[0].length && maze[currentRow][currentCol] != '#' && !visited[currentRow][currentCol]&& maze[currentRow][currentCol] != 'P') {
                if (findPath( new int[]{currentRow, currentCol}, goal,maze, visited)) {
                    return true;
                }
            }
        }

        // 取消标记
        visited[current[0]][current[1]] = false;
        maze[current[0]][current[1]] = ' ';
        return false;
    }

    public static int manhattanDistance(int[] newPosition, int[] goal) {
        return Math.abs(newPosition[0] - goal[0]) + Math.abs(newPosition[1] - goal[1]);
    }


    public static int[] findPosition(char[][] maze, char target) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }




    //游戏3
    public static void mazeRace(Scanner input) {
        System.out.println("Welcome to Maze Race: Player vs Computer!");
        System.out.println("Design your maze, then compete with the computer to reach the goal (G).");
        System.out.println("Enter b to return to the menu.");
        System.out.println("Enter e to exit the game");
        int level=getValidNumberInput(input, "level");
        if(level==-1)
            return;
        int [] goal = {level*5-2,(level*5-1)/2};
        char [][] maze=generateMaze(level,goal);
        int rows=level*5;
        int cols=level*5;
        if (maze == null ||rows == 0 || cols == 0) {
            return; // 返回主菜单
        }

        // 初始化玩家和电脑的位置
        int[]  playerPosition= {1,1};
        int[]  computerPosition= {1,maze[0].length-2};
        maze[computerPosition[0]][computerPosition[1]] = 'C';

        //轮流移动
        while (true) {
            boolean[][] visited = new boolean[rows][cols];
            // 玩家移动
            String directionInput;
            while(true) {
                //显示迷宫
                displayMaze(maze.length, maze[0].length, maze);
                System.out.println("Please enter the direction,w (up), s (down), a (left), d (right) :");
                //判断是否有输入
                directionInput = input.nextLine().trim();
                if (directionInput.isEmpty()) {
                    System.out.println("Invalid input. Please enter one of the directions: w, s, a, d.");
                    continue;
                }
                //判断是否退出游戏
                if (directionInput.equalsIgnoreCase("e")) {
                    System.out.println("Exiting the game......");
                    input.close();
                    System.exit(0);
                }
                //判断是否返回主菜单
                if (directionInput.equalsIgnoreCase("b")) {
                    System.out.println("Returning to main menu...");
                    return;
                }
                break;
            }
            visited[playerPosition[0]][playerPosition[1]]=false;

            char direction = directionInput.charAt(0);
            boolean isYourTurn=true;
            while(isYourTurn) {
                if (!move(direction, playerPosition,maze,maze.length,maze[0].length)) {
                    isYourTurn=false;
                    visited[playerPosition[0]][playerPosition[1]]=true;
                }
            }
            displayMaze(maze.length, maze[0].length, maze);
            if (maze[playerPosition[0]][playerPosition[1]] == 'G') {
                System.out.println("You win! Congratulations!");
                input.nextLine();
                return;
            }

            // 电脑移动
            System.out.println("Computer's turn...");
            if (findPath(computerPosition, goal,maze,visited)) {
                maze[goal[0]][goal[1]] = 'G';
                maze[playerPosition[0]][playerPosition[1]] = 'P';
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if(maze[i][j]=='O') {
                            maze[i][j]=' ';
                        }
                    }
                }
                visited[computerPosition[0]][computerPosition[1]]=false;
                int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                for (int i=0; i<4;i++) {
                    int newRow=computerPosition[0]+directions[i][0];
                    int newCol=computerPosition[1]+directions[i][1];
                    if(visited[newRow][newCol]==true) {
                        boolean computerWait = false;
                        // 检测碰撞
                        if (playerPosition[0] ==newRow && playerPosition[1] == newCol) {
                            System.out.println("Collision detected! Computer skips its turn.");
                            computerWait = true;
                        }

                        if (!computerWait) {
                            maze[newRow][newCol]='C';
                            computerPosition[0]=newRow;
                            computerPosition[1]=newCol;
                            if(computerPosition[0]==goal[0] && computerPosition[1]==goal[1]) {
                                System.out.println("Computer wins! Better luck next time.");
                                input.nextLine();
                                return;
                            }

                        } else {
                            computerWait = false;
                        }

                        break;
                    }
                }

            }
            displayMaze(maze.length, maze[0].length, maze);

        }
    }

    // 检查移动是否合法
    public static boolean isValidMove(char[][] maze, int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && (maze[x][y] == ' ' || maze[x][y] == 'G') && maze[x][y]!='C';
    }



    //游戏4
    public static void sudoku(Scanner input) {
        int[][] puzzle = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        int[][]solution = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        System.out.println("Welcome to Sudoku!");
        System.out.println("Enter b to return to the menu.");
        System.out.println("Enter e to exit the game");
        while (true) {
            // 打印数独
            printPuzzle(puzzle);
            System.out.println("Enter your move as 'row col value' (enter c to check and enter h to get a hint):");
            String command = input.nextLine();

            if (command.equalsIgnoreCase("e")) {
                System.out.println("Exiting the game......");
                input.close();
                System.exit(0);
            }
            // 检查是否完成
            else if (command.equalsIgnoreCase("c")) {
                if (isSolved(puzzle,solution)) {
                    System.out.println("Congratulations! You solved the puzzle!");
                    return;
                } else {
                    System.out.println("The puzzle is not solved yet. Keep trying!");
                }
            }
            else if (command.equalsIgnoreCase("h")){
                if(hint( puzzle, solution)==-1) {
                    return;
                }
            }
            else if (command.equalsIgnoreCase("b")) {
                System.out.println("Returning to main menu...");
                return;
            }
            else {

                String[] parts = command.split(" ");
                if (parts.length != 3) {
                    System.out.println("Invalid input. Please enter as 'row col value'.");
                    continue;
                }

                int row = Integer.parseInt(parts[0]) - 1;
                int col = Integer.parseInt(parts[1]) - 1;
                int value = Integer.parseInt(parts[2]);
                if (isValidNumber(puzzle,  row, col, value)) {
                    puzzle[row][col] = value;
                } else {
                    System.out.println("Invalid input. Try again.");
                }

            }
        }
    }

    public static void printPuzzle(int[][] puzzle) {
        System.out.println("Current Sudoku Puzzle:");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print((puzzle[i][j] == 0 ? "." : puzzle[i][j]) + " ");
            }
            System.out.println();
        }
    }

    public static boolean isValidNumber(int[][] puzzle,int row, int col, int value) {
        if (row < 0 || col < 0 || row >= 9 || col >= 9 || value < 1 || value > 9 || puzzle[row][col] != 0) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (puzzle[row][i] == value || puzzle[i][col] == value || puzzle[row / 3 * 3 + i / 3][col / 3 * 3 + i % 3] == value) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSolved(int[][] puzzle,int[][] solution) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i][j] == 0 || puzzle[i][j] != solution[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    public static int hint(int[][]puzzle,int[][] solution) {
        boolean isChange=false;
        while(!isChange) {
            if (isSolved(puzzle,solution)) {
                System.out.println("Congratulations! You solved the puzzle!");
                return -1;
            }
            int row=(int)(Math.random()*9);
            int col=(int)(Math.random()*9);
            if(puzzle[row][col]!=0)
                continue;
            else {
                puzzle[row][col]=solution[row][col];
                isChange=true;
            }
        }
        return 0;
    }



    //游戏5
    public static void memory(Scanner input) {
        System.out.println("Welcome to Memory Card!");
        System.out.println("Enter b to return to the menu.");
        System.out.println("Enter e to exit the game");
        char[][] card=generateCard();
        char[][] puzzle=new char [4][4];
        int count=0;
        int[][] old = new int[2][2];
        //判断是否完成
        while(!isComplete(puzzle)) {
            displayMaze(4,4,puzzle);
            System.out.println("Enter row and col (1-4): ");
            int row =getValidNumberInput(input,"row");
            if(row==-1)
                return ;
            int col =getValidNumberInput(input,"col");
            if(col==-1)
                return ;
            row--;
            col--;
            if(!displayNumber(row, col,card, puzzle)) {
                continue;
            }
            old[count][0] = row;
            old[count][1] = col;
            count++;
            // 两张牌都翻开
            if (count == 2) {
                displayMaze(4, 4, puzzle);
                //判断是否相同
                if (puzzle[old[0][0]][old[0][1]] == puzzle[old[1][0]][old[1][1]]) {
                    System.out.println("Match found!");
                    puzzle[old[0][0]][old[0][1]] = ' ';
                    puzzle[old[1][0]][old[1][1]] = ' ';
                }
                else {
                    System.out.println("No match. Cards will be hidden.");
                    puzzle[old[0][0]][old[0][1]] = '\u0000';
                    puzzle[old[1][0]][old[1][1]] = '\u0000';
                }
                count = 0;
            }
        }
        System.out.println("You have matched all the cards!");
        return;
    }
    public static char[][] generateCard() {
        char[][] card= new char [4][4];
        int [] count=new int [8];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int number;
                do{
                    number=(int)(Math.random()*8);
                }while (count[number]>=2);
                card[i][j]=(char)('1'+number);
                count[number]++;

            }
        }
        return card;
    }

    public static boolean displayNumber(int row,int col,char[][]card,char[][] puzzle) {
        if (row >= 0 && row < 4 && col >= 0 && col < 4 && puzzle[row][col] == '\u0000') {
            puzzle[row][col] = card[row][col];
            return true;
        } else {
            System.out.println("Invalid input. Try again.");
            return false;
        }
    }
    public static boolean isComplete(char [][] puzzle) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(puzzle[i][j]=='\u0000') {
                    return false;
                }
            }
        }
        return true;
    }

}
