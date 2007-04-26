
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.api.query;

/**
 * ILevelDefinition defines Levels that are used in hierarchy.
 */

public interface ILevelDefinition extends INamedObject
{
	/**
	 * Return the hierarchy this level belongs to.
	 * @return
	 */
	public IHierarchyDefinition getHierarchy();
	
	/**
	 * Return whether this level is mirrored.In the edge a mirrored level will always
	 * display all its members w/o considering whether that member's combination with
	 * other members of other levels have accompany entry in facttable. 
	 * @return
	 */
	public boolean isMirrored();
}
