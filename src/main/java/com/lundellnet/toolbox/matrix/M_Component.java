package com.lundellnet.toolbox.matrix;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

import com.lundellnet.toolbox.api.data_access.annotations.MatrixComponent;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixField;
import com.lundellnet.toolbox.matrix.collection.MatrixFacet;

public class M_Component <D extends Enum<D>, M extends Enum<M>> {
	
	private final M_Dimension dimension;
	private final MatrixField mField;
	private final MatrixComponent mComponent;
	private final Class<?> compClass;
	private final Field[] fields;
	private final MatrixProperties props;
	
	M_Component(M_Dimension<D, M> dimension, MatrixField mField, MatrixComponent mComponent, Class<?> compClass, Field[] fields) {
		this.dimension = dimension;
		this.mField = mField;
		this.mComponent = mComponent;
		this.compClass = compClass;
		this.fields = fields;
		this.props = new MatrixProperties(dimension.getConfiguration(), dimension.props(), mComponent);
	}
	
	public Class<?> componentClass() {
		return compClass;
	}
	
	public Stream<MatrixFacet<?, ?>> streamFacets() {
		for (Field field : fields) {
			
		}
	}
	
	public MatrixProperties props() {
		return props;
	}
}
