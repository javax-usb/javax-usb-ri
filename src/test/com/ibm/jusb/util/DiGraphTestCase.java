package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import junit.framework.*;

import com.ibm.jusb.test.JUnitUtility;

/**
 * A JUnit TestCase for the DiGraph interface and implementing class
 * @author E. Michael Maximilien
 */
public class DiGraphTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public DiGraphTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() { diGraph = new DefaultDiGraph(); }

	protected void tearDown() { diGraph = null; }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//


	/** Creates a simple 1 vertex graph with a self transition and verify */
	public void testDiGraph1()
	{
		//Test createXyz methods
		Graph.Vertex v1 = diGraph.createVertex( "v1", "v1" );
		Graph.Edge e1 = diGraph.createEdge( v1, v1 );

		Vector vVector = new Vector();
		Vector eVector = new Vector();

		vVector.addElement( v1 );
		eVector.addElement( e1 );

		diGraph.setVertices( vVector.elements() );
		diGraph.setEdges( eVector.elements() );

		assertTrue( "Could not create Graph.Vertex", v1 != null );
		assertTrue( "Could not create Graph.Edge", e1 != null );

		//Test getVertices method
		Enumeration vertices = diGraph.getVertices();
		Vector verticesVector = new Vector();
		verticesVector.addElement( v1 );

		assertTrue( "getVertices() result incorrect.  Expected [v1]", 
				JUnitUtility.isEquals( vertices, verticesVector.elements() ) );

		//Test getEdges method
		Enumeration edges = diGraph.getEdges();
		Vector edgesVector = new Vector();
		edgesVector.addElement( e1 );

		assertTrue( "getEdges() result incorrect.  Expected [e1]", 
				JUnitUtility.isEquals( edges, edgesVector.elements() ) );

		//Test Graph.getVertex
		Graph.Vertex vertex1 = diGraph.getVertex( "v1" );
		Graph.Vertex nullVertex = diGraph.getVertex( "v0" );

		assertTrue( "Expected Graph.Vertex with name v1", vertex1.getName().equals( "v1" ) );
		assertTrue( "Expected Graph.Vertex with Object valueof String v1", vertex1.getObject().equals( new String( "v1" ) ) );
		assertTrue( "Expected null Graph.Vertex since name v0 is not a vertex name", nullVertex == null );

		//Test the AdjacencyList for each Vertex
		DiGraph.AdjacentList v1AdjList = diGraph.getAdjacentList( v1 );

		assertTrue( "Expected an DiGraph.AdjacencyList with [v1]", v1AdjList.containsVertex( v1 ) );
		assertTrue( "Does not expect null to be a member of DiGraph.AdjacencyList", v1AdjList.containsVertex( null ) == false );
		assertTrue( "Expected an DiGraph.AdjacencyList with [v1]", JUnitUtility.isEquals( v1AdjList.getAdjVertices(), verticesVector.elements() ) );
		assertTrue( "Expected an DiGraph.AdjacencyList with size == 1", v1AdjList.getSize() == 1 );
		assertTrue( "Expected an DiGraph.AdjacencyList with [v1] != null", v1AdjList.getVertex() != null );
		assertTrue( "Expected an DiGraph.AdjacencyList with [v1]", v1AdjList.getVertex().equals( v1 ) );

		//Test getEdge( Graph.Vertex, Graph.Vertex )
		//<todo/>
	}

	/** Creates a 2 vertex fully connected diGraph and verify transitions */
	public void testDiGraph2()
	{
		//Test createXyz methods
		Graph.Vertex v1 = diGraph.createVertex( "v1", "v1" );
		Graph.Vertex v2 = diGraph.createVertex( "v2", "v2" );
		Graph.Edge e11 = diGraph.createEdge( v1, v1 );
		Graph.Edge e12 = diGraph.createEdge( v1, v2 );
		Graph.Edge e22 = diGraph.createEdge( v2, v2 );
		Graph.Edge e21 = diGraph.createEdge( v2, v1 );

		Vector vVector = new Vector();
		Vector eVector = new Vector();

		vVector.addElement( v1 ); vVector.addElement( v2 );
		eVector.addElement( e11 ); eVector.addElement( e12 );
		eVector.addElement( e22 ); eVector.addElement( e21 );

		diGraph.setVertices( vVector.elements() );
		diGraph.setEdges( eVector.elements() );

		assertTrue( "Could not create Graph.Vertex", v1 != null );
		assertTrue( "Could not create Graph.Vertex", v2 != null );
		assertTrue( "Could not create Graph.Edge", e11 != null );
		assertTrue( "Could not create Graph.Edge", e12 != null );
		assertTrue( "Could not create Graph.Edge", e22 != null );
		assertTrue( "Could not create Graph.Edge", e21 != null );

		//Test getVertices method
		Enumeration vertices = diGraph.getVertices();
		Vector verticesVector = new Vector();
		verticesVector.addElement( v1 ); verticesVector.addElement( v2 );

		assertTrue( "getVertices() result incorrect.  Expected [v1, v2]", 
				JUnitUtility.isEquals( vertices, verticesVector.elements() ) );

		//Test getEdges method
		Enumeration edges = diGraph.getEdges();
		Vector edgesVector = new Vector();
		edgesVector.addElement( e11 ); edgesVector.addElement( e12 );
		edgesVector.addElement( e22 ); edgesVector.addElement( e21 );

		assertTrue( "getEdges() result incorrect.  Expected [e11, e12, e22, e21]", 
				JUnitUtility.isEquals( edges, edgesVector.elements() ) );

		//Test Graph.getVertex
		Graph.Vertex vertex1 = diGraph.getVertex( "v1" );
		Graph.Vertex vertex2 = diGraph.getVertex( "v2" );
		Graph.Vertex nullVertex1 = diGraph.getVertex( "v0" );
		Graph.Vertex nullVertex2 = diGraph.getVertex( "v3" );

		assertTrue( "Expected Graph.Vertex with name v1", vertex1.getName().equals( "v1" ) );
		assertTrue( "Expected Graph.Vertex with Object valueof String v1", vertex1.getObject().equals( new String( "v1" ) ) );
		assertTrue( "Expected Graph.Vertex with name v2", vertex2.getName().equals( "v2" ) );
		assertTrue( "Expected Graph.Vertex with Object valueof String v2", vertex2.getObject().equals( new String( "v2" ) ) );

		assertTrue( "Expected null Graph.Vertex since name v0 is not a vertex name", nullVertex1 == null );
		assertTrue( "Expected null Graph.Vertex since name v3 is not a vertex name", nullVertex2 == null );

		//Test the AdjacencyList for each Vertex
		DiGraph.AdjacentList v1AdjList = diGraph.getAdjacentList( v1 );
		DiGraph.AdjacentList v2AdjList = diGraph.getAdjacentList( v2 );

		assertTrue( "Expected an DiGraph.AdjacencyList with [v1, v2]", v1AdjList.containsVertex( v1 ) );
		assertTrue( "Expected an DiGraph.AdjacencyList with [v1, v2]", v1AdjList.containsVertex( v2 ) );
		assertTrue( "Does not expect null to be a member of DiGraph.AdjacencyList", v1AdjList.containsVertex( null ) == false );
		assertTrue( "Expected an DiGraph.AdjacencyList with [v1, v2]", JUnitUtility.isEquals( v1AdjList.getAdjVertices(), verticesVector.elements() ) );
		assertTrue( "Expected an DiGraph.AdjacencyList with size == 2", v1AdjList.getSize() == 2 );
		assertTrue( "Expected an DiGraph.AdjacencyList with source vertex == v1", v1AdjList.getVertex() != null );
		assertTrue( "Expected an DiGraph.AdjacencyList with source vertex == v1", v1AdjList.getVertex().equals( v1 ) );
		assertTrue( "Expected an DiGraph.AdjacencyList with source vertex == v1", v1AdjList.getVertex().equals( v2 ) == false );

		assertTrue( "Expected an DiGraph.AdjacencyList with [v1, v2]", v2AdjList.containsVertex( v1 ) );
		assertTrue( "Expected an DiGraph.AdjacencyList with [v1, v2]", v2AdjList.containsVertex( v2 ) );
		assertTrue( "Does not expect null to be a member of DiGraph.AdjacencyList", v2AdjList.containsVertex( null ) == false );
		assertTrue( "Expected an DiGraph.AdjacencyList with [v1, v2]", JUnitUtility.isEquals( v2AdjList.getAdjVertices(), verticesVector.elements() ) );
		assertTrue( "Expected an DiGraph.AdjacencyList with size == 2", v2AdjList.getSize() == 2 );
		assertTrue( "Expected an DiGraph.AdjacencyList with source vertex == v2", v2AdjList.getVertex() != null );
		assertTrue( "Expected an DiGraph.AdjacencyList with source vertex == v2", v2AdjList.getVertex().equals( v2 ) );
		assertTrue( "Expected an DiGraph.AdjacencyList with source vertex == v2", v2AdjList.getVertex().equals( v1 ) == false );

		//Test getEdge( Graph.Vertex, Graph.Vertex )
		//<todo/>
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private DiGraph diGraph = null;
}
