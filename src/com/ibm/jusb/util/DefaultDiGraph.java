package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Default implementation for a Graph
 * @author E. Michael Maximilien
 */
public class DefaultDiGraph extends DefaultGraph implements DiGraph
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /** Default ctor */
    public DefaultDiGraph() {}

    /** 
     * Creates a graph with the set of Graph.Vertex and Edge specified
     * @param vertices an Enumeration of Graph.Vertex
     * @param edges an Enumeration of Graph.Edge
     */
    public DefaultDiGraph( Enumeration vertices, Enumeration edges ) { super( vertices, edges ); }

    //-------------------------------------------------------------------------
    // Protected methods
    //

    /** 
     * Initialize the list of Graph.Vertex
     * @param vertices an Enumeration of Graph.Vertex objects
     */
    protected void initVertices( Enumeration vertices )
    {
		super.initVertices( vertices );

		Enumeration verticesEnum = verticesTable.elements();
		while( verticesEnum.hasMoreElements() )
		{
			Graph.Vertex vertex = (Graph.Vertex)verticesEnum.nextElement();

			adjListTable.put( vertex.getName(), new DefaultDiGraph.DefaultAdjacentList( vertex ) );
		}
    }

    /** 
     * Initialize the list of Edge
     * @param edges an Enumeration of Graph.Edge objects
     */
    protected void initEdges( Enumeration edges )
    {
		Vector edgesVector = new Vector();

		while( edges.hasMoreElements() )
			edgesVector.addElement( edges.nextElement() );

		super.initEdges( edgesVector.elements() );

		Enumeration edgesEnum = edgesVector.elements();

		while( edgesEnum.hasMoreElements() )
		{
			Graph.Edge edge = (Graph.Edge)edgesEnum.nextElement();
			Graph.Vertex sourceVertex = edge.getSourceVertex();
			Graph.Vertex sinkVertex = edge.getSinkVertex();

			DiGraph.AdjacentList adjacentList = getAdjacentList( sourceVertex );
			adjacentList.addVertex( sinkVertex );
		}
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

	/** 
	 * Adds the Graph.Vertex to this Graph.  This will overwrite any Graph.Vertex with the same name
	 * @param vertex the Graph.Vertex to add
	 */
	public void addVertex( Graph.Vertex vertex ) 
	{
		super.addVertex( vertex );

		if( !adjListTable.containsKey( vertex.getName() ) )
			adjListTable.put( vertex.getName(), new DefaultDiGraph.DefaultAdjacentList( vertex ) );
	}

	/** 
	 * Adds the Graph.Edge to this Graph.  This will overwrite any exiting Graph.Edge
	 * with same source and sink Graph.Vertex
	 * @param edge the Graph.Edge to add
	 */
	public void addEdge( Graph.Edge edge )
	{
		super.addEdge( edge );

		Graph.Vertex sourceVertex = edge.getSourceVertex();
		Graph.Vertex sinkVertex = edge.getSinkVertex();

		DiGraph.AdjacentList adjacentList = getAdjacentList( sourceVertex );
		adjacentList.addVertex( sinkVertex );
	}

    /** 
     * @return Graph.Edge with the source Vertex and sink Vertex specified
     * @param sourceVertex the source Graph.Vertex 
     * @param sinkVertex the sink Graph.Vertex
     */
    public Graph.Edge getEdge( Graph.Vertex sourceVertex, Graph.Vertex sinkVertex )
	{
		throw new RuntimeException( "Not yet implemented!" );
	}

    /** 
     * @return true if a Graph.Edge with the source Vertex and sink Vertex exists
     * @param sourceVertex the source Graph.Vertex 
     * @param sinkVertex the sink Graph.Vertex
     */
    public boolean hasEdge( Graph.Vertex sourceVertex, Graph.Vertex sinkVertex )
	{
		if( getAdjacentList( sourceVertex ) != null )
		{
			DiGraph.AdjacentList adjacentList = getAdjacentList( sourceVertex );

			if( adjacentList.containsVertex( sinkVertex ) )
				return true;
			else
				return false;
		}
		else
            return false;
	}

	/**
	 * @return an AdjacentList for the Vertex specified
	 * @param vertex the Graph.Vertex object
	 */
	public DiGraph.AdjacentList getAdjacentList( Graph.Vertex vertex )
	{
		return (DiGraph.AdjacentList)adjListTable.get( vertex.getName() );
	}

    //-------------------------------------------------------------------------
    // Public inner classes
    //

    /**
     * Defines a default implementation of the DiGraph.AdjacentList of a Graph.Vertex
     * @author E. Michael Maximilien
     */
    public class DefaultAdjacentList extends Object implements DiGraph.AdjacentList
    {
		//---------------------------------------------------------------------
		// Ctor(s)
		//

		/**
		 * Creates a DefaultAdjacentList for the Graph.Vertex provided
		 * @param vertex the Graph.Vertex object
		 */
		public DefaultAdjacentList( Graph.Vertex vertex ) { this.vertex = vertex; }

		//---------------------------------------------------------------------
		// Public methods
		//

		/** @return the Graph.Vertex source for this list */
		public Graph.Vertex getVertex() { return vertex; }

		/** @return an Enumeration of Graph.Vertex adjacent to the source Graph.Vertex */
		public Enumeration getAdjVertices() { return adjVerticesVector.elements(); }

		/** @return the size of this adjacent list */
		public int getSize() { return adjVerticesVector.size(); }

		/**
		 * @return true if the vertex is in the AdjacentList
		 * @param vertex the Graph.Vetex object
		 */
		public boolean containsVertex( Graph.Vertex vertex ) { return adjVerticesVector.contains( vertex ); }

		/**
		 * Adds the Graph.Vertex to the adjacency list
		 * @param vertex the Graph.Vetex to add
		 */
		public void addVertex( Graph.Vertex vertex ) { adjVerticesVector.addElement( vertex ); }

		/**
		 * Removes the Graph.Vertex from the adjacency list
		 * @param vertex the Graph.Vetex to add
		 */
		public void removeVertex( Graph.Vertex vertex ) { adjVerticesVector.removeElement( vertex ); }

		//---------------------------------------------------------------------
		// Instance variables
		//

		private Graph.Vertex vertex = null;
		private Vector adjVerticesVector = new Vector();
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

	private Hashtable adjListTable = new Hashtable();
}
