package com.ibm.jusb.tools;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Panel to contruct (and display properties of) a byte[].
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class ByteArrayPanel extends JPanel
{
	/** Constructor */
	public ByteArrayPanel()
	{
		super( new BorderLayout() );

		init();
	}

	//*************************************************************************
	// Public methods

	//*************************************************************************
	// Protected methods

	protected void init()
	{
		initTextPanel();
		add( textPanel, BorderLayout.NORTH );

		initDataPanel();
		add( dataPanel, BorderLayout.SOUTH );
	}

	protected void initTextPanel()
	{
		textClearPanel.add( textClearButton );

		textInsertPanel.add( textInsertButton );

		textLengthPanel.add( textLengthLabel );
		textLengthPanel.add( textLengthField );

		textMetaPanel.add( textLengthPanel, BorderLayout.NORTH );
		textMetaPanel.add( textInsertPanel, BorderLayout.CENTER );
		textMetaPanel.add( textClearPanel, BorderLayout.SOUTH );

		textLabelPanel.add( textLabel );
		textTextAreaPanel.add( textTextArea );

		textContentPanel.add( textLabelPanel, BorderLayout.NORTH );
		textContentPanel.add( textTextAreaPanel, BorderLayout.CENTER );

		textPanel.add( textContentPanel, BorderLayout.WEST );
		textPanel.add( textMetaPanel, BorderLayout.EAST );
	}

	protected void initDataPanel()
	{
		dataClearPanel.add( dataClearButton );

		dataDataLengthPanel.add( dataDataLengthLabel );
		dataDataLengthPanel.add( dataDataLengthField );

		dataBufferLengthPanel.add( dataBufferLengthLabel );
		dataBufferLengthPanel.add( dataBufferLengthField );

		dataMetaPanel.add( dataBufferLengthPanel, BorderLayout.NORTH );
		dataMetaPanel.add( dataDataLengthPanel, BorderLayout.CENTER );
		dataMetaPanel.add( dataClearPanel, BorderLayout.SOUTH );

		dataLabelPanel.add( dataLabel );
		dataTextAreaPanel.add( dataTextArea );

		dataContentPanel.add( dataLabelPanel, BorderLayout.NORTH );
		dataContentPanel.add( dataTextAreaPanel, BorderLayout.CENTER );

		dataPanel.add( dataContentPanel, BorderLayout.WEST );
		dataPanel.add( dataMetaPanel, BorderLayout.EAST );
	}

	//*************************************************************************
	// Instance variables

	private JPanel textPanel = new JPanel( new BorderLayout() );
		private JPanel textContentPanel = new JPanel( new BorderLayout() );
			private JPanel textLabelPanel = new JPanel();
				private JLabel textLabel = new JLabel( TEXT_LABEL_TITLE );
			private JPanel textTextAreaPanel = new JPanel();
				private JTextArea textTextArea = new JTextArea( TEXT_TEXTAREA_DEFAULT_ROWS, TEXT_TEXTAREA_DEFAULT_COLS );
		private JPanel textMetaPanel = new JPanel( new BorderLayout() );
			private JPanel textLengthPanel = new JPanel();
				private JLabel textLengthLabel = new JLabel( TEXT_LENGTH_TITLE );
				private JTextField textLengthField = new JTextField( TEXT_LENGTH_DEFAULT_STRING, TEXT_LENGTH_DEFAULT_COLS );
			private JPanel textInsertPanel = new JPanel();
				private JButton textInsertButton = new JButton( TEXT_INSERT_TITLE );
			private JPanel textClearPanel = new JPanel();
				private JButton textClearButton = new JButton( TEXT_CLEAR_TITLE );
	private JPanel dataPanel = new JPanel( new BorderLayout() );
		private JPanel dataContentPanel = new JPanel( new BorderLayout() );
			private JPanel dataLabelPanel = new JPanel();
				private JLabel dataLabel = new JLabel( DATA_LABEL_TITLE );
			private JPanel dataTextAreaPanel = new JPanel();
				private JTextArea dataTextArea = new JTextArea( DATA_TEXTAREA_DEFAULT_ROWS, DATA_TEXTAREA_DEFAULT_COLS );
		private JPanel dataMetaPanel = new JPanel( new BorderLayout() );
			private JPanel dataBufferLengthPanel = new JPanel();
				private JLabel dataBufferLengthLabel = new JLabel( DATA_BUFFERLENGTH_TITLE );
				private JTextField dataBufferLengthField = new JTextField( DATA_BUFFERLENGTH_DEFAULT_STRING, DATA_BUFFERLENGTH_DEFAULT_COLS );
			private JPanel dataDataLengthPanel = new JPanel();
				private JLabel dataDataLengthLabel = new JLabel( DATA_DATALENGTH_TITLE );
				private JTextField dataDataLengthField = new JTextField( DATA_DATALENGTH_DEFAULT_STRING, DATA_DATALENGTH_DEFAULT_COLS );
			private JPanel dataClearPanel = new JPanel();
				private JButton dataClearButton = new JButton( DATA_CLEAR_TITLE );

	//*************************************************************************
	// Class constants

	private static final String TEXT_LABEL_TITLE = "Text";
	private static final int TEXT_TEXTAREA_DEFAULT_ROWS = 4;
	private static final int TEXT_TEXTAREA_DEFAULT_COLS = 20;
	private static final String TEXT_LENGTH_TITLE = "Length:";
	private static final String TEXT_LENGTH_DEFAULT_STRING = "0";
	private static final int TEXT_LENGTH_DEFAULT_COLS = 4;
	private static final String TEXT_INSERT_TITLE = "Insert";
	private static final String TEXT_CLEAR_TITLE = "Clear";
	private static final String DATA_LABEL_TITLE = "Data";
	private static final int DATA_TEXTAREA_DEFAULT_ROWS = 4;
	private static final int DATA_TEXTAREA_DEFAULT_COLS = 20;
	private static final String DATA_BUFFERLENGTH_TITLE = "Buffer Length:";
	private static final String DATA_BUFFERLENGTH_DEFAULT_STRING = "0";
	private static final int DATA_BUFFERLENGTH_DEFAULT_COLS = 4;
	private static final String DATA_DATALENGTH_TITLE = "Data Length:";
	private static final String DATA_DATALENGTH_DEFAULT_STRING = "0";
	private static final int DATA_DATALENGTH_DEFAULT_COLS = 4;
	private static final String DATA_CLEAR_TITLE = "Clear";

}
