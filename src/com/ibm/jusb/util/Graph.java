package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.Enumeration;

/**
 * Sample interface defining a generic Graph
 * @author E. Michael Maximilien
 */
public interface Graph
{
    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return Set of Graph.Vertex of this Graph */
    public Enumeration getVertices();

    /** @return Set of Graph.Edge of this Graph */
    public Enumeration getEdges();

	/** 
	 * Adds the Graph.Vertex to this Graph.  This will overwrite any Graph.Vertex with the same name
	 * @param vertex the Graph.Vertex to add
	 */
	public void addVertex( Graph.Vertex vertex );

	/** 
	 * Adds the Graph.Edge to this Graph.  This will overwrite any exiting Graph.Edge
	 * with same source and sink Graph.Vertex
	 * @param edge the Graph.Edge to add
	 */
	public void addEdge( Graph.Edge edge );

	/**
	 * Set the following Enumeration of Graph.Vertex as the set of vertices for this Graph
	 * @param vertices an Enumeration of Vertices
	 */
	public void setVertices( Enumeration vertices );

	/**
	 * Set the following Enumeration of Graph.Edge as the set of edges for this Graph
	 * @param edges an Enumeration of Edges
	 */
	public void setEdges( Enumeration edges );

	/**
	 * @return a new Graph.Vertex with the name and object specified
	 * @param name the String name
	 * @param object the Object name
	 */
	public Graph.Vertex createVertex( String name, Object object );

	/**
	 * @return a new Graph.Edge with the 2 Graph.Vertex specified
	 * @param v1 the first Graph.Vertex
	 * @param v2 the second Graph.Vertex
	 */
	public Graph.Edge createEdge( Graph.Vertex v1, Graph.Vertex v2 );

    /** 
     * @return Graph.Vertex with the name passed
     * @param vName the unique Graph.Vertex name
     */
    public Graph.Vertex getVertex( String vName );

    //-------------------------------------------------------------------------
    // Public inner interface 
    //

    /**
     * Defines a Vertex of a Graph
     * @author E. Michael Maximilien
     */
    public interface Vertex
    {
        /** @return the name for this vertex */
        public String getName();

        /** @return the Object that this vertex constains */
        public Object getObject();
    }

    /**
     * Defines a Vertex of a Graph
     * @author E. Michael Maximilien
     */
    public interface Edge
    {
        /** @return the source Vertex that this edge constains */
        public Graph.Vertex getSourceVertex();

        /** @return the sink Vertex that this edge constains */
        public Graph.Vertex getSinkVertex();
    }
}
