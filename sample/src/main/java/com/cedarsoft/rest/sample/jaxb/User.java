package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.cedarsoft.jaxb.JaxbStub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@XmlType( name = "abstractUser" )
public abstract class User extends AbstractJaxbObject {
  private String email;
  private String name;

  public String getEmail() {
    return email;
  }

  public void setEmail( String email ) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  @XmlType(name = "user")
  @XmlRootElement( name = "user", namespace = "http://cedarsoft.com/rest/sample/user" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb
    extends User {

    @XmlElement( name = "friend" )
    private List<Stub> friends;

    public List<Stub> getFriends() {
      return friends;
    }

    public void setFriends( List<Stub> friends ) {
      this.friends = friends;
    }
  }

  @XmlType(name = "userStub")
  @XmlRootElement( name = "user", namespace = "http://cedarsoft.com/rest/sample/user/stub" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub
    extends User implements JaxbStub {
  }
}
