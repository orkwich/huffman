import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import exceptions.NoDataToCompressException;
import exceptions.NotInitializedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Huffman {

    public int huffman(String path, boolean compress) throws NoDataToCompressException, NotInitializedException
    {
        int res=-1;
    	if(compress)
        {
            String tmp="";
            try{tmp=readAndFixData(path);}
            catch(IOException e){System.out.println("cant read given file");e.printStackTrace();}
            if(tmp!="")
            {
                tmp=compress(tmp);
                res = tmp.length();
                try{writeData("compressed/compressed",BinaryStringToBytes(tmp));}
                catch(IOException e){System.out.println("cant write file");e.printStackTrace();}
            } else throw new NoDataToCompressException("no data to compress, file is empty or not opened");
        } else
        {
            byte[] b=null;
            ArrayList<Node> l=null;
            try{b=readBytesFile(path+"/compressed");}
            catch(IOException e){System.out.println("cant read compressed file");e.printStackTrace();}
            try{l=readCountFromStr(readData(path+"/count.txt"));}
            catch(IOException e){System.out.println("cant read count file");e.printStackTrace();}
            System.out.println("git");
            if(b!=null && l!=null)
            {
                String tmp=decompress(b,l);
                System.out.println("decompressed:\n"+tmp);
                res=tmp.length();
            }
            else if(b==null) throw new NotInitializedException("bytes array not initialized");
            else if(l==null) throw new NotInitializedException("node array list not initialized");
        }
        System.out.println("result: "+res+'\n');
        return res;
    }

    private ArrayList<Node> count(String data)
    {
        ArrayList<Node> c = new ArrayList<>();
        c.add(new Node(data.charAt(0)));
        c.get(0).pp();
        for(int i=1;i<data.length();i++)
        {
            for(int j=0;j<c.size();j++)
            {
                if(c.get(j).getChar()==data.charAt(i))
                {
                    c.get(j).pp();
                    break;
                }
                if(j==c.size()-1)c.add(new Node(data.charAt(i)));
            }
        }
        return c;
    }

    private String compress(String data)
    {
        ArrayList<Node> c = count(data);
        Node root = tree(c);
        ArrayList<Elem> dictionary = new ArrayList<>();
        root.dictCreate(dictionary);
        String compressed="";
        for(int i=0;i<data.length();i++)
            for(int j=0;j<dictionary.size();j++)
                if(dictionary.get(j).c==data.charAt(i)) compressed+=dictionary.get(j).code;
        return compressed;
    }

    private Node tree(ArrayList<Node> count)
    {
        String countToStr = "";
        Queue q = new Queue();
        for(int i=0;i<count.size();i++)
        {
            q.add(count.get(i));
            countToStr+=count.get(i).toString()+'\n';
        }
        try{writeData("compressed/count.txt", countToStr.getBytes());}
        catch(IOException e){System.out.println("cant write counted letters");e.printStackTrace();}
        count = null;
        Node root = null;
        while(!q.isEmpty())
        {
            Node first, second, parent;
            first = q.pop();
            second = q.pop();
            parent = new Node('\0', first.getCount()+second.getCount());
            if(first.compareTo(second)==1)
            {
                Node tmp = first;
                first = second;
                second = tmp;
            }
            parent.setLeft(first);
            parent.setRight(second);
            if(!q.isEmpty()) q.add(parent);
            else root = parent;
        }
        q=null;
        return root;
    }

    private byte[] BinaryStringToBytes(String binaryString)
    {
        for(int i=0;i<binaryString.length();i++)
            if(binaryString.charAt(i)!='1' && binaryString.charAt(i)!='0') return null;
        int len = binaryString.length()%8;
        if(len!=0)
            for(int i=0;i<(8-len);i++) binaryString='0'+binaryString;
        byte[] result = new byte[(binaryString.length()/8)+1];
        int i=0,j=0;
        while (i < binaryString.length())
        {
            result[j++] = (byte)Integer.parseInt((binaryString.substring(i,i+8)),2);
            i+=8;
        }
        int tmp = 8-len;
        result[j] = (byte)tmp;
        return result;
    }

    private String decompress(byte[] data, ArrayList<Node> signsCount)
    {
        String read="";
        int z = data[data.length-1];
        if(z<8) read+=String.format("%"+(8-z)+"s", Integer.toBinaryString(data[0] & 0xFF)).replace(' ', '0');
        for(int i=1;i<data.length-1;i++)
            read+=String.format("%8s", Integer.toBinaryString(data[i] & 0xFF)).replace(' ', '0');
        Node root = tree(signsCount);
        ArrayList<Elem> dictionary = new ArrayList<>();
        root.dictCreate(dictionary);
        String decompressed="";
        while(read!="")
        {
            for(int i=0;i<dictionary.size();i++)
            {
                if(dictionary.get(i).code.length()>read.length()) continue;
                if(read.substring(0,dictionary.get(i).code.length()).equals(dictionary.get(i).code))
                {
                    decompressed+=dictionary.get(i).c;
                    if(read.length()==dictionary.get(i).code.length())
                    {
                        read="";
                        break;
                    }
                    read=read.substring(dictionary.get(i).code.length());
                    break;
                }
            }
        }
        return decompressed;
    }
    
    private ArrayList<Node> readCountFromStr(String data)
    {
        ArrayList<Node> l = new ArrayList<>();
        int i=0;
        while(i<data.length())
        {
            Node n = new Node(data.charAt(i++));
            String tmpStr="";
            do{tmpStr+=data.charAt(i++);}
            while(data.charAt(i)!='\n');
            i++;
            n.setCount(Integer.parseInt(tmpStr));
            l.add(n);
        }
        return l;
    }

    private String readAndFixData(String path) throws IOException
    {
        String s="";
        FileInputStream f=null;
        try{f = new FileInputStream(path);}
        catch(FileNotFoundException e)
        {
            System.out.println("FIlENOTFOUND");
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(f, "UTF8"));
        char tmp = fixChar((char)reader.read());
        while(tmp!=(char)-1)
        {
            if(tmp!='\0') s+=(char)tmp;
            tmp=fixChar((char)reader.read());
            
        }
        reader.close();
        return s;
    }

    private String readData(String path) throws IOException
    {
        String s="";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
        char tmp = (char)reader.read();
        while(tmp!=(char)-1)
        {
            if(tmp!='\0') s+=(char)tmp;
            tmp=(char)reader.read();
        }
        reader.close();
        return s;
    }

    private byte[] readBytesFile(String path) throws IOException
    {
        File file = new File(path);
        byte[] data = Files.readAllBytes(file.toPath());
        return data;
    }

    private void writeData(String fileName, byte[] data) throws IOException
    {
        OutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
    }
    
    private char fixChar(char c)
    {
        if(c==(char)-1) return c;
        if(c==(char)0x0104) c='A';
        if(c==(char)0x0105) c='a';
        if(c==(char)0x0106) c='C';
        if(c==(char)0x0107) c='c';
        if(c==(char)0x0118) c='E';
        if(c==(char)0x0119) c='e';
        if(c==(char)0x0141) c='L';
        if(c==(char)0x0142) c='l';
        if(c==(char)0x0143) c='N';
        if(c==(char)0x0144) c='n';
        if(c==(char)0x015A) c='S';
        if(c==(char)0x015B) c='s';
        if(c==(char)0x0179 || c==(char)0x017B) c='Z';
        if(c==(char)0x017A || c==(char)0x017C) c='z';
        if(c==(char)0x00D3) c='O';
        if(c==(char)0x00F3) c='o';


        if(c>=(char)0x201C && c<=(char)0x201F) c='"';
        if(c>=(char)0x2010 && c<=(char)0x2015) c='-';

        if(c>=(char)0x007F) c='\0';
        return c;
    }
}