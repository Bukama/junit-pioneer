/*
 * Copyright 2016-2021 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junitpioneer.jupiter;

import static org.junitpioneer.internal.PioneerUtils.wrap;

import java.util.List;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

class CartesianProductResolver implements ParameterResolver {

	private final List<?> parameters;

	CartesianProductResolver(List<?> parameters) {
		this.parameters = parameters;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		// the extension only supports injecting parameters into methods (not constructors, for example)
		boolean isTestMethod = extensionContext.getTestMethod().isPresent();
		if (!isTestMethod)
			return false;

		boolean parameterInRange = parameterContext.getIndex() < parameters.size();
		if (!parameterInRange)
			return false;

		Object parameter = parameters.get(parameterContext.getIndex());
		// need to go from primitives to wrapper class or `isAssignableFrom` returns false for primitive parameters
		Class<?> parameterClass = wrap(parameterContext.getParameter().getType());
		// parameter with correct type
		return parameterClass.isAssignableFrom(parameter.getClass());
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameters.get(parameterContext.getIndex());
	}

}
