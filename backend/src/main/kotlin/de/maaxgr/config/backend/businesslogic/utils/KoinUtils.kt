package de.maaxgr.config.backend.businesslogic.utils

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.mp.KoinPlatformTools

//
// Get Koin-Injection everywhere without need of specifying "implements KoinComponent"
// See Restriction: https://github.com/InsertKoinIO/koin/issues/129
//

inline fun <reified T : Any> get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    return getKoin().get(qualifier, parameters)
}

/**
 * Lazy inject instance from Koin
 * @param qualifier
 * @param mode - LazyThreadSafetyMode
 * @param parameters
 */
inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy(mode) { get(qualifier, parameters) }
