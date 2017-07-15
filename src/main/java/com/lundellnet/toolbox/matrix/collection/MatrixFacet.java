/*
 Copyright 2017 Appropriate Technologies LLC.

 This file is part of toolbox-matrix, a component of the Lundellnet Java Toolbox.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.lundellnet.toolbox.matrix.collection;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import com.lundellnet.toolbox.matrix.M_Component;
import com.lundellnet.toolbox.matrix.precedents.MatrixAnnotationPrecedent;
import com.lundellnet.toolbox.obj.facets.Facet;

public abstract class MatrixFacet <I, O>
		implements Facet<Field, PermutationElementConfig<I, O>>
{
	private final M_Component<?, ?, ?> mComponent;
	private final Supplier<?> parentSupplier;
	private final Field dataField;
	
	protected MatrixFacet(
			M_Component<?, ?, ?> mComponent, Supplier<?> parentSupplier, Field dataField
	) {
		this.mComponent = mComponent;
		this.parentSupplier = parentSupplier;
		this.dataField = dataField;
	}
	
	protected abstract MatrixAnnotationPrecedent<I, O> precedent();
	
	protected Class<?> parentClass() {
		return mComponent.getComponentClass();
	}
	
	protected Supplier<?> parentSupplier() {
		return parentSupplier;
	}
	
	@Override
	public Field identity() {
		return dataField;
	}
	
	@Override
	public PermutationElementConfig<I, O> elementConf() {
		return new PermutationElementConfig<>(this, null);//TODO
	}
}
