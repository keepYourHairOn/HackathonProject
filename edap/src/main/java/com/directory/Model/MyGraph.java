package com.directory.Model;

import java.util.*;

public class MyGraph<V extends Comparable<V>> {
	private HashMap<V, Vertex> vertices;
	private HashMap<String, Edge> edges;
	private boolean oriented = false;

	protected class Vertex implements Comparable<Vertex> {
		private Vertex parent;
		private V name;
		private boolean visited;
		private double val;
		private HashMap<V, Vertex> adjVert;
		private HashSet<V> inputVertices;
		private HashSet<V> outputVertices;

		Vertex(V v) {
			this.name = v;
			this.adjVert = new HashMap<V, Vertex>();
			this.inputVertices = new HashSet<V>();
			this.outputVertices = new HashSet<V>();
			this.parent = null;
			val = Double.POSITIVE_INFINITY;
		}

		private void addEdge(Vertex v) {
			if (!adjVert.containsKey(v)) {
				this.adjVert.put(v.name, v);
			}
		}

		private void addInput(V v) {
			inputVertices.add(v);
		}

		private void addOutput(V v) {
			outputVertices.add(v);
		}

		private void removeEdge(V v) {
			if (adjVert.containsKey(v)) {
				this.adjVert.remove(v);
			}
		}

		private boolean isVisited() {
			return this.visited;
		}

		private void makeVisited() {
			this.visited = true;
		}

		private void makeWeighted(double e) {
			this.visited = true;
			this.val = e;
		}

		private void makeBeg() {
			this.makeVisited();
			this.val = 0;
		}

		private void setParent(Vertex v) {
			this.parent = v;
		}

		private void refresh() {
			this.parent = null;
			this.val = Double.POSITIVE_INFINITY;
		}

		@Override
		public int compareTo(Vertex o) {
			if (this.val == o.val) {
				return 0;
			} else if (this.val > o.val) {
				return 1;
			}
			return -1;
		}
	}

	private class Edge implements Comparable<Edge> {
		private double val;
		private V begV;
		private V endV;

		Edge(V b, V e, double time) {
			this.begV = b;
			this.endV = e;
			this.val = time;
		}

		private boolean isLess(Edge b) {
			return (this.compareTo(b) < 0);
		}

		// public void add(Edge w) {
		// this.time += w.time;
		// this.cost += w.cost;
		// this.distance += w.distance;
		// }

		@Override
		public int compareTo(Edge o) {
			if (this.val == o.val) {
				return 0;
			} else if (this.val > o.val) {
				return 1;
			}
			return -1;
		}
	}

	public MyGraph(V[] vert, boolean oriented) {
		vertices = new HashMap<V, Vertex>();
		edges = new HashMap<String, Edge>();
		this.oriented = oriented;
		for (int i = 0; i < vert.length; i++) {
			Vertex tmp = new Vertex(vert[i]);
			vertices.put(vert[i], tmp);
		}
	}

	public void insertVertex(V v) {
		if (!vertices.containsKey(v)) {
			Vertex tmp = new Vertex(v);
			vertices.put(v, tmp);
		}
	}

	public void insertEdge(V v, V w, double e) {
		Vertex vtmp = vertices.get(v);
		Vertex wtmp = vertices.get(w);
		if (vtmp != null && wtmp != null) {
			if (!edges.containsKey(v.toString() + w.toString())) {
				vtmp.addEdge(wtmp);
				vtmp.addOutput(w);
				wtmp.addInput(v);
				Edge tmp = new Edge(v, w, e);
				edges.put(v.toString() + w.toString(), tmp);
				if (!oriented) {
					wtmp.addEdge(vtmp);
					wtmp.addOutput(v);
					vtmp.addInput(w);
				}
			}
		}
	}

	public void removeVertex(V v) {
		Vertex tmp = vertices.remove(v);
		if (tmp != null) {
			for (V w : tmp.adjVert.keySet()) {
				edges.remove(v.toString() + w.toString());
				edges.remove(w.toString() + v.toString());
				Vertex wtmp = vertices.get(w);
				if (wtmp != null) {
					wtmp.removeEdge(v);
				}
			}
		}
	}

	public double removeEdge(V v, V w) {
		Vertex vtmp = vertices.get(v);
		Vertex wtmp = vertices.get(w);
		Edge tmp = null;
		if (vtmp != null && wtmp != null) {
			if (edges.containsKey(v.toString() + w.toString())) {
				tmp = edges.remove(v.toString() + w.toString());
				vtmp.removeEdge(w);
			}
			if (!oriented && tmp == null) {
				tmp = edges.remove(w.toString() + v.toString());
				wtmp.removeEdge(v);
			}

		}
		return (tmp == null) ? 0 : tmp.val;
	}

	public boolean areAdjacent(V v, V w) {
		boolean tmp = edges.containsKey(v.toString() + w.toString());
		if (!oriented && tmp == false) {
			tmp = edges.containsKey(w.toString() + v.toString());
		}
		return tmp;
	}

	public int degree(V v) {
		if (vertices.containsKey(v)) {
			return vertices.get(v).adjVert.size();
		} else
			return -1;
	}

