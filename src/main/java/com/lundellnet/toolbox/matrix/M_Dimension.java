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
		private List<SimpleEntry<Class<?>, M_Component<D, M>>> mComponents;
		private List<SimpleEntry<Field, M_Field>> mFields;
		private List<SimpleEntry<Field, M_Mapping>> mMappings;
		
		List<SimpleEntry<Class<?>, M_Component<D, M>>> components() {
			if (mComponents == null) mComponents = new ArrayList<>();
			
			return mComponents;
		}
		
		List<SimpleEntry<Field, M_Field>> fields() {
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
	
	private static <D extends Enum<D>, M extends Enum<M>> void crawlComponents(
			M_Dimension<D, M> dimension, CrawlData<D, M> data, MatrixField compField, Class<?> compClass
	) {
		MatrixComponent comp = compClass.getAnnotation(MatrixComponent.class);
		
		if (comp == null) {
			throw new MatrixIndexingException(
				"Could not process Component: " + compClass.getName() +
				" because it has no class-level MatrixComponent annotation."
			);
		}
		
		Field[] fields = (comp.span() == void.class) ?
				Reflect.getDeclaredFields(compClass)
			:
				Reflect.getAllDeclaredFields(compClass, comp.span());
				
		data.components().add(new SimpleEntry<>(compClass, new M_Component<>(dimension, compField, comp, compClass, fields)));

		for (Field field : fields) {
			MatrixField mField = field.getAnnotation(MatrixField.class);
			
			if (mField == null) break;

			crawlComponents(dimension, data, mField, field.getType());
		}
	}
	
	private static <D extends Enum<D>, M extends Enum<M>> void processField(
			M_Dimension<D, M> dimension, CrawlData<D, M> data, Field[] fields
	) {
		int i = 0;
		fLoop: while (i < fields.length) {
			
			List<Annotation> annotations = Arrays.stream(fields[i].getAnnotations())
				.filter((f) -> CONCERNED_FIELD_ANNOTATIONS.contains(f.annotationType()))
				.collect(Collectors.toList());
			
			if (annotations.size() > 1) {
				//TODO
			}
			
			Annotation annotation = annotations.get(0);
			
			switch (FIELD_ANNOTATION_PARSING_STEP.get(annotation)) {
				case MAP:
					data.mappings().add(new SimpleEntry<>(fields[i], new M_Mapping()));
				break;
				case TRANSVERSE:
					data.fields().add(new SimpleEntry<>(fields[i], new M_Field()));
					
					
					
					crawlComponents(dimension, data, null, fields[i].getType());
				break;
				default:
					break fLoop;
			}
			
			i++;
		}
	}
	
	private final DimensionConfig<D, M> conf;
	private final Map<Class<?>, M_Component<D, M>> components;
	private final MatrixProperties props;
	
	M_Dimension(DimensionConfig<D, M> conf, M_Source<D, M> mSource) {
		MatrixSource src = mSource.getSource();
		
		Field[] fields = (src.span() == void.class) ?
				Reflect.getDeclaredFields(mSource.getSourceClass())
			:
				Reflect.getAllDeclaredFields(mSource.getSourceClass(), src.span());
		CrawlData<D, M> data = new CrawlData<>();
		
		for (Field field : fields) {
			MatrixField mField = field.getAnnotation(MatrixField.class);
			
			if (mField == null) break;
			
			crawlComponents(this, data, mField, field.getType());
		}
		
		this.conf = conf;
		this.components = null; //entryList.stream().collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
		this.props = new MatrixProperties(conf, mSource.getSource());
	}
	
	MatrixProperties props() {
		return props;
	}
	
	public DimensionConfig<D, M> getConfiguration() {
		return conf;
	}
	
	
}
