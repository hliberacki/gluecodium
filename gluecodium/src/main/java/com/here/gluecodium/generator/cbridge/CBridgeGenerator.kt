/*
 * Copyright (C) 2016-2019 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE
 */

package com.here.gluecodium.generator.cbridge

import com.here.gluecodium.common.LimeTypeRefsVisitor
import com.here.gluecodium.generator.cbridge.CBridgeNameRules.CBRIDGE_INTERNAL
import com.here.gluecodium.generator.cbridge.CBridgeNameRules.CBRIDGE_PUBLIC
import com.here.gluecodium.generator.common.GeneratedFile
import com.here.gluecodium.generator.common.GeneratorSuite
import com.here.gluecodium.generator.common.NameResolver
import com.here.gluecodium.generator.common.templates.TemplateEngine
import com.here.gluecodium.generator.cpp.Cpp2IncludeResolver
import com.here.gluecodium.generator.cpp.Cpp2NameResolver
import com.here.gluecodium.generator.cpp.CppFullNameResolver
import com.here.gluecodium.generator.cpp.CppNameResolver
import com.here.gluecodium.generator.cpp.CppNameRules
import com.here.gluecodium.generator.swift.SwiftNameRules
import com.here.gluecodium.model.common.Include
import com.here.gluecodium.model.lime.LimeContainer
import com.here.gluecodium.model.lime.LimeContainerWithInheritance
import com.here.gluecodium.model.lime.LimeElement
import com.here.gluecodium.model.lime.LimeEnumeration
import com.here.gluecodium.model.lime.LimeGenericType
import com.here.gluecodium.model.lime.LimeLambda
import com.here.gluecodium.model.lime.LimeList
import com.here.gluecodium.model.lime.LimeMap
import com.here.gluecodium.model.lime.LimeNamedElement
import com.here.gluecodium.model.lime.LimeSet
import com.here.gluecodium.model.lime.LimeStruct
import com.here.gluecodium.model.lime.LimeType
import com.here.gluecodium.model.lime.LimeTypeHelper
import com.here.gluecodium.model.lime.LimeTypeRef
import java.nio.file.Paths

