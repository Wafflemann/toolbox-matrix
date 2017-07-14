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
package com.lundellnet.toolbox.matrix.exceptions;

public class MatrixIndexingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4675321782468937391L;

	public MatrixIndexingException() {}
	
	public MatrixIndexingException(String msg) {
		super(msg);
	}
	
	public MatrixIndexingException(Throwable ex) {
		super(ex);
	}
	
	public MatrixIndexingException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
