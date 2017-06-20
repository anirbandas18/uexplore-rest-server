package com.ufl.uexplore;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.ufl.uexplore.core.EmbeddedHttpServer;

public class UExplore {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		EmbeddedHttpServer server = new EmbeddedHttpServer();
		FutureTask<Integer> serverTask = new FutureTask<>(server);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<?> result = executorService.submit(serverTask);
		Integer value = (Integer) result.get();
		//return value;
	}
	
}