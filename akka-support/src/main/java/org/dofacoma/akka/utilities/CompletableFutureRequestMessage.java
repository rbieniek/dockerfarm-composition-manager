package org.dofacoma.akka.utilities;

import java.util.concurrent.CompletableFuture;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompletableFutureRequestMessage<T, V> {

    private T request;
    private CompletableFuture<V> future;
}
