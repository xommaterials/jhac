package com.sap.hybris.hac.impex.chunk;

import com.sap.hybris.hac.impex.Impex;

import java.util.function.Function;

/**
 * Strategy chunking a given {@link Impex}.
 *
 * @author Klaus Hauschild
 */
public interface Strategy extends Function<Impex, Impex[]> {}
