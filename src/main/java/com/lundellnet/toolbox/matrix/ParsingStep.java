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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.lundellnet.toolbox.api.data_access.annotations.MatrixField;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixFields;
import com.lundellnet.toolbox.api.data_access.annotations.PointMapping;
import com.lundellnet.toolbox.api.data_access.annotations.PointMappings;
import com.lundellnet.toolbox.evince.MatchingConstraint;

public enum ParsingStep {
	MAP(
		new Class<?>[]{PointMappings.class, PointMapping.class},
		(a, c) -> {
			if (a.annotationType().equals(PointMappings.class)) {
				Map<Constraints.Mappings, Object> matchers = new EnumMap<>(Constraints.Mappings.class);
				
				matchers.put(Constraints.Mappings.MATCH_I_DOMAIN, c.domain().ordinal());
				matchers.put(Constraints.Mappings.MATCH_I_MODEL, c.model().ordinal());
				
				return MatchingConstraint.getMatcher(matchers)
						.apply(Arrays.stream(((PointMappings) a).value()))
						.collect(Collectors.toList());
			} else if (a.annotationType().equals(PointMapping.class)) {
				List<PointMapping> mappings = new ArrayList<>();
				
				mappings.add((PointMapping) a);
				
				return mappings;
			}
			
			return null;//TODO error
		}
	),
	TRANSVERSE(
		new Class<?>[]{MatrixFields.class, MatrixField.class},
		(a, c) -> {
			if (a.annotationType().equals(MatrixFields.class)) {
				Map<Constraints.Fields, Object> matchers = new EnumMap<>(Constraints.Fields.class);
				
				matchers.put(Constraints.Fields.MATCH_I_DOMAIN, c.domain().ordinal());
				matchers.put(Constraints.Fields.MATCH_I_MODEL, c.model().ordinal());
				
				return MatchingConstraint.getMatcher(matchers)
						.apply(Arrays.stream(((MatrixFields) a).value()))
						.collect(Collectors.toList());
			} else if (a.annotationType().equals(MatrixField.class)) {
				List<MatrixField> fields = new ArrayList<>();
				
				fields.add((MatrixField) a);
				
				return fields;
			}

			return null;//TODO error
		}
	);
	
	private final Class<?>[] stepTypes;
	private final BiFunction<Annotation, DimensionConfig<?, ?>, List<? extends Annotation>> dimensionMatcher;
	
	private ParsingStep(
			Class<?>[] stepTypes, BiFunction<Annotation, DimensionConfig<?, ?>, List<? extends Annotation>> dimensionMatcher
	) {
		this.stepTypes = stepTypes;
		this.dimensionMatcher = dimensionMatcher;
	}
	
	BiFunction<Annotation, DimensionConfig<?, ?>, List<? extends Annotation>> dimensionMatcher() {
		return dimensionMatcher;
	}
	
	public static ParsingStep fromValue(Class<?> annotationType) {
		for (ParsingStep step : ParsingStep.values()) 
			for (Class<?> annotationClass : step.stepTypes) 
				if (annotationClass.equals(annotationType)) return step;
			
		throw new IllegalArgumentException(
				"No " + ParsingStep.class.getSimpleName() + " was defined for the Class: " + annotationType.getName()
			);
	}
}
