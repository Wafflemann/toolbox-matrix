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

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.lundellnet.toolbox.Reflect;
import com.lundellnet.toolbox.api.data_access.adapters.MatrixItemAdapter;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixComponent;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixDomainConfig;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixSource;
import com.lundellnet.toolbox.commons.NumberUtils;
import com.lundellnet.toolbox.matrix.exceptions.MatrixIndexingException;

public class M_Source <D extends Enum<D>, M extends Enum<M>> {
	
	private final Class<?> sourceClass;
	private final MatrixSource src;
	
	private final Map<Class<?>, MatrixItemAdapter<?, ?>> srcAdapters = new HashMap<>();
	private final EnumMap<D, EnumMap<M, M_Dimension<D, M>>> dimension;
	
	@SuppressWarnings("unchecked")
	M_Source(Class<?> sourceClass) {
		this.src = sourceClass.getAnnotation(MatrixSource.class);
		
		if (this.src == null) {
			throw new MatrixIndexingException(
					"Cannot index the source: " + sourceClass.getCanonicalName() +
					" because it isn't annotated with a " + MatrixSource.class.getName() +
					" annotation for the source class."
				);
		}

		this.sourceClass = sourceClass;
		this.dimension = Arrays.stream(src.domainConfig()).map((c) -> {
				D domainIdt = (c.i_domain() == -1) ?
						(D) Reflect.enumValueOf(src.domainEnum(), c.domain())
					:
						((D[]) Reflect.enumValues(src.domainEnum()))[c.i_domain()];
				return new SimpleEntry<D, EnumMap<M, M_Dimension<D, M>>>(
						domainIdt,
						Arrays.stream((M[]) Reflect.enumValues(c.modelEnum())).map(
								(e) -> new DimensionConfig<D, M>(c, domainIdt, e)
						).collect(Collectors.toMap(
							DimensionConfig::model,
							(dConf) -> new M_Dimension<>(dConf, this),
							(l, r) -> l,
							() -> new EnumMap<M, M_Dimension<D, M>>(((Class<M>) c.modelEnum()))
						))
					);
			}).collect(Collectors.toMap(
				SimpleEntry::getKey,
				SimpleEntry::getValue,
				(l, r) -> l,
				() -> new EnumMap<D, EnumMap<M, M_Dimension<D, M>>>((Class<D>) src.domainEnum())
			));
	}
	
	protected Class<?> getSourceClass() {
		return sourceClass;
	}
	
	protected MatrixSource getSource() {
		return src;
	}
}
