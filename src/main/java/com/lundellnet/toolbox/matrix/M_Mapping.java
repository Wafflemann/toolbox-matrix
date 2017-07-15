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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.lundellnet.toolbox.api.data_access.annotations.PointMapping;
import com.lundellnet.toolbox.evince.MatchingConstraint;
import com.lundellnet.toolbox.matrix.precedents.MatrixAnnotationPrecedent;

public class M_Mapping <I, O>
		extends Deduce<FieldInstigate<?>, I, O>
{
	private final M_Component<?, ?, ?> mComponent;
	private final FieldConfig fConf;
	private final Field field;
	private final List<PointMapping> mMappings;
	
	M_Mapping(M_Component<?, ?, ?> mComponent, FieldConfig fConf, Field field, List<PointMapping> mMappings) {
		this.mComponent = mComponent;
		this.fConf = fConf;
		this.field = field;
		this.mMappings = mMappings;
	}

	@Override
	protected MatrixAnnotationPrecedent<I, O> deducePrecedence(FieldInstigate<?> fieldInst) {
		//a lot of this could be combined with M_Field
		Map<Constraints.Mappings, Object> matchers = new EnumMap<>(Constraints.Mappings.class);
		
		if (fieldInst.component() != null) matchers.put(Constraints.Mappings.MATCH_COMPONENT, fieldInst.component());
		if (fieldInst.plane() != null) matchers.put(Constraints.Mappings.MATCH_I_PLANE, fieldInst.component());
		
		List<PointMapping> matchingAnnotations = MatchingConstraint.getMatcher(matchers)
				.apply(mMappings.stream())
				.collect(Collectors.toList());
		
		if (matchingAnnotations.size() != 1) {
			if (matchingAnnotations.isEmpty()) {
				//TODO
			} else if (matchingAnnotations.size() > 1) {
				//TODO
			}
		}
		
		PointMapping mMapping = matchingAnnotations.get(0);
		
		return () -> new AbstractPrecedentConfig<I, O>(fConf) {
				@Override
				public Function<I, O> applicant() {
					// TODO Auto-generated method stub
					return null;
				}
			};
	}
}
