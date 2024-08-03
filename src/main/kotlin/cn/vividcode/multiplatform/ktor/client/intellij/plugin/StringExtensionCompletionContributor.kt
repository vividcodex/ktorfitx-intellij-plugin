package cn.vividcode.multiplatform.ktor.client.intellij.plugin

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.idea.KotlinIcons
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.isTypeParameter

/**
 * 项目：vividcode-multiplatform-ktor-client-intellij-plugin
 *
 * 作者：li-jia-wei
 *
 * 创建：2024/8/4 上午4:36
 *
 * 介绍：StringExtensionCompletionContributor
 */
class StringExtensionCompletionContributor : CompletionContributor() {
	
	init {
		extendTestMethod()
	}
	
	private fun extendTestMethod() {
		extend(
			CompletionType.BASIC,
			PlatformPatterns.psiElement().withLanguage(KotlinLanguage.INSTANCE),
			object : CompletionProvider<CompletionParameters>() {
				override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
					val parent = parameters.position.parent
					if (parent is KtNameReferenceExpression) {
						val dotQualifiedExpression = parent.parent as? KtDotQualifiedExpression ?: return
						val kotlinType = dotQualifiedExpression.receiverExpression.kotlinType ?: return
						if (kotlinType.isString()) {
							val element = LookupElementBuilder.create("testApi")
								.withIcon(KotlinIcons.FIELD_VAL)
								.withTypeText("TestApi")
							result.addElement(element)
						}
					}
				}
			}
		)
	}
	
	private val KtExpression.kotlinType: KotlinType?
		get() {
			val bindingContext = this.analyze(BodyResolveMode.PARTIAL)
			return bindingContext[BindingContext.EXPRESSION_TYPE_INFO, this]?.type
		}
	
	private fun KotlinType.isString(): Boolean {
		return !this.isTypeParameter() && this.constructor.declarationDescriptor?.fqNameSafe?.asString() == "kotlin.String"
	}
}