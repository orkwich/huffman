import java.util.ArrayList;

public class Queue
{
    private ArrayList<Node> l;

    public Queue()
    {
        l = new ArrayList<>();
    }

    public Queue(Node x)
    {
        l = new ArrayList<>();
        add(x);
    }

    public void add(Node x)
    {
        int i;
        for(i=0;i<l.size();i++)
            if(l.get(i).compareTo(x)!=-1) break;
        l.add(i,x);
    }

    public void print()
    {
        for(int i=0;i<l.size();i++) l.get(i).print();
    }

    public boolean isEmpty()
    {
        return l.isEmpty();
    }

    public Node pop()
    {
        Node tmp = l.get(0);
        l.remove(0);
        return tmp;
    }
}
