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
package com.lundellnet.toolbox.matrix;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.lundellnet.toolbox.Reflect;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixComponent;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixField;
import com.lundellnet.toolbox.api.data_access.annotations.PointMapping;
import com.lundellnet.toolbox.evince.MatchingConstraint;
import com.lundellnet.toolbox.evince.MutationType;
import com.lundellnet.toolbox.matrix.collection.MatrixFacet;
import com.lundellnet.toolbox.matrix.precedents.MatrixAnnotationPrecedent;

public class M_Component <D extends Enum<D>, M extends Enum<M>, P extends Enum<P>> {
	
	private static abstract class Facet <I, O>
			extends MatrixFacet<I, O>
	{
		Facet(M_Component<?, ?, ?> mComponent, Supplier<?> parentSupplier, Field dataField) {
			super(mComponent, parentSupplier, dataField);
		}
	}
	
	private static void checkMatchingAnnotations(List<PointMapping> matchingAnnotations) {
		if (matchingAnnotations.size() != 1) {
			if (matchingAnnotations.isEmpty()) {
				//TODO
			} else if (matchingAnnotations.size() > 1) {
				//TODO
			}
		}
	}
	
	private static Object getMappedScope(Object scope, PointMapping mapping) {
		Class<?> scopeClass = scope.getClass();
		return Reflect.invokePublicMethod(Reflect.getMutationMethod(
				MutationType.GET, scopeClass, Reflect.getMappedField(scopeClass, mapping.path(), "/")
			), scope);
	}
	
	private final M_Dimension<D, M> dimension;
	private final List<MatrixField> mCompFields;
	private final MatrixComponent mComponent;
	private final Class<?> compClass;
	private final Field[] fields;
	private final MatrixProperties props;
	
	private Object compScope;
	private Map<P, Object> compPlanes;
	
	M_Component(
			M_Dimension<D, M> dimension, List<MatrixField> mCompFields, MatrixComponent mComponent,
			Class<?> compClass, Field[] fields
	) {
		this.dimension = dimension;
		this.mCompFields = mCompFields;
		this.mComponent = mComponent;
		this.compClass = compClass;
		this.fields = fields;
		this.props = new MatrixProperties(dimension.getConfiguration(), dimension.props(), mComponent);
	}
	
	M_Component<D, M, P> instigateComponent(ComponentInstigate<?> compInst) {
		if (compInst.component() != null//would be null if root
		&& !Arrays.stream(mComponent.providers()).anyMatch((c) -> c.equals(compInst.component())))
		{
			//TODO provided component isn't in the providers list
		}
		
		Map<Constraints.Mappings, Object> matchers = new EnumMap<>(Constraints.Mappings.class);
		
		matchers.put(Constraints.Mappings.MATCH_I_DOMAIN, dimension.getConfiguration().domain().ordinal());
		matchers.put(Constraints.Mappings.MATCH_I_MODEL, dimension.getConfiguration().model().ordinal());
		
		List<PointMapping> matchingAnnotations;
		switch (compInst.mappingType()) {
			case PROVIDER:
				matchers.put(Constraints.Mappings.MATCH_COMPONENT, compInst.component());//instigator dictates
				
				matchingAnnotations = MatchingConstraint.getMatcher(matchers)
						.apply(Arrays.stream(mComponent.providerMappings()))
						.collect(Collectors.toList());
				
				checkMatchingAnnotations(matchingAnnotations);
				
				PointMapping mapping = matchingAnnotations.get(0);
				
				boolean iPlaneExists = mapping.i_plane() != -1;
				if (compInst.planeEnum() != null && (iPlaneExists || mapping.plane() != null)) {
					int mappingPlaneOrdinal = (mapping.i_plane() == -1) ?
							mapping.i_plane()
						:
							(int) Reflect.invokePublicMethod(
								Reflect.getPublicMethod("fromValue", compInst.planeEnum(), String.class), null, mapping.plane()
							);
					Enum<?>[] planeValues = Reflect.enumValues(compInst.planeEnum());
					
					if ((planeValues.length - 1) < mappingPlaneOrdinal) {
						//TODO
					}
					
					Enum<?> planeValue = planeValues[mappingPlaneOrdinal];
					Object planeScope = compInst.planes().get(planeValue);
					
					if (planeScope == null) {
						//TODO
					}
					
					compScope = getMappedScope(planeScope, mapping);
				}
				
				compScope = getMappedScope(compInst.componentScope(), mapping);
			break;
			case ROOT:
				matchingAnnotations = MatchingConstraint.getMatcher(matchers)
						.apply(Arrays.stream(mComponent.rootMappings()))
						.collect(Collectors.toList());
				
				checkMatchingAnnotations(matchingAnnotations);
				
				compScope = getMappedScope(dimension.getRootScope(), matchingAnnotations.get(0));
			break;
		}
		
		return this;
	}
	
	M_Dimension<D, M> dimension() {
		return dimension;
	}
	
	boolean hasProvider(Class<?> providerClass) {//Note: perhaps a more clever way to search?
		return Arrays.stream(mComponent.providers()).anyMatch((p) -> p.equals(providerClass));
	}
	
	public Class<?> getComponentClass() {
		return compClass;
	}
	
	public <I, O> Stream<MatrixFacet<?, ?>> streamFacets() {
		Object compObj = Reflect.instantiateClass(compClass);//Note: customizable object creator?
		
		Arrays.stream(fields).map((f) -> new Facet<I, O>(this, () -> compObj, f) {
				private final M_Field<I, O> mField = (M_Field<I, O>) dimension.getMatrixFieldData(f);
			
				@Override
				protected MatrixAnnotationPrecedent<I, O> precedent() {
					// TODO Auto-generated method stub
					return null;
				}
		});
		
		return null;
	}
	
	public MatrixProperties props() {
		return props;
	}
}
