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

package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement( namespace = "http://cedarsoft.com/rest/generator/test/foo-model" )
@XmlAccessorType( XmlAccessType.FIELD )
public class FooModelJaxb extends AbstractJaxbObject {

  private BarModelJaxb singleBar;
  @XmlElementRef
  private List<BarModelJaxb> theBars;

  public BarModelJaxb getSingleBar() {
    return singleBar;
  }

  public void setSingleBar( BarModelJaxb singleBar ) {
    this.singleBar = singleBar;
  }

  public List<BarModelJaxb> getTheBars() {
    return theBars;
  }

  public void setTheBars( List<BarModelJaxb> theBars ) {
    this.theBars = theBars;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof FooModelJaxb ) ) return false;

    FooModelJaxb that = ( FooModelJaxb ) o;

    if ( singleBar != null ? !singleBar.equals( that.singleBar ) : that.singleBar != null ) return false;
    if ( theBars != null ? !theBars.equals( that.theBars ) : that.theBars != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = singleBar != null ? singleBar.hashCode() : 0;
    result = 31 * result + ( theBars != null ? theBars.hashCode() : 0 );
    return result;
  }
}
