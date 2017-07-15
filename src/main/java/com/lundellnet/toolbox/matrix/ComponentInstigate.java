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

import java.util.Map;

import com.lundellnet.toolbox.api.data_access.annotations.MatrixComponent;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixField;

class ComponentInstigate <P extends Enum<P>> {

	private final MatrixComponent mComponent;
	private final MatrixField providerField;
	private final Class<?> compProvider;
	private final Class<P> providerPlaneEnum;
	private final Object providerCompScope;
	private final Map<P, Object> providerPlanes;
	private final ComponentMappingType mappingType;
	
	MatrixComponent mComponent() {
		return mComponent;
	}
	
	MatrixField mField() {
		return providerField;
	}
	
	Class<?> component() {
		return compProvider;
	}
	
	Class<P> planeEnum() {
		return providerPlaneEnum;
	}
	
	Object componentScope() {
		return providerCompScope;
	}
	
	Map<P, Object> planes() {
		return providerPlanes;
	}
	
	ComponentMappingType mappingType() {
		return mappingType;
	}
}
