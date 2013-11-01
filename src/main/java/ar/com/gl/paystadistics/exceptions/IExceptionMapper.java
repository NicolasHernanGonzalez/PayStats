package ar.com.gl.paystadistics.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Contract for a provider that maps Java exceptions to
 * {@link javax.ws.rs.core.Response}. An implementation of this interface must
 * be annotated with {@link Provider}.
 *
 * @see Provider
 * @see javax.ws.rs.core.Response
 */
public interface IExceptionMapper<E> {
    Response toResponse(E e);
}