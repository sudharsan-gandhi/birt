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

package org.eclipse.birt.report.engine.css.engine.value;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * This class provides an abstract implementation of the ValueManager interface.
 * 
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: AbstractValueManager.java,v 1.1 2005/11/11 06:26:44 wyan Exp $
 */
public abstract class AbstractValueManager extends AbstractValueFactory
		implements
			ValueManager
{

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	public Value createFloatValue( short unitType, float floatValue )
			throws DOMException
	{
		throw createDOMException( );
	}

	/**
	 * Implements {@link
	 * ValueManager#createStringValue(short,String,CSSEngine)}.
	 */
	public Value createStringValue( short type, String value, CSSEngine engine )
			throws DOMException
	{
		throw createDOMException( );
	}

	/**
	 * Implements {@link
	 * ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public Value computeValue( CSSStylableElement elt, CSSEngine engine,
			int idx, Value value )
	{

		if ( value.getCssValueType( ) == CSSValue.CSS_PRIMITIVE_VALUE )
		{
			CSSPrimitiveValue pvalue = (CSSPrimitiveValue) value;
			if ( pvalue.getPrimitiveType( ) == CSSPrimitiveValue.CSS_URI )
			{
				// Reveal the absolute value as the cssText now.
				return new URIValue( pvalue.getStringValue( ), pvalue
						.getStringValue( ) );
			}
		}
		return value;
	}
}
