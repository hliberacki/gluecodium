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

package com.here.gluecodium.model.java

import com.here.gluecodium.model.common.Comments
import com.here.gluecodium.model.common.ModelElement
import com.here.gluecodium.model.common.PseudoStreamable
import java.util.LinkedHashSet

abstract class JavaElement(val name: String) : PseudoStreamable<JavaElement>(), ModelElement {
    var comment = Comments()
    var visibility = JavaVisibility.PACKAGE
    val annotations: MutableSet<JavaType> = LinkedHashSet()

    override fun toString() = name

    override fun stream(): List<JavaElement> = annotations.toList()
}
