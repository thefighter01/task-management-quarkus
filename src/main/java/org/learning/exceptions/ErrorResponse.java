package org.learning.exceptions;

public record ErrorResponse(int status , String message , String details)  {
}
