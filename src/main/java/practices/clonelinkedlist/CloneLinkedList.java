package practices.clonelinkedlist;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

// https://www.geeksforgeeks.org/a-linked-list-with-next-and-arbit-pointer/
public class CloneLinkedList {
    
    static class Link {
        static final AtomicInteger idCounter = new AtomicInteger();
        
        final int    id = idCounter.getAndIncrement();
        final String value;
        
        Link next   = null;
        Link random = null;
        public Link(String value) {
            this(value, null);
        }
        public Link(String value, Link next) {
            this.value  = value;
            this.next   = next;
        }
        
        public String toString() {
            String nextValue   = (next   != null) ? "#" + next.id   + ":" + next.value   : null;
            String randomValue = (random != null) ? "#" + random.id + ":" + random.value : null;
            return id + "=" + value + ":(" + nextValue + "," + randomValue + ")" + ((next != null) ? " -> " + next : "");
        }
        public void traverse(Function<Link, Link> action) {
            Link next = action.apply(this);
            if (next  != null)
                next.traverse(action);
        }
    }
    
    private static Link insertClone(Link orgLink) {
        if (orgLink == null)
            return null;
        
        Link nextLink = orgLink.next;
        Link newLink  = new Link(orgLink.value, orgLink.next);
        orgLink.next  = newLink;
        return nextLink;
    }
    private static Link setRandom(Link orgLink) {
        if ((orgLink      == null)
         || (orgLink.next == null))
            return null;
        
        Link nextLink = orgLink.next.next;
        if (orgLink.random != null) 
            orgLink.next.random = orgLink.random.next;
        
        return nextLink;
    }
    private static Link splitLink(Link orgLink) {
        if (orgLink == null)
            return null;
        if (orgLink.next == null)
            return null;
        
        Link next = orgLink.next.next;
        if (next != null)
            orgLink.next.next = next.next;
        orgLink.next = next;
        
        return next;
    }
    
    static Link cloneLink(Link orgLink) {
        orgLink.traverse(link -> insertClone(link));
        orgLink.traverse(link -> setRandom  (link));
        
        Link newLink = orgLink.next;
        orgLink.traverse(link -> splitLink  (link));
        return newLink;
    }
    
    static Link create() {
        Link link1 = new Link("1");
        Link link2 = new Link("2");
        Link link3 = new Link("3");
        Link link4 = new Link("4");
        Link link5 = new Link("5");
        
        Link orgLink = link1;
        link1.next = link2;
        link2.next = link3;
        link3.next = link4;
        link4.next = link5;
        
        orgLink.random                     = orgLink.next.next;
        orgLink.next.random                = orgLink;
        orgLink.next.next.random           = orgLink.next.next.next.next;
        orgLink.next.next.next.random      = orgLink.next.next;
        orgLink.next.next.next.next.random = orgLink.next;
        return orgLink;
    }
    
    public static void main(String[] args) {
        Link orgLink = create();
        
        System.out.println(orgLink);
        
        Link newLink = cloneLink(orgLink);
        System.out.println(newLink);
    }
    
}
