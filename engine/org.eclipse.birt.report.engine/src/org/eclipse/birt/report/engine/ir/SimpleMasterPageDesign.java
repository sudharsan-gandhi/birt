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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;

/**
 * Simple master page object.
 * 
 * a simple master page contains a header, and a footer.
 * 
 * 
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
public class SimpleMasterPageDesign extends MasterPageDesign
{

	/**
	 * page header content
	 */
	private ArrayList header = new ArrayList( );
	/**
	 * page footer content
	 */
	private ArrayList footer = new ArrayList( );

	/**
	 * show page header on the first page
	 */
	private boolean showHeaderOnFirst = true;
	/**
	 * show footer on the last page
	 */
	private boolean showFooterOnLast = true;
	/**
	 * show the footer under the page content
	 */
	private boolean floatingFooter = false;

	/**
	 * @return Returns the floatingFooter.
	 */
	public boolean isFloatingFooter( )
	{
		return floatingFooter;
	}

	/**
	 * @param floatingFooter
	 *            The floatingFooter to set.
	 */
	public void setFloatingFooter( boolean floatingFooter )
	{
		this.floatingFooter = floatingFooter;
	}

	/**
	 * @return Returns the showFooterOnLast.
	 */
	public boolean isShowFooterOnLast( )
	{
		return showFooterOnLast;
	}

	/**
	 * @param showFooterOnLast
	 *            The showFooterOnLast to set.
	 */
	public void setShowFooterOnLast( boolean showFooterOnLast )
	{
		this.showFooterOnLast = showFooterOnLast;
	}

	/**
	 * @return Returns the showHeaderOnFirst.
	 */
	public boolean isShowHeaderOnFirst( )
	{
		return showHeaderOnFirst;
	}

	/**
	 * @param showHeaderOnFirst
	 *            The showHeaderOnFirst to set.
	 */
	public void setShowHeaderOnFirst( boolean showHeaderOnFirst )
	{
		this.showHeaderOnFirst = showHeaderOnFirst;
	}

	/**
	 * @return Returns the footer.
	 */
	public ArrayList getFooters( )
	{
		return footer;
	}

	/**
	 * get the total item in the footer
	 * 
	 * @return total items in the footer
	 */
	public int getFooterCount( )
	{
		return this.footer.size( );
	}

	/**
	 * get the index item in the footer
	 * 
	 * @param index
	 *            index of the item
	 * @return item at the index
	 */
	public ReportItemDesign getFooter( int index )
	{
		assert index >= 0 && index < footer.size( );
		return (ReportItemDesign) footer.get( index );
	}

	/**
	 * add an item into the footer
	 * 
	 * @param item
	 *            item to be added
	 */

	public void addFooter( ReportItemDesign item )
	{
		this.footer.add( item );
	}

	/**
	 * @return Returns the header.
	 */
	public ArrayList getHeaders( )
	{
		return header;
	}

	/**
	 * get the item count in the header band.
	 * 
	 * @return total items in the header band
	 */
	public int getHeaderCount( )
	{
		return this.header.size( );
	}

	/**
	 * get the index item in the header
	 * 
	 * @param index
	 *            index of the item
	 * @return item at index.
	 */
	public ReportItemDesign getHeader( int index )
	{
		assert index >= 0 && index < header.size( );
		return (ReportItemDesign) header.get( index );
	}

	/**
	 * add an item into the header band.
	 * 
	 * @param item
	 *            item to be added
	 */
	public void addHeader( ReportItemDesign item )
	{
		this.header.add( item );
	}
}
