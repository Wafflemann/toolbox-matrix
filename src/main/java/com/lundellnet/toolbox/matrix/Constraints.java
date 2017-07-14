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

import java.util.function.BiFunction;
import java.util.stream.Stream;

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
		MATCH_DOMAIN((domain, s) -> s.filter((a) -> a.domain().equals(domain))),
		MATCH_I_DOMAIN((ordinal, s) -> s.filter((a) -> a.i_domain() == (Integer) ordinal)),
		MATCH_MODEL((model, s) -> s.filter((a) -> a.model().equals(model))),
		MATCH_I_MODEL((ordinal, s) -> s.filter((a) -> a.i_model() == (Integer) ordinal));
		
		private final BiFunction<Object, Stream<MatrixAdapter>, Stream<MatrixAdapter>> matchingFunction;
		
		private Adapters(BiFunction<Object, Stream<MatrixAdapter>, Stream<MatrixAdapter>> mappingFunction) {
			this.matchingFunction = mappingFunction;
		}
	
		@Override
		public BiFunction<Object, Stream<MatrixAdapter>, Stream<MatrixAdapter>> matchingFunction() {
			return matchingFunction;
		}
	}
	
	enum DomainConf
			implements MatchingConstraint<MatrixDomainConfig>
	{
		MATCH_DOMAIN((domain, s) -> s.filter((c) -> c.domain().equals(domain))),
		MATCH_I_DOMAIN((ordinal, s) -> s.filter((c) -> c.i_domain() == (Integer) ordinal));
		
		private final BiFunction<Object, Stream<MatrixDomainConfig>, Stream<MatrixDomainConfig>> matchingFunction;
		
		private DomainConf(BiFunction<Object, Stream<MatrixDomainConfig>, Stream<MatrixDomainConfig>> matchingFunction) {
			this.matchingFunction = matchingFunction;
		}
	
		@Override
		public BiFunction<Object, Stream<MatrixDomainConfig>, Stream<MatrixDomainConfig>> matchingFunction() {
			return matchingFunction;
		}
	}
	
	public enum Fields
			implements MatchingConstraint<MatrixField>
	{
		MATCH_DOMAIN((domain, s) -> s.filter((f) -> f.domain().equals(domain))),
		MATCH_I_DOMAIN((ordinal, s) -> s.filter((f) -> f.i_domain() == (Integer) ordinal)),
		MATCH_MODEL((model, s) -> s.filter((f) -> f.model().equals(model))),
		MATCH_I_MODEL((ordinal, s) -> s.filter((f) -> f.i_model() == (Integer) ordinal)),
		MATCH_COMPONENT((compClass, s) -> s.filter((f) -> f.component().equals(compClass))),
		MATCH_PLANE((plane, s) -> s.filter((f) -> f.plane().equals(plane))),
		MATCH_I_PLANE((ordinal, s) -> s.filter((f) -> f.i_plane() == (Integer) ordinal));
	
		private final BiFunction<Object, Stream<MatrixField>, Stream<MatrixField>> matchingFunction;
	
		private Fields(BiFunction<Object, Stream<MatrixField>, Stream<MatrixField>> matchingFunction) {
			this.matchingFunction = matchingFunction;
		}
	
		@Override
		public BiFunction<Object, Stream<MatrixField>, Stream<MatrixField>> matchingFunction() {
			return matchingFunction;
		}
	}
	
	enum Mappings
			implements MatchingConstraint<PointMapping>
	{
		MATCH_DOMAIN((domain, s) -> s.filter((m) -> m.domain().equals(domain))),
		MATCH_I_DOMAIN((ordinal, s) -> s.filter((m) -> m.i_domain() == (Integer) ordinal)),
		MATCH_MODEL((model, s) -> s.filter((m) -> m.model().equals(model))),
		MATCH_I_MODEL((ordinal, s) -> s.filter((m) -> m.i_model() == (Integer) ordinal)),
		MATCH_COMPONENT((compClass, s) -> s.filter((m) -> m.component().equals(compClass))),
		MATCH_PLANE((plane, s) -> s.filter((m) -> m.plane().equals(plane))),
		MATCH_I_PLANE((ordinal, s) -> s.filter((m) -> m.i_plane() == (Integer) ordinal));
		
		private final BiFunction<Object, Stream<PointMapping>, Stream<PointMapping>> matchingFunction;
		
		private Mappings(BiFunction<Object, Stream<PointMapping>, Stream<PointMapping>> matchingFunction) {
			this.matchingFunction = matchingFunction;
		}
	
		@Override
		public BiFunction<Object, Stream<PointMapping>, Stream<PointMapping>> matchingFunction() {
			return matchingFunction;
		}
	}
	
	enum Properties
			implements MatchingConstraint<MappingProperty>
	{
		MATCH_DOMAIN((domain, s) -> s.filter((p) -> p.domain().equals(domain))),
		MATCH_I_DOMAIN((ordinal, s) -> s.filter((p) -> p.i_domain() == (Integer) ordinal)),
		MATCH_MODEL((model, s) -> s.filter((p) -> p.model().equals(model))),
		MATCH_I_MODEL((ordinal, s) -> s.filter((p) -> p.i_model() == (Integer) ordinal));
		
		private final BiFunction<Object, Stream<MappingProperty>, Stream<MappingProperty>> matchingFunction;
		
		private Properties(BiFunction<Object, Stream<MappingProperty>, Stream<MappingProperty>> matchingFunction) {
			this.matchingFunction = matchingFunction;
		}
	
		@Override
		public BiFunction<Object, Stream<MappingProperty>, Stream<MappingProperty>> matchingFunction() {
			return matchingFunction;
		}
	}
	
	
}
