import javax.swing.tree.TreeNode;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}


class Solution {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    List<Integer> answer = new ArrayList<>();
    Set<Integer> visited = new HashSet<>();

    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        buildGraph(root, null); // this basically kicks off the graph construction where the root has no parent
        visited.add(target.val);
        dfs(target.val, 0, k);
        return answer;
    }

    private void buildGraph(TreeNode cur, TreeNode parent) {
        if (cur != null && parent != null) { // will descend tree
            graph.computeIfAbsent(cur.val, k -> new ArrayList<>()).add(parent.val); // add parent as value with child as
            // key. 'k' here is part of a lambda expression and is a different 'k' than 'int k' for dist b/w nodes
            graph.computeIfAbsent(parent.val, k -> new ArrayList<>()).add(cur.val); // this will create a new arraylist
            // for parent val when firrst edge is created, but will return existing value otherwise which in this case
            // would retrive the parent nodes arraylist which already contains one child as an edge an dthen would add
            // second child as second edge. Kind of like a 'getOrDefault' method but more like 'createOrRetrieve'
        }
        if (cur.left != null) {
            buildGraph(cur.left, cur);
        }
        if (cur.right != null) {
            buildGraph(cur.right, cur);
        }
    }

    private void dfs(int cur, int distance, int k) {
        if (distance == k) {
            answer.add(cur);
            return;
        }
        for (int neighbor : graph.getOrDefault(cur, new ArrayList<>())) { // retrieves arrayList value from int key 'cur'
            // If not arrraylist val present, then generate new list. If arrayList does exist, then loop through its
            // elements of type int 'neighbor'. A 'full' arraylist will contain 3 ints, since an arraylist edge was
            // generated when adding a parent edge, then retrieved 2 more times when adding a edges with children
            // leaves will only have one edge (one element in array list) and the OG root will have 2 elements max,
            // assuminng R and L branches both exist
            if (!visited.contains(neighbor)) { // this prevents loop of looping neighbor of a parent node, then going to
                // first child and then looping child arraylist back to parent etc. only DFS if node not visited
                visited.add(neighbor);
                dfs(neighbor, distance + 1, k); // distance value passed with each method  so for a parent
                // with say dist at 1 from target with k = 2, and parent has 2 child nodes, both of which are valid paths
                // distance of 1+1 is passed to both recursive calls with child nodes to add both to return collecion
            }
        }
    }
}


/*

thoughts. DFS tree, create flag that will become true whene target val foound
where count will increment either until count = k or null found before reaching count.
will need to decrement count though when node popped off to show its no longer being used in the path count.
Gets trickier when target val is popped off stack because then count will = 0 and will have to start incrementing
when a node popped all the way back to root but then switch to incrementing count on .right of root.
this is all to count preceding nodes that are k distance or preceding nodes distance + distance of nodes in a different
subtree that togetheer with preciding nodes in same subtree, add to = k
has to return an array of the nodees, not just the count

target is node, not val, so don't worry about duplicate valued nodes

 */


class Solution {
    List<Integer> ans = new ArrayList<>();
    boolean flag = false;
    int dist = 0;

    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        recurse(root,target,k);
        return ans;

    }

    public void recurse(TreeNode root, TreeNode target, int k){
        if(root == null ){return;}

        if(flag){
            dist++;
        }

        if(dist == k){
            ans.add(root.val);
        }

        if(dist > k || (root.left == null && root.right == null)){
            dist--;
            return;
        }

        if(root == target){
            flag = true;
        }

        recurse(root.left,target,k);
        recurse(root.right,target,k);

        dist--;
    }
} // this fails i think when trying to include paths of preceding nodes or nodes of diff subtree. passes 16/57 cases