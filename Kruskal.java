/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/


import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;
import lib280.tree.ArrayedMinHeap280;

public class Kruskal {

    public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {

        // TODO -- Complete this method.

        WeightedGraphAdjListRep280<Vertex280> minST = new WeightedGraphAdjListRep280<>(G.capacity(), false);
        UnionFind280 UF = new UnionFind280(G.numVertices());

        ArrayedMinHeap280<WeightedEdge280<Vertex280>> Heap;
        Heap = new ArrayedMinHeap280<>(50);


        G.goFirst();
        do {
            G.eGoFirst(G.item());
            do {
                if (G.itemIndex() - G.eItemAdjacentIndex() < 0) {
                    Heap.insert(G.eItem());
                }
                G.eGoForth();
            } while (G.eItemExists());
            G.goForth();
        } while (!G.after());


        while (!Heap.isEmpty()) {
            int a = Heap.item().firstItem().index();
            int b = Heap.item().secondItem().index();
            minST.ensureVertices(G.numVertices());

            if (UF.find(a) != UF.find(b)) {
                minST.addEdge(a, b);
                minST.setEdgeWeight(a, b, G.getEdgeWeight(a, b));
                UF.union(a, b);
            } else {
                Heap.deleteItem();
            }
        }

        return minST;
    }


    public static void main(String args[]) {
        WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
        G.initGraphFromFile("E:\\course work\\CMPT-280-ASM8\\A8Q1\\mst.graph");
        System.out.println(G);

        WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);

        System.out.println(minST);
    }
}


