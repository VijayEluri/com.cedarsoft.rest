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

package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.rest.model.AbstractJaxbObject;
import com.cedarsoft.rest.model.JaxbStub;

import javax.annotation.Nonnull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlTransient
public abstract class CameraInfo extends AbstractJaxbObject {

  public static final String NS = "http://cedarsoft.com/rest/sample/camera-info";

  private String model;
  private String make;

  protected CameraInfo() {
  }

  protected CameraInfo( @Nonnull  String id ) {
    super( id );
  }

  public String getModel() {
    return model;
  }

  public void setModel( String model ) {
    this.model = model;
  }

  public String getMake() {
    return make;
  }

  public void setMake( String make ) {
    this.make = make;
  }

  @XmlType( name = "cameraInfoStub", namespace = Stub.NS_STUB )
  @XmlRootElement( name = "cameraInfo", namespace = Stub.NS_STUB )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends CameraInfo implements JaxbStub<Jaxb> {

    public static final String NS_STUB = NS + NS_STUB_SUFFIX;

    public Stub() {
    }

    public Stub( @Nonnull  String id ) {
      super( id );
    }

    @Nonnull
    @Override
    public Class<Jaxb> getJaxbType() {
      return Jaxb.class;
    }
  }

  @XmlType( name = "cameraInfo", namespace = Jaxb.NS )
  @XmlRootElement( name = "cameraInfo", namespace = Jaxb.NS )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends CameraInfo {
    private long serial;
    private String internalSerial;

    public Jaxb() {
    }

    public Jaxb( @Nonnull  String id ) {
      super( id );
    }

    public long getSerial() {
      return serial;
    }

    public void setSerial( long serial ) {
      this.serial = serial;
    }

    public String getInternalSerial() {
      return internalSerial;
    }

    public void setInternalSerial( String internalSerial ) {
      this.internalSerial = internalSerial;
    }
  }
}
