import java.util.ArrayList;

public class Node implements Comparable<Node>{
    private char c;
    private int count;
    private Node left, right;

    public Node(char c)
    {
        this.c=c;
        this.count=0;
        this.left=null;
        this.right=null;
    }

    public Node(char c, int x)
    {
        this.c=c;
        this.count=x;
        this.left=null;
        this.right=null;
    }

    @Override
    public int compareTo(Node n)
    {
        if(this.count<n.getCount()) return -1;
        if(this.count==n.getCount()) return 0;
        return 1;
    }
    
    public char getChar()
    {
        return this.c;
    }

    public int getCount()
    {
        return this.count;
    }

    public Node getLeft()
    {
        return this.left;
    }

    public Node getRight()
    {
        return this.right;
    }

    public void setLeft(Node left)
    {
        this.left=left;
    }

    public void setRight(Node right)
    {
        this.right=right;
    }

    public void pp()
    {
        this.count++;
    }

    public void setCount(int count)
    {
        this.count=count;
    }

    public String toString()
    {
        return c+Integer.toString(count);
    }

    public void print()
    {
        System.out.println(c+": "+count);
    }

    public void printFromRoot()
    {
        if(this.left!=null) this.left.printFromRoot();
        if(this.right!=null) this.right.printFromRoot();
        if(this.left == null && this.right == null) print();
    }

    public void dictCreate(ArrayList<Elem> dict)
    {
        dictrec(dict,"");
    }

    private void dictrec(ArrayList<Elem> dict, String code)
    {
        if(this.left!=null)
        {
            code+='0';
            this.left.dictrec(dict,code);
        }
        if(this.right!=null)
        {
            String tmp="";
            for(int i=0;i<code.length()-1;i++) tmp+=code.charAt(i);
            code=tmp;
            tmp=null;
            code+='1';
            this.right.dictrec(dict,code);
        }
        if(this.left==null && this.right==null) dict.add(new Elem(code,this.c));
    }
}