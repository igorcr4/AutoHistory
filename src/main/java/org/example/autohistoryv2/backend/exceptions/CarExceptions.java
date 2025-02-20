package org.example.autohistoryv2.backend.exceptions;

public class CarExceptions extends RuntimeException {

    public static class CarCreateValidationException extends RuntimeException {
        public CarCreateValidationException(String error) {
            super(error);
        }
    }

    public static class FindCarException extends RuntimeException {
        public FindCarException(String error) {
            super(error);
        }
    }

    public static class InformationUpdateException extends RuntimeException {
        public InformationUpdateException(String error) {
            super(error);
        }
    }

    public static class InformationValidityException extends RuntimeException {
        public InformationValidityException(String error) {
            super(error);
        }
    }
}
