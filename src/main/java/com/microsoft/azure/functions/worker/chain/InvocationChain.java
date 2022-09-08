package com.microsoft.azure.functions.worker.chain;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.middleware.FunctionWorkerChain;
import com.microsoft.azure.functions.middleware.FunctionWorkerMiddleware;

import java.util.Iterator;
import java.util.List;

public class InvocationChain implements FunctionWorkerChain {
     private final Iterator<FunctionWorkerMiddleware> middlewareIterator;

     private InvocationChain(List<FunctionWorkerMiddleware> middlewares){
         this.middlewareIterator = middlewares.iterator();
     }

    @Override
    public void doNext(ExecutionContext context) throws Exception {
        while (middlewareIterator.hasNext()) {
            middlewareIterator.next().invoke(context, this);
        }
    }

    public static class InvocationChainBuilder{

        private final List<FunctionWorkerMiddleware> middlewares;

        public InvocationChainBuilder(List<FunctionWorkerMiddleware> middlewares) {
            this.middlewares = middlewares;
        }

        public FunctionWorkerChain build(){
            return new InvocationChain(middlewares);
        }
    }
}
