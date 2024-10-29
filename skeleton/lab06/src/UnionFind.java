import java.util.Arrays;

public class UnionFind {
    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     * You can assume that we are only working with non-negative integers as the items
     * in our disjoint sets.
     */
    private final int[] data;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        data = new int[N];
        Arrays.fill(data, -1);
    }

    public UnionFind(int[] data) {
        this.data = data;
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        int root = find(v);
        return data[root];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return data[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // 直接判断v1和v2所处集合根节点是否相同
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v >= data.length || v < 0) {
            throw new IllegalArgumentException("输入节点为负数或者超出集合大小");
        }
        // 值为负,则该节点为所处集合的根节点
        if (data[v] < 0) {
            return v;
        }
        // 利用递归找到根节点的同时，将所有除根节点以外的节点连接到根节点上，实现path-compression
        return data[v] = find(parent(v));
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie-break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        // 如果v1和v2已经连通，直接返回
        if (connected(v1, v2)) {
            return;
        }
        // 找到v1和v2的根节点
        int root1 = find(v1);
        int root2 = find(v2);
        if (sizeOf(v1) < sizeOf(v2)) {
            // 若v1所处集合大于v2,
            // 先修改v1的大小
            data[root1] += data[root2];
            // 再将v2连接到v1的根
            data[root2] = root1;
        } else {
            data[root2] += data[root1];
            data[root1] = root2;
        }
    }

    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     */
    public int[] returnData() {
        return data;
    }
}
