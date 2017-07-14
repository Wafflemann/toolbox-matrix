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

import com.lundellnet.toolbox.obj.collections.configurables.CreatingCollection;
import com.lundellnet.toolbox.obj.collections.configurables.DataCollection;

public interface PermutationMatrixCollection <T, R>
	extends
		DataCollection<PermutationCollectionConfig<T, R>, R, PermutationMatrixElement<Object, Object>>,
		CreatingCollection<PermutationCollectionConfig<T, R>, R, Field, PermutationElementConfig<Object, Object>, PermutationMatrixElement<Object, Object>>
{
	
	@Override
	default PermutationMatrixElement<Object, Object> create(PermutationElementConfig<Object, Object> elementConf) {
		return conf().elementBuilder().build(elementConf);
	}
	
	@Override
	default PermutationMatrixElement<Object, Object> identify(Field field) {
		return conf().collectionStream().retreiveElement(field);
	}
}
