package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.util.Enumeration;

/**
 * Sample interface defining a generic DiGraph (directed graph)
 * @author E. Michael Maximilien
 */
public interface DiGraph extends Graph
{
    //-------------------------------------------------------------------------
    // Public methods
    //

    /** 
     * @return Graph.Edge with the source Vertex and sink Vertex specified
     * @param sourceVertex the source Graph.Vertex 
     * @param sinkVertex the sink Graph.Vertex
     */
    public Graph.Edge getEdge( Graph.Vertex sourceVertex, Graph.Vertex sinkVertex );

    /** 
     * @return true if a Graph.Edge with the source Vertex and sink Vertex exists
     * @param sourceVertex the source Graph.Vertex 
     * @param sinkVertex the sink Graph.Vertex
     */
    public boolean hasEdge( Graph.Vertex sourceVertex, Graph.Vertex sinkVertex );

	/**
	 * @return an AdjacentList for the Vertex specified
	 * @param vertex the Graph.Vertex object
	 */
	public DiGraph.AdjacentList getAdjacentList( Graph.Vertex vertex );

    //-------------------------------------------------------------------------
    // Public inner classes
    //

    /**
     * Defines a AdjacentList of a Graph.Vertex
     * @author E. Michael Maximilien
     */
    public interface AdjacentList
    {
		/** @return the Graph.Vertex source for this list */
		public Graph.Vertex getVertex();

		/** @return an Enumeration of Graph.Vertex adjacent to the source Graph.Vertex */
		public Enumeration getAdjVertices();

		/** @return the size of this adjacent list */
		public int getSize();

		/**
		 * @return true if the vertex is in the AdjacentList
		 * @param vertex the Graph.Vetex object
		 */
		public boolean containsVertex( Graph.Vertex vertex );

		/**
		 * Adds the Graph.Vertex to the adjacency list
		 * @param vertex the Graph.Vetex to add
		 */
		public void addVertex( Graph.Vertex vertex );

		/**
		 * Removes the Graph.Vertex from the adjacency list
		 * @param vertex the Graph.Vetex to add
		 */
		public void removeVertex( Graph.Vertex vertex );
    }
}
