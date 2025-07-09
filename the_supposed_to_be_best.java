import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import za.ac.wits.snake.DevelopmentAgent;

public class MyAgent extends DevelopmentAgent {

    public static void main(String args[]) {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }
    public static int[][] drawLine(int[][] boards, String point1, String point2, int sn) {
    	int[][] board=boards;
        String[] p1 = point1.split(",");
        String[] p2 = point2.split(",");
        int x1 = Integer.parseInt(p1[0]);
        int y1 = Integer.parseInt(p1[1]);
        int x2 = Integer.parseInt(p2[0]);
        int y2 = Integer.parseInt(p2[1]);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                board[y][x] = sn;
            }
        }
        return board;
    }
    public static void drawSnake(String snakeInfo, int snakeNumber, int[][] boards,int bool,int[] heads,int i) {
    	int[][] board=boards;
    	int numheads=7;
        String[] points = snakeInfo.split(" ");
        if(points[0].equals("dead")) {
			numheads--;
			heads[i+7]=1;
		    return;
		}else {
			heads[i+7]=0;
		}
        int len =points.length - 1;
        if(bool==2) {
        	len-=3;
        }
        if(bool != 0 ) {
        	int pos=13;
        	String[] p1 ;
        	if(bool == 1) {
        		 p1 = points[0].split(",");
        		 pos = 1+2*i;
        	}else {
        		 p1 = points[3].split(",");
        		 pos=11+2*i;
        	}
        	int ch=Integer.parseInt(p1[0]);
        	int rh=Integer.parseInt(p1[1]);
        	heads[pos]=rh;
        	heads[pos+1]=ch;
        }
        for (int j = 0; j < len; j++) {
        	if(bool != 2) {
        		board=drawLine(board, points[j], points[j + 1], snakeNumber);
        	}else {
        		board=drawLine(board, points[j+3], points[j + 1+3], snakeNumber);
        	}
        }
        heads[0]=numheads;
    }
    public int go_to(int[][] matrix,String[] h,int ra,int ca) {
    	int ch=Integer.parseInt(h[0]);
    	int rh=Integer.parseInt(h[1]);
    	if(ch<ca  &&  matrix[rh][ch+1]==0  ) { 
    		return 3 ;    	
    	}if( ca<ch  &&  matrix[rh][ch-1]==0 ) {
    		return 2;
    	}
    	if( rh<ra  &&  matrix[rh+1][ch]==0) {
    		return 1;
    	}
    	if ( ra<rh &&  matrix[rh-1][ch]==0) {
    		return 0;
    	}
    	if (rh!=0) {
    		if(matrix[rh-1][ch]==0) {
    			return 0;
    		}
    	}if(rh<49) {
    		if(matrix[rh+1][ch]==0) {
    			return 1;
    		}
    	}
    	if(ch!=0) {
    		if(matrix[rh][ch-1]==0) {
    			return 2;
    		}
    	}
    	return 3;
    }
    public static boolean isValidMove(int[][] matrix, boolean[][] visited, int row, int col,int[] heads,int t) {
    	boolean retur = row >= 0 && row < 50 && col >= 0 && col < 50 && (matrix[row][col] == 0);
    	
    	if(t==0 || retur ==false)  {
    		retur=retur && !visited[row][col] ;
    		return retur;
    	}
    	else {
    		for(int i=0;i<3;i++) {
    			int dis =Math.abs(heads[1+2*i]-row)+Math.abs(heads[2+2*i]-col);
				if(dis==1) {
					return false;
				}
    		}
    		for(int i=0;i<4;i++) {
    			if(heads[7+i]==0  & i!=heads[19] ) {
    				int dis =Math.abs(heads[11+2*i]-row)+Math.abs(heads[12+2*i]-col);
    				if(dis==1) {
    					return false;
    				}
    			}
    		}
    		
    	}
        return retur;
    }
    public static List<int[]> findShortestPath(int[][] matrix, int[] A, int[] B,int[] heads) {
    	final int[] ROW_DIRECTIONS = {-1, 1, 0, 0};
        final int[] COL_DIRECTIONS = {0, 0, -1, 1};
        int n = matrix.length;
        boolean[][] visited = new boolean[n][n]; 
        Queue<int[]> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>(); 

        queue.offer(new int[]{A[0], A[1], 0}); 
        visited[A[0]][A[1]] = true;
        parentMap.put(A[0] + "," + A[1], null); 

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            int dist = current[2];
            if (row == B[0] && col == B[1]) {
                return reconstructPath(parentMap, B); 
            }
            for (int i = 0; i < 4; i++) {
                int newRow = row + ROW_DIRECTIONS[i];
                int newCol = col + COL_DIRECTIONS[i];

                if (isValidMove(matrix, visited, newRow, newCol,heads,0)) {
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol, dist + 1});
                    parentMap.put(newRow + "," + newCol, row + "," + col);
                }
            }
        }
        return Collections.emptyList(); 
    }
    public static List<int[]> reconstructPath(Map<String, String> parentMap, int[] destination) {
        List<int[]> path = new ArrayList<>();
        String current = destination[0] + "," + destination[1]; 
        while (current != null) {
            String[] parts = current.split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            path.add(new int[]{row, col});
            current = parentMap.get(current); 
        }
        Collections.reverse(path);
        return path;
    }
    public int direction(int [] a, int[] b) {
    	if(a[0]>b[0] ) {
    		return 0;
    	}
    	if(a[0]<b[0]) {
    		return 1;
    	}
    	if(a[1]>b[1]) {
    		return 2;
    	}
    	return 3;
    }
    public int[] decide(int[][] matrix, int[] apple_c,int[] heads) {
    	boolean continuee= true;
    	for(int t=0;t<4;t++) {
    		
    	}
    	if(continuee) {
    		return apple_c;
    	}else {
    		
    	}
    	int[] coords= new int[2];
    	coords= apple_c;
    	return coords;
    }
    public int check_safety(int[][] matrix, int[] heads) {
    	return 0;
    }
    public int[] go_to_safety(int[][] matrix) {
    	return null;
    }
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
        	final int[] ROW_DIRECTIONS = {-1, 1, 0, 0};
            final int[] COL_DIRECTIONS = {0, 0, -1, 1};
            final String[] ddddd= {"up","down","left","right"};
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            int [][] matrix = new int[50][50];
            int[] heads = new int[20] ;
            List<int[]> path = new ArrayList<>();
            while (true) {
            	for(int t=0;t<50;t++) {
                	for(int i =0 ; i<50 ;i++) {
                		matrix[t][i]=0;
                	}
                }
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }
                
                String[] p1 = line.split(" ");
                int acol = Integer.parseInt(p1[0]);
                int arow = Integer.parseInt(p1[1]);
                int nObstacles = 3;
                for (int obstacle = 0; obstacle < nObstacles; obstacle++) {
                    String obs = br.readLine();
                    drawSnake(obs,9,matrix,0,heads,obstacle);
                }
                int nZombies = 3;
                for (int zombie = 0; zombie < nZombies; zombie++) {
                    String zom = br.readLine();
                    System.out.println("log "+zom);
                    drawSnake(zom,7,matrix,1,heads,zombie);
                }
                String[] head = null;
                int mySnakeNum = Integer.parseInt(br.readLine());
                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();
                    if (i == mySnakeNum) {
                    	System.out.println("log "+i+"---"+snakeLine);
                    	heads[19]=i;
                    	drawSnake(snakeLine,i+1,matrix,2,heads,i);
                    	String[] splited=snakeLine.split(" ");
                    	head = splited[3].split(",");
                    }
                    else {
                    	drawSnake(snakeLine,i+1,matrix,2,heads,i);
                    	System.out.println("log "+snakeLine);
                    }
                }int move=0;
                int rh=Integer.parseInt(head[1]);
                int ch=Integer.parseInt(head[0]);
                path = findShortestPath( matrix, new int[] {Integer.parseInt(head[1]),Integer.parseInt(head[0])}   , new int[] {arow,acol},heads);
                if(path.isEmpty()) {
                	System.out.println("THERE IS NO PATH" );
                	move = go_to(matrix,head,arow,acol);
                	int row =rh+ROW_DIRECTIONS[move];
                	int col=ch+COL_DIRECTIONS[move];
                	if(!isValidMove(matrix, new boolean[][] {},row,col,heads,1) ) {
                		int new_move = 0;
                		if(row >= 0 && row < 50 && col >= 0 && col < 50 ) {
                			matrix[row][col]=5;
                		}
                		new_move=go_to(matrix,head,arow,acol);
                		if(isValidMove(matrix, new boolean[50][50],rh+ROW_DIRECTIONS[new_move],ch+COL_DIRECTIONS[new_move],heads,1)) {
                			move=new_move;
                		}else {
                			row =rh+ROW_DIRECTIONS[new_move];
                        	col=ch+COL_DIRECTIONS[new_move];
                        	if(-1<row && row<50 && col<50 && -1<col ) {
                        		matrix[row][col]=5;
                        	}
                			for (int i = 0; i < 4; i++) {
                    			row = rh + ROW_DIRECTIONS[i];
                                col = ch + COL_DIRECTIONS[i];
                                if(isValidMove(matrix, new boolean[50][50],row,col,heads,1)) {
                                	move=i;break;
                                }
                            }
                		}
                	}
                }
                else {
                	move = direction(path.get(0),path.get(1));
                	int row =rh+ROW_DIRECTIONS[move];
                	int col =ch+COL_DIRECTIONS[move];
                	boolean found=false;
                	if(row==arow && col==acol) {
                		found=true;
                		for(int i=0;i<4;i++) {
                			if(heads[7+i]==0) {
                				int dis =Math.abs(heads[11+2*i]-row)+Math.abs(heads[12+2*i]-col);
                				if(dis==1 && heads[19]!=i){
                					System.out.println("Snake "+ i+ " will eat the apple:-( ");
                					found = false;
                				}
                			}
                		}
                	}
                	if(!found) {
                		if(!isValidMove(matrix, new boolean[][] {},row,col,heads,1) ) {
                			System.out.println("log going in the inital direction is not safe---- "+ddddd[move]);
                			for (int i = 0; i < 4; i++) {
                				matrix[row][col]=5;
                    			int newRow = rh + ROW_DIRECTIONS[i];
                                int newCol = ch + COL_DIRECTIONS[i];
                                if(newRow==row  && newCol==col) {
                                	continue;
                                }
                                path=findShortestPath( matrix, new int[] {Integer.parseInt(head[1]),Integer.parseInt(head[0])}   , new int[] {arow,acol},heads);
                                if(path.isEmpty()) {
                                	System.out.println("log there is no other path");
                                	int new_move = go_to(matrix,head,arow,acol);
                                	int nrow =rh+ROW_DIRECTIONS[new_move];
                                	int ncol=ch+COL_DIRECTIONS[new_move];
                                	if(!isValidMove(matrix, new boolean[][] {},nrow,ncol,heads,1) ) {
                                		System.out.println("log and going "+ ddddd[new_move] +"is not safe");
                                		if(nrow >= 0 && nrow < 50 && ncol >= 0 && ncol < 50 ) {
                                			matrix[nrow][ncol]=5;
                                		}
                                		new_move=go_to(matrix,head,arow,acol);
                                		if(isValidMove(matrix, new boolean[50][50],rh+ROW_DIRECTIONS[new_move],ch+COL_DIRECTIONS[new_move],heads,1)) {
                                			System.out.println("log i find going  "+ ddddd[new_move] +" safe" );
                                			move=new_move;break;
                                		}else {
                                			System.out.println("log i find going  "+ ddddd[new_move] +" un_safe" );
                                			nrow =rh+ROW_DIRECTIONS[new_move];
                                        	ncol=ch+COL_DIRECTIONS[new_move];
                                        	if(-1<nrow && nrow<50 && ncol<50 && -1<ncol ) {
                                        		matrix[nrow][ncol]=5;
                                        	}
                                			for (int ii = 0;ii < 2;ii++) {
                                				new_move=go_to(matrix,head,arow,acol);
                                    			nrow = rh + ROW_DIRECTIONS[new_move];
                                                ncol = ch + COL_DIRECTIONS[new_move];
                                                if(isValidMove(matrix, new boolean[50][50],nrow,ncol,heads,1)) {
                                                	move=i;break;
                                                }else {
                                                	System.out.println("log i find going  "+ ddddd[new_move] +" unsafe" );
                                                	if(nrow >= 0 && nrow < 50 && ncol >= 0 && ncol < 50 ) {
                                            			matrix[nrow][ncol]=5;
                                            		}
                                                }
                                            }
                                		}
                                	}else {
                                		System.out.println("log i find going  "+ ddddd[new_move] +" safe" );
                                		move=new_move;
                                	}
                                break;	
                                	
                                }else {
                                	int newmove= direction(path.get(0),path.get(1));
                                    if(isValidMove(matrix, new boolean[][] {},rh+ROW_DIRECTIONS[newmove],ch+COL_DIRECTIONS[newmove],heads,1)) {
                                    	System.out.println("log i find going  "+ ddddd[newmove] +" safe" );
                                    	move=newmove;break;
                                    }
                                    System.out.println("log i find going  "+ ddddd[newmove] +" as a mistake!" );
                                    row=rh+ROW_DIRECTIONS[newmove];col=ch+COL_DIRECTIONS[newmove];
                                }
                                
                            }
                    	}
                	}
                }
                System.out.println(move);
                System.out.println("log final decision was going "+ ddddd[move] );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}