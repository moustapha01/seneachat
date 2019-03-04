package com.signaretech.seneachat.persistence.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author Mouhamadou Bop
 *
 * Base class to be extended by all the persistent objects to
 * inherit an id primary key as well as equals and hashCode
 * implementations.
 */

public class UUIDUtil {

    /**
     * @return a random 128 bit {@link UUID}
     */
    public static byte[] createUUID(){
        UUID uuid = UUID.randomUUID();
        long leastBytes = uuid.getLeastSignificantBits();
        long mostBytes = uuid.getMostSignificantBits();

        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(mostBytes);
        buffer.putLong(leastBytes);

        return buffer.array();
    }

    public static byte[] toByteArray(UUID uuid){
        long leastBytes = uuid.getLeastSignificantBits();
        long mostBytes = uuid.getMostSignificantBits();

        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(mostBytes);
        buffer.putLong(leastBytes);

        return buffer.array();
    }

    public static UUID getUUIDFromByteArray(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long mostBytes = buffer.getLong();
        long leastBytes = buffer.getLong();

        return new UUID(mostBytes, leastBytes);
    }

    public static byte[] getUUIDFromString(String uuid){

        return toByteArray(UUID.fromString(uuid));
    }

}

