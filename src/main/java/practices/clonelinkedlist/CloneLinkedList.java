package practices.clonelinkedlist;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

// https://www.geeksforgeeks.org/a-linked-list-with-next-and-arbit-pointer/
public class CloneLinkedList {
    
    static class Node {
        // The ID is added to show i
        static final AtomicInteger idCounter = new AtomicInteger(1);
        final int                  id        = idCounter.getAndIncrement();
        
        final String value;
        
        Node next   = null;
        Node random = null;
        public Node(String value) {
            this(value, null);
        }
        public Node(String value, Node next) {
            this.value  = value;
            this.next   = next;
        }
        
        public String toString() {
            String nextValue   = (next   != null) ? "#" + next.id   + ":" + next.value   : null;
            String randomValue = (random != null) ? "#" + random.id + ":" + random.value : null;
            return id + "=" + value + ":(" + nextValue + "," + randomValue + ")" + ((next != null) ? " -> " + next : "");
        }
        public void traverse(Function<Node, Node> action) {
            Node next = action.apply(this);
            if (next  != null)
                next.traverse(action);
        }
    }
    
    private static Node insertClone(Node orgNode) {
        if (orgNode == null)
            return null;
        
        Node nextNode = orgNode.next;
        Node newNode  = new Node(orgNode.value, orgNode.next);
        orgNode.next  = newNode;
        return nextNode;
    }
    private static Node setRandom(Node orgNode) {
        if ((orgNode      == null)
         || (orgNode.next == null))
            return null;
        
        Node nextLink = orgNode.next.next;
        if (orgNode.random != null) 
            orgNode.next.random = orgNode.random.next;
        
        return nextLink;
    }
    private static Node unzip(Node orgNode) {
        if (orgNode == null)
            return null;
        if (orgNode.next == null)
            return null;
        
        Node next = orgNode.next.next;
        if (next != null)
            orgNode.next.next = next.next;
        
        orgNode.next = next;
        
        return next;
    }
    
    static Node cloneList(Node orgHead) {
        orgHead.traverse(node -> insertClone(node));
        Node newHead = orgHead.next;
        
        orgHead.traverse(node -> setRandom(node));
        orgHead.traverse(node -> unzip    (node));
        return newHead;
    }
    
    static Node create() {
        Node node1 = new Node("1");
        Node node2 = new Node("2");
        Node node3 = new Node("3");
        Node node4 = new Node("4");
        Node node5 = new Node("5");
        
        Node orgNode = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        
        orgNode.random                     = orgNode.next.next;
        orgNode.next.random                = orgNode;
        orgNode.next.next.random           = orgNode.next.next.next.next;
        orgNode.next.next.next.random      = orgNode.next.next;
        orgNode.next.next.next.next.random = orgNode.next;
        return orgNode;
    }
    
    public static void main(String[] args) {
        Node orgLink = create();
        
        System.out.println(orgLink);
        
        Node newLink = cloneList(orgLink);
        System.out.println(newLink);
    }
    
}
