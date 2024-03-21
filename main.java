import java.util.Random;

class RBTree {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        int key;
        int value;
        Node left, right;
        boolean color;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.color = RED;
        }
    }

    private Node root;
    private Random random = new Random();

    public void add(int key, int value) {
        root = add(root, key, value);
        root.color = BLACK;
    }

    private Node add(Node node, int key, int value) {
        if (node == null)
            return new Node(key, value);

        if (key < node.key)
            node.left = add(node.left, key, value);
        else if (key > node.key)
            node.right = add(node.right, key, value);
        else
            node.value = value;

        if (isRed(node.right) && !isRed(node.left))
            node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))
            node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
            flipColors(node);

        return node;
    }

    public int remove(int key) {
        Node removedNode = remove(root, key);
        if (removedNode != null)
            return removedNode.value;
        else
            return -1; // Return -1 if key not found
    }

    private Node remove(Node node, int key) {
        if (node == null)
            return null;

        if (key < node.key)
            node.left = remove(node.left, key);
        else if (key > node.key)
            node.right = remove(node.right, key);
        else {
            if (node.left == null)
                return node.right;
            else if (node.right == null)
                return node.left;
            else {
                Node minNode = findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = deleteMin(node.right);
            }
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null)
            node = node.left;
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null)
            return node.right;
        node.left = deleteMin(node.left);
        return node;
    }

    public int get(int key) {
        Node node = root;
        while (node != null) {
            if (key < node.key)
                node = node.left;
            else if (key > node.key)
                node = node.right;
            else
                return node.value;
        }
        return -1; // Return -1 if key not found
    }

    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null)
            return -1;
        else
            return 1 + Math.max(height(node.left), height(node.right));
    }

    private boolean isRed(Node node) {
        if (node == null)
            return false;
        return node.color == RED;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    private void flipColors(Node node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    public static void main(String[] args) {
        RBTree rbTree = new RBTree();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int key = random.nextInt(10);
            int value = random.nextInt(100);
            rbTree.add(key, value);
        }

        System.out.println("");
        printTree(rbTree.root);

        System.out.println("");
        System.out.println("Height of the tree: " + rbTree.height());

        // Testing get method
        int keyToFind = 5;
        System.out.println("Value for key " + keyToFind + ": " + rbTree.get(keyToFind));

        // Testing remove method
        int keyToRemove = 3;
        int removedValue = rbTree.remove(keyToRemove);
        System.out.println("Removed value for key " + keyToRemove + ": " + removedValue);

        System.out.println("");
        printTree(rbTree.root);
    }

    private static void printTree(Node node) {
        if (node != null) {
            printTree(node.left);
            System.out.println("Key: " + node.key + ", Value: " + node.value + ", Color: " + (node.color == RED ? "RED" : "BLACK"));
            printTree(node.right);
        }
    }
}
