/**
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.xtext.template.tests.compiler;

import com.google.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.util.ReflectExtensions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtext.template.tests.TemplateInjectorProvider;

@RunWith(XtextRunner.class)
@InjectWith(TemplateInjectorProvider.class)
@SuppressWarnings("all")
public class IntegrationTest {
  @Inject
  @Extension
  private CompilationTestHelper _compilationTestHelper;
  
  @Inject
  @Extension
  private ReflectExtensions _reflectExtensions;
  
  @Test
  public void testParseAndCompile_01() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<!--<< >>-->");
      _builder.newLine();
      _builder.append("Hello World");
      _builder.newLine();
      String _replace = this.replace(_builder);
      final IAcceptor<CompilationTestHelper.Result> _function = new IAcceptor<CompilationTestHelper.Result>() {
        @Override
        public void accept(final CompilationTestHelper.Result it) {
          try {
            Class<?> _compiledClass = it.getCompiledClass();
            Object _newInstance = _compiledClass.newInstance();
            final Object result = IntegrationTest.this._reflectExtensions.invoke(_newInstance, "generate", null);
            String _string = result.toString();
            Assert.assertEquals("Hello World", _string);
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      this._compilationTestHelper.compile(_replace, _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void testParseAndCompile_02() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<!--<<");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("param name = \'Foo\'");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("param nullString");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("param String string");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("param list = #[\'one\', \'two\', \'three\', \'four\']");
      _builder.newLine();
      _builder.append(">>-->");
      _builder.newLine();
      _builder.append("<html><<nullString>>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<title><<name>>/<<nullString>>/<<string>></title>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<<FOR element : list>>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<<IF element == \'one\'>>");
      _builder.newLine();
      _builder.append("      ");
      _builder.append("<h1><<element>></h1>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<<ELSEIF element == \'two\'>>");
      _builder.newLine();
      _builder.append("      ");
      _builder.append("<h2><<element>></h2>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<<ELSE>>");
      _builder.newLine();
      _builder.append("      ");
      _builder.append("<p><<element>></p>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<<ENDIF>>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<<ENDFOR>>");
      _builder.newLine();
      _builder.append("</html>");
      _builder.newLine();
      String _replace = this.replace(_builder);
      final IAcceptor<CompilationTestHelper.Result> _function = new IAcceptor<CompilationTestHelper.Result>() {
        @Override
        public void accept(final CompilationTestHelper.Result it) {
          try {
            Class<?> _compiledClass = it.getCompiledClass();
            Object _newInstance = _compiledClass.newInstance();
            final Object result = IntegrationTest.this._reflectExtensions.invoke(_newInstance, "generate", null);
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("<html>");
            _builder.newLine();
            _builder.append("  ");
            _builder.append("<title>Foo//</title>");
            _builder.newLine();
            _builder.append("      ");
            _builder.append("<h1>one</h1>");
            _builder.newLine();
            _builder.append("      ");
            _builder.append("<h2>two</h2>");
            _builder.newLine();
            _builder.append("      ");
            _builder.append("<p>three</p>");
            _builder.newLine();
            _builder.append("      ");
            _builder.append("<p>four</p>");
            _builder.newLine();
            _builder.append("</html>");
            String _string = _builder.toString();
            String _string_1 = result.toString();
            Assert.assertEquals(_string, _string_1);
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      this._compilationTestHelper.compile(_replace, _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String replace(final CharSequence s) {
    String _string = s.toString();
    String _replace = _string.replace("<<", "«");
    return _replace.replace(">>", "»");
  }
}
