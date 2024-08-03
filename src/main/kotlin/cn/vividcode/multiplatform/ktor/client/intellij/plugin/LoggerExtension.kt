package cn.vividcode.multiplatform.ktor.client.intellij.plugin

import com.intellij.openapi.diagnostic.Logger
import kotlin.reflect.KClass

inline val <reified T : Any> T.logger: Logger
	get() = loggerCache.getOrPut(T::class) { Logger.getInstance(T::class.java) }

val loggerCache = mutableMapOf<KClass<out Any>, Logger>()