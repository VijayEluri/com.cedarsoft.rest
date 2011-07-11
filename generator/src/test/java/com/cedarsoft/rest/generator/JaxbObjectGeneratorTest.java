/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.rest.generator;

import com.cedarsoft.AssertUtils;
import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.MemoryCodeWriter;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.DomainObjectDescriptorFactory;
import com.cedarsoft.codegen.parser.Parser;
import com.cedarsoft.codegen.parser.Result;
import com.google.common.collect.ImmutableList;
import com.sun.mirror.declaration.FieldDeclaration;
import javax.annotation.Nonnull;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;


/**
 *
 */
public class JaxbObjectGeneratorTest {
  private DomainObjectDescriptor barDescriptor;
  private DomainObjectDescriptor userDescriptor;
  private DomainObjectDescriptor fooDescriptor;
  private DomainObjectDescriptor anotherModelDescriptor;
  private DomainObjectDescriptor groupDescriptor;
  private CodeGenerator codeGenerator;

  @Before
  public void setUp() throws Exception {
    List<File> files = ImmutableList.of(
      new File( getClass().getResource( "test/BarModel.java" ).toURI() ),
      new File( getClass().getResource( "test/FooModel.java" ).toURI() ),
      new File( getClass().getResource( "test/AnotherModel.java" ).toURI() ),
      new File( getClass().getResource( "test/Group.java" ).toURI() ),
      new File( getClass().getResource( "test/User.java" ).toURI() )
    );

    Result result = Parser.parse( null, files );

    barDescriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration( "com.cedarsoft.rest.generator.test.BarModel" ) ).create();
    userDescriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration( "com.cedarsoft.rest.generator.test.User" ) ).create();
    fooDescriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration( "com.cedarsoft.rest.generator.test.FooModel" ) ).create();
    anotherModelDescriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration( "com.cedarsoft.rest.generator.test.AnotherModel" ) ).create();
    groupDescriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration( "com.cedarsoft.rest.generator.test.Group" ) ).create();

    codeGenerator = new CodeGenerator( new JaxbObjectGenerator.StubDecisionCallback() );
  }

  @Test
  public void testConvertFoo() {
    Generator generator = new Generator( codeGenerator, fooDescriptor );
    assertEquals( "java.util.List<com.cedarsoft.rest.generator.test.BarModel>", fooDescriptor.findFieldDeclaration( "theBars" ).getType().toString() );
    assertEquals( "com.cedarsoft.rest.generator.test.BarModel", fooDescriptor.findFieldDeclaration( "singleBar" ).getType().toString() );

    assertEquals( "com.cedarsoft.rest.generator.test.jaxb.BarModel$Jaxb", generator.getJaxbModelType( fooDescriptor.findFieldDeclaration( "singleBar" ).getType() ).binaryName() );
    assertEquals( "java.util.List<com.cedarsoft.rest.generator.test.jaxb.BarModel$Jaxb>", generator.getJaxbModelType( fooDescriptor.findFieldDeclaration( "theBars" ).getType() ).binaryName() );

    assertEquals( "com.cedarsoft.rest.generator.test.jaxb.BarModel$Stub", generator.getJaxbModelType( fooDescriptor.findFieldDeclaration( "singleBar" ).getType(), true ).binaryName() );
    assertEquals( "java.util.List<com.cedarsoft.rest.generator.test.jaxb.BarModel$Stub>", generator.getJaxbModelType( fooDescriptor.findFieldDeclaration( "theBars" ).getType(), true ).binaryName() );
  }

  @Test
  public void testConvertAnother() {
    Generator generator = new Generator( codeGenerator, anotherModelDescriptor );
    FieldDeclaration barsDeclaration = anotherModelDescriptor.findFieldDeclaration( "theBars" );
    FieldDeclaration singleBarDeclaration = anotherModelDescriptor.findFieldDeclaration( "singleBar" );
    FieldDeclaration wildcardBarsDeclaration = anotherModelDescriptor.findFieldDeclaration( "wildcardBars" );
    FieldDeclaration wildcardStringsDeclaration = anotherModelDescriptor.findFieldDeclaration( "wildcardStrings" );
    FieldDeclaration integersDeclaration = anotherModelDescriptor.findFieldDeclaration( "integers" );


    assertTrue( generator.isProbablyOwnType( singleBarDeclaration.getType() ) );
    assertTrue( generator.isProbablyOwnType( barsDeclaration.getType() ) );
    assertTrue( generator.isProbablyOwnType( wildcardBarsDeclaration.getType() ) );
    assertFalse( generator.isProbablyOwnType( integersDeclaration.getType() ) );
    assertFalse( generator.isProbablyOwnType( wildcardStringsDeclaration.getType() ) );

    assertEquals( "java.util.List<? extends com.cedarsoft.rest.generator.test.BarModel>", wildcardBarsDeclaration.getType().toString() );

    assertEquals( "java.util.List<? extends java.lang.String>", wildcardStringsDeclaration.getType().toString() );
    assertEquals( "java.util.List<java.lang.Integer>", integersDeclaration.getType().toString() );
    assertEquals( "com.cedarsoft.rest.generator.test.BarModel", singleBarDeclaration.getType().toString() );
    assertEquals( "java.util.List<com.cedarsoft.rest.generator.test.BarModel>", barsDeclaration.getType().toString() );
    assertEquals( "java.util.List<? extends com.cedarsoft.rest.generator.test.BarModel>", wildcardBarsDeclaration.getType().toString() );

    assertEquals( "java.util.List<? extends java.lang.String>", generator.getJaxbModelType( wildcardStringsDeclaration.getType() ).fullName() );
    assertEquals( "java.util.List<java.lang.Integer>", generator.getJaxbModelType( integersDeclaration.getType() ).fullName() );
    assertEquals( "java.util.List<? extends com.cedarsoft.rest.generator.test.jaxb.BarModel.Jaxb>", generator.getJaxbModelType( wildcardBarsDeclaration.getType() ).fullName() );
    assertEquals( "com.cedarsoft.rest.generator.test.jaxb.BarModel.Jaxb", generator.getJaxbModelType( singleBarDeclaration.getType() ).fullName() );
    assertEquals( "java.util.List<com.cedarsoft.rest.generator.test.jaxb.BarModel.Jaxb>", generator.getJaxbModelType( barsDeclaration.getType() ).fullName() );
  }

  @Test
  public void testGeneratModelBar() throws Exception {
    new Generator( codeGenerator, barDescriptor ).generate();

    assertCodeGeneration( getClass().getResource( "JaxbObjectGeneratorTest.BarModelJaxb.txt" ) );
  }

  @Test
  public void testGeneratModelUser() throws Exception {
    new Generator( codeGenerator, userDescriptor ).generate();

    assertCodeGeneration( getClass().getResource( "JaxbObjectGeneratorTest.UserJaxb.txt" ) );
  }

  @Test
  public void testGeneratModelGroup() throws Exception {
    new Generator( codeGenerator, groupDescriptor ).generate();
    assertCodeGeneration( getClass().getResource( "JaxbObjectGeneratorTest.GroupJaxb.txt" ) );
  }

  @Test
  public void testGenerateModelFoo() throws Exception {
    codeGenerator = new CodeGenerator( new JaxbObjectGenerator.StubDecisionCallback() );

    new Generator( codeGenerator, fooDescriptor ).generate();
    assertCodeGeneration( getClass().getResource( "JaxbObjectGeneratorTest.FooModelJaxb.txt" ) );
  }

  @Test
  public void testGenerateBarTest() throws Exception {
    new TestGenerator( codeGenerator, barDescriptor ).generateTest();
    assertCodeGeneration( getClass().getResource( "JaxbObjectGeneratorTest.BarModelJaxbTest.txt" ) );
  }

  @Test
  public void testGenerateGroupTest() throws Exception {
    new TestGenerator( codeGenerator, groupDescriptor ).generateTest();
    assertCodeGeneration( getClass().getResource( "JaxbObjectGeneratorTest.GroupJaxbTest.txt" ) );
  }

  @Test
  public void testGeneratFooTest() throws Exception {
    new TestGenerator( codeGenerator, fooDescriptor ).generateTest();
    assertCodeGeneration( getClass().getResource( "JaxbObjectGeneratorTest.FooModelJaxbTest.txt" ) );
  }

  @Test
  public void testGeneratUserTest() throws Exception {
    new TestGenerator( codeGenerator, userDescriptor ).generateTest();
    URL expected = getClass().getResource( "JaxbObjectGeneratorTest.UserJaxbTest.txt" );
    assertCodeGeneration( expected );
  }

  private void assertCodeGeneration( @Nonnull URL expected ) throws IOException {
    MemoryCodeWriter memoryWriter = new MemoryCodeWriter();
    codeGenerator.getModel().build( memoryWriter );
    AssertUtils.assertEquals( expected, memoryWriter.allFilesToString() );
  }

  @Test
  public void testPac() {
    assertEquals( "a.b.c.d.ins.E", Generator.insertSubPackage( "a.b.c.d.E", "ins" ) );
    assertEquals( "a.b.c.d.e.E", Generator.insertSubPackage( "a.b.c.d.E", "e" ) );
  }
}
