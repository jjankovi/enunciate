/**
 * Copyright © 2006-2016 Web Cohesion (info@webcohesion.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webcohesion.enunciate.modules.swagger;

import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.util.freemarker.FreemarkerUtil;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * Template method used to determine the objective-c "simple name" of an accessor.
 *
 * @author Ryan Heaton
 */
public class ReferencedDatatypeNameForMethod implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  public Object exec(List list) throws TemplateModelException {
    if (list.size() < 1) {
      throw new TemplateModelException("The datatypeNameFor method must have a parameter.");
    }

    TemplateModel from = (TemplateModel) list.get(0);
    Object unwrapped = FreemarkerUtil.unwrap(from);

    if (!DataTypeReference.class.isAssignableFrom(unwrapped.getClass())) {
      throw new TemplateModelException("No referenced data type name for: " + unwrapped);
    }
    DataTypeReference reference = DataTypeReference.class.cast(unwrapped);

    BaseType baseType = reference.getBaseType();
    String format = reference.getBaseTypeFormat();

    String defaultType = "file";
    if (list.size() > 1) {
      defaultType = FreemarkerUtil.unwrap((TemplateModel) list.get(1)).toString();
    }

    switch (baseType) {
      case bool:
        return "boolean";
      case number:
        if ("int32".equals(format) || "int64".equals(format)) {
          return "integer";
        }
        else {
          return "number";
        }
      case string:
        return "string";
      default:
        return defaultType;
    }
  }
}