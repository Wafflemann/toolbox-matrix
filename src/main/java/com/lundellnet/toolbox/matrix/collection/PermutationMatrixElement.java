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

import com.lundellnet.toolbox.obj.data_access.configurables.ConvertingDataAccess;
import com.lundellnet.toolbox.obj.elements.AnnotatedElement;

public interface PermutationMatrixElement <I, O>
	extends
		AnnotatedElement<MatrixFacet<I, O>, O, PermutationElementConfig<I, O>>,
		ConvertingDataAccess<MatrixFacet<I, O>, O, PermutationElementConfig<I, O>>
{
	public static <I, O> PermutationElementConfig.PermutationElementBuilder<I, O> builder() {
		return PermutationMatrixElement::createElement;
	}
	
	public static <I, O> PermutationMatrixElement<I, O> createElement(PermutationElementConfig<I, O> elementConf) {
		return () -> elementConf;
	}
}
