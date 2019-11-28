import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
class Board
{
	int N;
	int[] board;
	public static int[] goal_board = new int[9];
	Board()
	{
		
	}
	public Board(int[] board)
	{
		this.N = 3;
		this.board = new int[9];
		for(int i=0;i<9;i++)
		{
			this.board[i] = board[i];
		}
	}
	public Board(int[][] initial)
	{
		this.N = 3;
		this.board = new int[9];
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				this.board[i*3+j]=initial[i][j];
			}
		}
		
	}
	public boolean isGoal()
	{
		for(int i=0;i<8;i++)
		{
			if(goal_board[i]!=board[i])
				return false;
		}
		return true;
	}
	
	public Iterable<Board> neighbours()
	{
		Queue<Board> q = new LinkedList<Board>();
		Board new1;
		int indx = 0;
		for(int i=0;i<9;i++)
		{
			if(board[i]==0)
			{
				indx = i;
				break;
			}
		}
		if(indx/3 != 0)
		{
			new1  = new Board(board);
			new1 = exchange(new1,indx,indx-N);
			q.add(new1);
		}
		if(indx/3 != 2)
		{
			new1  = new Board(board);
			new1 = exchange(new1,indx,indx+N);
			q.add(new1);
		}
		if(indx%3 != 0)
		{
			new1  = new Board(board);
			new1 = exchange(new1,indx,indx-1);
			q.add(new1);
		}
		if(indx%3 != 2)
		{
			new1  = new Board(board);
			new1 = exchange(new1,indx,indx+1);
			q.add(new1);
		}
		return q;
	}
	public Board exchange(Board b,int a1,int a2)
	{
		int temp = b.board[a1];
		b.board[a1] = b.board[a2];
		b.board[a2] = temp;
		return b;
	}
	public boolean equals(Object y)
	{
		if(y==this)
			return true;
		else if(y == null)
			return false;
		else if(y.getClass() != this.getClass())
			return false;
		
		Board that =(Board)y;
		return  Arrays.equals(that.board, this.board);
	}
	
	public int Manhattan()
	{
		int sum = 0;
		for(int i=0;i<9;i++)
		{
			if(board[i]!=0 && board[i]!=i+1)
				sum+=Manhattan(board[i],i);
		}
		return sum;
	}
	public int Manhattan(int goal,int intial)
	{
		int row = Math.abs((goal-1)/3 - (intial/3));
		int col = Math.abs((goal-1)%3 - (intial%3));
		return row + col;
	}
}
public class Solver
{
	private static SearchNode goal;
	private class SearchNode
	{
		Board b;
		int moves;
		SearchNode prev;
		public SearchNode(Board b) 
		{
			this.b = b;
			this.moves = 0;
			this.prev = null;
		}
	}
	Solver(Board b)
	{
		PriorityOrder po = new PriorityOrder();
		PriorityQueue<SearchNode> pq = new PriorityQueue<SearchNode>(po);
		SearchNode n = new SearchNode(b);
		pq.add(n);
		int counter = 0;
		SearchNode min = pq.remove();
		while(!min.b.isGoal())
		{
			if(counter++==150)
			{
				System.out.println("Taking long time! Solution not possible!");
				System.exit(0);
			}
			for(Board neigh: min.b.neighbours())
			{
				if(min.prev == null || !neigh.equals(min.prev.b))
				{
					SearchNode new1 = new SearchNode(neigh);
					new1.moves = min.moves+1;
					new1.prev = min;
					pq.add(new1);
				}
				
			}
			for(int i=0;i<9;i++)
			{
				System.out.print(min.b.board[i]);
			}
			System.out.println();
			min = pq.remove();
		}
		
		if(min.b.isGoal())
		{
			goal = min;
		}
		else
			goal = null;
	}
	public boolean isSolvable()
	{
		if(goal == null)
		{
			return false;
		}
		return true;
	}
	public int noofmoves()
	{
		if(isSolvable())
		{
			return goal.moves;
		}
		return -1;
	}
	public void printSteps(SearchNode result)
	{
		Stack<SearchNode> stk  = new Stack<SearchNode>();
		SearchNode temp = result;
		while(temp!=null)
		{
			stk.add(result);
			temp = temp.prev;
		}
		
		while(!stk.isEmpty())
		{
			pprint(stk.peek().b);
			stk.pop();
		}
	}
	public void pprint(Board b)
	{
		for(int i=0;i<9;i++)
		{
			if(i%3 == 0)
			{
				System.out.println();
			}
			System.out.print(b.board[i]+" ");
		}
		System.out.println("\n----------------");
	}
	class PriorityOrder implements Comparator<SearchNode>
	{

		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			int f1 = o1.b.Manhattan()+o1.moves;
			int f2 = o2.b.Manhattan()+o2.moves;
			if(f1 > f2)
				return 1;
			else if(f1<f2)
				return -1;
			return 0;
		}
		
	}
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int[][] board = new int[3][3];
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
				board[i][j] = sc.nextInt();
		}
		Board temp = new Board();
		for(int j=0;j<9;j++)
			temp.goal_board[j] = sc.nextInt();
		Board b = new Board(board);
		Solver s = new Solver(b);
		if(!s.isSolvable())
		{
			System.out.println("Can't be solved!");
		}
		else
		{
			System.out.println("No. of Steps: "+s.noofmoves());
			s.printSteps(goal);
		}
	}
}