internal class CBridgeGenerator(
    limeReferenceMap: Map<String, LimeElement>,
    rootNamespace: List<String>,
    cachingNameResolver: CppNameResolver,
    private val internalNamespace: List<String>,
    swiftNameRules: SwiftNameRules,
    internalPrefix: String?,
    cppNameRules: CppNameRules
) {
    private val nameResolver = CBridgeNameResolver(limeReferenceMap, swiftNameRules, internalPrefix ?: "")
    private val cppNameResolver = Cpp2NameResolver(limeReferenceMap, internalNamespace, cachingNameResolver)
    private val cppRefNameResolver =
        CBridgeCppNameResolver(limeReferenceMap, CppFullNameResolver(cachingNameResolver), cppNameResolver)
    private val headerIncludeResolver = CBridgeHeaderIncludeResolver(limeReferenceMap, rootNamespace)
    private val cppIncludeResolver = Cpp2IncludeResolver(limeReferenceMap, cppNameRules, internalNamespace)
    private val implIncludeResolver = CBridgeImplIncludeResolver(rootNamespace, cppIncludeResolver)
    private val predicates = CBridgeGeneratorPredicates(cppNameResolver).predicates

    fun generate(rootElement: LimeNamedElement): List<GeneratedFile> {
        val templateData = mutableMapOf("model" to rootElement, "internalNamespace" to internalNamespace)
        val nameResolvers = mapOf<String, NameResolver>("" to nameResolver, "C++" to cppRefNameResolver)

        val headerFilePath = headerIncludeResolver.getHeaderFilePath(rootElement)
        val selfInclude = Include.createInternalInclude(headerFilePath)
        templateData["includes"] = headerIncludeResolver.resolveIncludes(rootElement).distinct().sorted() - selfInclude
        templateData["contentTemplate"] = (selectHeaderTemplate(rootElement) ?: return emptyList())
        val headerFileContent = TemplateEngine.render("cbridge/CBridgeHeader", templateData, nameResolvers, predicates)
        val headerFile = GeneratedFile(headerFileContent, headerFilePath)

        templateData["includes"] =
            listOf(selfInclude) + implIncludeResolver.resolveIncludes(rootElement).distinct().sorted()
        templateData["contentTemplate"] = (selectImplTemplate(rootElement) ?: return listOf(headerFile))
        val implFileContent =
            TemplateEngine.render("cbridge/CBridgeImplementation", templateData, nameResolvers, predicates)
        val implFile = GeneratedFile(implFileContent, implIncludeResolver.getImplFilePath(rootElement))

        return listOf(headerFile, implFile)
    }

    fun generateCollections(limeModel: List<LimeNamedElement>): List<GeneratedFile> {
        val allTypes = limeModel.flatMap { LimeTypeHelper.getAllTypes(it) }
        val allParentTypes = getAllParentTypes(allTypes)
        val genericTypes = GenericTypesCollector(nameResolver).getAllGenericTypes(allTypes + allParentTypes)
        val templateData = mutableMapOf<String, Any>(
            "lists" to genericTypes.filterIsInstance<LimeList>(),
            "maps" to genericTypes.filterIsInstance<LimeMap>(),
            "sets" to genericTypes.filterIsInstance<LimeSet>(),
            "internalNamespace" to internalNamespace
        )
        val nameResolvers = mapOf<String, NameResolver>("" to nameResolver, "C++" to cppRefNameResolver)

        templateData["includes"] =
            genericTypes.flatMap { headerIncludeResolver.resolveIncludes(it) }.distinct().sorted()
        val headerFileContent =
            TemplateEngine.render("cbridge/CBridgeCollectionsHeader", templateData, nameResolvers, predicates)
        val headerFile = GeneratedFile(headerFileContent, CBRIDGE_COLLECTIONS_HEADER)

        templateData["includes"] = listOf(Include.createInternalInclude(CBRIDGE_COLLECTIONS_HEADER)) +
            (genericTypes.flatMap { implIncludeResolver.resolveIncludes(it) } +
                    CBridgeImplIncludeResolver.BASE_HANDLE_IMPL_INCLUDE +
                    cppIncludeResolver.optionalInclude).distinct().sorted()
        val implFileContent =
            TemplateEngine.render("cbridge/CBridgeCollectionsImpl", templateData, nameResolvers, predicates)
        val implFile = GeneratedFile(implFileContent, CBRIDGE_COLLECTIONS_IMPL)

        return listOf(headerFile, implFile)
    }

    fun generateHelpers() =
        listOf(
            generateHelperContent("BaseHandleImpl", BASE_HANDLE_IMPL_FILE),
            generateHelperContent(
                "StringHandle", Paths.get(CBRIDGE_PUBLIC, SRC_DIR, "StringHandle.cpp").toString()),
            generateHelperContent(
                "ByteArrayHandle",
                Paths.get(CBRIDGE_PUBLIC, SRC_DIR, "ByteArrayHandle.cpp").toString()
            ),
            generateHelperContent(
                "LocaleHandle",
                Paths.get(CBRIDGE_PUBLIC, SRC_DIR, "LocaleHandle.cpp").toString()
            ),
            generateHelperContent(
                "DateHandle",
                Paths.get(CBRIDGE_PUBLIC, SRC_DIR, "DateHandle.cpp").toString()
            ),
            generateHelperContent(
                "BuiltinHandle",
                Paths.get(CBRIDGE_PUBLIC, SRC_DIR, "BuiltinHandle.cpp").toString()
            ),
            generateHelperContent("TypeInitRepository",
                Paths.get(CBRIDGE_INTERNAL, INCLUDE_DIR, "TypeInitRepository.h").toString()
            ),
            generateHelperContent(
                "TypeInitRepositoryImpl",
                Paths.get(CBRIDGE_PUBLIC, SRC_DIR, "TypeInitRepository.cpp").toString()
            ),
            generateHelperContent("WrapperCacheHeader", Paths.get(
                CBRIDGE_INTERNAL, INCLUDE_DIR, "WrapperCache.h"
            ).toString()
            ),
            generateHelperContent(
                "WrapperCacheImpl",
                Paths.get(CBRIDGE_PUBLIC, SRC_DIR, "WrapperCache.cpp").toString()
            )
        )

    private fun generateHelperContent(template: String, path: String): GeneratedFile {
        val content = TemplateEngine.render(
            "cbridge/common/$template",
            mapOf("internalNamespace" to internalNamespace)
        )
        return GeneratedFile(content, path, GeneratedFile.SourceSet.COMMON)
    }

    private fun selectHeaderTemplate(limeElement: LimeNamedElement) =
        when (limeElement) {
            is LimeStruct -> "cbridge/CBridgeStructHeader"
            is LimeContainer -> "cbridge/CBridgeClassHeader"
            is LimeLambda -> "cbridge/CBridgeLambdaHeader"
            is LimeEnumeration -> "cbridge/CBridgeEnumeration"
            else -> null
        }

    private fun selectImplTemplate(limeElement: LimeNamedElement) =
        when (limeElement) {
            is LimeStruct -> "cbridge/CBridgeStructImpl"
            is LimeContainer -> "cbridge/CBridgeClassImpl"
            is LimeLambda -> "cbridge/CBridgeLambdaImpl"
            is LimeEnumeration -> null
            else -> null
        }

    private fun getAllParentTypes(allTypes: List<LimeType>): List<LimeType> {
        if (allTypes.isEmpty()) return emptyList()
        val parents = allTypes.filterIsInstance<LimeContainerWithInheritance>().mapNotNull { it.parent?.type }
        return parents + getAllParentTypes(parents)
    }

    private class GenericTypesCollector(private val nameResolver: CBridgeNameResolver) :
        LimeTypeRefsVisitor<List<LimeGenericType>>() {

        override fun visitTypeRef(parentElement: LimeNamedElement, limeTypeRef: LimeTypeRef?): List<LimeGenericType> {
            val limeType = limeTypeRef?.type?.actualType as? LimeGenericType ?: return emptyList()
            return listOf(limeType) + when (limeType) {
                is LimeList -> visitTypeRef(parentElement, limeType.elementType)
                is LimeSet -> visitTypeRef(parentElement, limeType.elementType)
                is LimeMap -> visitTypeRef(parentElement, limeType.keyType) +
                        visitTypeRef(parentElement, limeType.valueType)
                else -> emptyList()
            }
        }

        fun getAllGenericTypes(allTypes: List<LimeType>) =
            traverseTypes(allTypes).flatten().associateBy { nameResolver.resolveName(it) }.toSortedMap().values
    }

    companion object {
        private const val SRC_DIR = "src"
        private const val INCLUDE_DIR = "include"

        private val CBRIDGE_COLLECTIONS_HEADER =
            Paths.get(CBRIDGE_PUBLIC, INCLUDE_DIR, "GenericCollections.h").toString()
        private val CBRIDGE_COLLECTIONS_IMPL = Paths.get(CBRIDGE_PUBLIC, SRC_DIR, "GenericCollections.cpp").toString()
        private val BASE_HANDLE_IMPL_FILE = Paths.get(CBRIDGE_INTERNAL, INCLUDE_DIR, "BaseHandleImpl.h").toString()

        private val BASE_HANDLE_FILE = Paths.get(CBRIDGE_PUBLIC, INCLUDE_DIR, "BaseHandle.h").toString()
        private val STRING_HANDLE_FILE = Paths.get(CBRIDGE_PUBLIC, INCLUDE_DIR, "StringHandle.h").toString()
        private val DATE_HANDLE_FILE = Paths.get(CBRIDGE_PUBLIC, INCLUDE_DIR, "DateHandle.h").toString()
        private val EXPORT_FILE = Paths.get(CBRIDGE_PUBLIC, INCLUDE_DIR, "Export.h").toString()
        private val PROXY_CACHE_FILENAME = Paths.get(CBRIDGE_INTERNAL, INCLUDE_DIR, "CachedProxyBase.h").toString()

        val STATIC_FILES = listOf(
            GeneratorSuite.copyCommonFile(BASE_HANDLE_FILE, ""),
            GeneratorSuite.copyCommonFile(STRING_HANDLE_FILE, ""),
            GeneratorSuite.copyCommonFile(DATE_HANDLE_FILE, ""),
            GeneratorSuite.copyCommonFile(EXPORT_FILE, ""),
            GeneratorSuite.copyCommonFile(Paths.get(CBRIDGE_PUBLIC, INCLUDE_DIR, "BuiltinHandle.h").toString(), ""),
            GeneratorSuite.copyCommonFile(Paths.get(CBRIDGE_PUBLIC, INCLUDE_DIR, "ByteArrayHandle.h").toString(), ""),
            GeneratorSuite.copyCommonFile(Paths.get(CBRIDGE_PUBLIC, INCLUDE_DIR, "LocaleHandle.h").toString(), ""),
            GeneratorSuite.copyCommonFile(PROXY_CACHE_FILENAME, "")
        )
    }
}
