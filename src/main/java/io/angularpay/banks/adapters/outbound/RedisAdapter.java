package io.angularpay.banks.adapters.outbound;

import io.angularpay.banks.ports.outbound.OutboundMessagingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisAdapter implements OutboundMessagingPort {

    private final RedisHashClient redisHashClient;

    @Override
    public void updateBankHash(String reference, String bank) {
        this.redisHashClient.updateBankHash(reference, bank);
    }

    @Override
    public void publishBanks(Map<String, String> banks) {
        this.redisHashClient.publishedBanks(banks);
    }

    @Override
    public Map<String, String> getPlatformConfigurations(String hashName) {
        return this.redisHashClient.getPlatformConfigurations(hashName);
    }
}
