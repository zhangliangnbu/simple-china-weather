package com.tech.heathcilff.simplechinaweather;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
	@Test
	public void addition_isCorrect() throws Exception {
		assertEquals(4, 2 + 2);
	}

	@Test
	public void testRxJava() {
//		rxJava22();
//		rxJava23();
//		rxJava24();
//		rxJavaMap();
//		rxJavaFlatMap();
		rxJavaRange();
	}

	@Test
	public void testBiFlat() {
		List<String> list1 = Arrays.asList("a", "b", "c");
		final List<String> list2 = Arrays.asList("1", "2", "3", "c");
		Flowable.fromIterable(list1)
				.flatMap(new Function<String, Publisher<String>>() {
					@Override
					public Publisher<String> apply(final String s) throws Exception {
						return Flowable.fromIterable(list2).filter(new Predicate<String>() {
							@Override
							public boolean test(String os) throws Exception {
								return s.equals(os);
							}
						});
					}
				})
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String s) throws Exception {
						print(s);
					}
				});
	}

	@Test
	public void testToList() {
		List<String> list1 = Arrays.asList("a", "b", "c");
		Flowable.fromIterable(list1)
				.filter(new Predicate<String>() {
					@Override
					public boolean test(String s) throws Exception {
						return !s.equals("c");
					}
				})
				.toList()
				.subscribe(new Consumer<List<String>>() {
					@Override
					public void accept(List<String> strings) throws Exception {
						print(strings.toString());
					}
				});
	}

	private void rxJavaRange() {
		Flowable.range(5, 10)
				.doOnNext(new Consumer<Integer>() {
					@Override
					public void accept(Integer integer) throws Exception {
						print("store" + integer);
					}
				})
				.filter(new Predicate<Integer>() {
					@Override
					public boolean test(Integer integer) throws Exception {
						return integer % 2 == 0;
					}
				})
				.take(2)
				.subscribe(new Consumer<Integer>() {
					@Override
					public void accept(Integer integer) throws Exception {
						print(integer + "");
					}
				});
	}

	private void rxJavaFlatMap() {
		ArrayList<String[]> list = new ArrayList<>();
		String[] words1 = {"Hello,", "I am", "China!"};
		String[] words2 = {"Hello,", "I am", "Beijing!"};
		list.add(words1);
		list.add(words2);
		Flowable.fromIterable(list)
				.flatMap(new Function<String[], Publisher<String>>() {
					@Override
					public Publisher<String> apply(String[] strings) throws Exception {
						return Flowable.fromArray(strings);
					}
				})
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String s) throws Exception {
						print(s);
					}
				});
	}

	private void rxJavaMap() {
		Flowable.just("hello cosmos")
				.map(new Function<String, String>() {
					@Override
					public String apply(String s) throws Exception {
						return s + " by cliff";
					}
				})
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String s) throws Exception {
						print(s);
					}
				});
	}

	private void rxJava24() {
		Consumer<String> consumer = new Consumer<String>() {
			@Override
			public void accept(String s) throws Exception {
				print(s);
			}
		};

//		Flowable.just("hello china")
//				.subscribe(consumer);
		Flowable.fromArray("hello america")
				.subscribe(consumer);
	}

	@Test
	public void rxJava23() {
		Subscriber<String> subscriber = new Subscriber<String>() {
			@Override
			public void onSubscribe(Subscription s) {
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(String s) {
				print(s);
			}

			@Override
			public void onError(Throwable t) {
				print("onError");
				print(t.getMessage());
				t.printStackTrace();
			}

			@Override
			public void onComplete() {
				print("complete");
			}
		};

		Flowable.create(new FlowableOnSubscribe<String>() {
			@Override
			public void subscribe(FlowableEmitter<String> e) throws Exception {
				e.onNext("hello china");
				e.onComplete();

			}
		}, BackpressureStrategy.BUFFER)
				.subscribe(subscriber);

//		Flowable.just("hello world").subscribe(subscriber);
	}

	@Test
	public void rxJava22() {
		Observable observable = Observable.create(new ObservableOnSubscribe() {
			@Override
			public void subscribe(ObservableEmitter e) throws Exception {
				print("ObservableOnSubscribe.subscribe");
				e.onNext("hello world");
			}
		});
		Observer observer = new Observer<String>() {
			@Override
			public void onSubscribe(Disposable d) {
				print("onSubscribe");
			}

			@Override
			public void onNext(String value) {
				print(value);
			}

			@Override
			public void onError(Throwable e) {
				print(e.getMessage());
			}

			@Override
			public void onComplete() {
				print("onComplete");
			}
		};
		observable
				.doOnNext(new Consumer() {
					@Override
					public void accept(Object o) throws Exception {
						print("doOnNext");
					}
				})
				.doOnSubscribe(new Consumer<Disposable>() {
					@Override
					public void accept(Disposable disposable) throws Exception {
						print("doOnSubscribe");
					}
				})
				.subscribe(observer);
	}

	private void rxJava21() {
		String[] names = new String[]{"Adobe", "Bob", "Cross"};
		Observable.fromArray(names)
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String s) throws Exception {
						print(s);
					}
				});
	}

	private void print(String s) {
		System.out.println(s);
	}

	private void printList(List<String> list) {
		Flowable.fromIterable(list)
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String s) throws Exception {
						print(s);
					}
				});
	}
}