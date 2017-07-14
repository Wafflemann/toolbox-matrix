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

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.lundellnet.toolbox.api.data_access.annotations.MappingProperty;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixComponent;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixSource;
import com.lundellnet.toolbox.commons.ReferenceProperties;
import com.lundellnet.toolbox.evince.MatchingConstraint;

public class MatrixProperties
	extends ReferenceProperties
{
	private static final BiFunction<DimensionConfig<?, ?>, MappingProperty[], Properties> DIMENSION_PROPERTIES = (conf, props) -> {
			Map<Constraints.Properties, Object> propertyDimensionMatchers = new EnumMap<>(Constraints.Properties.class);
			
			propertyDimensionMatchers.put(Constraints.Properties.MATCH_I_DOMAIN, conf.domain().ordinal());
			propertyDimensionMatchers.put(Constraints.Properties.MATCH_I_MODEL, conf.model().ordinal());
			
			return MatchingConstraint.getMatchingStream(Constraints.Properties.class,  propertyDimensionMatchers)
					.collect(
							() -> new Properties(),
							(p, o) -> o.apply(Arrays.stream(props))
										.collect(Collectors.toMap(
											MappingProperty::key,
											MappingProperty::value,
											(l, r) -> l,
											() -> p
										)),
							(p1, p2) -> p1.putAll(p2)
					);
		};
	
	MatrixProperties(DimensionConfig<?, ?> conf, MatrixSource src) {
		super(DIMENSION_PROPERTIES.apply(conf, src.properties()));
	}
	
	MatrixProperties(DimensionConfig<?, ?> conf, MatrixProperties parentProps, MatrixComponent comp) {
		super(parentProps.properties(), DIMENSION_PROPERTIES.apply(conf, comp.properties()));
	}
}
