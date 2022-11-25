package com.bastion.bank.infrustructure.exception.handlers;


public class InvalidCurrencyExceptionHandler implements ExceptionMapper<InvalidCurrencyException> {

    @Override
    public Response toResponse(InvalidCurrencyException exception) {
        return Response.status(BAD_REQUEST)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_XML)
                .build();
    }
}
