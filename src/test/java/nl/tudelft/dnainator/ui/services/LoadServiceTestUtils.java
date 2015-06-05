package nl.tudelft.dnainator.ui.services;

import java.util.concurrent.CompletableFuture;

import javafx.concurrent.Service;

/**
 * Utility class providing useful methods for testing loadservices.
 */
public final class LoadServiceTestUtils {

	private LoadServiceTestUtils() {
	}
	/**
	 * Registers a listener on the service to update the completableFuture.
	 * @param service The service.
	 * @param completableFuture	The future.
	 * @param <T> The expected type.
	 */
	public static <T> void registerListeners(Service<T> service,
			CompletableFuture<T> completableFuture) {
		service.setOnSucceeded(e -> completableFuture.complete(service.getValue()));
		service.setOnFailed(e ->
			completableFuture.completeExceptionally(service.getException()));
		service.setOnCancelled(e -> completableFuture.cancel(true));
	}

}
