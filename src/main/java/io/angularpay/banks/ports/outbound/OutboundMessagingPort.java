package io.angularpay.banks.ports.outbound;

import java.util.Map;

public interface OutboundMessagingPort {
    void updateBankHash(String reference, String bank);
    void publishBanks(Map<String, String> banks);
    Map<String, String> getPlatformConfigurations(String hashName);
}