	public Collection<Vertex> neighbours(V v) {
		Vertex tmp = vertices.get(v);
		if (tmp != null) {
			return tmp.adjVert.values();
		}
		return null;
	}

	public HashSet<V> getAllInputs(V v) {
		Vertex tmp = vertices.get(v);
		if (tmp != null) {
			return tmp.inputVertices;
		}
		return null;
	}

	public HashSet<V> getAllOutputs(V v) {
		Vertex tmp = vertices.get(v);
		if (tmp != null) {
			return tmp.outputVertices;
		}
		return null;
	}

	public List<Edge> incidentEdges(V v) {
		if (vertices.containsKey(v) && degree(v) > 0) {
			List<Edge> e = new LinkedList<Edge>();
			for (V w : vertices.get(v).adjVert.keySet()) {
				Edge tmp = edges.get(v.toString() + w.toString());
				if (!oriented && tmp == null) {
					tmp = edges.get(w.toString() + v.toString());
				}
				e.add(tmp);
			}
			return e;
		}
		return null;
	}

	public List<V> AllVertices() {
		List<V> list = new LinkedList<V>(vertices.keySet());
		return list;
	}

	private boolean isVisited(V v) {
		Vertex tmp = vertices.get(v);
		if (tmp != null) {
			return tmp.isVisited();
		} else {
			throw new NoSuchElementException();
		}
	}

	private Vertex makeVisited(V v) {
		Vertex tmp = vertices.get(v);
		if (tmp != null) {
			tmp.makeVisited();
			return vertices.get(v);
		} else {
			throw new NoSuchElementException();
		}
	}

	private Vertex getAdjUnvisVert(V v) {
		Vertex tmp = vertices.get(v);
		for (Vertex w : tmp.adjVert.values()) {
			if (!w.isVisited()) {
				return w;
			}
		}
		return null;
	}

	private void dfs() {
		Stack<Vertex> st = new Stack<Vertex>();
		List<V> vert = AllVertices();
		for (V v : vert) {
			if (!isVisited(v)) {
				Vertex beg = makeVisited(v);
				st.push(beg);
				Vertex cur = getAdjUnvisVert(v);
				while (!st.isEmpty()) {
					if (cur == null) {
						cur = st.pop();
					} else {
						if (!isVisited(cur.name)) {
							makeVisited(cur.name);
							st.push(cur);
						}
						cur = getAdjUnvisVert(cur.name);
						if (cur == null) {
							st.pop();
						}
					}
				}
			}
		}
	}

	private void bfs() {
		List<V> vert = AllVertices();
		Queue<Vertex> qu = new ArrayDeque<Vertex>();
	}

	public void mst(V v) {

		if (oriented == true) {
			System.out.println(
					"Sorry, but the algorithm is two Chinese are not yet implemented. Try to make the graph undirected.");
		} else {

			Vertex vtmp = vertices.get(v);
			if (vtmp != null && !vtmp.adjVert.isEmpty()) {
				PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>();
				for (Vertex t : vertices.values()) {
					pq.add(t);
				}
				while (!pq.isEmpty()) {
					Vertex tmp = pq.remove();
					Collection<Vertex> neighs = neighbours(tmp.name);
					for (Vertex k : neighs) {
						if (pq.contains(k)) {
							double min = Double.min(k.val, getEdge(tmp, k).val);
							if (min < k.val) {
								pq.remove(k);
								k.makeWeighted(min);
								k.setParent(tmp);
								pq.add(k);
							}

						}
					}
				}

			}

		}

	}

	private Edge getEdge(Vertex v, Vertex w) {
		Edge tmp = edges.get(v.name.toString() + w.name.toString());
		if (tmp == null) {
			tmp = edges.get(w.name.toString() + v.name.toString());
		}
		return tmp;
	}

	public double shortestPath(V v, V w, boolean mst) {
		double result = 0.0;
		Vertex vtmp = vertices.get(v);
		Vertex wtmp = vertices.get(w);
		if (vtmp != null && wtmp != null && !vtmp.adjVert.isEmpty() && !wtmp.adjVert.isEmpty()) {
			for (Vertex t : vertices.values()) {
				t.refresh();
			}
			vtmp.makeBeg();
			if (!mst) {
				dijkstra(v);
			} else {
				mst(v);
			}
			if (vtmp == wtmp) {
				return result;

			}
			while (wtmp.parent != null) {
				Edge e = getEdge(wtmp, wtmp.parent);
				result += e.val;
				wtmp = wtmp.parent;
			}

		}
		return result;
	}

	public void dijkstra(V v) {
		Vertex vtmp = vertices.get(v);

		if (vtmp != null && !vtmp.adjVert.isEmpty()) {
			PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>();
			for (Vertex t : vertices.values()) {
				pq.add(t);
			}
			while (!pq.isEmpty()) {
				Vertex tmp = pq.remove();
				Collection<Vertex> neighs = neighbours(tmp.name);
				for (Vertex k : neighs) {
					if (pq.contains(k)) {
						double min = Double.min(k.val, (tmp.val + getEdge(tmp, k).val));
						if (min < k.val) {
							pq.remove(k);
							k.makeWeighted(min);
							k.setParent(tmp);
							pq.add(k);
						}
					}
				}
			}
		}
	}
}
