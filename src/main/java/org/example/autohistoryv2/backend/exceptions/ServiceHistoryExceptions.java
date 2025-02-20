package org.example.autohistoryv2.backend.exceptions;

  public class ServiceHistoryExceptions extends RuntimeException {

    public static class NotFoundException extends RuntimeException {
      public NotFoundException(String message) {
        super(message);
      }
    }

    public static class InformationValidityException extends RuntimeException {
      public InformationValidityException(String message) {
        super(message);
      }
    }
  }