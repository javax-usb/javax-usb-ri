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
public class DefaultGraph extends Object implements Graph
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /** Default ctor */
    public DefaultGraph() {}

    /** 
     * Creates a graph with the set of Graph.Vertex and Edge specified
     * @param vertices an Enumeration of Graph.Vertex
     * @param edges an Enumeration of Graph.Edge
     */
    public DefaultGraph( Enumeration vertices, Enumeration edges ) 
    {
        initVertices( vertices );
        initEdges( edges );
    }

    //-------------------------------------------------------------------------
    // Protected methods
    //

    /** 
     * Initialize the list of Graph.Vertex
     * @param vertices an Enumeration of Graph.Vertex objects
     */
    protected void initVertices( Enumeration vertices )
    {
		verticesTable.clear();

		while( vertices.hasMoreElements() )
		{
			Graph.Vertex vertex = (Graph.Vertex)vertices.nextElement();
			verticesTable.put( vertex.getName(), vertex );
		}
    }

    /** 
     * Initialize the list of Edge
     * @param edges an Enumeration of Graph.Edge objects
     */
    protected void initEdges( Enumeration edges )
    {
		edgesVector.clear();

		while( edges.hasMoreElements() )
		{
			Graph.Edge edge = (Graph.Edge)edges.nextElement();
			edgesVector.addElement( edge );
		}
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return Set of Graph.Graph.Vertex of this Graph */
    public Enumeration getVertices() { return verticesTable.elements(); }

    /** @return Set of Graph.Edge of this Graph */
    public Enumeration getEdges() { return edgesVector.elements(); }

	/** 
	 * Adds the Graph.Vertex to this Graph.  This will overwrite any Graph.Vertex with the same name
	 * @param vertex the Graph.Vertex to add
	 */
	public void addVertex( Graph.Vertex vertex ) { verticesTable.put( vertex.getName(), vertex ); }

	/** 
	 * Adds the Graph.Edge to this Graph.  This will overwrite any exiting Graph.Edge
	 * with same source and sink Graph.Vertex
	 * @param edge the Graph.Edge to add
	 */
	public void addEdge( Graph.Edge edge )
	{
		if( !edgesVector.contains( edge ) )
			edgesVector.addElement( edge );
	}

	/**
	 * Set the following Enumeration of Graph.Vertex as the set of vertices for this Graph
	 * @param vertices an Enumeration of Vertices
	 */
	public void setVertices( Enumeration vertices ) { initVertices( vertices ); }

	/**
	 * Set the following Enumeration of Graph.Edge as the set of edges for this Graph
	 * @param edges an Enumeration of Edges
	 */
	public void setEdges( Enumeration edges ) { initEdges( edges ); }

	/**
	 * @return a new Graph.Vertex with the name and object specified
	 * @param name the String name
	 * @param object the Object name
	 */
	public Graph.Vertex createVertex( String name, Object object )
	{ return this.new DefaultVertex( name, object ); }

	/**
	 * @return a new Graph.Edge with the 2 Graph.Vertex specified
	 * @param v1 the first Graph.Vertex
	 * @param v2 the second Graph.Vertex
	 */
	public Graph.Edge createEdge( Graph.Vertex v1, Graph.Vertex v2 )
	{ return this.new DefaultEdge( v1, v2 ); }

    /** 
     * @return Graph.Vertex with the name passed
     * @param vName the unique Graph.Vertex name
     */
    public Graph.Vertex getVertex( String vName ) { return (Graph.Vertex)verticesTable.get( vName ); }

    //-------------------------------------------------------------------------
    // Public inner interface 
    //

    /**
     * Defines a Vertex of a Graph
     * @author E. Michael Maximilien
     */
    public class DefaultVertex extends Object implements Graph.Vertex
    {
        //---------------------------------------------------------------------
        // Ctor(s)
        //

        /**
         * Creates a Vertex with info passed
         * @param name the unique name for this Graph.Vertex
         * @param object the Object that this Graph.Vertex contains
         */
        public DefaultVertex( String name, Object object )
        { this.name = name; this.object = object; }

        //---------------------------------------------------------------------
        // Public overridden methods
        //

		/** @return a String representation of this Graph.Vertex */
		public String toString() { return "Graph.Vertex[name = " + getName() + "]"; }

		/** @return a hashCode that depends on the Graph.Vertex name */
		public int hashCode() { return getName().hashCode(); }

		/**
		 * @return true if the two Vertex have the same name and Object 
		 * @param obj the Other Graph.Vertex object
		 */
		public boolean equals( Object obj ) 
		{ 
			if( obj == null ) return false;

			if( !( obj instanceof Graph.Vertex ) ) return false;

			Graph.Vertex otherVertex = (Graph.Vertex)obj;

			if( otherVertex.getObject() == null && getObject() != null ) return false;

			if( getObject() == null && otherVertex.getObject() != null ) return false;

			return ( getObject().equals( otherVertex.getObject() ) && getName().equals( otherVertex.getName() ) );
		}

        //---------------------------------------------------------------------
        // Public methods
        //

        /** @return the name for this vertex */
        public String getName() { return name; }

        /** @return the Object that this vertex constains */
        public Object getObject() { return object; }

        //---------------------------------------------------------------------
        // Instance variables
        //

        private String name = "";
        private Object object = null;
    }

    /**
     * Defines a Vertex of a Graph
     * @author E. Michael Maximilien
     */
    public class DefaultEdge extends Object implements Graph.Edge
    {
        //---------------------------------------------------------------------
        // Ctor(s)
        //

        /**
         * Creates an Graph.Edge with source and sink Graph.Vertex specified
         * @param sourceVertex the source Graph.Vertex for this Graph.Edge
         * @param sinkVertex the sink Graph.Vertex for this Graph.Edge
         */
        public DefaultEdge( Graph.Vertex sourceVertex, Graph.Vertex sinkVertex )
        {
            this.sourceVertex = sourceVertex;
            this.sinkVertex = sinkVertex;
        }

        //---------------------------------------------------------------------
        // Public overridden methods
        //

		/** @return a String representation of this Graph.Edge */
		public String toString() 
		{ 
			return "Graph.Edge[sourceVertex = " + getSourceVertex().getName() + ", " +
			                   "sinkVertex = " + getSinkVertex().getName() + "]"; 
		}

		/** @return a hashCode that depends on the Graph.Edge name */
		public int hashCode() { return toString().hashCode(); }

		/**
		 * @return true if the two Edge have the same name and Object 
		 * @param obj the Other Graph.Edge object
		 */
		public boolean equals( Object obj ) 
		{ 
			if( obj == null ) return false;

			if( !( obj instanceof Graph.Edge ) ) return false;

			Graph.Edge otherEdge = (Graph.Edge)obj;

			return ( getSourceVertex().equals( otherEdge.getSourceVertex() ) && 
					 getSinkVertex().equals( otherEdge.getSinkVertex() ) );
		}

        //---------------------------------------------------------------------
        // Public methods
        //

        /** @return the source Vertex that this edge constains */
        public Graph.Vertex getSourceVertex() { return sourceVertex; }
        
        /** @return the sink Vertex that this edge constains */
        public Graph.Vertex getSinkVertex() { return sinkVertex; }

        //---------------------------------------------------------------------
        // Instance variables
        //

        private Graph.Vertex sourceVertex = null;
        private Graph.Vertex sinkVertex = null;
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    protected Hashtable verticesTable = new Hashtable();
    protected Vector edgesVector = new Vector();
}
