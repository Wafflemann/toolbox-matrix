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

import com.lundellnet.toolbox.matrix.collection.PermutationElementConfig;
import com.lundellnet.toolbox.obj.collections.configs.DataCollectionConf;
import com.lundellnet.toolbox.obj.data_containers.CollectionContainer;
import com.lundellnet.toolbox.obj.data_containers.MapCollectionContainer;

public class PermutationCollectionConfig <T, R>
	implements DataCollectionConf<R, PermutationMatrixElement<Object, Object>, PermutationElementConfig.PermutationElementBuilder<Object, Object>>
{
	private final Class<R> resultClass;
	private final Supplier<R> resultSupplier;
	private final CollectionContainer<Field, PermutationMatrixElement<Object, Object>> container;

	public PermutationCollectionConfig(Class<R> resultClass, Supplier<R> resultSupplier) {
		this.resultClass = resultClass;
		this.resultSupplier = resultSupplier;
		this.container = MapCollectionContainer.<Field, PermutationMatrixElement<Object, Object>>builder().build((e) -> e.conf().dataPoint().field());
	}

	@Override
	public PermutationElementConfig.PermutationElementBuilder<Object, Object> elementBuilder() {
		return PermutationMatrixElement.builder();
	}

	@Override
	public CollectionContainer<Field, PermutationMatrixElement<Object, Object>> collectionStream() {
		return container;
	}

	@Override
	public Class<R> resultClass() {
		return resultClass;
	}

	@Override
	public Supplier<R> resultSupplier() {
		return resultSupplier;
	}
}
