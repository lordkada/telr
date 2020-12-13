package com.lordkada.telr.usvcs.translator;

import java.util.concurrent.CompletableFuture;

public interface UsvcTranslator {

    CompletableFuture<String> translate(String text);

}
