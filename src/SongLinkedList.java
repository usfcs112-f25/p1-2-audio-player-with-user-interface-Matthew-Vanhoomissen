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
            head.setPrev(newNode);
        }
        head = newNode;
        size++;
        if(size == 1) {
            tail = newNode;
        }
        
        
    }
    public void insertBefore(int index, Song song) {
        SongNode temp = get(index);
        SongNode newSong = new SongNode(song);
        if(temp == null) {
            System.out.println("Invalid index");
            return;
        }
        if(temp.getPrev() == null) {
            addFirst(song);
        } 
        
        temp.getPrev().setNext(newSong);
        
        newSong.setNext(temp);
        newSong.setPrev(temp.getPrev());
        temp.setPrev(newSong);
    }

    public void insertAfter(int index, Song song) {
        SongNode temp = get(index);
        SongNode newSong = new SongNode(song);
        if(temp == null) {
            System.out.println("Invalid index");
        }
        if(temp.getSongNext() == null) {
            addLast(song);
        }
        newSong.setPrev(temp);
        newSong.setNext(temp.getSongNext());
        temp.setNext(newSong);
        newSong.getSongNext().setPrev(newSong);
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
        SongNode next = get(index);
        next.setPrev(newNode);
        newNode.setNext(next);
        prev.setNext(newNode);
        newNode.setPrev(prev);
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
            removeFirst();
            return;
        }
        else if(index == size) {
            removeLast();
            return;
        }
        SongNode prev = get(index - 1);
        SongNode next = get(index + 1);
        SongNode remove = prev.getSongNext();
        next.setPrev(prev);
        prev.setNext(next);
        remove.setPrev(null);

        
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
        SongNode start = head;
        while(start != null) {
            SongNode temp = start.getSongNext();
            start.setPrev(null);
            start.setNext(null);
            start = temp;
        }
        
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
            newNode.setPrev(tail);
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
        if(size == 1) {
            tail = null;
            head = null;
            size--;
            return;
        }

        head.getSongNext().setPrev(null);
        head = head.getSongNext();
        
        size--;
        

    }
    /*
     * Removes last of list
     */
    public void removeLast() {
        
        if(size == 1) {
            tail = null;
            head = null;
            size--;
            return;
        }
        SongNode newEnd = tail.getPrev();
        tail.getPrev().setNext(null);
        tail.setPrev(null);
        tail = newEnd;
        size--;
        
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
