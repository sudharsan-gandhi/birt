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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.command.NameException;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.birt.report.model.metadata.IStructureDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyValueException;

/**
 * Filter data processor
 *  
 */
public class FilterModelProvider
{

	/**
	 * The list of allowed FilterCondition.OPERATOR_MEMBER
	 */
	private ChoiceSet choiceSet;

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];
	
	private DataSetItemModel[] models;

	/**
	 * Gets the display names of the given property keys.
	 * 
	 * @param keys
	 *            Property keys
	 * @return String array contains display names
	 */
	public String[] getColumnNames( String[] keys )
	{
		assert keys != null;
		String[] columnNames = new String[keys.length];
		for ( int i = 0; i < keys.length; i++ )
		{
			MetaDataDictionary metaData = MetaDataDictionary.getInstance( );
			IStructureDefn structure = metaData.getStructure( FilterCondition.FILTER_COND_STRUCT );
			columnNames[i] = structure.getMember( keys[i] ).getDisplayName( );
		}
		return columnNames;
	}

	/**
	 * Gets all elements of the given input.
	 * 
	 * @param input
	 *            The input object.
	 * @return Filters array.
	 */
	public Object[] getElements( List input )
	{
		Object obj = input.get( 0 );
		if ( !( obj instanceof DesignElementHandle ) )
			return EMPTY;
		DesignElementHandle element = (DesignElementHandle) obj;
		PropertyHandle propertyHandle = element.getPropertyHandle( TableItem.FILTER_PROP );
		Iterator iterator = propertyHandle.iterator( );
		if ( iterator == null )
			return EMPTY;
		List list = new ArrayList( );
		while ( iterator.hasNext( ) )
			list.add( iterator.next( ) );
		return list.toArray( );
	}

	/**
	 * Gets property display name of a given element.
	 * 
	 * @param element
	 *            Filter object
	 * @param key
	 *            Property key
	 * @return The text according the key
	 */
	public String getText( Object element, String key )
	{
		if ( !( element instanceof StructureHandle ) )
		{
			return "";//$NON-NLS-1$
		}

		String value = ( (StructureHandle) element ).getMember( key )
				.getStringValue( );
		if ( value == null )
		{
			value = "";//$NON-NLS-1$
		}

		if ( key.equals( FilterCondition.OPERATOR_MEMBER ) )
		{
			Choice choice = choiceSet.findChoice( value );
			if ( choice != null )
			{
				return choice.getDisplayName( );
			}
		}
		else
		{
			return value;
		}

		return "";//$NON-NLS-1$

	}

	/**
	 * Saves new property value to filter
	 * 
	 * @param element
	 *            Filter object
	 * @param key
	 *            Property key
	 * @param newValue
	 *            new value
	 * @return The value according the key
	 */
	public boolean setStringValue( Object item, Object element, String key,
			String newValue ) throws NameException, SemanticException
	{
		if(!key.equals(FilterCondition.OPERATOR_MEMBER ))
		{
			String value = DEUtil.getExpression(getResultSetColumn(newValue));
			if(value != null)
				newValue = value; 
		}
//		if ( !( element instanceof StructureHandle ) )
//		{
//			FilterCondition filterCond = StructureFactory.createFilterCond( );
//			if ( key.equals( FilterCondition.EXPR_MEMBER ) )
//			{
//				filterCond.setExpr( newValue );
//			}
//			DesignElementHandle handle = (DesignElementHandle) item;
//			PropertyHandle propertyHandle = handle.getPropertyHandle( ListingElement.FILTER_PROP );
//			propertyHandle.addItem( filterCond );
//			element = filterCond.getHandle( propertyHandle );
//		}

		String saveValue = newValue;
		StructureHandle handle = (StructureHandle) element;
		if ( key.equals( FilterCondition.OPERATOR_MEMBER ) )
		{
			Choice choice = choiceSet.findChoiceByDisplayName( newValue );
			if ( choice == null )
				saveValue = null;
			else
				saveValue = choice.getName( );
		}
		handle.getMember( key ).setStringValue( saveValue );
		return true;
	}

	/**
	 * Gets the choice set of one property
	 * 
	 * @param item
	 *            ReportItem object
	 * @param key
	 *            Property key
	 * @return Choice set
	 */
	public String[] getChoiceSet( Object item, String key )
	{
		if ( key.equals( FilterCondition.OPERATOR_MEMBER ) )
		{
			choiceSet = ChoiceSetFactory.getStructChoiceSet( FilterCondition.FILTER_COND_STRUCT,
					key );
			return ChoiceSetFactory.getDisplayNamefromChoiceSet( choiceSet );
		}
		if ( !( item instanceof ReportItemHandle ) )
			return EMPTY;
		return getDataSetColumns( (ReportItemHandle) item );
	}

	/**
	 * Gets all columns in a dataSet.
	 * 
	 * @param handle
	 *            ReportItem object
	 * @return Columns array.
	 */
	private String[] getDataSetColumns( ReportItemHandle handle )
	{
		DataSetHandle dataSet = (DataSetHandle) handle.getDataSet( );
		if ( dataSet == null )
			return EMPTY;
		models = DataSetManager.getCurrentInstance().getColumns(dataSet,false);
		if(models == null)
			return EMPTY;
		String[] values = new String[models.length];
		for(int i=0; i<models.length; i++)
		{
			values[i] = models[i].getDisplayName();
		}
		return values;
	}
	
	private Object getResultSetColumn(String name)
	{
		if(models == null)return null;
		for(int i=0; i<models.length; i++)
		{
			DataSetItemModel model = models[i];
			if(model.getDisplayName().equals(name))return model;
		}
		return null;
	}

	/**
	 * Moves one item from a position to another.
	 * 
	 * @param item
	 *            DesignElement object
	 * @param oldPos
	 *            The item's current position
	 * @param newPos
	 *            The item's new position
	 * @return True if success, otherwise false.
	 */
	public boolean moveItem( Object item, int oldPos, int newPos )
			throws PropertyValueException
	{
		DesignElementHandle element = (DesignElementHandle) item;
		PropertyHandle propertyHandle = element.getPropertyHandle( TableItem.FILTER_PROP );
		propertyHandle.moveItem( oldPos, newPos );

		return true;
	}

	/**
	 * Deletes an item.
	 * 
	 * @param item
	 *            DesignElement object
	 * @param pos
	 *            The item's current position
	 * @return True if success, otherwise false.
	 */
	public boolean deleteItem( Object item, int pos )
			throws PropertyValueException
	{
		DesignElementHandle element = (DesignElementHandle) item;
		PropertyHandle propertyHandle = element.getPropertyHandle( TableItem.FILTER_PROP );
		if ( propertyHandle.getAt( pos ) != null )
		{
			propertyHandle.removeItem( pos );
		}

		return true;
	}
	
	/**
	 * Inserts one item into the given position.
	 * 
	 * @param item
	 *            DesignElement object
	 * @param pos
	 *            The position.
	 * @return True if success, otherwise false.
	 * @throws SemanticException
	 */
	public boolean doAddItem( Object item, int pos ) throws SemanticException
	{
		FilterCondition filterCond = StructureFactory.createFilterCond( );
		filterCond.setExpr("Expression");//$NON-NLS-1$
		DesignElementHandle handle = (DesignElementHandle) item;
		PropertyHandle propertyHandle = handle
				.getPropertyHandle( ListingElement.FILTER_PROP );
		propertyHandle.addItem( filterCond );
		return true;
	}
}