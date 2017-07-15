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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.lundellnet.toolbox.api.data_access.annotations.MatrixField;
import com.lundellnet.toolbox.evince.MatchingConstraint;
import com.lundellnet.toolbox.matrix.precedents.MatrixAnnotationPrecedent;

public class M_Field <I, O>
		extends Deduce<FieldInstigate<?>, I, O>
{
	private final M_Component<?, ?, ?> mComponent;
	private final FieldConfig fConf;
	private final Field field;
	private final List<MatrixField> mFields;
	
	M_Field(M_Component<?, ?, ?> mComponent, FieldConfig fConf, Field field, List<MatrixField> mFields) {
		this.mComponent = mComponent;
		this.fConf = fConf;
		this.field = field;
		this.mFields = mFields;
	}
	
	private MatrixAnnotationPrecedent<I, O> processMField(
			FieldInstigate<?> fieldInst, MatrixField mField, List<Class<?>> reqProviders
	) {
		M_Component<?, ?, ?> fieldComp = mComponent.dimension().getMatixComponentData(field.getType());
		
		if (!reqProviders.stream().allMatch((c) -> fieldComp.hasProvider(c))) {
			//TODO doesnt have access
		}
		
		//precedent which creates a collector
		return () -> new AbstractPrecedentConfig<I, O>(fConf) {
				@Override
				public Function<I, O> applicant() {
					// TODO Auto-generated method stub
					return null;
				}
			};
	}
	
	@Override
	protected MatrixAnnotationPrecedent<I, O> deducePrecedence(FieldInstigate<?> fieldInst) {
		List<Class<?>> reqProviders = new ArrayList<>();
		
		reqProviders.add(mComponent.getComponentClass());
		
		if (mFields.size() == 1) {
			MatrixField mField = mFields.get(0);
			
			if (mField.component() != null) reqProviders.add(mField.component());
			
			return processMField(fieldInst, mField, reqProviders);
		} else {
			Map<Constraints.Fields, Object> matchers = new EnumMap<>(Constraints.Fields.class);
			
			if (fieldInst.component() != null) matchers.put(Constraints.Fields.MATCH_COMPONENT, fieldInst.component());
			if (fieldInst.plane() != null) matchers.put(Constraints.Fields.MATCH_I_PLANE, fieldInst.plane().ordinal());
			
			List<MatrixField> matchingAnnotations = MatchingConstraint.getMatcher(matchers)
					.apply(mFields.stream())
					.collect(Collectors.toList());
			
			if (matchingAnnotations.size() != 1) {
				if (matchingAnnotations.isEmpty()) {
					//TODO 
				} else if (matchingAnnotations.size() > 1) {
					//TODO
				}
			}
			
			MatrixField mField = matchingAnnotations.get(0);
			
			reqProviders.add(mField.component());
			
			return processMField(fieldInst, mField, reqProviders);
		}
	}
}
