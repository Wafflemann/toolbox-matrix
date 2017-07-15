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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lundellnet.toolbox.Reflect;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixComponent;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixField;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixFields;
import com.lundellnet.toolbox.api.data_access.annotations.MatrixSource;
import com.lundellnet.toolbox.api.data_access.annotations.PointMapping;
import com.lundellnet.toolbox.api.data_access.annotations.PointMappings;
import com.lundellnet.toolbox.matrix.exceptions.MatrixIndexingException;

public class M_Dimension <D extends Enum<D>, M extends Enum<M>> {
	
	private static class CrawlData <D extends Enum<D>, M extends Enum<M>> {
		private List<SimpleEntry<Class<?>, M_Component<D, M, ?>>> mComponents;
		private List<SimpleEntry<Field, M_Field<?, ?>>> mFields;
		private List<SimpleEntry<Field, M_Mapping>> mMappings;
		
		List<SimpleEntry<Class<?>, M_Component<D, M, ?>>> components() {
			if (mComponents == null) mComponents = new ArrayList<>();
			
			return mComponents;
		}
		
		List<SimpleEntry<Field, M_Field<?, ?>>> fields() {
			if (mFields == null) mFields = new ArrayList<>();
			
			return mFields;
		}
		
		List<SimpleEntry<Field, M_Mapping>> mappings() {
			if (mMappings == null) mMappings = new ArrayList<>();
			
			return mMappings;
		}
	}

	private static final List<Class<? extends Annotation>> CONCERNED_FIELD_ANNOTATIONS;
	static {
		CONCERNED_FIELD_ANNOTATIONS = new ArrayList<>();
		
		CONCERNED_FIELD_ANNOTATIONS.add(MatrixFields.class);
		CONCERNED_FIELD_ANNOTATIONS.add(MatrixField.class);
		CONCERNED_FIELD_ANNOTATIONS.add(PointMappings.class);
		CONCERNED_FIELD_ANNOTATIONS.add(PointMapping.class);
	}
	private static final Map<Class<? extends Annotation>, ParsingStep> FIELD_ANNOTATION_PARSING_STEP = CONCERNED_FIELD_ANNOTATIONS.stream()
			.map((a) -> new SimpleEntry<Class<? extends Annotation>, ParsingStep>(a, ParsingStep.fromValue(a)))
			.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	
	private static Field[] getComponentFields(Class<?> compClass, Class<?> spanClass) {
		return (spanClass == void.class) ?
				Reflect.getDeclaredFields(compClass)
			:
				Reflect.getAllDeclaredFields(compClass, spanClass);
	}
	
	private static <D extends Enum<D>, M extends Enum<M>> void crawlComponents(
			M_Dimension<D, M> dimension, CrawlData<D, M> data, List<MatrixField> mCompFields, Class<?> compClass
	) {
		MatrixComponent comp = compClass.getAnnotation(MatrixComponent.class);
		
		if (comp == null) {
			throw new MatrixIndexingException(
				"Could not process Component: " + compClass.getName() +
				" because it has no class-level MatrixComponent annotation."
			);
		}
		
		Field[] fields = getComponentFields(compClass, comp.span());
		M_Component<D, M, ?> mComponent = new M_Component<>(dimension, mCompFields, comp, compClass, fields); 
		
		data.components().add(new SimpleEntry<>(compClass, mComponent));
		processFields(dimension, data, mComponent, fields);
	}
	
	@SuppressWarnings("unchecked")
	private static <D extends Enum<D>, M extends Enum<M>> void processFields(
			M_Dimension<D, M> dimension, CrawlData<D, M> data, M_Component<?, ?, ?> mComponent, Field[] fields
	) {
		int i = 0;
		fLoop: while (i < fields.length) {
			
			List<Annotation> annotations = Arrays.stream(fields[i].getAnnotations())
				.filter((f) -> CONCERNED_FIELD_ANNOTATIONS.contains(f.annotationType()))
				.collect(Collectors.toList());
			
			if (annotations.size() > 1) {
				//TODO there can only be one of those types
			}
			
			Annotation annotation = annotations.get(0);
			ParsingStep step = FIELD_ANNOTATION_PARSING_STEP.get(annotation);
			switch (step) {
				case MAP:
					List<PointMapping> mMappings = (List<PointMapping>) step.dimensionMatcher().apply(annotation, dimension.getConfiguration());
					
					data.mappings().add(new SimpleEntry<>(
						fields[i],
						new M_Mapping<>(
							mComponent,
							new FieldConfig(step, annotation, fields[i]),
							fields[i],
							(List<PointMapping>) step.dimensionMatcher().apply(annotation, dimension.getConfiguration())
					)));
				break;
				case TRANSVERSE:
					List<MatrixField> mFields = (List<MatrixField>) step.dimensionMatcher().apply(annotation, dimension.getConfiguration());
					
					data.fields().add(new SimpleEntry<>(
						fields[i],
						new M_Field<>(
							mComponent,
							new FieldConfig(step, annotation, fields[i]),
							fields[i],
							mFields
					)));
					crawlComponents(dimension, data, mFields, fields[i].getType());
				break;
				default:
					break fLoop;
			}
			
			i++;
		}
	}
	
	private final DimensionConfig<D, M> conf;
	private final Map<Class<?>, M_Component<D, M, ?>> componentDataMap;
	private final Map<Field, M_Field<?, ?>> fieldDataMap;
	private final MatrixProperties props;
	
	private Object dataSrc;
	
	M_Dimension(DimensionConfig<D, M> conf, M_Source<D, M> mSource) {
		MatrixSource src = mSource.getSource();
		Field[] fields = getComponentFields(mSource.getSourceClass(), src.span());
		CrawlData<D, M> data = new CrawlData<>();
		
		processFields(this, data, null, fields);
		
		this.conf = conf;		//where the combining will happen
		this.componentDataMap = null; //entryList.stream().collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
		this.fieldDataMap = null;
		this.props = new MatrixProperties(conf, mSource.getSource());
	}
	
	M_Component<D, M, ?> getMatixComponentData(Class<?> componentType) {
		M_Component<D, M, ?> mComponent = componentDataMap.get(componentType);
		
		if (mComponent == null) {
			//TODO
		}
		
		return mComponent;
	}
	
	M_Field<?, ?> getMatrixFieldData(Field field) {
		M_Field<?, ?> mField = fieldDataMap.get(field);
		
		if (mField == null) {
			//TODO hasn't been indexed
		}
		
		return mField;
	}
	
	Object getRootScope() {
		return null;//TODO
	}
	
	MatrixProperties props() {
		return props;
	}
	
	public M_Dimension<D, M> setDataSource(Object dataSrc) {
		this.dataSrc = dataSrc;
		
		return this;
	}
	
	public DimensionConfig<D, M> getConfiguration() {
		return conf;
	}
}
