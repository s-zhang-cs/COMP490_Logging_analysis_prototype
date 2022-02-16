COMP490 - personal research project in the topic of static analysis and log analysis at Concordia University, beginning at Summer 2021 and continued during Fall 2021 on a part-time basis. Supervised by Professor Weiyi Shang.

This project is an Eclipse plugin built using Eclipse JDT (https://www.eclipse.org/jdt/). It requires the installation of Eclipse JDT and should be run as an Eclipse Application inside of Eclipse. 

Key ideas of this project:
1. to use a recursive constructor chain to go through each JDT ASTNode's inner statements recursively in order to build a control flow graph (CFG). This inward process can make the building of CFG more modular.

2. to build a call graph (CG) revolving around all the logging methods by reversely traversing
the JDT's ASTNodes pertaining to logging statements. 

DEMO:
For the following test source code (based on the game of life example found on https://www.cs.utexas.edu/~scottm/cs307/codingSamples.htm) :

```
package java_demo2;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSample1 {
    static Logger logger = LoggerFactory.getLogger(TestSample1.class);
	
    public static void show(boolean[][] grid){
        logger.info("in show method");
        String s = "";
        for(boolean[] row : grid){
            for(boolean val : row) {
                if(val)
                    s += "*";
                else
                    s += ".";
            }
            s += "\n";
            if(1 > 2) {
            	int a = 1;
            }
        }
        System.out.println(s);
    }
    
    public static boolean[][] gen(){
        boolean[][] grid = new boolean[10][10];
        for(int r = 0; r < 10; r++)
            for(int c = 0; c < 10; c++)
                if( Math.random() > 0.7 )
                    grid[r][c] = true;
        return grid;
    }
    
    public static void main(String[] args){
        logger.debug("in main method");
        boolean[][] world = gen();
        show(world);
        System.out.println();
        world = nextGen(world);
        show(world);
        Scanner s = new Scanner(System.in);
        while(s.nextLine().length() == 0){
            System.out.println();
            world = nextGen(world);
            show(world);
        }
        show(world);
    }
    
    public static boolean[][] nextGen(boolean[][] world){
        logger.debug("in nextGen method");
        boolean[][] newWorld 
            = new boolean[world.length][world[0].length];
        int num;
        for(int r = 0; r < world.length; r++){
            for(int c = 0; c < world[0].length; c++){
                num = numNeighbors(world, r, c);
                if( occupiedNext(num, world[r][c]) ) 
                    newWorld[r][c] = true;
            }
        }
        show(world);
        return newWorld;
    }
    
    public static boolean occupiedNext(int numNeighbors, boolean occupied){
        if( occupied && (numNeighbors == 2 || numNeighbors == 3))
            return true;
        else if (!occupied && numNeighbors == 3)
            return true;
        else
            return false;
    }
    
    private static int numNeighbors(boolean[][] world, int row, int col) {
        int num = world[row][col] ? -1 : 0;
        for(int r = row - 1; r <= row + 1; r++)
            for(int c = col - 1; c <= col + 1; c++)
                if( inbounds(world, r, c) && world[r][c] )
                    num++;
        return num;
    }
    
    private static boolean inbounds(boolean[][] world, int r, int c) {
        return r >= 0 && r < world.length && c >= 0 &&
        c < world[0].length;
    }
}

```
Dot files will be generated for each method's control flow graph. A sample dot file for the "show" method is:

```
digraph G {
expressionStatement_info_11 -> variableDeclaration_12
java_demo2_TestSample1_show10 -> expressionStatement_info_11
enhancedForStatement_14 -> expressionStatement_20
ifStatement_15->expressionStatement_18
expressionStatement_16 -> enhancedForStatement_14
variableDeclaration_12 -> enhancedForStatement_13
expressionStatement_20 -> enhancedForStatement_13
expressionStatement_18 -> enhancedForStatement_14
enhancedForStatement_13 -> expressionStatement_println_22
enhancedForStatement_14 -> ifStatement_15
ifStatement_15->expressionStatement_16
enhancedForStatement_13 -> enhancedForStatement_14
}
```


which can be viewed using a graphviz viewer such as (https://dreampuf.github.io/GraphvizOnline/)
and output:

<img src="capture1.png">

where the numbers indicate the line number inside the source code.

Similarly, a call graph for the ensemble of logging methods is generated:

<img src="capture2.png">

Due to limitations in time and knowledge, this project remains on a 'toy project' scale, but I hope some of its ideas can be interesting and inspire a 'real' project... 



