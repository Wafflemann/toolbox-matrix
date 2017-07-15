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

import java.util.function.BiPredicate;

import com.lundellnet.toolbox.api.data_access.annotations.MappingProperty;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixAdapter;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixDomainConfig;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixField;
import com.lundellnet.toolbox.api.data_access.annotations.PointMapping;
import com.lundellnet.toolbox.evince.MatchingConstraint;

class Constraints {
	enum Adapters
			implements MatchingConstraint<MatrixAdapter>
	{
		MATCH_DOMAIN((domain, a) -> a.domain().equals(domain)),
		MATCH_I_DOMAIN((ordinal, a) -> a.i_domain() == (Integer) ordinal),
		MATCH_MODEL((model, a) -> a.model().equals(model)),
		MATCH_I_MODEL((ordinal, a) -> a.i_model() == (Integer) ordinal);
		
		private final BiPredicate<Object, MatrixAdapter> matchingFunction;
		
		private Adapters(BiPredicate<Object, MatrixAdapter> mappingFunction) {
			this.matchingFunction = mappingFunction;
		}
	
		@Override
		public BiPredicate<Object, MatrixAdapter> matchingFunction() {
			return matchingFunction;
		}
	}
	
	enum DomainConf
			implements MatchingConstraint<MatrixDomainConfig>
	{
		MATCH_DOMAIN((domain, a) -> a.domain().equals(domain)),
		MATCH_I_DOMAIN((ordinal, a) -> a.i_domain() == (Integer) ordinal);
		
		private final BiPredicate<Object, MatrixDomainConfig> matchingFunction;
		
		private DomainConf(BiPredicate<Object, MatrixDomainConfig> matchingFunction) {
			this.matchingFunction = matchingFunction;
		}
	
		@Override
		public BiPredicate<Object, MatrixDomainConfig> matchingFunction() {
			return matchingFunction;
		}
	}
	
	enum Fields
			implements MatchingConstraint<MatrixField>
	{
		MATCH_DOMAIN((domain, a) -> a.domain().equals(domain)),
		MATCH_I_DOMAIN((ordinal, a) -> a.i_domain() == (Integer) ordinal),
		MATCH_MODEL((model, a) -> a.model().equals(model)),
		MATCH_I_MODEL((ordinal, a) -> a.i_model() == (Integer) ordinal),
		MATCH_COMPONENT((compClass, a) -> a.component().equals(compClass)),
		MATCH_PLANE((plane, a) -> a.plane().equals(plane)),
		MATCH_I_PLANE((ordinal, a) -> a.i_plane() == (Integer) ordinal);
	
		private final BiPredicate<Object, MatrixField> matchingFunction;
	
		private Fields(BiPredicate<Object, MatrixField> matchingFunction) {
			this.matchingFunction = matchingFunction;
		}
	
		@Override
		public BiPredicate<Object, MatrixField> matchingFunction() {
			return matchingFunction;
		}
	}
	
	enum Mappings
			implements MatchingConstraint<PointMapping>
	{
		MATCH_DOMAIN((domain, a) -> a.domain().equals(domain)),
		MATCH_I_DOMAIN((ordinal, a) -> a.i_domain() == (Integer) ordinal),
		MATCH_MODEL((model, a) -> a.model().equals(model)),
		MATCH_I_MODEL((ordinal, a) -> a.i_model() == (Integer) ordinal),
		MATCH_COMPONENT((compClass, a) -> a.component().equals(compClass)),
		MATCH_PLANE((plane, a) -> a.plane().equals(plane)),
		MATCH_I_PLANE((ordinal, a) -> a.i_plane() == (Integer) ordinal);
		
		private final BiPredicate<Object, PointMapping> matchingFunction;
		
		private Mappings(BiPredicate<Object, PointMapping> matchingFunction) {
			this.matchingFunction = matchingFunction;
		}
	
		@Override
		public BiPredicate<Object, PointMapping> matchingFunction() {
			return matchingFunction;
		}
	}
	
	enum Properties
			implements MatchingConstraint<MappingProperty>
	{
		MATCH_DOMAIN((domain, a) -> a.domain().equals(domain)),
		MATCH_I_DOMAIN((ordinal, a) -> a.i_domain() == (Integer) ordinal),
		MATCH_MODEL((model, a) -> a.model().equals(model)),
		MATCH_I_MODEL((ordinal, a) -> a.i_model() == (Integer) ordinal);
		
		private final BiPredicate<Object, MappingProperty> matchingFunction;
		
		private Properties(BiPredicate<Object, MappingProperty> matchingFunction) {
			this.matchingFunction = matchingFunction;
		}
	
		@Override
		public BiPredicate<Object, MappingProperty> matchingFunction() {
			return matchingFunction;
		}
	}
	
	
}
