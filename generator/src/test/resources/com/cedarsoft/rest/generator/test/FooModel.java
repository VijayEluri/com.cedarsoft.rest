package com.cedarsoft.rest.generator.test;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class FooModel {
  private BarModel singleBar;

  private final List<BarModel> theBars = new ArrayList<BarModel>();

  public FooModel( @NotNull Collection<? extends BarModel> theBars ) {
    this.theBars.addAll( theBars );
  }

  public List<? extends BarModel> getTheBars() {
    return Collections.unmodifiableList( theBars );
  }

  public BarModel getSingleBar() {
    return singleBar;
  }

  public void setSingleBar( BarModel singleBar ) {
    this.singleBar = singleBar;
  }
}
