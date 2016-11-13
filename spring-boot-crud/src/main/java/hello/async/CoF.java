package hello.async;


public class CoF {

	// public static void main(String[] args) {
	// CompletableFuture<String> s1 = CompletableFuture.supplyAsync(() -> "s1");
	// CompletableFuture<String> s2 = CompletableFuture.supplyAsync(() -> "s2");
	// CompletableFuture<String> s3 = CompletableFuture.supplyAsync(() -> "s3");
	//
	// CompletableFuture.allOf(s1, s2, s3).thenApply(x -> {
	// System.out.println(s1.join());
	// System.out.println(s2.join());
	// System.out.println(s3.join());
	// return x;
	// });
	// s1.thenApply((p) -> {
	// System.out.println(p);
	// if (2 > 1) {
	// throw new NullPointerException("ehh");
	// }
	// return p;
	// }).exceptionally((e) -> {
	// System.out.println("f " + e);
	// throw (RuntimeException) e;
	// }).thenApply((p) -> {
	// System.out.println(p);
	// return p;
	// }).exceptionally((e) -> {
	// System.out.println("l " + e);
	// return "e2";
	// }).thenApply((p) -> {
	// System.out.println(p);
	// return p;
	// });
	//
	// }
}
