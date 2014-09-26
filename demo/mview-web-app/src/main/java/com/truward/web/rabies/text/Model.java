package com.truward.web.rabies.text;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * A holder for model attributes. This interface is a reincarnation of the Spring MVC's Model interface.
 * Primarily designed for adding attributes to the model.
 * Allows for accessing the overall model as a <code>java.util.Map</code>.
 *
 * @author Alexander Shabanov
 */
public abstract class Model {

  private static final Model EMPTY = new EmptyModel();

  @Nonnull
  public static Model of() {
    return EMPTY;
  }

  /**
   * Add the supplied attribute under the supplied name.
   * @param attributeName the name of the model attribute (never <code>null</code>)
   * @param attributeValue the model attribute value (can be <code>null</code>)
   */
  @Nonnull
  public abstract Model addAttribute(@Nonnull String attributeName, @Nullable Object attributeValue);

  /**
   * Copy all attributes in the supplied <code>Map</code> into this <code>Map</code>.
   * @see #addAttribute(String, Object)
   */
  @Nonnull
  public abstract Model addAllAttributes(@Nonnull Map<String, ?> attributes);

  /**
   * Copy all attributes in the supplied <code>Map</code> into this <code>Map</code>,
   * with existing objects of the same name taking precedence (i.e. not getting
   * replaced).
   */
  @Nonnull
  public abstract Model mergeAttributes(@Nonnull Map<String, ?> attributes);

  /**
   * Does this model contain an attribute of the given name?
   * @param attributeName the name of the model attribute (never <code>null</code>)
   * @return whether this model contains a corresponding attribute
   */
  public abstract boolean containsAttribute(@Nonnull String attributeName);

  /**
   * Return the current set of model attributes as a Map.
   */
  @Nonnull
  public abstract Map<String, Object> asMap();

  //
  // Private
  //

  private static final class EmptyModel extends Model {

    @Nonnull
    @Override
    public Model addAttribute(@Nonnull String attributeName, @Nullable Object attributeValue) {
      throw new IllegalStateException("Not supported");
    }

    @Nonnull
    @Override
    public Model addAllAttributes(@Nonnull Map<String, ?> attributes) {
      throw new IllegalStateException("Not supported");
    }

    @Nonnull
    @Override
    public Model mergeAttributes(@Nonnull Map<String, ?> attributes) {
      throw new IllegalStateException("Not supported");
    }

    @Override
    public boolean containsAttribute(@Nonnull String attributeName) {
      return false;
    }

    @Nonnull
    @Override
    public Map<String, Object> asMap() {
      return ImmutableMap.of();
    }
  }
}
