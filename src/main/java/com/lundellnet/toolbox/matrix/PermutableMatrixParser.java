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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import com.lundellnet.toolbox.matrix.collection.MatrixFacet;
import com.lundellnet.toolbox.matrix.collection.PermutationMatrixCollection;
import com.lundellnet.toolbox.matrix.collection.PermutationMatrixElement;
import com.lundellnet.toolbox.obj.parsers.DataParser;

public interface PermutableMatrixParser <T, R>
		extends
			DataParser<MatrixFacet<Object, Object>, PermutationMatrixCollection<T, R>, PermutationMatrixElement<Object, Object>, R>
{
	@Override
	default BiFunction<PermutationMatrixCollection<T, R>, MatrixFacet<Object, Object>, PermutationMatrixElement<Object, Object>> elementInit() {
		return (col, facet) -> col.create(facet.elementConf());
	}

	@Override
	default BiFunction<PermutationMatrixCollection<T, R>, MatrixFacet<Object, Object>, PermutationMatrixElement<Object, Object>> identifier() {
		return (col, facet) -> col.identify(facet.identity());
	}

	@Override
	default BiConsumer<PermutationMatrixElement<Object, Object>, MatrixFacet<Object, Object>> accepter() {
		return (element, facet) -> element.set(facet);
	}

	@Override
	default BinaryOperator<PermutationMatrixElement<Object, Object>> checker() {
		return (e1, e2) -> e1;
	}

	@Override
	default Function<PermutationMatrixCollection<T, R>, R> finisher() {
		return (col) -> col.getData();
	}
}
