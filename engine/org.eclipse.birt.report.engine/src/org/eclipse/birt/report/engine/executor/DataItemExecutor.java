/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.TextItemContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.elements.Style;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>StyledItemExecutor</code> that manipulates data items from database
 * columns, expressions and so on.
 * <p>
 * Data item executor calculates expressions in data item design, generate a
 * text content instance, set bookmark, action and help text property and pass
 * this instance to emitter.
 * 
 * @version $Revision: #3 $ $Date: 2005/02/02 $
 */
public class DataItemExecutor extends StyledItemExecutor
{

	/**
	 * construct a data item executor by giving execution context and report
	 * executor visitor
	 * 
	 * @param context
	 *            the executor context
	 * @param itemEmitter
	 *            the emitter
	 */
	public DataItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.excutor.ReportItemExecutor#execute()
	 */
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		DataItemDesign dataItem = (DataItemDesign) item;

		IReportItemEmitter textEmitter = emitter.getEmitter( "text" ); //$NON-NLS-1$
		/*
		 * Getting item emitter returns null means that excuting this item is
		 * not necessary. for example, items in masterpage for html emitter will
		 * be discarded.
		 */
		if ( textEmitter == null )
		{
			return;
		}
		IResultSet rs = openResultSet( item );
		if ( rs != null )
		{
			rs.next( );
		}

		Object value = context.evaluate( dataItem.getValue( ) );
		//get the mapping value
		value = getMapVal( value, dataItem );

		TextItemContent textObj = new TextItemContent( dataItem );
		textObj.setHelpText( getLocalizedString( dataItem.getHelpTextKey( ),
				dataItem.getHelpText( ) ) );
		setStyles( textObj, item );

		StringBuffer formattedString = new StringBuffer( );
		boolean isFormatted = formatValue( value, null, dataItem.getStyle( ),
				formattedString );
		textObj.setValue( formattedString.toString( ) );

		if ( value != null )
		{
			if ( value instanceof Number
					&& textObj.getMergedStyle( ).getNumberAlign( ) != null )
			{
				// set number alignment
				textObj.setStyleProperty( Style.TEXT_ALIGN_PROP, textObj
						.getMergedStyle( ).getNumberAlign( ) );

			}

			// set StringFormatted flag
			textObj.setStringFormatted( isFormatted );
		}

		setVisibility( item, textObj );
		processAction( textObj.getAction( ), textObj );
		String bookmarkStr = evalBookmark( item );
		if (bookmarkStr != null)
			textObj.setBookmarkValue(bookmarkStr);
		//pass the text content instance to emitter
		textEmitter.start( textObj );
		textEmitter.end( );

		closeResultSet( rs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		//do nothing
	}
}