/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/


import lib280.graph.Edge280;
import lib280.graph.GraphAdjListRep280;
import lib280.graph.Vertex280;


public class UnionFind280 {
	GraphAdjListRep280<Vertex280, Edge280<Vertex280>> G;
	
	/**
	 * Create a new union-find structure.
	 * 
	 * @param numElements Number of elements (numbered 1 through numElements, inclusive) in the set.
	 * @postcond The structure is initialized such that each element is in its own subset.
	 */
	public UnionFind280(int numElements) {
		G = new GraphAdjListRep280<Vertex280, Edge280<Vertex280>>(numElements, true);
		G.ensureVertices(numElements);		
	}
	
	/**
	 * Return the representative element (equivalence class) of a given element.
	 * @param id The elements whose equivalence class we wish to find.
	 * @return The representative element (equivalence class) of the element 'id'.
	 */
	public int find(int id) {
		// TODO - Write this method

		Vertex280 k = G.vertex(id);
		G.goVertex(k);
		G.eGoFirst(k);

		for (int i = 1; i <= G.numVertices(); i++) {
			if (G.eItemExists()) {
				id = find(G.eItemAdjacentIndex());
			}
		}
		return id;

	}
	
	/**
	 * Merge the subsets containing two items, id1 and id2, making them, and all of the other elemnets in both sets, "equivalent".
	 * @param id1 First element.
	 * @param id2 Second element.
	 */
	public void union(int id1, int id2) {
		// TODO - Write this method.

		int pt1 = this.find(id1);
		int pt2 = this.find(id2);

		if(pt1 != pt2){
			G.addEdge(pt1, pt2);
		} else {
			return;
		}

	}
	
	
	
}
