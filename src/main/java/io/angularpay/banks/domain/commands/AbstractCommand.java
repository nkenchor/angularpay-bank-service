package io.angularpay.banks.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.banks.domain.Role;
import io.angularpay.banks.exceptions.CommandException;
import io.angularpay.banks.exceptions.ErrorObject;
import io.angularpay.banks.exceptions.ValidationException;
import io.angularpay.banks.models.AccessControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executors;

import static io.angularpay.banks.exceptions.ErrorCode.*;
import static io.angularpay.banks.helpers.Helper.*;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractCommand<T extends AccessControl, R> {

    protected final String name;
    protected final ObjectMapper mapper;

    public R execute(T request) {
        try {
            if (this instanceof SensitiveDataCommand) {
                T commandMask = (T) ((SensitiveDataCommand) this).mask(request);
                log.info("received {} request {}", this.name, maskAuthenticatedUser(mapper, commandMask));
            } else {
                log.info("received {} request {}", this.name, maskAuthenticatedUser(mapper, request));
            }
            log.info("validating {} request...", this.name);

            List<ErrorObject> validationErrors = this.validate(request);

            if (!CollectionUtils.isEmpty(validationErrors)) {
                log.info("{} request validation failed!", this.name);
                log.info("validation errors: {}", writeAsStringOrDefault(mapper, validationErrors));
                ValidationException exception = new ValidationException(validationErrors);
                throw CommandException.builder()
                        .status(resolveStatus(validationErrors))
                        .errorCode(VALIDATION_ERROR)
                        .cause(exception)
                        .message(String.format("Validation failed for %s request", this.name))
                        .build();
            }

            boolean hasPermittedRole = hasPermittedRole(this.permittedRoles(), request.getAuthenticatedUser().getRoles());

            if (!hasPermittedRole) {
                throw CommandException.builder()
                        .status(HttpStatus.FORBIDDEN)
                        .errorCode(AUTHORIZATION_ERROR)
                        .message(String.format("Authorization failed for %s request", this.name))
                        .build();
            }

            R response = this.handle(request);
            log.info("{} request successfully processed", this.name);
            if (this instanceof LargeDataResponseCommand) {
                log.info("returning {} response - truncating large data response", this.name);
            } else {
                log.info("returning {} response {}", this.name, writeAsStringOrDefault(mapper, response));
            }

            if (this instanceof BankUpdatePublisherCommand && response instanceof BankSupplier) {
                log.info("publishing {} bank update to REDIS hash", this.name);
                Executors.newSingleThreadExecutor().submit(() -> {
                    ((BankUpdatePublisherCommand)this).publishBankUpdate((BankSupplier)response);
                });
            }

            if (this instanceof ResourceReferenceCommand) {
                return ((ResourceReferenceCommand<R, R>) this).map(response);
            } else {
                return response;
            }
        } catch (Exception exception) {
            log.error("An error occurred while processing {} request", this.name, exception);
            if (exception instanceof CommandException) {
                throw ((CommandException) exception);
            } else {
                throw CommandException.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .errorCode(GENERIC_ERROR)
                        .cause(exception)
                        .message(String.format("An error occurred while processing %s request", this.name))
                        .build();
            }
        }
    }

    protected abstract R handle(T request);

    protected abstract List<ErrorObject> validate(T request);

    protected abstract List<Role> permittedRoles();
}
