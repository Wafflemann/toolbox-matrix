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

import com.lundellnet.toolbox.matrix.precedents.MatrixAnnotationPrecedent;
import com.lundellnet.toolbox.obj.data_access.DataAccessTools;
import com.lundellnet.toolbox.obj.data_access.DataPoint;
import com.lundellnet.toolbox.obj.data_access.configs.DataAccessConf;
import com.lundellnet.toolbox.obj.elements.compilation.ElementBuilder;

public class PermutationElementConfig <I, O>
		implements DataAccessConf<MatrixFacet<I, O>, O>
{
	public interface PermutationElementBuilder <I, O>
			extends ElementBuilder<PermutationElementConfig<I, O>, PermutationMatrixElement<I, O>>
	{}
	
	private final DataPoint<MatrixFacet<I, O>, O> dataPoint;

	PermutationElementConfig(MatrixFacet<I, O> facet, MatrixAnnotationPrecedent<MatrixFacet<I, O>, O> precedent) {
		this.dataPoint = DataAccessTools.<MatrixFacet<I, O>, O> convertingDataPointObjectBuilder().build(
				facet.parentClass(), facet.parentSupplier(), facet.identity(), precedent.applicant()
			);
	}
	
	@Override
	public DataPoint<MatrixFacet<I, O>, O> dataPoint() {
		return dataPoint;
	}
}
