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
 * A JUnit TestCase for the Graph interface and implementing class
 * @author E. Michael Maximilien
 */
public class GraphTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public GraphTestCase( String name )
	{
		super( name );
	}

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() { graph = new DefaultGraph(); }

	protected void tearDown() { graph = null; }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** Creates a simple 1 vertex graph with a self transition and verify */
	public void testGraph1()
	{
		//Test createXyz methods
		Graph.Vertex v1 = graph.createVertex( "v1", "v1" );
		Graph.Edge e1 = graph.createEdge( v1, v1 );

		Vector vVector = new Vector();
		Vector eVector = new Vector();

		vVector.addElement( v1 );
		eVector.addElement( e1 );

		graph.setVertices( vVector.elements() );
		graph.setEdges( eVector.elements() );

		assertTrue( "Could not create Graph.Vertex", v1 != null );
		assertTrue( "Could not create Graph.Edge", e1 != null );

		//Test getVertices method
		Enumeration vertices = graph.getVertices();
		Vector verticesVector = new Vector();
		verticesVector.addElement( v1 );

		assertTrue( "getVertices() result incorrect.  Expected [v1]", 
				JUnitUtility.isEquals( vertices, verticesVector.elements() ) );

		//Test getEdges method
		Enumeration edges = graph.getEdges();
		Vector edgesVector = new Vector();
		edgesVector.addElement( e1 );

		assertTrue( "getEdges() result incorrect.  Expected [e1]", 
				JUnitUtility.isEquals( edges, edgesVector.elements() ) );

		//Test Graph.getVertex
		Graph.Vertex vertex1 = graph.getVertex( "v1" );
		Graph.Vertex nullVertex = graph.getVertex( "v0" );

		assertTrue( "Expected Graph.Vertex with name v1", vertex1.getName().equals( "v1" ) );
		assertTrue( "Expected Graph.Vertex with Object valueof String v1", vertex1.getObject().equals( new String( "v1" ) ) );
		assertTrue( "Expected null Graph.Vertex since name v0 is not a vertex name", nullVertex == null );
	}

	/** Creates a 2 vertex fully connected graph and verify transitions */
	public void testGraph2()
	{
		//Test createXyz methods
		Graph.Vertex v1 = graph.createVertex( "v1", "v1" );
		Graph.Vertex v2 = graph.createVertex( "v2", "v2" );
		Graph.Edge e11 = graph.createEdge( v1, v1 );
		Graph.Edge e12 = graph.createEdge( v1, v2 );
		Graph.Edge e22 = graph.createEdge( v2, v2 );
		Graph.Edge e21 = graph.createEdge( v2, v1 );

		Vector vVector = new Vector();
		Vector eVector = new Vector();

		vVector.addElement( v1 ); vVector.addElement( v2 );
		eVector.addElement( e11 ); eVector.addElement( e12 );
		eVector.addElement( e22 ); eVector.addElement( e21 );

		graph.setVertices( vVector.elements() );
		graph.setEdges( eVector.elements() );

		assertTrue( "Could not create Graph.Vertex", v1 != null );
		assertTrue( "Could not create Graph.Vertex", v2 != null );
		assertTrue( "Could not create Graph.Edge", e11 != null );
		assertTrue( "Could not create Graph.Edge", e12 != null );
		assertTrue( "Could not create Graph.Edge", e22 != null );
		assertTrue( "Could not create Graph.Edge", e21 != null );

		//Test getVertices method
		Enumeration vertices = graph.getVertices();
		Vector verticesVector = new Vector();
		verticesVector.addElement( v1 ); verticesVector.addElement( v2 );

		assertTrue( "getVertices() result incorrect.  Expected [v1, v2]", 
				JUnitUtility.isEquals( vertices, verticesVector.elements() ) );

		//Test getEdges method
		Enumeration edges = graph.getEdges();
		Vector edgesVector = new Vector();
		edgesVector.addElement( e11 ); edgesVector.addElement( e12 );
		edgesVector.addElement( e22 ); edgesVector.addElement( e21 );

		assertTrue( "getEdges() result incorrect.  Expected [e11, e12, e22, e21]", 
				JUnitUtility.isEquals( edges, edgesVector.elements() ) );

		//Test Graph.getVertex
		Graph.Vertex vertex1 = graph.getVertex( "v1" );
		Graph.Vertex vertex2 = graph.getVertex( "v2" );
		Graph.Vertex nullVertex1 = graph.getVertex( "v0" );
		Graph.Vertex nullVertex2 = graph.getVertex( "v3" );

		assertTrue( "Expected Graph.Vertex with name v1", vertex1.getName().equals( "v1" ) );
		assertTrue( "Expected Graph.Vertex with Object valueof String v1", vertex1.getObject().equals( new String( "v1" ) ) );
		assertTrue( "Expected Graph.Vertex with name v2", vertex2.getName().equals( "v2" ) );
		assertTrue( "Expected Graph.Vertex with Object valueof String v2", vertex2.getObject().equals( new String( "v2" ) ) );

		assertTrue( "Expected null Graph.Vertex since name v0 is not a vertex name", nullVertex1 == null );
		assertTrue( "Expected null Graph.Vertex since name v3 is not a vertex name", nullVertex2 == null );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private Graph graph = null;
}
