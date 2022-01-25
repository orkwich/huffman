import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HuffmanTest {
    @Test
    public void testHuffmanComp()
    {
        Huffman huf = new Huffman();
        assertTrue(huf.huffman("txt/niepewnosc.txt", true)>0);
    }
    @Test
    public void testHuffmanDeComp()
    {
        Huffman huf = new Huffman();
        assertTrue(huf.huffman("compressed", false)>0);
    }
}
