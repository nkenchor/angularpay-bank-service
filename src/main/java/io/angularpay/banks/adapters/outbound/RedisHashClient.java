package io.angularpay.banks.adapters.outbound;

import io.angularpay.banks.configurations.AngularPayConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;

import static io.angularpay.banks.common.Constants.BANKS_HASH;

@Service
@RequiredArgsConstructor
public class RedisHashClient {

    private final AngularPayConfiguration configuration;

    private Jedis jedisInstance() {
        return new Jedis(
                configuration.getRedis().getHost(),
                configuration.getRedis().getPort(),
                configuration.getRedis().getTimeout()
        );
    }

    public Map<String, String> getPlatformConfigurations(String hashName) {
        try (Jedis jedis = jedisInstance()) {
            return jedis.hgetAll(hashName);
        }
    }

    public void publishedBanks(Map<String, String> banks) {
        try (Jedis jedis = jedisInstance()) {
            jedis.hmset(BANKS_HASH, banks);
        }
    }

    public void updateBankHash(String reference, String bank) {
        try (Jedis jedis = jedisInstance()) {
            jedis.hset(BANKS_HASH, reference, bank);
        }
    }
}
