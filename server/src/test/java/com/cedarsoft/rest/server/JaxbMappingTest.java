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

package com.cedarsoft.rest.server;

import com.cedarsoft.rest.model.AbstractJaxbObject;
import com.cedarsoft.rest.model.JaxbObject;
import com.cedarsoft.rest.model.JaxbStub;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.junit.*;

import javax.ws.rs.core.UriBuilder;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class JaxbMappingTest {
  private JaxbMapping<MyObject, MyObjectJaxb, MyObjectJaxbStub> mapping;
  private JaxbMapping<Parent, ParentJaxb, ParentJaxbStub> parentMapping;
  private JaxbMapping<GrandFather, GrandFatherJaxb, GrandFatherJaxbStub> grandFatherMapping;
  private UriContext uriContext;

  @Before
  public void setUp() throws Exception {
    mapping = new JaxbMapping<MyObject, MyObjectJaxb, MyObjectJaxbStub>() {
      @Nonnull
      @Override
      protected UriBuilder getUri( @Nonnull JaxbObject object, @Nonnull UriContext context ) {
        return context.getUriBuilder().path( "uriForMyObjectJaxb" );
      }

      @Override
      protected void copyFieldsToJaxbObject( @Nonnull MyObject source, @Nonnull MyObjectJaxb target, @Nonnull UriContext context ) {
        target.setDaInt( source.daInt );
      }

      @Nonnull
      @Override
      protected MyObjectJaxb createJaxbObject( @Nonnull MyObject object ) {
        return new MyObjectJaxb( "daId" );
      }

      @Nonnull
      @Override
      protected MyObjectJaxbStub createJaxbStub( @Nonnull MyObject object ) {
        return new MyObjectJaxbStub( "daId" );
      }

      @Override
      protected void copyFieldsToStub( @Nonnull MyObject source, @Nonnull MyObjectJaxbStub target, @Nonnull UriContext context ) {
        target.stubInt = source.daInt;
      }
    };

    parentMapping = new JaxbMapping<Parent, ParentJaxb, ParentJaxbStub>() {
      {
        getDelegatesMapping().addMapping( MyObjectJaxb.class, MyObjectJaxbStub.class, mapping );
      }

      @Nonnull
      @Override
      protected UriBuilder getUri( @Nonnull JaxbObject object, @Nonnull UriContext context ) {
        return context.getUriBuilder().path( "uriForParentJaxb" );
      }

      @Nonnull
      @Override
      protected ParentJaxbStub createJaxbStub( @Nonnull Parent object ) {
        return new ParentJaxbStub( "daId" );
      }

      @Override
      protected void copyFieldsToStub( @Nonnull Parent source, @Nonnull ParentJaxbStub target, @Nonnull UriContext context ) {
      }

      @Nonnull
      @Override
      protected ParentJaxb createJaxbObject( @Nonnull Parent object ) {
        return new ParentJaxb( "daId" );
      }

      @Override
      protected void copyFieldsToJaxbObject( @Nonnull Parent source, @Nonnull ParentJaxb target, @Nonnull UriContext context ) {
        target.setChild( get( MyObjectJaxb.class, source.child, context ) );
      }
    };

    grandFatherMapping = new JaxbMapping<GrandFather, GrandFatherJaxb, GrandFatherJaxbStub>() {
      {
        getDelegatesMapping().addMapping( ParentJaxb.class, ParentJaxbStub.class, parentMapping );
      }

      @Nonnull
      @Override
      protected GrandFatherJaxbStub createJaxbStub( @Nonnull GrandFather object ) {
        return new GrandFatherJaxbStub( "daId" );
      }

      @Override
      protected void copyFieldsToStub( @Nonnull GrandFather source, @Nonnull GrandFatherJaxbStub target, @Nonnull UriContext context ) {
      }

      @Nonnull
      @Override
      protected UriBuilder getUri( @Nonnull JaxbObject object, @Nonnull UriContext context ) {
        return context.getUriBuilder().path( "uriGrandParent" );
      }

      @Nonnull
      @Override
      protected GrandFatherJaxb createJaxbObject( @Nonnull GrandFather object ) {
        return new GrandFatherJaxb( "daId" );
      }

      @Override
      protected void copyFieldsToJaxbObject( @Nonnull GrandFather source, @Nonnull GrandFatherJaxb target, @Nonnull UriContext context ) {
        target.setParent( get( ParentJaxb.class, source.parent, context ) );
      }
    };

    uriContext = UriContextTest.createTestUriContext();
  }

  @Test
  public void testGetJaxbObject() throws Exception {
    MyObject myObject = new MyObject( 24 );

    MyObjectJaxb myObjectJaxb = mapping.getJaxbObject( myObject, uriContext );
    assertSame( myObject.daInt, myObjectJaxb.daInt );

    //test cache
    assertEquals( myObjectJaxb, mapping.getJaxbObject( myObject, uriContext ) );
  }

  @Test
  public void testGetJaxbObject2() throws Exception {
    MyObject myObject1 = new MyObject( 7 );
    MyObject myObject2 = new MyObject( 8 );

    List<MyObjectJaxb> myObjectJaxbs = mapping.getJaxbObjects( Lists.newArrayList( myObject1, myObject2 ), uriContext );
    assertSame( myObject1.daInt, myObjectJaxbs.get( 0 ).daInt );
    assertSame( myObject2.daInt, myObjectJaxbs.get( 1 ).daInt );

    //test cache
    assertEquals( myObjectJaxbs.get( 0 ), mapping.getJaxbObject( myObject1, uriContext ) );
    assertEquals( myObjectJaxbs.get( 1 ), mapping.getJaxbObject( myObject2, uriContext ) );
  }

  @Test
  public void testGetJaxbStubs() throws Exception {
    MyObject myObject1 = new MyObject( 7 );
    MyObject myObject2 = new MyObject( 8 );

    List<MyObjectJaxbStub> myObjectJaxbs = mapping.getJaxbStubs( Lists.newArrayList( myObject1, myObject2 ), uriContext );
    assertSame( myObject1.daInt, myObjectJaxbs.get( 0 ).stubInt );
    assertSame( myObject2.daInt, myObjectJaxbs.get( 1 ).stubInt );

    //test cache
    assertEquals( myObjectJaxbs.get( 0 ), mapping.getJaxbObjectStub( myObject1, uriContext ) );
    assertEquals( myObjectJaxbs.get( 1 ), mapping.getJaxbObjectStub( myObject2, uriContext ) );
  }

  @Test
  public void testUri() throws Exception {
    assertEquals( "http://test.running/here/uriForMyObjectJaxb", mapping.getJaxbObject( new MyObject( 7 ), uriContext ).getHref().toString() );
  }

  @Test
  public void testParent() throws Exception {
    MyObject myObject1 = new MyObject( 7 );
    Parent parent = new Parent( myObject1 );

    ParentJaxb parentJaxb = parentMapping.getJaxbObject( parent, uriContext );
    assertNotNull( parentJaxb );
    assertNotNull( parentJaxb.getChild() );
    assertEquals( myObject1.daInt, parentJaxb.getChild().daInt );

    //Cached
    assertEquals( parentJaxb, parentMapping.getJaxbObject( parent, uriContext ) );
    assertEquals( parentJaxb.getChild(), parentMapping.getJaxbObject( parent, uriContext ).getChild() );
  }

  @Test
  public void testParentWithUri() throws Exception {
    MyObject myObject1 = new MyObject( 7 );
    Parent parent = new Parent( myObject1 );

    ParentJaxb parentJaxb = parentMapping.getJaxbObject( parent, uriContext );
    assertNotNull( parentJaxb );
    assertNotNull( parentJaxb.getChild() );
    assertEquals( "http://test.running/here/uriForParentJaxb", String.valueOf( parentJaxb.getHref() ) );
    assertEquals( myObject1.daInt, parentJaxb.getChild().daInt );
    assertEquals( "http://test.running/here/uriForParentJaxb/uriForMyObjectJaxb", String.valueOf( parentJaxb.getChild().getHref() ) );

    assertEquals( parentJaxb, parentMapping.getJaxbObject( parent, uriContext ) );
  }

  @Test
  public void testGrandp() throws Exception {
    MyObject myObject1 = new MyObject( 7 );
    Parent parent = new Parent( myObject1 );
    GrandFather grandFather = new GrandFather( parent );

    GrandFatherJaxb jaxbObject = grandFatherMapping.getJaxbObject( grandFather, uriContext );
    assertNotNull( jaxbObject );
    assertNotNull( jaxbObject.getParent() );
    assertNotNull( jaxbObject.getParent().getChild() );
    assertEquals( myObject1.daInt, jaxbObject.getParent().getChild().daInt );

    assertEquals( jaxbObject, grandFatherMapping.getJaxbObject( grandFather, uriContext ) );
    assertEquals( jaxbObject.getParent(), grandFatherMapping.getJaxbObject( grandFather, uriContext ).getParent() );
    assertEquals( jaxbObject.getParent().getChild(), grandFatherMapping.getJaxbObject( grandFather, uriContext ).getParent().getChild() );

    assertEquals( jaxbObject.getParent(), parentMapping.getJaxbObject( parent, uriContext ) );
    assertEquals( jaxbObject.getParent().getChild(), mapping.getJaxbObject( myObject1, uriContext ) );
  }

  protected static class MyObject {
    private final int daInt;

    MyObject( int daInt ) {
      this.daInt = daInt;
    }
  }

  protected static class MyObjectJaxbStub extends AbstractJaxbObject implements JaxbStub<MyObjectJaxb> {
    public MyObjectJaxbStub() {
    }

    public MyObjectJaxbStub( @Nonnull  String id ) {
      super( id );
    }

    private int stubInt;

    @Nonnull
    @Override
    public Class<MyObjectJaxb> getJaxbType() {
      return MyObjectJaxb.class;
    }
  }


  protected static class MyObjectJaxb extends AbstractJaxbObject {
    private int daInt;

    public MyObjectJaxb() {
    }

    public MyObjectJaxb( @Nullable  String id ) {
      super( id );
    }

    public int getDaInt() {
      return daInt;
    }

    public void setDaInt( int daInt ) {
      this.daInt = daInt;
    }
  }

  protected static class Parent {
    private final MyObject child;

    Parent( MyObject child ) {
      this.child = child;
    }
  }

  protected static class ParentJaxb extends AbstractJaxbObject {
    public ParentJaxb() {
    }

    public ParentJaxb( @Nonnull  String id ) {
      super( id );
    }

    private MyObjectJaxb child;

    public MyObjectJaxb getChild() {
      return child;
    }

    public void setChild( MyObjectJaxb child ) {
      this.child = child;
    }

  }

  protected static class GrandFather {

    private final Parent parent;

    GrandFather( Parent parent ) {
      this.parent = parent;
    }

  }

  protected static class ParentJaxbStub extends AbstractJaxbObject implements JaxbStub<ParentJaxb> {
    public ParentJaxbStub() {
    }

    public ParentJaxbStub( @Nonnull  String id ) {
      super( id );
    }

    @Nonnull
    @Override
    public Class<ParentJaxb> getJaxbType() {
      return ParentJaxb.class;
    }
  }

  protected static class GrandFatherJaxbStub extends AbstractJaxbObject implements JaxbStub<GrandFatherJaxb> {
    public GrandFatherJaxbStub() {
    }

    public GrandFatherJaxbStub( @Nonnull  String id ) {
      super( id );
    }

    @Nonnull
    @Override
    public Class<GrandFatherJaxb> getJaxbType() {
      return GrandFatherJaxb.class;
    }
  }

  protected static class GrandFatherJaxb extends AbstractJaxbObject {
    private ParentJaxb parent;

    public GrandFatherJaxb() {
    }

    public GrandFatherJaxb( @Nonnull  String id ) {
      super( id );
    }

    public ParentJaxb getParent() {
      return parent;
    }

    public void setParent( ParentJaxb parent ) {
      this.parent = parent;
    }
  }

}
