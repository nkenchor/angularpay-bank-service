package io.angularpay.banks.adapters.inbound;

import io.angularpay.banks.domain.commands.PlatformConfigurationsConverterCommand;
import io.angularpay.banks.models.platform.PlatformConfigurationIdentifier;
import io.angularpay.banks.ports.inbound.InboundMessagingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static io.angularpay.banks.models.platform.PlatformConfigurationSource.TOPIC;

@Service
@RequiredArgsConstructor
@Profile("!test")
public class RedisMessageAdapter implements InboundMessagingPort {

    private final PlatformConfigurationsConverterCommand converterCommand;

    @Override
    public void onMessage(String message, PlatformConfigurationIdentifier identifier) {
        this.converterCommand.execute(message, identifier, TOPIC);
    }
}
