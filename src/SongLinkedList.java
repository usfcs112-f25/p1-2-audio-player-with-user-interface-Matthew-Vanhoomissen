/*
 * Makes songLinkedList with head tail and size
 */
public class SongLinkedList {
    private SongNode head;
    private SongNode tail;
    private int size;

    /*
     * Constructor
     */
    public SongLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /*
     * setters
     */
    public void setHead(SongNode head) {
        this.head = head;
    }
    public void setTail(SongNode tail) {
        this.tail = tail;
    }
    public void setSize(int size) {
        this.size = size;
    }

    /*
     * Adds to beginning of list
     * @params the song
     */
    public void addFirst(Song song) {
        SongNode newNode = new SongNode(song);
        if(head != null) {
            newNode.setNext(head);
        }
        head = newNode;
        size++;
        if(size == 1) {
            tail = newNode;
        }
        
        
    }
    /*
     * Adds at index
     * @params index and song 
     */
    public void addAt(int index, Song song) {
        if(get(index) == null) {
            System.out.println("Invalid index");
            return;
        }
        if(index == 1) {
            addFirst(song);
            return;
        }
        else if(index == size) {
            addLast(song);
            return;
        }
        SongNode newNode = new SongNode(song);
        SongNode prev = get(index - 1);
        newNode.setNext(prev.getSongNext());
        prev.setNext(newNode);
        size++;
        
    }
    /*
     * Same as above but removes at index
     * @params where to remove
     */
    public void removeAt(int index) {
        if(get(index) == null) {
            System.out.println("Invalid index");
            return;
        }

        if (index == 1) {
            head = head.getSongNext();
            if (head == null) {
                tail = null;
            } // list became empty
            size--;
            return;
        }
        SongNode prev = get(index - 1);
        SongNode remove = prev.getSongNext();
        prev.setNext(remove.getSongNext());

        if(index == size) {
            tail = prev;
        }
        
        size--;
        
    }

    /*
     * Checks if the playlist contains the
     * @params song
     */
    public boolean contains(Song song) {
        SongNode temp = head;
        while(temp != null) {
            if(temp.getSong().equals(song)) {
                return true;
            }
            temp = temp.getSongNext();
        }
        return false;
    }

    /*
     * gives index of song
     * @params song needed
     * @returns int index
     */
    public int indexOf(Song song) {
        SongNode temp = head;
        int index = 1;
        while(temp != null) {
            if(temp.getSong().equals(song)) {
                return index;
            }
            temp = temp.getSongNext();
            index++;
        }
        return -1;
    }

    /*
     * Clears the linkedlist
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /*
     * Uses index to find song
     * @params index used
     * @returns SongNode if found
     */
    public SongNode get(int index) {
        if(index < 1 || index > size) {
            return null;
        }
        SongNode temp = head;
        int count = 1;
        while(temp != null) {
            if(index == count) {
                return temp;
            }
            else {
                temp = temp.getSongNext();
                count++;
            }
        }
        return null;

    }
    /*
     * Adds to last of list
     * @params song added
     */
    public void addLast(Song song) {
        SongNode newNode = new SongNode(song);
        if(tail != null) {
            tail.setNext(newNode);
        }
        tail = newNode;
        size++;
        if(size == 1) {
            head = newNode;
        }
    }
    /*
     * Removes from from of list
     */
    public void removeFirst() {
        
        head = head.getSongNext();
        
        size--;
        if(size == 0) {
            tail = null;
        }

    }
    /*
     * Removes last of list
     */
    public void removeLast() {
        SongNode currNode = head;
        if(size == 1) {
            tail = null;
            head = null;
            size--;
            return;
        }
        while((currNode.getSongNext()).getSongNext() != null) {
            
            currNode = currNode.getSongNext();
        }
        tail = currNode;
        currNode.setNext(null);
        size--;
        if(size == 0) {
            head = null;
        }
    }

    /*
     * Getters
     */
    public SongNode getHead() {
        return head;
    }
    public SongNode getTail() {
        return tail;
    }
    public int size() {
       return size; 
    }
    /*
     * Checks if size is zero
     * @returns boolean if true
     */
    public boolean isEmpty() {
        if(size == 0) {
            return true;
        }
        return false;
    }
    /*
     * Displays all nodes in list
     */
    public void display() {
        SongNode currNode = head;
        while(currNode != null) {
            System.out.println(currNode);
            currNode = currNode.getSongNext();
        }
    }
        
}
