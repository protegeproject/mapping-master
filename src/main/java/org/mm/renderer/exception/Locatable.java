package org.mm.renderer.exception;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface Locatable<T> {

   void setErrorLocation(T location);
}
