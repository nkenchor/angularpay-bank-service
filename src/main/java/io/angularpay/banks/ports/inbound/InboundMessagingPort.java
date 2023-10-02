package io.angularpay.banks.ports.inbound;

import io.angularpay.banks.models.platform.PlatformConfigurationIdentifier;

public interface InboundMessagingPort {
    void onMessage(String message, PlatformConfigurationIdentifier identifier);
}
